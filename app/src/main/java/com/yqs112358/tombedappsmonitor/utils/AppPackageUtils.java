package com.yqs112358.tombedappsmonitor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppPackageUtils {
    private static Map<String, PackageInfo> packageInfos = new HashMap<String, PackageInfo>();
    private static Set<String> allPackageNames = new HashSet<String>();

    public static void saveAllInstalledPackages() {
        try {
            Context context = ApplicationUtils.getContext();
            List<PackageInfo> infos = context.getPackageManager().getInstalledPackages(0);
            for(PackageInfo info : infos)
            {
                packageInfos.put(info.packageName, info);
                allPackageNames.add(info.packageName);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Map<String, PackageInfo> getAllPackagesInfo() {
        return packageInfos;
    }

    public static PackageInfo getPackageInfo(String packageName) {
        return packageInfos.get(packageName);
    }

    public static String getPackageAppName(String packageName) {
        Context context = ApplicationUtils.getContext();
        return (String)context.getPackageManager()
                .getApplicationLabel(getPackageInfo(packageName).applicationInfo);
    }

    public static Drawable getPackageAppIcon(String packageName) {
        Context context = ApplicationUtils.getContext();
        return context.getPackageManager()
                .getApplicationIcon(getPackageInfo(packageName).applicationInfo);
    }

    public static boolean isPackageName(String name) {
        return !name.isEmpty() && allPackageNames.contains(name);
    }
}
