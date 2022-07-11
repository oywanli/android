package com.dspread.demoui.utils;

import com.dspread.demoui.BaseApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Tools for uploading logs
public class PostLogToBaseFireHelper {
    private static String deviceInfo;

    static {
        deviceInfo = "DeviceName:" + PhoneDeviceInfoUtil.getManufacturer() + "####DeviceModel:"
                + PhoneDeviceInfoUtil.getModel() + "####AndroidSystemVersion:"
                + PhoneDeviceInfoUtil.getAndroidVersion();
    }

    /**
     * Update the log to the real-time database
     *
     * @param appName
     * @param strLog
     */
    public static void postFireBaseLogChild(String appName, String strLog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.child(PhoneDeviceInfoUtil.getDeviceID(BaseApplication.getApplicationInstance)).setValue(deviceInfo);
        DatabaseReference refChild = database.getReference(PhoneDeviceInfoUtil.getDeviceID(BaseApplication.getApplicationInstance));
        refChild.child(DateUtils.getNowTime()).setValue(appName + ":" + strLog);
    }
}
