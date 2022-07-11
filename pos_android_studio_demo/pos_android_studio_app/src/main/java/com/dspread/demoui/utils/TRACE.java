package com.dspread.demoui.utils;

import android.util.Log;

public class TRACE {
    public static String NEW_LINE = System.getProperty("line.separator");

    private static String AppName = "POS_LOG";
    private static Boolean isTesting = true;

    public static void i(String string) {
        if (isTesting) {
            Log.i(AppName, string);
            PostLogToBaseFireHelper.postFireBaseLogChild(AppName, string); //each log
        }
    }

    public static void w(String string) {
        if (isTesting) {
            Log.e(AppName, string);
            PostLogToBaseFireHelper.postFireBaseLogChild(AppName, string); //each log
        }
    }

    public static void e(Exception exception) {
        if (isTesting) {
            //Log.e(AppName, exception.toString());
            PostLogToBaseFireHelper.postFireBaseLogChild(AppName, exception.toString()); //each log
        }
    }

    public static void d(String string) {
        if (isTesting) {
            Log.d(AppName, string);
            PostLogToBaseFireHelper.postFireBaseLogChild(AppName, string); //each log
        }
    }

    public static void a(int num) {
        if (isTesting) {
            Log.d(AppName, Integer.toString(num));
        }
    }
}