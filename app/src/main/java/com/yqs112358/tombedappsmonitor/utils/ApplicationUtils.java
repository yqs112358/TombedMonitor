package com.yqs112358.tombedappsmonitor.utils;

import android.app.Application;

public class ApplicationUtils {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        ApplicationUtils.application = application;
    }
}
