// IAlertService.aidl
package com.softwareproject.chandy.teller.AIDL;

// Declare any non-default types here with import statements

interface IAlertService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void startAlert(int time);
    void stop(int curTime);
}
