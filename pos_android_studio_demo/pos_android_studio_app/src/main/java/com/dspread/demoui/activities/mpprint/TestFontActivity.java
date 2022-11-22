package com.dspread.demoui.activities.mpprint;

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

import java.util.Hashtable;

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
                    pos.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.NORMAL);
                } else if (item.equals("Right")) {
                    pos.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.ALIGN_OPPOSITE);
                } else if (item.equals("Center")) {
                    pos.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
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
                    pos.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.NORMAL);
                } else if (item.equals("BOLD")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD);
                } else if (item.equals("ITALIC")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.ITALIC);
                } else if (item.equals("BOLD_ITALIC")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_STYLE, PrintStyle.FontStyle.BOLD_ITALIC);
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
                    pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "");
                } else if (item.equals("微软雅黑")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "fonts/msyh.ttc");
                } else if (item.equals("arial")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "fonts/arial.ttf");
                } else if (item.equals("宋体")) {
                    pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize(), "fonts/simsun.ttc");
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
        pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize());
        pos.printText(getText());
        return 0;
    }

    @Override
    void onPrintFinished(Hashtable<String, String> result) {

    }

    @Override
    void onPrintError(Hashtable<String, String> result) {

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


    private Bitmap generateFontTestBitmap() {
        StringBuilder contentTextBuilder = new StringBuilder();
        contentTextBuilder.append("abcdefghijklmnopqrstuvwxyz\n")
                .append("ABCDEFGHIJKLMNOPQRSTUVWXYZ\n")
                .append("1234567890\n")
                .append(". : , ; ' \\ \" ( ! ? ) + - * / =\n")
                .append("。：，；“！”‘ ？（）、《》\n")
                .append("中国智造，惠及全球\n\n");

        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAlpha(255);
        textPaint.setTextSize(26);
//        textPaint.setAntiAlias(true);

        int totalFontTextHeight = 0;
        //totalFontTextHeight += mesureFontTextHeight("", "系统默认", contentTextBuilder.toString(), textPaint);
        // totalFontTextHeight += mesureFontTextHeight("fonts/arial.ttf", "arial", contentTextBuilder.toString(), textPaint);
        totalFontTextHeight += mesureFontTextHeight("fonts/msyh.ttc", "微软雅黑", contentTextBuilder.toString(), textPaint);
        // totalFontTextHeight += mesureFontTextHeight("fonts/simsun.ttc", "宋体", contentTextBuilder.toString(), textPaint);

        Bitmap bitmap = Bitmap.createBitmap(PAPER_WIDTH, totalFontTextHeight, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bitmap);
        Paint bgPaint = new Paint();
        bgPaint.setAlpha(0);
        bgPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(0, 0, PAPER_WIDTH, totalFontTextHeight);
        canvas.drawRect(rectF, bgPaint);

        //drawFontText("", "系统默认", contentTextBuilder.toString(), canvas, textPaint);
        // drawFontText("fonts/arial.ttf", "arial", contentTextBuilder.toString(), canvas, textPaint);
        drawFontText("fonts/msyh.ttc", "微软雅黑", contentTextBuilder.toString(), canvas, textPaint);
        //drawFontText("fonts/simsun.ttc", "宋体", contentTextBuilder.toString(), canvas, textPaint);

        return bitmap;
    }

    private void drawFontText(String fontpath, String fontName, String text, Canvas canvas, TextPaint textPaint) {
        if (TextUtils.isEmpty(fontpath)) {
            textPaint.setTypeface(Typeface.DEFAULT);
        } else {
            textPaint.setTypeface(Typeface.createFromAsset(getAssets(), fontpath));
        }
        StaticLayout layout = new StaticLayout(fontName + ": \n" + text, textPaint, 384, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
        layout.draw(canvas);
        canvas.translate(0, layout.getHeight());
    }

    private int mesureFontTextHeight(String fontpath, String fontName, String text, TextPaint textPaint) {
        if (TextUtils.isEmpty(fontpath)) {
            textPaint.setTypeface(Typeface.DEFAULT);
        } else {
            textPaint.setTypeface(Typeface.createFromAsset(getAssets(), fontpath));
        }
        StaticLayout layout = new StaticLayout(fontName + ": \n" + text, textPaint, 384, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
        return layout.getHeight();
    }

}
