package com.yqs112358.tombedappsmonitor.utils;

import android.graphics.drawable.Drawable;

import com.topjohnwu.superuser.Shell;
import com.yqs112358.tombedappsmonitor.R;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessUtils {

    // <user> <pid> <ppid> <visual-size> <phy-memory> <wchan> <addr> <status> <name>
    private static final String queryCommand
            = "ps -A | grep -E 'refrigerator|do_freezer|signal'";

    // process name ignore list
    private static final List<String> ignoreProcessesList = Arrays.asList("sh", "su");

    // process status map
    private static final HashMap<String, ProcessAndAppInfo.Status> processStatusMap
            = new HashMap<String, ProcessAndAppInfo.Status>()
    {{
        put("S", ProcessAndAppInfo.Status.Sleeping);
        put("R", ProcessAndAppInfo.Status.Running);
        put("I", ProcessAndAppInfo.Status.Idle);
        put("D", ProcessAndAppInfo.Status.DiskSleep);
        put("Z", ProcessAndAppInfo.Status.Zombie);
        put("T", ProcessAndAppInfo.Status.Stopped);
        put("t", ProcessAndAppInfo.Status.Tracked);
        put("X", ProcessAndAppInfo.Status.Died);
    }};

    // process frozen type map
    private static final HashMap<String, ProcessAndAppInfo.FrozenType> processFrozenTypeMap
            = new HashMap<String, ProcessAndAppInfo.FrozenType>()
    {{
        put("__refrigerator", ProcessAndAppInfo.FrozenType.FreezerV1);
        put("do_freezer_trap", ProcessAndAppInfo.FrozenType.FreezerV2);
        put("do_signal_stop", ProcessAndAppInfo.FrozenType.SIGSTOP);
        put("get_signal", ProcessAndAppInfo.FrozenType.MaybeV2);
    }};

    // default process icon
    private static Drawable defaultProcessIcon = null;


    public static List<ProcessAndAppInfo> getAllProcessesInfo() throws Throwable {
        // execute commands synchronously
        Shell.Result result = Shell.cmd(queryCommand).exec();
        if (!result.isSuccess())
            return new ArrayList<ProcessAndAppInfo>();

        List<ProcessAndAppInfo> resList = new ArrayList<ProcessAndAppInfo>();
        List<String> out = result.getOut();  // stdout
        for (String line : out) {
            try {
                String[] datas = line.split("\\s+");

                String processName = datas[8];
                if (ignoreProcessesList.contains(processName))
                    continue;           // skip this line

                // fill basic data
                ProcessAndAppInfo res = new ProcessAndAppInfo();
                res.setPid(Integer.valueOf(datas[1]));
                res.setProcessName(processName);

                // fill app info
                if (defaultProcessIcon == null)
                    defaultProcessIcon = ApplicationUtils.getContext().getResources().getDrawable(R.drawable.linux, null);
                res.setAppIcon(defaultProcessIcon);
                if (!processName.contains("[") && !processName.contains("]")) {
                    // maybe app
                    String packageName = processName.split(":")[0];
                    if (AppPackageUtils.isPackageName(packageName)) {
                        // actually app
                        res.setIsApp(true);
                        res.setPackageName(packageName);
                        res.setAppName(AppPackageUtils.getPackageAppName(packageName));
                        res.setAppIcon(AppPackageUtils.getPackageAppIcon(packageName));
                    }
                }

                // fill other data
                res.setUser(datas[0]);
                String basicStatusChar = datas[7].substring(0, 1);
                res.setStatus(processStatusMap.getOrDefault(basicStatusChar, ProcessAndAppInfo.Status.Unknown));
                res.setFrozenType(processFrozenTypeMap.getOrDefault(datas[5], ProcessAndAppInfo.FrozenType.None));

                resList.add(res);
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return resList;
    }
}
