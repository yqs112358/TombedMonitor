package com.yqs112358.tombedappsmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.topjohnwu.superuser.Shell;

import com.yqs112358.tombedappsmonitor.adapter.AppItemAdapter;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.tasks.AppListUpdateTask;
import com.yqs112358.tombedappsmonitor.utils.AppPackageUtils;

import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private List<ProcessAndAppInfo> appItemList = null;
    private AppItemAdapter appItemAdapter = null;
    private Handler handler = new Handler();
    private Timer appListUpdateTimer = null;
    private String searchFilter = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check root access
        boolean rootAccess = Shell.getShell().isRoot();
        if (!rootAccess) {
            //TODO
            this.alertNoRootPermissionAndExit();
        }

        // Set layout
        setContentView(R.layout.activity_main);

        // read all packages list
        AppPackageUtils.saveAllInstalledPackages();

        // Init ui
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if(appListUpdateTimer != null)
                appListUpdateTimer.cancel();
            appListUpdateTimer = new Timer();
            appListUpdateTimer.schedule(new AppListUpdateTask(), 0, 1000);
        }
        catch(Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        appListUpdateTimer.cancel();
        appListUpdateTimer = null;
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

    public void refreshAppList() {
        handler.post(() -> {
            synchronized (appItemList) {
                appItemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initUI() {
        // init search bar
        EditText editText = findViewById(R.id.searchBar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchFilter = s.toString();
            }
        });

        // init app list
        ListView listView = findViewById(R.id.appList);
        appItemAdapter = new AppItemAdapter(MainActivity.this, R.layout.app_item, appItemList);
        listView.setAdapter(appItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
        });
    }
}
