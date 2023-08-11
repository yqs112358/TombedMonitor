package com.yqs112358.tombedappsmonitor;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

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

        ApplicationUtils.setApplication(this);
        ApplicationUtils.setContext(getApplicationContext());

        // require sui
        isSui = Sui.init(getPackageName());
        if (!isSui) {
            // If this is a multi-process application
            //ShizukuProvider.enableMultiProcessSupport( /* is current process the same process of ShizukuProvider's */ );
        }
    }
}
