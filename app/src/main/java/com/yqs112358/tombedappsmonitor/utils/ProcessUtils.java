package com.yqs112358.tombedappsmonitor.utils;

import com.topjohnwu.superuser.Shell;
import com.yqs112358.tombedappsmonitor.entities.ProcessAndAppInfo;

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
        put("D", ProcessAndAppInfo.Status.D);
    }};

    // process frozen type map
    private static final HashMap<String, ProcessAndAppInfo.FrozenType> processFrozenTypeMap
            = new HashMap<String, ProcessAndAppInfo.FrozenType>()
    {{
        put("__refrigerator", ProcessAndAppInfo.FrozenType.FreezerV1);
        put("do_freezer_trap", ProcessAndAppInfo.FrozenType.FreezerV2);
        put("do_signal_stop", ProcessAndAppInfo.FrozenType.SIGSTOP);
        put("get_signal", ProcessAndAppInfo.FrozenType.IncompleteV2);
    }};


    // Map<processName, info>
    public static Map<String, ProcessAndAppInfo> getAllProcessesInfo() throws Throwable {
        // Execute commands synchronously
        Shell.Result result = Shell.cmd(queryCommand).exec();
        if (!result.isSuccess())
            return null;

        Map<String, ProcessAndAppInfo> resMap = new HashMap<String, ProcessAndAppInfo>();
        List<String> out = result.getOut();  // stdout
        for (String line : out) {
            String[] datas = line.split("\\s+");

            String processName = datas[8];
            if(ignoreProcessesList.contains(processName))
                continue;           // skip this line

            // process names
            ProcessAndAppInfo res = new ProcessAndAppInfo();
            res.setProcessName(processName);
            if(!processName.contains("[") && !processName.contains("]"))
            {
                // maybe app
                String packageName = processName.split(":")[0];
                if(AppPackageUtils.isPackageName(packageName))
                {
                    // actually app
                    res.setIsApp(true);
                    res.setPackageName(packageName);
                    res.setAppName(AppPackageUtils.getPackageAppName(packageName));
                    res.setAppIcon(AppPackageUtils.getPackageAppIcon(packageName));
                }
            }

            // process other data
            res.setUser(datas[0]);
            res.setStatus(processStatusMap.getOrDefault(datas[7], ProcessAndAppInfo.Status.Unknown));
            res.setFrozenType(processFrozenTypeMap.getOrDefault(datas[5], ProcessAndAppInfo.FrozenType.None));

            resMap.put(processName, res);
        }
        return resMap;
    }
}