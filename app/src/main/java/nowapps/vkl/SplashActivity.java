package nowapps.vkl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    int readPhonePermission = ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE);
                    int storagePermission = ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (storagePermission != PackageManager.PERMISSION_GRANTED || readPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(SplashActivity.this, PermissionActivity.class));
                        SplashActivity.this.finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    }
                }
            }
        };
        timerThread.start();
    }
}
