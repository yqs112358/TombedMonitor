package com.yqs112358.tombedappsmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    private int refreshInterval = 500;

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
            this.alertNoRootPermission();
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


    // alert no root permission and check again later
    private void alertNoRootPermission() {
        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(R.string.no_root_dialog_title)
            .setMessage(R.string.no_root_dialog_content)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean rootAccess = Shell.getShell().isRoot();
                            if (!rootAccess) {
                                alertNoRootPermission();
                            }
                        }
                    }, 2000);
                }
            }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    // init all ui
    private void initUI() {
        // init toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(item -> {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, SettingActivity.class);
//            startActivity(intent);
            return true;
        });

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

    // refresh app list
    public void refreshAppList() {
        try {
            List<ProcessAndAppInfo> newAppItemList = ProcessUtils.getAllProcessesInfo();
            if(!searchFilter.isEmpty())
            {
                // Filter list
                newAppItemList.removeIf(item->{
                   return !item.getAppName().contains(searchFilter) && !item.getProcessName().contains(searchFilter);
                });
            }

            // Calc diff and update recycleview
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return appItemList.size();
                }

                @Override
                public int getNewListSize() {
                    return newAppItemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    // 判断两项是否是同一项
                    return appItemList.get(oldItemPosition).getProcessName().equals(newAppItemList.get(newItemPosition).getProcessName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    // 判断两项的内容是否相同
                    ProcessAndAppInfo oldItem = appItemList.get(oldItemPosition);
                    ProcessAndAppInfo newItem = newAppItemList.get(newItemPosition);
                    return oldItem.getStatus().equals(newItem.getStatus())
                            && oldItem.getFrozenType().equals(newItem.getFrozenType());
                }
            });

            appItemList.clear();
            appItemList.addAll(newAppItemList);

            diffResult.dispatchUpdatesTo(appListItemAdapter);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
