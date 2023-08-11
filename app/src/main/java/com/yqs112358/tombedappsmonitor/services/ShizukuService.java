package com.yqs112358.tombedappsmonitor.services;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.yqs112358.tombedappsmonitor.BuildConfig;
import com.yqs112358.tombedappsmonitor.IUserService;

import rikka.shizuku.Shizuku;

public class ShizukuService {
    public static int SHIZUKU_REQUEST_CODE = 0;

    public static String SHIZUKU_NO_FOUND = "Shizuku no found!";
    public static String SHIZUKU_TOO_OLD = "Shizuku version is too old!";

    // request permission of shizuku
    public static void requestPermission() throws Error {
        if (!Shizuku.pingBinder())
            throw new Error(SHIZUKU_NO_FOUND);
        if(Shizuku.isPreV11())
            throw new Error(SHIZUKU_TOO_OLD);

        Shizuku.requestPermission(SHIZUKU_REQUEST_CODE);
    }

    // check permission of shizuku (and get if not granted)
    public static boolean checkPermission() throws Error {
        if (!Shizuku.pingBinder())
            throw new Error(SHIZUKU_NO_FOUND);
        if(Shizuku.isPreV11())
            throw new Error(SHIZUKU_TOO_OLD);

        if(Shizuku.checkSelfPermission() == PERMISSION_GRANTED)
            return true;
        else if (Shizuku.shouldShowRequestPermissionRationale()) {
            // Users choose "Deny and don't ask again"
            return false;
        } else {
            // Request the permission
            ShizukuService.requestPermission();
            return false;
        }
    }

    // remote user service connection functions
    private static final ServiceConnection userServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            if (binder != null && binder.pingBinder()) {
                IUserService service = IUserService.Stub.asInterface(binder);
                try {
                    service.doSomething();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("TombedMonitor", "invalid binder for " + componentName + " received");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // service disconnected
        }
    };

    // remote user service args
    private static final Shizuku.UserServiceArgs userServiceArgs =
            new Shizuku.UserServiceArgs(new ComponentName(BuildConfig.APPLICATION_ID, RemoteUserService.class.getName()))
                    .daemon(false)
                    .processNameSuffix("service")
                    .debuggable(BuildConfig.DEBUG)
                    .version(BuildConfig.VERSION_CODE);

    // start remote user service
    public static void startRemoteUserService() throws Error {
        try {
            if (Shizuku.getVersion() < 10) {
                throw new Error(SHIZUKU_TOO_OLD);
            } else {
                Shizuku.bindUserService(userServiceArgs, userServiceConnection);
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    // stop remote user service
    public static void stopRemoteUserService() throws Error {
        try {
            if (Shizuku.getVersion() < 10) {
                throw new Error(SHIZUKU_TOO_OLD);
            } else {
                Shizuku.unbindUserService(userServiceArgs, userServiceConnection, true);
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }
}
