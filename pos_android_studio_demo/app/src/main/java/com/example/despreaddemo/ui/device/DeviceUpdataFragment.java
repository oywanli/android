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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DeviceUpdataFragment extends Fragment implements View.OnClickListener {
    MyListener myListener;
    private RelativeLayout updateIpek;
//    private RelativeLayout updateGetsleeptime;
//    private RelativeLayout updateEmvapp;
//    private RelativeLayout updateEmvrid;
    private RelativeLayout setMasterkey;
//    private RelativeLayout rlClear;
    private RelativeLayout updateWorkkey;
    private RelativeLayout updateFirmware;
    private RelativeLayout updateEmvByXml;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (MyListener) getActivity();
        myListener.sendValue(getString(R.string.device_update));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_device_updata, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        updateIpek =view.findViewById(R.id.update_ipek);
        setMasterkey = view.findViewById(R.id.set_masterkey);
        updateWorkkey = view.findViewById(R.id.update_workkey);
        updateFirmware = view.findViewById(R.id.update_firmware);
        updateEmvByXml = view.findViewById(R.id.update_emvByXml);
        updateIpek.setOnClickListener(this);
        setMasterkey.setOnClickListener(this);
        updateWorkkey.setOnClickListener(this);
        updateFirmware.setOnClickListener(this);
        updateEmvByXml.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_ipek:
                updateDevice("updateIpeK");
                break;
            case R.id.set_masterkey:
                updateDevice("setMasterkey");
                break;
            case R.id.update_workkey:
                updateDevice("updateWorkkey");
                break;
            case R.id.update_firmware:
                updateDevice("updateFirmware");
                break;
            case R.id.update_emvByXml:
                updateDevice("updateEmvByXml");
                break;
            default:
                break;
        }
    }

    public void updateDevice(String updatedevice){

        SharedPreferencesUtil connectType=SharedPreferencesUtil.getmInstance();
        String conType= (String) connectType.get(getActivity(),"conType","");
        if (conType.equals("blue")){
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("deviceUpdate",updatedevice);
            intent.putExtra("connect_type", 1);
            startActivityForResult(intent, REQUEST_CODE);
        }else if(conType.equals("uart")){
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("deviceUpdate",updatedevice);
            intent.putExtra("connect_type", 2);
            startActivityForResult(intent, REQUEST_CODE);
        }else if(conType.equals("usb")){
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra("deviceUpdate",updatedevice);
            intent.putExtra("connect_type", 3);
            startActivityForResult(intent, REQUEST_CODE);
        }


    }

    private int REQUEST_CODE = 3;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 2) {
            String info = data.getStringExtra("info");
            Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
        }
    }

}