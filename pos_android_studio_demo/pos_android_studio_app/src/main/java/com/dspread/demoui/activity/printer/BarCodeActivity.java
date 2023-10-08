package com.dspread.demoui.activity.printer;

import android.graphics.Bitmap;
import android.os.Build;
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

import com.action.printerservice.barcode.Barcode1D;
import com.dspread.demoui.R;
import com.dspread.demoui.ui.dialog.PrintDialog;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.util.QRCodeUtil;
import com.dspread.print.widget.PrintLine;

public class BarCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBackTitle;
    private TextView tvTitle;
    private TextView brcodeTextContent;
    private LinearLayout brcodeContent;
    private TextView brcodeTextHeight;
    private LinearLayout brcodeHeight;
    private TextView brcodeTextWidth;
    private LinearLayout brcodeWidth;
    private TextView bcTextAlign;
    private LinearLayout brcodeAlign;
    private TextView bcTextGraylevel;
    private LinearLayout brcodeGraylevel;
    private ImageView brcodeImage;
    private TextView bcTextSpeedlevels;
    private LinearLayout brcodeSpeedlevels;
    private Button btnBrcodePrint;
    private TextView brTextDensitylevel;
    private LinearLayout brcodeDensitylevel;
    private PrinterDevice mPrinter;
    private PrintLineStyle printLineStyle;
    private String alignText = "";
    private String brContent = "";
    private String brHeight = "";
    private String brWidth = "";
    private String brGraylevel = "";
    private String brSpeedlevel = "";
    private String brDensitylevel="";
    private int printLineAlign;
    private int height;
    private int width;
    private int grayLevel;
    private int speedLevel;
    private int densityLevel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_bar_code);
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
        tvTitle.setText(getText(R.string.print_brcode));
        brcodeTextContent = findViewById(R.id.brcode_text_content);
        brcodeContent = findViewById(R.id.brcode_content);
        brcodeTextHeight = findViewById(R.id.brcode_text_height);
        brcodeHeight = findViewById(R.id.brcode_height);
        brcodeTextWidth = findViewById(R.id.brcode_text_width);
        brcodeWidth = findViewById(R.id.brcode_width);
        bcTextAlign = findViewById(R.id.bc_text_align);
        brcodeAlign = findViewById(R.id.brcode_align);
        bcTextGraylevel = findViewById(R.id.bc_text_graylevel);
        brcodeGraylevel = findViewById(R.id.brcode_graylevel);
        brcodeImage = findViewById(R.id.brcode_image);
        btnBrcodePrint = findViewById(R.id.btn_brcode_print);
        bcTextSpeedlevels = findViewById(R.id.bc_text_speedlevels);
        brcodeSpeedlevels = findViewById(R.id.brcode_speedlevels);
        brTextDensitylevel = findViewById(R.id.br_text_densitylevel);
        brcodeDensitylevel = findViewById(R.id.brcode_densitylevel);
        if ("mp600".equals(Build.MODEL)) {
            brcodeSpeedlevels.setVisibility(View.VISIBLE);
            brcodeDensitylevel.setVisibility(View.VISIBLE);
        } else {
            brcodeSpeedlevels.setVisibility(View.GONE);
            brcodeDensitylevel.setVisibility(View.GONE);
        }
        ivBackTitle.setOnClickListener(this);
        brcodeContent.setOnClickListener(this);
        brcodeHeight.setOnClickListener(this);
        brcodeWidth.setOnClickListener(this);
        brcodeAlign.setOnClickListener(this);
        brcodeGraylevel.setOnClickListener(this);
        btnBrcodePrint.setOnClickListener(this);
        brcodeSpeedlevels.setOnClickListener(this);
        brcodeDensitylevel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.brcode_content:
                PrintDialog.printInputDialog(BarCodeActivity.this, getString(R.string.input_barcode), new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {
                        PrintDialog.printInputDialog.dismiss();
                    }

                    @Override
                    public void onConfirm(String str) {
                        brcodeTextContent.setText(str);
                        brContent = str;
                    }


                });
                break;
            case R.id.brcode_height:
                PrintDialog.showSeekBarDialog(BarCodeActivity.this, getResources().getString(R.string.Barcode_height), 1, 200, brcodeTextHeight, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        Log.w("height", "heigt=" + str);
                        brcodeTextHeight.setText(str);
                        brHeight = str;

                    }
                });

                break;
            case R.id.brcode_width:
                PrintDialog.showSeekBarDialog(BarCodeActivity.this, getResources().getString(R.string.Barcode_width), 1, 600, brcodeTextWidth, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        Log.w("width", "width=" + str);
                        brcodeTextWidth.setText(str);
                        brWidth = str;

                    }
                });
                break;
            case R.id.brcode_align:
                final String[] alignStrings = new String[]{getResources().getString(R.string.at_the_left),
                        getResources().getString(R.string.at_the_right),
                        getResources().getString(R.string.at_the_center)};
                PrintDialog.setDialog(BarCodeActivity.this, getString(R.string.set_align), alignStrings, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String content) {
                        bcTextAlign.setText(content);
                        alignText = content;
                        if ("LEFT".equals(content) || "居左".equals(content)) {
                            alignText = "LEFT";
                        } else if ("RIGHT".equals(content) || "居右".equals(content)) {
                            alignText = "RIGHT";
                        } else if ("CENTER".equals(content) || "居中".equals(content)) {
                            alignText = "CENTER";
                        }
                    }
                });
                break;
            case R.id.brcode_graylevel:
                final String[] graylevel = new String[]{"1", "2", "3", "4", "5"};
                PrintDialog.setDialog(BarCodeActivity.this, getString(R.string.grayLevel), graylevel, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        bcTextGraylevel.setText(str);
                        brGraylevel = str;
                    }
                });
                break;
            case R.id.brcode_speedlevels:
                final String[] speedlevels = new String[]{"1", "2", "3", "4", "5"};
                PrintDialog.setDialog(BarCodeActivity.this, getString(R.string.speedlevel), speedlevels, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        bcTextSpeedlevels.setText(str);
                        brSpeedlevel = str;
                    }
                });
                break;
            case R.id.brcode_densitylevel:
                final String[] densitylevels = new String[]{"1", "2", "3", "4", "5"};
                PrintDialog.setDialog(BarCodeActivity.this, getString(R.string.density_level), densitylevels, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        brTextDensitylevel.setText(str);
                        brDensitylevel = str;
                    }
                });
                break;
            case R.id.btn_brcode_print:
                try {
                    if (!"".equals(alignText)) {
                        if ("LEFT".equals(alignText)) {
                            printLineAlign = PrintLine.LEFT;
                        } else if ("RIGHT".equals(alignText)) {
                            printLineAlign = PrintLine.RIGHT;
                        } else if ("CENTER".equals(alignText)) {
                            printLineAlign = PrintLine.CENTER;
                        }
                    }
                    if (!"".equals(brHeight)) {
                        height = Integer.parseInt(brHeight);
                    } else {
                        height = Integer.parseInt(brcodeTextHeight.getText().toString());
                    }
                    if (!"".equals(brWidth)) {
                        width = Integer.parseInt(brWidth);
                    } else {
                        width = Integer.parseInt(brcodeTextWidth.getText().toString());
                    }
                    if (!"".equals(brGraylevel)) {
                        grayLevel = Integer.parseInt(brGraylevel);
                    } else {
                        grayLevel = Integer.parseInt(bcTextGraylevel.getText().toString());
                    }
                    if (!"".equals(brSpeedlevel)) {
                        speedLevel = Integer.parseInt(brSpeedlevel);
                    } else {
                        speedLevel = Integer.parseInt(bcTextSpeedlevels.getText().toString());
                    }
                    if (!"".equals(brDensitylevel)) {
                        densityLevel = Integer.parseInt(brDensitylevel);
                    } else {
                        densityLevel = Integer.parseInt(brTextDensitylevel.getText().toString());
                    }

                    if ("".equals(brContent)) {
                        brContent = brcodeTextContent.getText().toString();
                    }
                    Bitmap bitmap = QRCodeUtil.getBarCodeBM(brContent, width, height, Barcode1D.CODE_128.name());
                    brcodeImage.setImageBitmap(bitmap);
                    if ("mp600".equals(Build.MODEL)) {
                        mPrinter.setPrintSpeed(speedLevel);
                        mPrinter.setPrintDensity(densityLevel);
                    }
                    mPrinter.setPrinterGrey(grayLevel);
                    mPrinter.setPrintStyle(printLineStyle);
                    mPrinter.printBarCode(this, Barcode1D.CODE_128.name(), width, height, brContent, printLineAlign);

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

        }
    }
}