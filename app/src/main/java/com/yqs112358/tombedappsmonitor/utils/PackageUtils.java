package com.yqs112358.tombedappsmonitor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class PackageUtils {
    private static List<PackageInfo> packageInfos = new ArrayList<PackageInfo>();

    public static void saveAllInstalledPackages() {
        try {
            Context context = ApplicationUtils.getContext();
            packageInfos = context.getPackageManager().getInstalledPackages(0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static List<PackageInfo> getPackageInfos() {
        return packageInfos;
    }
}
