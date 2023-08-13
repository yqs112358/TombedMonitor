package com.yqs112358.tombedappsmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.topjohnwu.superuser.Shell;

import com.yqs112358.tombedappsmonitor.adapter.AppListItemAdapter;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.utils.AppPackageUtils;
import com.yqs112358.tombedappsmonitor.utils.FreezerUtils;
import com.yqs112358.tombedappsmonitor.utils.ProcessUtils;
import com.yqs112358.tombedappsmonitor.utils.RootUtils;
import com.yqs112358.tombedappsmonitor.utils.SystemPropUtils;
import com.yqs112358.tombedappsmonitor.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView appCountText = null;
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
        if(!RootUtils.checkRootPermission())
        {
            Log.i("TombedMonitor", "No root perm. Try getting it...");
            if(!RootUtils.requireRootPermission()) {
                Log.e("TombedMonitor", "Root perm denied.");
                this.alertNoRootPermission();
            }
            else
                Log.i("TombedMonitor", "Root perm granted");
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
        appListRefreshRunner.run();
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
                            if(!RootUtils.requireRootPermission())
                                alertNoRootPermission();
                        }
                    }, 2000);
                }
            })
            .setNeutralButton(R.string.no_root_dialog_exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UiUtils.isNightMode() ? Color.WHITE : Color.BLACK);
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(UiUtils.isNightMode() ? Color.WHITE : Color.BLACK);
    }

    // init all ui
    private void initUI() {
        // init data of about dialog
        StringBuilder message = new StringBuilder("内核墓碑支持状态：\n");
        if(FreezerUtils.doesSupportFreezerV2Frozen())
            message.append("✔️已挂载 FreezerV2(FROZEN)");
        else if (FreezerUtils.doesSupportFreezerV2Uid())
            message.append("✔️已挂载 FreezerV2(UID)");
        else if(FreezerUtils.doesSupportFreezerV1Frozen())
            message.append("✔️已挂载 FreezerV1(FROZEN)");
        else
            message.append("❓不支持Freezer，仅可用Signal-19/20方式");

        message.append("\n\n安卓版本：").append(SystemPropUtils.getAndroidVersion())
                .append("(API ").append(SystemPropUtils.getAndroidApiVersion())
                .append(")\n内核版本：").append(SystemPropUtils.getLinuxCoreVersion())
                .append("\n\n项目开源地址：");

        // add color and link to message
        String preMessage = message.toString();
        SpannableString realMessage = new SpannableString(preMessage + "https://github.com/yqs112358/TombedAppsMonitor");
        Linkify.addLinks(realMessage, Linkify.WEB_URLS);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
        realMessage.setSpan(foregroundColorSpan, preMessage.length(), realMessage.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        // init toolbar and about dialog
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(item -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.about_dialog_title)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage(realMessage)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    }).show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(UiUtils.isNightMode() ? Color.WHITE : Color.BLACK);
            TextView dialogMessage = (TextView)dialog.findViewById(android.R.id.message);
            dialogMessage.setMovementMethod(LinkMovementMethod.getInstance());
            return true;
        });

        // init search bar
        appCountText = findViewById(R.id.appCount);
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
        appListItemAdapter = new AppListItemAdapter(this, new ArrayList<ProcessAndAppInfo>());
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

            // Update count
            appCountText.setText(new StringBuilder("共有")
                    .append(newAppItemList.size()).append("个被冻结进程").toString());

            // Calc diff and update recycleview
            appListItemAdapter.updateData(newAppItemList);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
}
