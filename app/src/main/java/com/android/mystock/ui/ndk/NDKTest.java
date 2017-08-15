package com.android.mystock.ui.ndk;

/**
 * Created by xiaowh on 2017/5/27.
 */

public class NDKTest {
    static {
        System.loadLibrary("jniTest");
    }
    public static native String getString();
}
