package com.yqs112358.tombedappsmonitor.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationUtils {
    private static Application application;
    private static Context context;

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application app) {
        ApplicationUtils.application = app;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context cont) {
        ApplicationUtils.context = cont;
    }
}
