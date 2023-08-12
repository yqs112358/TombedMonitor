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
        None, FreezerV1, FreezerV2, SIGSTOP, SIGTSTP, IncompleteV2
    }
    public enum Status {
        Unknown, Sleeping, Running, D, Idle
    }

    String processName;
    String user;

    Boolean isApp;
    String packageName;
    String appName;
    Drawable appIcon;

    Status status;
    FrozenType frozenType;
}