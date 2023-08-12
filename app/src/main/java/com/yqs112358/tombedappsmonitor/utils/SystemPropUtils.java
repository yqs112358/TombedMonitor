package com.yqs112358.tombedappsmonitor.utils;

import com.topjohnwu.superuser.Shell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemPropUtils {

    public static int getAndroidVersion() {
        Shell.Result result = Shell.cmd("getprop ro.build.version.release").exec();
        if (!result.isSuccess())
            return 0;

        List<String> out = result.getOut();
        if(out.isEmpty())
            return 0;
        return Integer.parseInt(out.get(0));
    }

    public static int getAndroidApiVersion() {
        Shell.Result result = Shell.cmd("getprop ro.build.version.sdk").exec();
        if (!result.isSuccess())
            return 0;

        List<String> out = result.getOut();
        if(out.isEmpty())
            return 0;
        return Integer.parseInt(out.get(0));
    }

    public static String getLinuxCoreVersion() {
        Shell.Result result = Shell.cmd("uname -r").exec();
        if (!result.isSuccess())
            return "";

        List<String> out = result.getOut();
        if(out.isEmpty())
            return "";
        return out.get(0);
    }
}
