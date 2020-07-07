package com.jerryzhu.lib.breakpad;
/**
 * @Auther: JerryZhu
 * @datetime: 2020/7/7
 */
public class Breakpad {

    static{
        System.loadLibrary("breakpad_core");
    }

    private static native void nativeBreakpadInit(String path);

    public static void init(String path){
        nativeBreakpadInit(path);
    }
}
