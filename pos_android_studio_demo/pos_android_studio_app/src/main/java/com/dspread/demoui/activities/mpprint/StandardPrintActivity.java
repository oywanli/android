package com.dspread.demoui.activities.mpprint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.action.printerservice.PrintStyle;
import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;

import java.util.Hashtable;

import androidx.annotation.NonNull;

public class StandardPrintActivity extends CommonActivity {
    private Spinner mSpGreyLevel, mSpinnerSpAlignment;
    private Spinner mSpSpeedLevel;
    private TextView message;
    private EditText etText, etFontsize;
    private boolean isContinuousPrint = false;
    private boolean IsPrintPause = false;
    private boolean iSAutoPaperOut = false;
    CheckBox cbContuningPrint, cbNeedInterval, cbAutoFeedLine;
    private int mPrintcount;
    private int mAutoPaperCount;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int arg1 = msg.arg1;
            if (arg1 == 1) {
                mPrintcount++;
                if (mPrintcount < 10) {
                    if (IsPrintPause) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    pos.printText(getText());
                }
            } else if (arg1 == 2) {
                mAutoPaperCount++;
                if (mAutoPaperCount < 30) {
                    pos.printText("");
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.mp_standard_print));
        isContinuousPrint = false;
        IsPrintPause = false;
        etText = findViewById(R.id.et_text);
        etFontsize = findViewById(R.id.et_font_size);
        //spContentDensity = findViewById(R.id.sp_content_density);
        //spHeightFactor = findViewById(R.id.sp_height_factor);
        //spContentDensity.setSelection(1);
        // spHeightFactor.setSelection(1);
        mSpinnerSpAlignment = findViewById(R.id.sp_alignment);
        mSpGreyLevel = findViewById(R.id.sp_grey_level);
        mSpSpeedLevel = findViewById(R.id.sp_speed_level);
        findViewById(R.id.align_area).setVisibility(View.VISIBLE);
        findViewById(R.id.gray_level).setVisibility(View.VISIBLE);
        findViewById(R.id.content_density_area).setVisibility(View.GONE);
        findViewById(R.id.height_factor_area).setVisibility(View.GONE);
        findViewById(R.id.speed_level_area).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_continuous_print).setVisibility(View.VISIBLE);
        message = findViewById(R.id.message);
        cbContuningPrint = findViewById(R.id.cb_contuning_print);
        cbNeedInterval = findViewById(R.id.cb_need_interval);
        cbAutoFeedLine = findViewById(R.id.cb_line_feed);
        cbContuningPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbNeedInterval.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                pos.setPrintStyle(PrintStyle.Key.FONT_SIZE, getFontSize());
                if (isChecked) {
                    isContinuousPrint = true;
                    mPrintcount = 0;
                    pos.printText(getText());
                }
                if (!isChecked) {
                    cbNeedInterval.setChecked(false);
                }
            }
        });

        cbNeedInterval.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsPrintPause = true;
                } else {
                    IsPrintPause = false;
                }
            }
        });
        mSpGreyLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                message.setText("");
                String item = (String) parent.getAdapter().getItem(position);
                pos.setPrintDensity(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mSpSpeedLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                message.setText("");
                String item = (String) parent.getAdapter().getItem(position);
                pos.setPrintSpeed(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cbAutoFeedLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAutoPaperCount = 0;
                    iSAutoPaperOut = true;
                    pos.printText("");
                } else {
                    iSAutoPaperOut = false;
                }
            }
        });


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

    }


    @Override
    int getLayoutId() {
        return R.layout.activity_print_standard_test;
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
        TRACE.d("sssssss" + result.toString());
        String code = (String) result.get("code");
        if (code.equals("true")) {
            String type = result.get("type");
            if (type.equals("03")) {
                String printDensityFlag = result.get("printDensityFlag") == null ? "" : result.get("printDensityFlag"); //
                String printDensityValue = result.get("printDensityValue") == null ? "" : result.get("printDensityValue");
                String printSpeedFlag = result.get("printSpeedFlag") == null ? "" : result.get("printSpeedFlag");
                String printSpeedValue = result.get("printSpeedValue") == null ? "" : result.get("printSpeedValue");
                String printTempFlag = result.get("printTempFlag") == null ? "" : result.get("printTempFlag");
                String printTempValue = result.get("printTempValue") == null ? "" : result.get("printTempValue");
                String printVoltageFlg = result.get("printVoltageFlg") == null ? "" : result.get("printVoltageFlg");
                String printVoltageValue = result.get("printVoltageValue") == null ? "" : result.get("printVoltageValue");
                String printStateFlg = result.get("printStateFlg") == null ? "" : result.get("printStateFlg");
                String printStateValue = result.get("printStateValue") == null ? "" : result.get("printStateValue");
                String content = "";
                content += "printDensityFlag:" + printDensityFlag + "\n";
                content += "printDensityValue:" + printDensityValue + "\n";
                content += "printSpeedFlag:" + printSpeedFlag + "\n";
                content += "printSpeedValue:" + printSpeedValue + "\n";
                content += "printTempFlag:" + printTempFlag + "\n";
                content += "printTempValue:" + printTempValue + "\n";
                content += "printVoltageFlg:" + printVoltageFlg + "\n";
                content += "printVoltageValue:" + printVoltageValue + "\n";
                content += "printStateFlg:" + printStateFlg + "\n";
                content += "printStateValue:" + printStateValue + "\n";
                message.setText(code + "==" + content);
            } else if (type.equals("01")) {
                String printStateValue = result.get("printStateValue") == null ? "" : result.get("printStateValue");
                message.setText(code + ";" + printStateValue);
                TRACE.d("SSSSS:isContinuousPrint" + isContinuousPrint);

                if (isContinuousPrint) {
                    Message obtain = Message.obtain();
                    obtain.arg1 = 1;
                    mHandler.sendMessage(obtain);
                }
                if (iSAutoPaperOut) {
                    Message obtain = Message.obtain();
                    obtain.arg1 = 2;
                    mHandler.sendMessage(obtain);
                }
            } else {
                message.setText(code);
            }
        }
        //message.setText(result.toString());
    }

    @Override
    void onPrintError(Hashtable<String, String> result) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_print, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.get_print_density) {
            pos.getPrintDensity();
        } else if (item.getItemId() == R.id.get_print_speed) {
            pos.getPrintSpeed();
        } else if (item.getItemId() == R.id.get_print_temperature) {
            pos.getPrintTemperature();
        } else if (item.getItemId() == R.id.get_print_voltage) {
            pos.getPrintVoltage();
        } else if (item.getItemId() == R.id.get_print_status) {
            pos.getPrinterStatus();
        }
        return true;
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
