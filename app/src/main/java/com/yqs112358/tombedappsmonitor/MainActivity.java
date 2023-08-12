package com.yqs112358.tombedappsmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.topjohnwu.superuser.Shell;

import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.utils.ProcessUtils;
import com.yqs112358.tombedappsmonitor.utils.AppPackageUtils;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check root access
        boolean rootAccess = Shell.getShell().isRoot();
        if (!rootAccess) {
            this.alertNoRootPermissionAndExit();
        }

        // Set layout
        setContentView(R.layout.activity_main);

        // read all packages list
        AppPackageUtils.saveAllInstalledPackages();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Map<String, ProcessAndAppInfo> res = ProcessUtils.getAllProcessesInfo();
            Log.i("TombedMonitor", "=====================================");
            Log.i("TombedMonitor", res.toString());
            Log.i("TombedMonitor", "=====================================");
        }
        catch(Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void alertNoRootPermissionAndExit() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.no_root_dialog_title)
            .setMessage(R.string.no_root_dialog_content)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity(); // exit
                }
            })
            .show();
    }
}
