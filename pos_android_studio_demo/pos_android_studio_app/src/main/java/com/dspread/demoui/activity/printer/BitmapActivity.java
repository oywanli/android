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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

public class BitmapActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBackTitle;
    private TextView tvTitle;
    private ImageView bitmapImage;
    private Button btnBitmapPrint;
    private PrinterDevice mPrinter;
    private PrintLineStyle printLineStyle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_bitmap);
        initView();
        PrinterManager instance = PrinterManager.getInstance();
        mPrinter = instance.getPrinter();
        mPrinter.initPrinter(this);
        MyPrinterListener myPrinterListener = new MyPrinterListener();
        mPrinter.setPrintListener(myPrinterListener);
        printLineStyle = new PrintLineStyle();
    }

    private void initView() {
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.print_bitmap));
        bitmapImage = findViewById(R.id.bitmap_image);
        btnBitmapPrint = findViewById(R.id.btn_bitmap_print);
        ivBackTitle.setOnClickListener(this);
        btnBitmapPrint.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.btn_bitmap_print:
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                    mPrinter.printBitmap(this,bitmap);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);

                }
                break;

        }
    }
    class MyPrinterListener implements PrintListener {

        @Override
        public void printResult(boolean b, String s, int i) {
            Log.w("printResult", "boolean b==" + b);
            Log.w("printResult", "String s==" + s);
            Log.w("printResult", "int i==" + i);

        }
    }
}