package com.dspread.demoui;

import android.app.Application;
import android.content.Context;

import xcrash.XCrash;

public class BaseApplication extends Application {
    public static Context getApplicationInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  Default init
        XCrash.init(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationInstance = this;
    }
}
