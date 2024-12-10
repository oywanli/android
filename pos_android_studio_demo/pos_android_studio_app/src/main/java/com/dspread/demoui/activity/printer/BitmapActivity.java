package com.dspread.demoui.activity.printer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dspread.demoui.R;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

public class BitmapActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBackTitle;
    private TextView tvTitle;
    private ImageView bitmapImage;
    private Button btnBitmapPrint;
    private PrintLineStyle printLineStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bitmap;
    }

    @Override
    protected void initView() {
        super.initView();
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.print_bitmap));
        bitmapImage = findViewById(R.id.bitmap_image);
        btnBitmapPrint = findViewById(R.id.btn_bitmap_print);
        ivBackTitle.setOnClickListener(this);
        btnBitmapPrint.setOnClickListener(this);
        printLineStyle = new PrintLineStyle();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.btn_bitmap_print:
                try {
                    if (mPrinter != null) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                        mPrinter.setFooter(30);
                        printLineStyle.setAlign(PrintLine.CENTER);
                        mPrinter.setPrintStyle(printLineStyle);
                        mPrinter.printBitmap(this, bitmap);
                        btnBitmapPrint.setEnabled(true);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);

                }
                break;
            default:
                break;

        }
    }


    @Override
    protected void onReturnPrintResult(boolean isSuccess, String status, PrinterDevice.ResultType resultType) {
        btnBitmapPrint.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrinter != null) {
            mPrinter.close();
        }
    }
}