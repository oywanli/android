package com.dspread.demoui.activity.printer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.bean.PrintLineStyle;

public class PrintFunctionMultiActivity extends BaseActivity implements View.OnClickListener {
    private PrintLineStyle printLineStyle;
    private TextView tvInfo;
    private Button btnPrint;
    private ImageView ivBackTitle;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnPrint = findViewById(R.id.btn_Print);
        tvInfo = findViewById(R.id.tvInfo);
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.function_multi));
        btnPrint.setOnClickListener(this);
        ivBackTitle.setOnClickListener(this);
        printLineStyle = new PrintLineStyle();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_print_function_multi;
    }

    @Override
    protected void onReturnPrintResult(boolean isSuccess, String status, PrinterDevice.ResultType resultType) {
        btnPrint.setEnabled(true);
        Log.w("printResult", "boolean b==" + isSuccess);
        Log.w("printResult", "String s==" + status);
        Log.w("printResult", "resultType==" + resultType.toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrinter != null) {
            mPrinter.close();
        }
    }
}