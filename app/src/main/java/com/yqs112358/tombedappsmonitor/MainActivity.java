package com.yqs112358.tombedappsmonitor;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.yqs112358.tombedappsmonitor.utils.PackageUtils;
import com.yqs112358.tombedappsmonitor.services.ShizukuService;

import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // require shizuku
        Shizuku.addRequestPermissionResultListener(this::onRequestPermissionsResult);
        try {
            ShizukuService.requestPermission();
        }
        catch(Throwable e) {
            e.printStackTrace();
        }

        // read all packages list
        PackageUtils.saveAllInstalledPackages();

        // bind remote user service
        ShizukuService.startRemoteUserService();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Shizuku.removeRequestPermissionResultListener(this::onRequestPermissionsResult);
    }

    private void onRequestPermissionsResult(int requestCode, int grantResult) {
        if (grantResult == PERMISSION_GRANTED) {
            Log.i("TombedMonitor", "shizuku permission granted");
        } else {
            Log.e("TombedMonitor", "Fail to grant shizuku permission");
        }
    }
}
