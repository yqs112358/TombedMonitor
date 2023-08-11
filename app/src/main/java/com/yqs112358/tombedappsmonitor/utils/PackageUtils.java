package com.yqs112358.tombedappsmonitor.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class PackageUtils {

    public static List<PackageInfo> getAllInstalledPackages() {
        List<PackageInfo> packageInfos = new ArrayList<PackageInfo>();
        try {
            packageInfos = ApplicationUtils.getApplication()
                    .getPackageManager().getInstalledPackages(0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return packageInfos;
    }
}
