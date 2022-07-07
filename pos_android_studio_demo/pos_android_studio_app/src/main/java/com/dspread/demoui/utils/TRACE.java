package com.dspread.demoui.utils;

import android.util.Log;

import com.dspread.demoui.BaseApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TRACE {
    public static String NEW_LINE = System.getProperty("line.separator");
    private static String deviceInfo;

    static {
        deviceInfo = "DeviceName:" + PhoneDeviceInfoUtil.getManufacturer() + "####DeviceModel:"
                + PhoneDeviceInfoUtil.getModel() + "####AndroidSystemVersion:"
                + PhoneDeviceInfoUtil.getAndroidVersion();
    }

    public static StringBuilder sb = new StringBuilder(deviceInfo);
    private static String AppName = "POS_SDK";
    private static Boolean isTesting = true;

    public static void i(String string) {
        if (isTesting) {
            Log.i(AppName, string);
            postFireBaseLogChild(AppName, string);//each log
        }
    }

    public static void w(String string) {
        if (isTesting) {
            Log.e(AppName, string);
            postFireBaseLogChild(AppName, string);//each log
        }
    }

    public static void e(Exception exception) {
        if (isTesting) {
            Log.e(AppName, exception.toString());
            //getFireBaseLogChildHelper(AppName,  exception.toString());//each log
        }
    }

    public static void d(String string) {
        if (isTesting) {
            Log.d(AppName, string);
            String nowTime = DateUtils.getNowTime();
            sb.append(nowTime).append(":").append(string).append("####");
            postFireBaseLogChild(AppName, string);//each log
        }
    }

    public static void a(int num) {
        if (isTesting) {
            Log.d(AppName, Integer.toString(num));
        }
    }

    /**
     * Update the log to the real-time database
     *
     * @param appName
     * @param strLog
     */
    private static void postFireBaseLogChild(String appName, String strLog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.child(PhoneDeviceInfoUtil.getDeviceID(BaseApplication.getApplicationInstance)).setValue(deviceInfo);
        DatabaseReference refChild = database.getReference(PhoneDeviceInfoUtil.getDeviceID(BaseApplication.getApplicationInstance));
        refChild.child(DateUtils.getNowTime()).setValue(strLog);
    }

}