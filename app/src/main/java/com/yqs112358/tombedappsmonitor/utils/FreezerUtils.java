package com.yqs112358.tombedappsmonitor.utils;

import com.topjohnwu.superuser.Shell;

import java.util.List;

public class FreezerUtils {

    private final String checkV2UidCommand
            = "if [ -e /sys/fs/cgroup/uid_0/cgroup.freeze ]; then echo 'true'; else echo 'false'; fi";

    private final String checkV2FrozenCommand
            = "if [[ -e /sys/fs/cgroup/frozen/cgroup.freeze ]] && [[ -e /sys/fs/cgroup/unfrozen/cgroup.freeze ]]; then echo 'true'; else echo 'false'; fi";

    private final String checkV1FrozenCommand
            = "if [ -e /sys/fs/cgroup/freezer/perf/frozen/freezer.state ]; then echo 'true'; else echo 'false'; fi";

    public final String freezerCheckError = "Fail to check freezer status!";


    public boolean doesSupportFreezerV2Uid() throws Error{
        Shell.Result result = Shell.cmd(checkV2UidCommand).exec();
        if (!result.isSuccess())
            throw new Error(freezerCheckError);

        List<String> out = result.getOut();
        if(out.isEmpty())
            return false;
        return out.get(0).contains("true");
    }

    public boolean doesSupportFreezerV2Frozen() throws Error{
        Shell.Result result = Shell.cmd(checkV2FrozenCommand).exec();
        if (!result.isSuccess())
            throw new Error(freezerCheckError);

        List<String> out = result.getOut();
        if(out.isEmpty())
            return false;
        return out.get(0).contains("true");
    }

    public boolean doesSupportFreezerV1Frozen() throws Error{
        Shell.Result result = Shell.cmd(checkV1FrozenCommand).exec();
        if (!result.isSuccess())
            throw new Error(freezerCheckError);

        List<String> out = result.getOut();
        if(out.isEmpty())
            return false;
        return out.get(0).contains("true");
    }
}
