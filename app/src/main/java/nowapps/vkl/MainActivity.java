package nowapps.vkl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    static String path;
    static LinearLayout mFolderView;
    static CardView mExpandButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        path = Environment.getExternalStorageDirectory().toString() + "/VKL";
        mFolderView = (LinearLayout) findViewById(R.id.middle_fragment_view);
        mExpandButton = (CardView) findViewById(R.id.expand);
        changeHeadFragment(new HeadFragment());
        changeBodyFragment(new BodyFragment());
    }

    @OnClick(R.id.expand)
    void clickExpand(){
        if(mFolderView.getVisibility()== View.VISIBLE){
            mFolderView.setVisibility(View.GONE);
        } else {
            mFolderView.setVisibility(View.VISIBLE);
        }
    }

    private void changeHeadFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.head_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void changeBodyFragment(Fragment fm) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.body_fragment, fm);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 3) {
            getSupportFragmentManager().popBackStack();
        } else {
           super.onBackPressed();
        }
    }
}