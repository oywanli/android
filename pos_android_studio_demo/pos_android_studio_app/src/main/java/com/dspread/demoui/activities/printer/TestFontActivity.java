package com.dspread.demoui.activities.printer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;

public class TestFontActivity extends CommonActivity {

    private static final int PAPER_WIDTH = 384;
    private EditText etText, etFontsize;
    private Spinner mSpinnerSpAlignment, mSpinnerSpFontStyle, mSpFont;
    private Spinner mSpinnerSpGreyLevel;
    private TextView tvFontStyle;
    private TextView tvGrayLevel;
    private LinearLayout mFontStyleArea, mFontArea, mGrayLevelArea;
    private TextView mFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.mp_font_test));
        initView();
    }

    private void initView() {
        etText = findViewById(R.id.et_text);
        etFontsize = findViewById(R.id.et_font_size);
        mFontStyleArea = findViewById(R.id.font_style_area);
        mGrayLevelArea = findViewById(R.id.gray_level_area);
        mFontArea = findViewById(R.id.font_area);
        mFont = findViewById(R.id.font);
        mSpFont = findViewById(R.id.sp_font);
        mSpinnerSpAlignment = findViewById(R.id.sp_alignment);
        mSpinnerSpGreyLevel = findViewById(R.id.sp_grey_level);
        mSpinnerSpFontStyle = findViewById(R.id.sp_font_style);
        tvFontStyle = findViewById(R.id.font_style);
        tvGrayLevel = findViewById(R.id.gray_level);
        mGrayLevelArea.setVisibility(View.GONE);
        mFontStyleArea.setVisibility(View.VISIBLE);
        mFontArea.setVisibility(View.VISIBLE);
        mSpinnerSpAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (item.equals("Left")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.NORMAL);
                } else if (item.equals("Right")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.ALIGN_OPPOSITE);
                } else if (item.equals("Center")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerSpFontStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (item.equals("NORMAL")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
                } else if (item.equals("BOLD")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
                } else if (item.equals("ITALIC")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.ITALIC);
                } else if (item.equals("BOLD_ITALIC")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD_ITALIC);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mSpFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (item.equals("系统默认")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "");
                } else if (item.equals("微软雅黑")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "");
                } else if (item.equals("arial")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "fonts/arial.ttf");
                } else if (item.equals("宋体")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    int getLayoutId() {
        return R.layout.activity_print_font_test;
    }

    @Override
    public void onToolbarLinstener() {
        finish();
    }

    @Override
    int printTest() throws RemoteException {
        mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize());
        mPrinter.printText(getText());
        return 0;
    }

    @Override
    void onPrintFinished(boolean isSuccess, String status) {
        if (status != null) {
            TRACE.d("ssss" + status);
        }

    }

    @Override
    void onPrintError(boolean isSuccess, String status) {

    }

    private String getText() {
        if (etText.getText() != null) {
            return etText.getText().toString();
        } else {
            return "";
        }
    }

    private int getFontSize() {
        if (etFontsize.getText() != null) {
            return Integer.parseInt(etFontsize.getText().toString());
        } else {
            return 14;
        }
    }

}
