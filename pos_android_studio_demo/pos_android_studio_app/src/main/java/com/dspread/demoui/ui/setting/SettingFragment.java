package com.dspread.demoui.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dspread.demoui.R;
import com.dspread.demoui.activity.PaymentActivity;
import com.dspread.demoui.utils.MyListener;
import com.dspread.demoui.utils.SharedPreferencesUtil;

public class SettingFragment extends Fragment implements View.OnClickListener {
    MyListener myListener;
    private RelativeLayout btnSettingConnType;
    private RadioButton rBtnBlue, rBtnSerialPort, rBtnUsb;
    private RadioGroup rgType;
    private TextView tvConnectType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (MyListener) getActivity();
        myListener.sendValue(getString(R.string.menu_setting));

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        myListener.sendValue(getString(R.string.menu_setting));
        initView(view);
        SharedPreferencesUtil connectType = SharedPreferencesUtil.getmInstance();
        String conType = (String) connectType.get(getActivity(), "conType", "");
        if (conType.equals("blue")) {
            rBtnBlue.setEnabled(true);
            rgType.check(R.id.rbtn_blue);

        } else if (conType.equals("uart")) {
            rBtnSerialPort.setEnabled(true);
            rgType.check(R.id.rbtn_serialport);

        } else if (conType.equals("usb")) {
            rBtnUsb.setEnabled(true);
            rgType.check(R.id.rbtn_usb);

        }

        return view;


    }

    private void initView(View view) {
        btnSettingConnType = view.findViewById(R.id.btn_setting_conntype);
        rBtnBlue = view.findViewById(R.id.rbtn_blue);
        rBtnSerialPort = view.findViewById(R.id.rbtn_serialport);
        rBtnUsb = view.findViewById(R.id.rbtn_usb);
        rgType = view.findViewById(R.id.rg_type);
        tvConnectType = view.findViewById(R.id.tv_connect_type);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SharedPreferencesUtil conType = SharedPreferencesUtil.getmInstance();
                switch (checkedId) {
                    case R.id.rbtn_blue:
                        conType.put(getActivity(), "conType", "blue");
                        tvConnectType.setText(getString(R.string.setting_blu));
                        disbuart();
                        break;
                    case R.id.rbtn_serialport:
                        conType.put(getActivity(), "conType", "uart");
                        tvConnectType.setText(getString(R.string.setting_uart));
                        disbluetooth();
                        break;
                    case R.id.rbtn_usb:
                        conType.put(getActivity(), "conType", "usb");
                        tvConnectType.setText(getString(R.string.setting_usb));
                        disbluetooth();
                        disbuart();
                        break;
                    default:
                        break;
                }
            }
        });


//
//        //根据ID获取到开关按钮
//        Switch swtTest = view.findViewById(R.id.swtTest);
//
//        //给开关按钮设置监听状态改变事件
//        swtTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
//
//                if (flag){
//                    System.out.println("开关按钮的状态  开= " + flag);
//                }else{
//                    System.out.println("开关按钮的状态  关= " + flag);
//                }
//            }
//        });


    }


    public void disbluetooth() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("connect_type", 1);
        intent.putExtra("disblue", "disblue");
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void disbuart() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("connect_type", 1);
        intent.putExtra("disbuart", "disbuart");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_conntype:
                break;
            default:
                break;
        }
    }


    private int REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 2) {
            String info = data.getStringExtra("info");
//            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }


}