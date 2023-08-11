// IUserService.aidl
package com.yqs112358.tombedappsmonitor;

// Declare any non-default types here with import statements

interface IUserService {

    void destroy() = 16777114; // Destroy method defined by Shizuku server

    void exit() = 1; // Exit method defined by user

    String doSomething() = 2;
}