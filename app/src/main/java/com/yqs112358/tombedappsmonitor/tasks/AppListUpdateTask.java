package com.yqs112358.tombedappsmonitor.tasks;

import android.util.Log;

import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;
import com.yqs112358.tombedappsmonitor.utils.ProcessUtils;

import java.util.Map;
import java.util.TimerTask;

public class AppListUpdateTask extends TimerTask {
    public void run() {
        try {
            Map<String, ProcessAndAppInfo> res = ProcessUtils.getAllProcessesInfo();

            Log.i("TombedMonitor", "=====================================");
            Log.i("TombedMonitor", res.toString());
            Log.i("TombedMonitor", "=====================================");
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}