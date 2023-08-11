package com.yqs112358.tombedappsmonitor.services;

import android.content.Context;
import android.os.RemoteException;
import android.system.Os;
import android.util.Log;

import androidx.annotation.Keep;

import com.yqs112358.tombedappsmonitor.IUserService;

public class RemoteUserService extends IUserService.Stub {

    /**
     * Constructor is required.
     */
    public RemoteUserService() {
        Log.i("UserService", "constructor");
    }

    /**
     * Constructor with Context. This is only available from Shizuku API v13.
     */
    @Keep
    public RemoteUserService(Context context) {
        Log.i("UserService", "constructor with Context: context=" + context.toString());
    }

    /**
     * Reserved destroy method
     */
    @Override
    public void destroy() {
        Log.i("TombedMonitor", "destroy");
        System.exit(0);
    }

    @Override
    public void exit() {
        destroy();
    }

    @Override
    public String doSomething() throws RemoteException {
        return "************************* pid=" + Os.getpid() + ", uid=" + Os.getuid();
    }
}
