package com.yqs112358.tombedappsmonitor.utils;

import com.topjohnwu.superuser.Shell;

import java.io.IOException;

public class RootUtils {
    public static boolean checkRootPermission(){
        return Shell.getShell().isRoot();
    }

    public static boolean requireRootPermission(){
        try {
            Process process = Runtime.getRuntime().exec(new String[] { "su", "-c", "ls"});
            process.waitFor();
            process.destroy();
            return checkRootPermission();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
