package com.dspread.demoui.activity.printer;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dspread.demoui.R;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;


public class PrinterStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBackTitle;
    private TextView tvTitle;
    private Button btnGetstatus;
    private Button btnGetDensity;
    private Button btnGetSpeed;
    private Button btnGetTemperature;
    private Button btnGetVoltage;
    private TextView tvPrintStatusInfo;
    private PrinterDevice mPrinter;
    private PrintLineStyle printLineStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_printer_status);
        initView();
        PrinterManager instance = PrinterManager.getInstance();
        mPrinter = instance.getPrinter();
        mPrinter.initPrinter(this);
        MyPrinterListener myPrinterListener = new MyPrinterListener();
        mPrinter.setPrintListener(myPrinterListener);
        printLineStyle = new PrintLineStyle();
        int fontSize = printLineStyle.getFontSize();
    }

    private void initView() {
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.get_printer_status));
        btnGetstatus = findViewById(R.id.btn_getstatus);
        btnGetDensity = findViewById(R.id.btn_get_density);
        btnGetSpeed = findViewById(R.id.btn_get_speed);
        btnGetTemperature = findViewById(R.id.btn_get_temperature);
        btnGetVoltage = findViewById(R.id.btn_get_voltage);
        tvPrintStatusInfo = findViewById(R.id.tv_printStatusInfo);
        ivBackTitle.setOnClickListener(this);
        btnGetstatus.setOnClickListener(this);
        btnGetDensity.setOnClickListener(this);
        btnGetSpeed.setOnClickListener(this);
        btnGetTemperature.setOnClickListener(this);
        btnGetVoltage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.btn_getstatus:
                try {
                    mPrinter.getPrinterStatus();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn_get_density:
                try {
                    mPrinter.getPrintDensity();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn_get_speed:
                try {
                    mPrinter.getPrintSpeed();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn_get_temperature:
                try {
                    mPrinter.getPrintTemperature();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn_get_voltage:
                try {
                    mPrinter.getPrintVoltage();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
    }

    class MyPrinterListener implements PrintListener {

        @Override
        public void printResult(boolean b, String s, int i) {
            Log.w("printResult", "boolean b==" + b);
            Log.w("printResult", "String s==" + s);
            Log.w("printResult", "int i==" + i);
            tvPrintStatusInfo.setText(s);

        }
    }
}