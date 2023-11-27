package com.dspread.demoui.activity.printer;

import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.demoui.ui.dialog.PrintDialog;
import com.dspread.helper.printer.PrinterClass;
import com.dspread.print.device.PrintListener;
import com.dspread.print.device.PrinterDevice;
import com.dspread.print.device.PrinterInitListener;
import com.dspread.print.device.PrinterManager;
import com.dspread.print.device.bean.PrintLineStyle;
import com.dspread.print.widget.PrintLine;

public class PrintTextActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivBackTitle;
    private TextView tvTitle;
    private TextView textTextAlign;
    private LinearLayout layoutSetAlign;
    private TextView textSetFontStyle;
    private LinearLayout layoutSetFontStyle;
    private TextView textTextSize;
    private LinearLayout layoutTextSize;
    private LinearLayout textSet;
    private EditText editText;
    private Button btnPrint;
    private LinearLayout textAll;
    private PrinterDevice mPrinter;
    private PrintLineStyle printLineStyle;
    private String alignText = "";
    private String fontText = "";
    private String textSizeStr = "";
    private int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_print_text);
        initView();
        PrinterManager instance = PrinterManager.getInstance();
        mPrinter = instance.getPrinter();
        if ("D30".equals(Build.MODEL)) {
            mPrinter.initPrinter(PrintTextActivity.this, new PrinterInitListener() {
                @Override
                public void connected() {
                    mPrinter.setPrinterTerminatedState(PrinterDevice.PrintTerminationState.PRINT_STOP);
                /*When no paper, the
                printer terminates printing and cancels the printing task.*/
//              PrinterDevice.PrintTerminationState.PRINT_STOP
               /* When no paper, the
                printer will prompt that no paper. After loading the paper, the printer
                will continue to restart printing.*/
//              PrinterDevice.PrintTerminationState. PRINT_NORMAL
                }
                @Override
                public void disconnected() {
                }
            });

        }else{
            mPrinter.initPrinter(this);
        }
        MyPrinterListener myPrinterListener = new MyPrinterListener();
        mPrinter.setPrintListener(myPrinterListener);
        printLineStyle = new PrintLineStyle();
    }

    private void initView() {
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.print_text));
        textTextAlign = findViewById(R.id.text_text_align);
        layoutSetAlign = findViewById(R.id.layout_setAlign);
        textSetFontStyle = findViewById(R.id.text_setFontStyle);
        layoutSetFontStyle = findViewById(R.id.layout_setFontStyle);
        textTextSize = findViewById(R.id.text_text_size);
        layoutTextSize = findViewById(R.id.Layout_textSize);
        textSet = findViewById(R.id.text_set);
        editText = findViewById(R.id.edit_text);
        btnPrint = findViewById(R.id.btn_Print);
        textAll = findViewById(R.id.text_all);
        ivBackTitle.setOnClickListener(this);
        layoutSetAlign.setOnClickListener(this);
        layoutSetFontStyle.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        layoutTextSize.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_title:
                finish();
                break;
            case R.id.layout_setAlign:
                final String[] alignStrings = new String[]{getResources().getString(R.string.at_the_left),
                        getResources().getString(R.string.at_the_right),
                        getResources().getString(R.string.at_the_center)};

                PrintDialog.setDialog(this, getString(R.string.set_align), alignStrings, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String content) {
                        textTextAlign.setText(content);
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
            case R.id.layout_setFontStyle:
                final String[] fontStrings = new String[]{getResources().getString(R.string.fontStyle_normal),
                        getResources().getString(R.string.fontStyle_bold),
                        getResources().getString(R.string.fontStyle_italic),
                        getResources().getString(R.string.fontStyle_bold_italic)};
                PrintDialog.setDialog(this, getString(R.string.set_font_style), fontStrings, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String content) {
                        textSetFontStyle.setText(content);
                        fontText = content;
                        if ("NORMAL".equals(content) || "正常".equals(content)) {
                            textSetFontStyle.setText(content);
                            fontText = "NORMAL";
                        } else if ("BOLD".equals(content) || "粗体".equals(content)) {
                            textSetFontStyle.setText(content);
                            fontText = "BOLD";
                        } else if ("ITALIC".equals(content) || "斜体".equals(content)) {
                            textSetFontStyle.setText(content);
                            fontText = "ITALIC";
                        } else if ("BOLD_ITALIC".equals(content) || "斜体加粗".equals(content)) {
                            textSetFontStyle.setText(content);
                            fontText = "BOLD_ITALIC";
                        }
                    }
                });

                break;
            case R.id.btn_Print:
                try {
                    if (!"".equals(alignText)) {
                        if ("LEFT".equals(alignText)) {
                            printLineStyle.setAlign(PrintLine.LEFT);
                        } else if ("RIGHT".equals(alignText)) {
                            printLineStyle.setAlign(PrintLine.RIGHT);
                        } else if ("CENTER".equals(alignText)) {
                            printLineStyle.setAlign(PrintLine.CENTER);
                        }
                    }
                    if (!"".equals(fontText)) {
                        if ("NORMAL".equals(fontText)) {
                            printLineStyle.setFontStyle(PrintStyle.FontStyle.NORMAL);
                            printLineStyle.setFontStyle(PrintStyle.Key.ALIGNMENT);
                        } else if ("BOLD".equals(fontText)) {
                            printLineStyle.setFontStyle(PrintStyle.FontStyle.BOLD);
                        } else if ("ITALIC".equals(fontText)) {
                            printLineStyle.setFontStyle(PrintStyle.FontStyle.ITALIC);

                        } else if ("BOLD_ITALIC".equals(fontText)) {
                            printLineStyle.setFontStyle(PrintStyle.FontStyle.BOLD_ITALIC);
                        }
                    }
                    if ("".equals(textSizeStr)) {
                        textSizeStr = textTextSize.getText().toString();
                        textSize = Integer.parseInt(textSizeStr);
                    } else {
                        textSize = Integer.parseInt(textSizeStr);

                    }
                    printLineStyle.setFontSize(textSize);
                    mPrinter.setPrintStyle(printLineStyle);
                    mPrinter.printText(getString(R.string.text_print));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);

                }
                break;
            case R.id.Layout_textSize:
                PrintDialog.showSeekBarDialog(this, getResources().getString(R.string.set_font_size), 12, 40, textTextSize, new PrintDialog.PrintClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(String str) {
                        Log.w("width", "width=" + str);
                        textTextSize.setText(str);
                        textSizeStr = str;

                    }
                });

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

