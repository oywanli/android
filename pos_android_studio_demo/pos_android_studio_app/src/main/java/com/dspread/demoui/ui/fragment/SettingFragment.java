package com.dspread.demoui.ui.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static com.dspread.demoui.ui.dialog.Mydialog.BLUETOOTH;
import static com.dspread.demoui.ui.dialog.Mydialog.UART;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dspread.demoui.R;
import com.dspread.demoui.activity.PaymentActivity;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.TitleUpdateListener;

public class SettingFragment extends Fragment implements View.OnClickListener {
    TitleUpdateListener myListener;
    private RelativeLayout btnSettingConnType;
    private RadioButton rBtnBlue, rBtnSerialPort, rBtnUsb;
    private RadioGroup rgType;
    private TextView tvConnectType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (TitleUpdateListener) getActivity();
        myListener.sendValue(getString(R.string.menu_setting));

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        myListener.sendValue(getString(R.string.menu_setting));
        initView(view);
        SharedPreferencesUtil connectType = SharedPreferencesUtil.getmInstance(getActivity());
        String conType = (String) connectType.get( "conType", "");
        Log.w("setting","contyep=="+conType);
        if (!"".equals(conType) && "blue".equals(conType)) {
            rBtnBlue.setEnabled(true);
            rgType.check(R.id.rbtn_blue);

        } else if (!"".equals(conType) && "uart".equals(conType)) {
            rBtnSerialPort.setEnabled(true);
            rgType.check(R.id.rbtn_serialport);

        } else if (!"".equals(conType) && "usb".equals(conType)) {
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

                SharedPreferencesUtil conType = SharedPreferencesUtil.getmInstance(getActivity());
                switch (checkedId) {
                    case R.id.rbtn_blue:
                        bluetoothRelaPer();
                        conType.put( "conType", "blue");
                        tvConnectType.setText(getString(R.string.setting_blu));
//                        closeUart();
                        break;
                    case R.id.rbtn_serialport:
                        conType.put( "conType", "uart");
                        tvConnectType.setText(getString(R.string.setting_uart));
                        disconnectbluetooth();
                        break;
                    case R.id.rbtn_usb:
                        conType.put( "conType", "usb");
                        tvConnectType.setText(getString(R.string.setting_usb));
                        disconnectbluetooth();
//                        closeUart();
                        break;
                    default:
                        break;
                }
            }
        });

    }


    public void disconnectbluetooth() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("connect_type", BLUETOOTH);
        intent.putExtra("disblue", "disblue");
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void closeUart() {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("connect_type", UART);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private int REQUEST_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 2) {
//            String info = data.getStringExtra("info");
//            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }
    private static final int BLUETOOTH_CODE = 100;
    private static final int LOCATION_CODE = 101;
    LocationManager lm;//【Location management】
    public void bluetoothRelaPer() {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {//if bluetooth is disabled, add one fix
            Intent enabler = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enabler);
        }
        lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//Location service is on
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission denied
                // Request authorization
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                        String[] list = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_ADVERTISE};
                        ActivityCompat.requestPermissions(getActivity(), list, BLUETOOTH_CODE);

                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                }
//                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "System detects that the GPS location service is not turned on", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                }
            });
            launcher.launch(intent);


        }
    }

}