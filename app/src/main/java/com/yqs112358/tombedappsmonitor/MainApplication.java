package com.yqs112358.tombedappsmonitor;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.yqs112358.tombedappsmonitor.services.SystemService;
import com.yqs112358.tombedappsmonitor.utils.ApplicationUtils;

import rikka.sui.Sui;

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
        SystemService.setActivityManager((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE));

        // require sui
        isSui = Sui.init(BuildConfig.APPLICATION_ID);
        if (!isSui) {
            // If this is a multi-process application
            //ShizukuProvider.enableMultiProcessSupport( /* is current process the same process of ShizukuProvider's */ );
        }
    }
}
