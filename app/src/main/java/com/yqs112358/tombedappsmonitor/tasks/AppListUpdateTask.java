package com.yqs112358.tombedappsmonitor.tasks;

import com.yqs112358.tombedappsmonitor.adapter.AppListItemAdapter;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.utils.ProcessUtils;

import java.util.List;
import java.util.TimerTask;

public class AppListUpdateTask extends TimerTask {
    List<ProcessAndAppInfo> appItemList;
    AppListItemAdapter appListItemAdapter;

    public AppListUpdateTask(List<ProcessAndAppInfo> appItemList, AppListItemAdapter appListItemAdapter) {
        this.appListItemAdapter = appListItemAdapter;
        this.appItemList = appItemList;
    }

    public void run() {
        try {
            appItemList.clear();
            appItemList = ProcessUtils.getAllProcessesInfo();

            synchronized (appItemList) {
                appListItemAdapter.notifyDataSetChanged();
            }
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}