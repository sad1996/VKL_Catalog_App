package nowapps.vkl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class PermissionActivity extends Activity {

    int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    @Override
    public  void onStart(){
        super.onStart();
        verifyPermissions();
    }

    private void verifyPermissions() {
        int readPhonePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int storagePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

        if (storagePermission != PackageManager.PERMISSION_GRANTED || readPhonePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    PermissionActivity.this,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_PERMISSION) {
            // do something
            startActivity(new Intent(PermissionActivity.this, MainActivity.class));
            this.finish();
        }
    }

}
