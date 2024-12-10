package com.dspread.demoui.activity.printer;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.action.printerservice.barcode.Barcode1D;
import com.action.printerservice.barcode.Barcode2D;
import com.dspread.demoui.R;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

public class PrintTicketActivity extends BaseActivity implements View.OnClickListener {
    private PrintLineStyle printLineStyle;
    private ImageView ivBackTitle;
    private TextView tvTitle;
    private Button btnComposite;
    private Button btnMulti;
    private Button btnStopPrint;
    private Button btnPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_print_ticket;
    }

    @Override
    protected void initView() {
        super.initView();
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.print_ticket));
        btnComposite = findViewById(R.id.btn_composite);
        btnMulti = findViewById(R.id.btn_multi);
        btnStopPrint = findViewById(R.id.btn_stopprint);
        btnPrint = findViewById(R.id.btn_Print);
        ivBackTitle.setOnClickListener(this);
        btnComposite.setOnClickListener(this);
        btnMulti.setOnClickListener(this);
        btnStopPrint.setOnClickListener(this);
    }

    @Override
    protected void onReturnPrintResult(boolean isSuccess, String status, PrinterDevice.ResultType resultType) {
        btnPrint.setEnabled(true);
        Log.w("printResult", "boolean b==" + isSuccess);
        Log.w("printResult", "String s==" + status);
        Log.w("printResult", "resultType==" + resultType.toString());
    }

    private void printtext() {
        try {
            if (mPrinter != null) {
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.BOLD, PrintLine.CENTER, 16));
                mPrinter.addText("Testing");
                mPrinter.addText("POS Signing of purchase orders");
                mPrinter.addText("MERCHANT COPY");
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 14));
                mPrinter.addText("- - - - - - - - - - - - - -");
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.LEFT, 14));
                mPrinter.addText("ISSUER Agricultural Bank of China");
                mPrinter.addText("ACQ 48873110");
                mPrinter.addText("CARD number.");
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.LEFT, 14));
                mPrinter.addText("6228 48******8 116 S");
                mPrinter.addText("TYPE of transaction(TXN TYPE)");
                mPrinter.addText("SALE");
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 14));
                mPrinter.addText("- - - - - - - - - - - - - -");
                mPrinter.addTexts(new String[]{"BATCH NO", "000043"}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"VOUCHER NO", "000509"}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"AUTH NO", "000786"}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"DATE/TIME", "2010/12/07 16:15:17"}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"REF NO", "000001595276"}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"2014/12/07 16:12:17", ""}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addTexts(new String[]{"AMOUNT:", ""}, new int[]{5, 5}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                mPrinter.addText("RMB:249.00");
                mPrinter.addPrintLintStyle(new PrintLineStyle(PrintStyle.FontStyle.NORMAL, PrintLine.CENTER, 12));
                mPrinter.addText("- - - - - - - - - - - - - -");
                mPrinter.addText("Please scan the QRCode for getting more information: ");
                mPrinter.addBarCode(this, Barcode1D.CODE_128.name(), 400, 100, "123456", PrintLine.CENTER);
                mPrinter.addText("Please scan the QRCode for getting more information:");
                mPrinter.addQRCode(300, Barcode2D.QR_CODE.name(), "123456", PrintLine.CENTER);
                mPrinter.setFooter(20);
                mPrinter.print(this);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.btn_composite:
                printtext();
                break;
            case R.id.btn_Print:

                printtext();
                btnPrint.setEnabled(false);
                break;
            case R.id.btn_multi:
                try {
                    mPrinter.addTexts(new String[]{"TEST1"}, new int[]{1}, new int[]{PrintStyle.Alignment.NORMAL});
                    mPrinter.addTexts(new String[]{"TEST1", "TEST2"}, new int[]{1, 4}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER});
                    mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3"}, new int[]{1, 2, 2}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3", "TEST4"}, new int[]{1, 1, 1, 2}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addTexts(new String[]{"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"}, new int[]{1, 1, 1, 1, 1}, new int[]{PrintStyle.Alignment.NORMAL, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.CENTER, PrintStyle.Alignment.ALIGN_OPPOSITE});
                    mPrinter.addText(" ");
                    mPrinter.print(this);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case R.id.btn_stopprint:
                try {
                    mPrinter.stopPrint();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
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