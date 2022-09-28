package com.dspread.demoui.utils;

import com.dspread.demoui.BaseApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Tools for uploading logs
public class PostLogToBaseFireHelper {
    private static String deviceInfo;
    private static String childNode;

    static {
        deviceInfo = "DeviceName:" + PhoneDeviceInfoUtil.getManufacturer() + "--DeviceModel:"
                + PhoneDeviceInfoUtil.getModel() + "--OSVersion:"
                + PhoneDeviceInfoUtil.getAndroidVersion();
        String childNodeStr = deviceInfo + "--" + "AndroidID:" + PhoneDeviceInfoUtil.getDeviceID(BaseApplication.getApplicationInstance);
        childNode = childNodeStr.replace(".", "-");
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
        myRef.child(childNode).setValue(deviceInfo);
        DatabaseReference refChild = database.getReference(childNode);
        refChild.child(DateUtils.getNowTime()).setValue(appName + ":" + strLog);
    }
}
