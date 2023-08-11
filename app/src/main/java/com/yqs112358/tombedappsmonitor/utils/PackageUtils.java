package com.yqs112358.tombedappsmonitor.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageUtils {
    private static Map<String, PackageInfo> packageInfos = new HashMap<String, PackageInfo>();

    public static void saveAllInstalledPackages() {
        try {
            Context context = ApplicationUtils.getContext();
            List<PackageInfo> infos = context.getPackageManager().getInstalledPackages(0);
            for(PackageInfo info : infos)
            {
                packageInfos.put(info.packageName, info);
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
}
