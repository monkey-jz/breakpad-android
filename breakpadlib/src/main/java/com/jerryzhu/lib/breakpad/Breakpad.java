package com.jerryzhu.lib.breakpad;

public class Breakpad {

    static{
        System.loadLibrary("breakpad_core");
    }


    private static native void nativeBreakpadInit(String path);

    public static void init(String path){
        nativeBreakpadInit(path);
    }
}
