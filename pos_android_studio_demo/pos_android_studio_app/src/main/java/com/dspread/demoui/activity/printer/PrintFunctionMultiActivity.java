package com.dspread.demoui.activity.printer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterInitListener;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;

public class PrintFunctionMultiActivity extends AppCompatActivity implements View.OnClickListener {
    private PrinterDevice mPrinter;
    private PrintLineStyle printLineStyle;
    private TextView tvInfo;
    private Button btnPrint;
    private ImageView ivBackTitle;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_print_function_multi);
        btnPrint = findViewById(R.id.btn_Print);
        tvInfo = findViewById(R.id.tvInfo);
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.function_multi));
        btnPrint.setOnClickListener(this);
        ivBackTitle.setOnClickListener(this);
        PrinterManager instance = PrinterManager.getInstance();
        mPrinter = instance.getPrinter();

        if (mPrinter == null) {
            PrinterAlertDialog.showAlertDialog(this);
            return;
        }
        if ("D30".equalsIgnoreCase(Build.MODEL)) {
            mPrinter.initPrinter(PrintFunctionMultiActivity.this, new PrinterInitListener() {
                @Override
                public void connected() {
                    mPrinter.setPrinterTerminatedState(PrinterDevice.PrintTerminationState.PRINT_STOP);
                }

                @Override
                public void disconnected() {
                }
            });

        } else {
            mPrinter.initPrinter(this);
        }
        MyPrinterListener myPrinterListener = new MyPrinterListener();
        mPrinter.setPrintListener(myPrinterListener);
        printLineStyle = new PrintLineStyle();
        mPrinter = instance.getPrinter();
    }

    public void printFunctionMulti() {
        try {
            if (mPrinter != null) {
                mPrinter.setFooter(30);
                mPrinter.addTexts(new String[]{"TEST1"}, new int[]{1}, new int[]{PrintStyle.Alignment.NORMAL});
                mPrinter.addTexts(new String[]{"TEST1", "TEST2"}, new int[]{1, 4}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3"}, new int[]{1, 2, 2}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3", "TEST4"}, new int[]{1, 1, 1, 2}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"}, new int[]{1, 1, 1, 1, 1}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                mPrinter.addText(" ");
                mPrinter.print(this);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_title:
                finish();
                break;

            case R.id.btn_Print:
                printFunctionMulti();
                btnPrint.setEnabled(false);
                break;
        }
    }

    class MyPrinterListener implements PrintListener {
        @Override
        public void printResult(boolean b, String s, PrinterDevice.ResultType resultType) {
            btnPrint.setEnabled(true);
            Log.w("printResult", "boolean b==" + b);
            Log.w("printResult", "String s==" + s);
            Log.w("printResult", "resultType==" + resultType.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrinter != null) {
            mPrinter.close();
        }
    }
}