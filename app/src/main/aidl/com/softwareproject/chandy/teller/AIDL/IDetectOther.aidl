// IDetectOther.aidl
package com.softwareproject.chandy.teller.AIDL;

// Declare any non-default types here with import statements

interface IDetectOther {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean isRunningOther(int time);
    void stop();
    int getRunTimes();
}
