package com.dspread.demoui.utils;

import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dspread.demoui.BaseApplication;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import io.sentry.Scope;
import io.sentry.ScopeCallback;
import io.sentry.Sentry;
import io.sentry.android.core.SentryAndroid;
import io.sentry.protocol.User;

public class TRACE {
    public static String NEW_LINE = System.getProperty("line.separator");

    private static String AppName = "POS_LOG";
    private static Boolean isTesting = true;

    public static void i(String string) {
        if (isTesting) {
            Log.i(AppName, string);
            Sentry.captureMessage(string);
        }
    }

    public static void w(String string) {
        if (isTesting) {
            Log.e(AppName, string);
            Sentry.captureMessage(string);
        }
    }

    public static void e(Exception exception) {
        if (isTesting) {
            //Log.e(AppName, exception.toString());

        }
    }

    public static void d(String string) {
        if (isTesting) {
            Log.d(AppName, string);
            String posID = BaseApplication.getmPosID();
            User user = new User();
            user.setId(posID);
            Sentry.setUser(user);
            Sentry.captureMessage(string);
        }
    }

    public static void a(int num) {
        if (isTesting) {
            Log.d(AppName, Integer.toString(num));
        }
    }
}