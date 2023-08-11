package com.yqs112358.tombedappsmonitor.utils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.util.Log;

import rikka.shizuku.Shizuku;

public class ShizukuUtils {
    public static int SHIZUKU_REQUEST_CODE = 0;
    public static String SHIZUKU_NO_FOUND = "Shizuku no found!";

    // request permission of shizuku
    public static void requestPermission() {
        if (!Shizuku.pingBinder()) {
            throw new Error(SHIZUKU_NO_FOUND);
        }

        if(!checkPermission()) {
            Shizuku.requestPermission(SHIZUKU_REQUEST_CODE);
        }
    }

    // check permission of shizuku
    public static boolean checkPermission() {
        if (!Shizuku.pingBinder())
            throw new Error(SHIZUKU_NO_FOUND);
        return Shizuku.checkSelfPermission() == PERMISSION_GRANTED;
    }
}
