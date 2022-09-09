package com.dspread.demoui.activities.serialprint;

import android.os.RemoteException;
import android.util.Log;

import com.action.printerservice.ErrorCode;
import com.action.printerservice.IPrinterCallback;

public class MyPrinterCallback extends IPrinterCallback.Stub {

    private static final String TAG = "MyPrinterCallback";

    @Override
    public void onPrintStart() throws RemoteException {
        Log.d(TAG, "onPrintStart");
    }

    @Override
    public void onPrintPause(int reason) throws RemoteException {

        Log.d(TAG, "onPrintPause, reason " + ErrorCode.valueOfString(reason));
    }

    @Override
    public void onPrintResume(int reason) throws RemoteException {

        Log.d(TAG, "onPrintResume, resason " + ErrorCode.valueOfString(reason));
    }

    @Override
    public void onPrintFinish(int height) throws RemoteException {
        Log.d(TAG, "onPrintFinish, height " + height);
    }

    @Override
    public void onError(int error, String message) throws RemoteException {
        Log.d(TAG, String.format("onException, error " + ErrorCode.valueOfString(error) + ", "+ message));
    }
}
