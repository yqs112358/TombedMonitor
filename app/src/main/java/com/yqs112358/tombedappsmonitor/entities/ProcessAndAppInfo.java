package com.yqs112358.tombedappsmonitor.entities;

import android.graphics.drawable.Drawable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessAndAppInfo {

    public enum FrozenType {
        None, FreezerV1, FreezerV2, SIGSTOP, MaybeV2
    }
    public enum Status {
        Unknown, Sleeping, Running, DiskSleep, Idle, Stopped, Tracked, Zombie, Died
    }

    String processName = "";
    String user = "";

    Boolean isApp = false;
    String packageName = "";
    String appName = "";
    Drawable appIcon = null;

    Status status = Status.Unknown;
    FrozenType frozenType = FrozenType.None;
}
