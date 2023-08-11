package com.yqs112358.tombedappsmonitor;

import android.app.Application;

import com.yqs112358.tombedappsmonitor.utils.ApplicationUtils;

public class MainApplication extends Application {

    private static boolean isSui;

    public static boolean isSui() {
        return isSui;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init global vars
        ApplicationUtils.setApplication(this);
        ApplicationUtils.setContext(getApplicationContext());
    }
}
