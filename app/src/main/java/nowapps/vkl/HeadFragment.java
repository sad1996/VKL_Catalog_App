package nowapps.vkl;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static nowapps.vkl.MainActivity.mExpandButton;
import static nowapps.vkl.MainActivity.mFolderView;
import static nowapps.vkl.MainActivity.path;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadFragment extends Fragment {

    @BindView(R.id.listView)
    ListView listView;

    int listSize = 0;
    static String listSelected, middleSelected;
    private List<PathItem> list = new ArrayList<>();
    SharedPreferences sh;

    public HeadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head, container, false);
        ButterKnife.bind(this, view);

        sh = getContext().getSharedPreferences("PRESSED", Context.MODE_PRIVATE);
        getDirectory();
        return view;

    }


    private void getDirectory() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getContext(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                Log.d("Files", "Size: " + files.length);
                for (File file : files) {
                    if (file.isDirectory()) {
                        listSize += 1;
                        PathItem pathItem = new PathItem(file.getAbsolutePath(), file.getName());
                        list.add(pathItem);
                    }
                }
                Collections.sort(list, new Comparator<PathItem>() {
                    public int compare(PathItem obj1, PathItem obj2) {
                        return obj1.getName().compareToIgnoreCase(obj2.getName());
                    }
                });
                CustomAdapter adapter = new CustomAdapter(list, getContext());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mFolderView.setVisibility(View.GONE);
                        mExpandButton.setVisibility(View.GONE);
                        SharedPreferences.Editor edit = sh.edit();
                        edit.putString("pressed", "headPressed");
                        edit.apply();
                        listSelected = adapterView.getItemAtPosition(i).toString();
                        changeBodyFragment(new BodyFragment());
                    }
                });
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "VKL folder not found.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<PathItem> {

        private List<PathItem> headList;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            TextView txtName;
            LinearLayout headView;
        }

        public CustomAdapter(List<PathItem> headList, Context context) {
            super(context, R.layout.list_text, headList);
            this.headList = headList;
            this.mContext = context;

        }

        private int lastPosition = -1;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PathItem headList = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            final ViewHolder holder; // view lookup cache stored in tag

            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_text, parent, false);

                holder.txtName = convertView.findViewById(R.id.name);
                holder.headView = convertView.findViewById(R.id.headView);

                if (listSize > 9) {
                    holder.txtName.setText(headList.getName().substring(2));
                } else {
                    holder.txtName.setText(headList.getName().substring(1));
                }

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    private void changeBodyFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.body_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
