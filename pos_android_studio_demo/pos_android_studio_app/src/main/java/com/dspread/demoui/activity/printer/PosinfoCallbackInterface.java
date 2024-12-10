package com.dspread.demoui.activity.printer;

import com.dspread.xpos.QPOSService;

import java.util.Hashtable;

public interface PosinfoCallbackInterface {
    void onQposIdResult(Hashtable<String, String> posId);
    void onQposInfoResult(Hashtable<String, String> posInfoData);

    void onRequestUpdateKey(String arg0);

    void onGetKeyCheckValue(Hashtable<String, String> checkValue);

    void onGetDevicePubKey(Hashtable<String, String> clearKeys);

    void onReturnUpdateIPEKResult(boolean isSuccess);

    void onUpdateMasterKeyResult(boolean result, Hashtable<String, String> resultTable);

    void onReturnSetMasterKeyResult(boolean isSuccess, Hashtable<String, String> result);

    void onRequestUpdateWorkKeyResult(QPOSService.UpdateInformationResult result, Hashtable<String, String> keyCheckValue);

    void onRequestUpdateWorkKeyResult(QPOSService.UpdateInformationResult result);

    void onReturnUpdateEMVResult(boolean isSuccess);

    void onUpdatePosFirmwareResult(QPOSService.UpdateInformationResult result);


}
