package com.example.despreaddemo.ui.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.despreaddemo.R;
import com.example.despreaddemo.activity.PaymentActivity;
import com.example.despreaddemo.utils.MyListener;
import com.example.despreaddemo.utils.SharedPreferencesUtil;
import com.example.despreaddemo.utils.TRACE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DeviceInfoFragment extends Fragment implements View.OnClickListener {
    MyListener myListener;
    private RelativeLayout getPosid;
    private RelativeLayout getPosinfo;
    private RelativeLayout getUpdatekey;
    private RelativeLayout getKeycheckvalue;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (MyListener) getActivity();
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
        getPosid.setOnClickListener(this);
        getPosinfo.setOnClickListener(this);
        getUpdatekey.setOnClickListener(this);
        getKeycheckvalue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.get_posid:
                getposInfo("posid");
                break;
            case R.id.get_posinfo:
                getposInfo("posinfo");
                break;
            case R.id.get_updatekey:
                getposInfo("updatekey");
                break;

            case R.id.get_keycheckvalue:
                getposInfo("keycheckvalue");
                break;
            default:
                break;
        }
    }

    public void getposInfo(String posInfo) {
        TRACE.d("get pos info :"+posInfo);
        SharedPreferencesUtil connectType = SharedPreferencesUtil.getmInstance();
        String conType = (String) connectType.get(getActivity(), "conType", "");
        if (conType.equals("blue")) {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", 1);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (conType.equals("uart")) {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", 2);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (conType.equals("usb")) {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("posinfo", posInfo);
            intent.putExtra("connect_type", 3);
            startActivityForResult(intent, REQUEST_CODE);
        }


    }


    private int REQUEST_CODE = 3;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 2) {
            String info = data.getStringExtra("info");
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }

}