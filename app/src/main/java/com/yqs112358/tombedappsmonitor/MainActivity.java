package com.yqs112358.tombedappsmonitor;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import com.yqs112358.tombedappsmonitor.utils.ApplicationUtils;
import com.yqs112358.tombedappsmonitor.utils.PackageUtils;
import com.yqs112358.tombedappsmonitor.utils.ShizukuUtils;

import java.util.List;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // require shizuku
        Shizuku.addRequestPermissionResultListener(this::onRequestPermissionsResult);
        try {
            ShizukuUtils.requestPermission();
        }
        catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private void onRequestPermissionsResult(int requestCode, int grantResult) {
        if (grantResult == PERMISSION_GRANTED) {
            Log.i("TombedMonitor", "shizuku permission granted");
        } else {
            Log.e("TombedMonitor", "Fail to grant shizuku permission");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<PackageInfo> infos = PackageUtils.getAllInstalledPackages();
        Log.i("TombedMonitor", "All packages installed:");
        for (PackageInfo info : infos) {
            Log.i("TombedMonitor", info.packageName);
        }
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
}
