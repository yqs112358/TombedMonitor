package com.yqs112358.tombedappsmonitor.services;

import android.app.ActivityManager;
import android.content.Context;

import com.yqs112358.tombedappsmonitor.utils.ApplicationUtils;


public class SystemService {
    private static ActivityManager activityManager = null;

    public static void setActivityManager(ActivityManager activityManager) {
        SystemService.activityManager = activityManager;
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }
}
