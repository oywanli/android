package com.dspread.demoui.ui.fragment;

import static com.dspread.demoui.activity.BaseApplication.handler;
import static com.dspread.demoui.activity.BaseApplication.pos;
import static com.dspread.demoui.ui.dialog.Mydialog.BLUETOOTH;
import static com.dspread.demoui.ui.dialog.Mydialog.UART;
import static com.dspread.demoui.ui.dialog.Mydialog.USB_OTG_CDC_ACM;
import static com.dspread.demoui.utils.Utils.getKeyIndex;
import static com.dspread.demoui.utils.Utils.open;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dspread.demoui.R;
import com.dspread.demoui.activity.BaseApplication;
import com.dspread.demoui.activity.MainActivity;
import com.dspread.demoui.activity.MyQposClass;
import com.dspread.demoui.activity.PaymentActivity;
import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.utils.TRACE;
import com.dspread.demoui.utils.TitleUpdateListener;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.xpos.QPOSService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DeviceInfoFragment extends Fragment implements View.OnClickListener {
    TitleUpdateListener myListener;
    private RelativeLayout getPosid;
    private RelativeLayout getPosinfo;
    private RelativeLayout getUpdatekey;
    private RelativeLayout getKeycheckvalue;
    private TextView tvShowPosInfo;
    SharedPreferencesUtil connectType;
    String conType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (TitleUpdateListener) getActivity();
        myListener.sendValue(getString(R.string.device_info));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        getPosid = view.findViewById(R.id.get_posid);
        getPosinfo = view.findViewById(R.id.get_posinfo);
        getUpdatekey = view.findViewById(R.id.get_updatekey);
        getKeycheckvalue = view.findViewById(R.id.get_keycheckvalue);
        tvShowPosInfo = view.findViewById(R.id.tv_pos_info);
        getPosid.setOnClickListener(this);
        getPosinfo.setOnClickListener(this);
        getUpdatekey.setOnClickListener(this);
        getKeycheckvalue.setOnClickListener(this);
        connectType = SharedPreferencesUtil.getmInstance(getActivity());
        conType = (String) connectType.get("conType", "");
        if (conType != null && "uart".equals(conType)) {
            open(QPOSService.CommunicationMode.UART_SERVICE, getActivity());
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        conType = (String) connectType.get("conType", "");
        BaseApplication.getApplicationInstance=getActivity();
        switch (v.getId()) {
            case R.id.get_posid:
                if (conType != null && "uart".equals(conType)) {
                    pos.getQposId();
                } else {
                    getposInfo("posid");
                }
                break;
            case R.id.get_posinfo:
                if (conType != null && "uart".equals(conType)) {
                    pos.getQposInfo();
                } else {
                    getposInfo("posinfo");
                }
                break;
            case R.id.get_updatekey:
                if (conType != null && "uart".equals(conType)) {
                    pos.getUpdateCheckValue();
                } else {
                    getposInfo("updatekey");
                }
                break;

            case R.id.get_keycheckvalue:
                if (conType != null && "uart".equals(conType)) {
                    int keyIdex = getKeyIndex();
                    pos.getKeyCheckValue(keyIdex, QPOSService.CHECKVALUE_KEYTYPE.DUKPT_MKSK_ALLTYPE);
                } else {
                    getposInfo("keycheckvalue");
                }
                break;
            default:
                break;
        }
    }

    public void getposInfo(String posInfo) {
        conType = (String) connectType.get("conType", "");
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        if ("blue".equals(conType)) {
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", BLUETOOTH);
            startActivity(intent);
        } else if ("uart".equals(conType)) {
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", UART);
            startActivity(intent);
        } else if ("usb".equals(conType)) {
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", USB_OTG_CDC_ACM);
            startActivity(intent);
        }


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        conType = (String) connectType.get("conType", "");
        Log.w("onHiddenChanged", "onHiddenChanged==" + conType);
        if (!hidden) {
            if (conType != null && "uart".equals(conType)) {
                open(QPOSService.CommunicationMode.UART_SERVICE, getActivity());
            }
        } else {
            if (conType != null && "uart".equals(conType)) {
                pos.closeUart();
            }

        }

    }

}