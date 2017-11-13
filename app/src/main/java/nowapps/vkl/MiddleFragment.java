package nowapps.vkl;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static nowapps.vkl.HeadFragment.listSelected;
import static nowapps.vkl.HeadFragment.middleSelected;
import static nowapps.vkl.MainActivity.mExpandButton;
import static nowapps.vkl.MainActivity.mFolderView;
import static nowapps.vkl.MainActivity.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiddleFragment extends Fragment {

    @BindView(R.id.gridView)
    RecyclerView mRecyclerView;

    File files;
    SharedPreferences sh;
    private List<PathItem> DirectoryList = new ArrayList<>();
    FolderAdapter myAdapter;

    public MiddleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_middle, container, false);
        ButterKnife.bind(this, view);
        sh = getContext().getSharedPreferences("PRESSED", Context.MODE_PRIVATE);
        DirectoryList.clear();

        if (sh.getString("pressed", "").equals("middlePressed")) {
            if (middleSelected != null) {
                mFolderView.setVisibility(View.VISIBLE);
                mExpandButton.setVisibility(View.VISIBLE);
                files = new File(path + "/" + middleSelected);
            }
        } else {
            if (listSelected != null) {
                mFolderView.setVisibility(View.VISIBLE);
                mExpandButton.setVisibility(View.VISIBLE);
                files = new File(path + "/" + listSelected);
            }
        }


        if (files.isDirectory()) {
            File[] listFile = files.listFiles();
            for (File folderfile : listFile) {
                if (folderfile.isDirectory()) {
                    String folderImage = folderfile.getAbsolutePath() + ".jpg";
                    PathItem pathItem = new PathItem(folderImage, folderfile.getName());
                    DirectoryList.add(pathItem);
                }
            }
        }
        if(DirectoryList.isEmpty()){
            mFolderView.setVisibility(View.GONE);
            mExpandButton.setVisibility(View.GONE);
        }

        Collections.sort(DirectoryList, new Comparator<PathItem>() {
            public int compare(PathItem obj1, PathItem obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        myAdapter = new FolderAdapter(DirectoryList);
        mRecyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        return view;
    }


    class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {

        private List<PathItem> folderList;

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.image)
            ImageView myImage;
            @BindView(R.id.name)
            TextView tv;
            @BindView(R.id.folder)
            CardView folder;

            MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }

        FolderAdapter(List<PathItem> moviesList) {
            this.folderList = moviesList;
        }

        @Override
        public FolderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grid_folder, parent, false);

            return new FolderAdapter.MyViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final FolderAdapter.MyViewHolder holder, final int position) {
            final PathItem pathItem = folderList.get(position);
            File file = new File(pathItem.getPath());
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(pathItem.getPath());
                holder.myImage.setImageBitmap(myBitmap);
            }
            holder.tv.setText(pathItem.getName());
            holder.folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sh.getString("pressed", "").equals("middlePressed")) {
                        middleSelected += "/" + pathItem.getName();
                    } else {
                        middleSelected = listSelected;
                        middleSelected += "/" + pathItem.getName();
                    }

                    File files = new File(path + "/" + middleSelected);
                    if (files.isDirectory()) {
                        changeMiddleFragment(new MiddleFragment());
                    }
                    SharedPreferences.Editor edit = sh.edit();
                    edit.putString("pressed", "middlePressed");
                    edit.apply();
                    changeBodyFragment(new BodyFragment());
                    Toast.makeText(getContext(), middleSelected, Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return folderList.size();
        }

    }

    private void changeMiddleFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.middle_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void changeBodyFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.body_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
