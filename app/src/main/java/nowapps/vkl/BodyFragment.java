package nowapps.vkl;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static nowapps.vkl.HeadFragment.listSelected;
import static nowapps.vkl.HeadFragment.middleSelected;
import static nowapps.vkl.MainActivity.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class BodyFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<PathItem> ImageList = new ArrayList<>();
    ImageAdapter adapter;
    SharedPreferences sh;

    public BodyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        ButterKnife.bind(this, view);
        sh = getContext().getSharedPreferences("PRESSED", Context.MODE_PRIVATE);
        if (sh.getString("pressed", "").equals("middlePressed")) {
            if (middleSelected != null) {
                File files = new File(path + "/" + middleSelected);
                if (files.isDirectory()) {
                    File[] listFile = files.listFiles();
                    for (File folderfile : listFile) {
                        if (!folderfile.isDirectory()) {
                            PathItem pathItem = new PathItem(folderfile.getAbsolutePath(), folderfile.getName());
                            ImageList.add(pathItem);
                        }
                    }
                }
            }
        } else {
            if (listSelected == null) {
                listSelected = "1Introduction";
            }
            File files = new File(path + "/" + listSelected);
            if (files.isDirectory()) {
                File[] listFile = files.listFiles();
                for (File folderfile : listFile) {
                    if (folderfile.isDirectory()) {
                        changeMiddleFragment(new MiddleFragment());
                    } else {
                        PathItem pathItem = new PathItem(folderfile.getAbsolutePath(), folderfile.getName());
                        ImageList.add(pathItem);
                    }
                }
            }
        }

        Collections.sort(ImageList, new Comparator<PathItem>() {
            public int compare(PathItem obj1, PathItem obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());
            }
        });

        //  final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        //  layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(false);
        adapter = new ImageAdapter(ImageList);
        mRecyclerView.setAdapter(adapter);
        // add pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // pager indicator
        mRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

        //   mRecyclerView.addOnScrollListener(new CenterScrollListener());

        return view;
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

        private List<PathItem> imageList;

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.image)
            SimpleDraweeView imageView;

            @BindView(R.id.name)
            TextView textView;

            @BindView(R.id.videoIcon)
            ImageView videoIcon;

            MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }

        ImageAdapter(List<PathItem> moviesList) {
            this.imageList = moviesList;
        }

        @Override
        public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_image, parent, false);

            return new ImageAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ImageAdapter.MyViewHolder holder, final int position) {
            final PathItem pathItem = imageList.get(position);

            if (isVideoFile(pathItem.getPath())) {
                holder.textView.setText(pathItem.getName().substring(0, pathItem.getName().lastIndexOf(".")));
                holder.videoIcon.setVisibility(View.VISIBLE);
            } else {
                holder.textView.setVisibility(View.GONE);
                holder.videoIcon.setVisibility(View.GONE);
            }
            Uri imageUri = Uri.fromFile(new File(pathItem.getPath()));// For files on device
            holder.imageView.setImageURI(imageUri);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isVideoFile(pathItem.getPath())) {
                        Intent i = new Intent(getContext(), Video.class);
                        String videoUrl = pathItem.getPath();
                        String text = pathItem.getName();
                        i.putExtra("videoUrl", videoUrl);
                        i.putExtra("text", text);
                        getContext().startActivity(i);
                    }
                    if (isImageFile(pathItem.getPath())) {
                        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        dialog.setContentView(R.layout.activity_zoom);
                        ZoomageView zoomageView = dialog.findViewById(R.id.myZoomageView);
                        CardView close = dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        Bitmap myBitmap = BitmapFactory.decodeFile(pathItem.getPath());
                        zoomageView.setImageBitmap(myBitmap);
                        dialog.show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

    }

    private void changeMiddleFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.middle_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

}
