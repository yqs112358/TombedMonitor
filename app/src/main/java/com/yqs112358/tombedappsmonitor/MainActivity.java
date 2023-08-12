package com.yqs112358.tombedappsmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.topjohnwu.superuser.Shell;

import com.yqs112358.tombedappsmonitor.adapter.AppListItemAdapter;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.utils.AppPackageUtils;
import com.yqs112358.tombedappsmonitor.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ProcessAndAppInfo> appItemList = new ArrayList<ProcessAndAppInfo>();
    private AppListItemAdapter appListItemAdapter = null;
    private String searchFilter = "";
    private boolean isAppRunning = false;
    private int refreshInterval = 2000;

    // app list refresher
    private final Handler handler = new Handler();
    Runnable appListRefreshRunner = new Runnable(){
        @Override
        public void run() {
            refreshAppList();
            if(isAppRunning)
                handler.postDelayed(this, refreshInterval);
        }
    };


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
        isAppRunning = true;
        handler.postDelayed(appListRefreshRunner, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAppRunning = false;
        handler.removeCallbacks(appListRefreshRunner);
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
        try {
            // List<AppItem> filter = PackageUtils.filter(this, type, text);
            appItemList.clear();
            appItemList.addAll(ProcessUtils.getAllProcessesInfo());
            appListItemAdapter.notifyDataSetChanged();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
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
        appListItemAdapter = new AppListItemAdapter(this, appItemList);
        RecyclerView rv = findViewById(R.id.appList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(appListItemAdapter);
    }
}
