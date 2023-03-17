package com.dspread.demoui.activities.printer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;
import com.dspread.print.QPOSPrintService;
import com.dspread.print.mp600.PrintStyle;


public class MPPrintTextActivity extends CommonActivity {

    private EditText etText, etFontsize;
    private Spinner mSpinnerSpAlignment;
    private Spinner mSpinnerSpGreyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etText = findViewById(R.id.et_text);
        etFontsize = findViewById(R.id.et_font_size);
        setTitle(getString(R.string.mp_text_print));
        initView();
    }

    private void initView() {
        mSpinnerSpAlignment = findViewById(R.id.sp_alignment);
        mSpinnerSpGreyLevel = findViewById(R.id.sp_grey_level);
        mSpinnerSpAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (item.equals("Left")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.LEFT);
                } else if (item.equals("Right")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.RIGHT);
                } else if (item.equals("Center")) {
                    mPrinter.setPrintStyle(PrintStyle.Key.ALIGNMENT, PrintStyle.Alignment.CENTER);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerSpGreyLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                mPrinter.setPrintDensity(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onToolbarLinstener() {
        finish();

    }

    @Override
    int getLayoutId() {
        return R.layout.activity_mpprint_text;
    }

    @Override
    int printTest() {
        mPrinter.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize());
        if (model.equalsIgnoreCase("MP600") || model.equalsIgnoreCase("D60")) {
            mPrinter.printText(getText());
        }
        if (model.equalsIgnoreCase("D30")) {
            mPrinter.printText(getText());
            mPrinter.setLineFeed(5);
            mPrinter.setReturnPrintState(new QPOSPrintService.OnReturnPrintResultState() {
                @Override
                public void onPrintStart() {
                }

                @Override
                public void onPrintFinish(int height) {

                }

                @Override
                public void onPrintError(int error, String message) {

                }
            });
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
