package com.dspread.demoui.activities.mpprint;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.action.printerservice.ActionPrinter;
import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;
import com.dspread.xpos.CQPOSService;
import com.dspread.xpos.QPOSService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public abstract class CommonActivity extends AppCompatActivity {
    public String TAG = this.getClass().getSimpleName();
    private Button btnPrint;
    CheckBox cbContuningPrint, cbNeedInterval, cbAutoFeedLine;
    private TextView tvMessage;
    private EditText etLoadThreadCount;
    private long printStartAt = 0;
    Spinner spAlignment, spGrayLevel, mSpSpeedLevel;
    protected ActionPrinter printer;
    private long totalPrintTime;
    private long totalPrintDistance;
    private BatteryManager mBatteryManager;
    private volatile boolean isFinished = false;
    private int mCalThreadCount = 0;
    private SharedPreferences mSp;

    private String mPrintLogPath = "";
    private SimpleDateFormat mLogDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat mFileNameDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    protected Toolbar toolbar;
    private TextView txt_toolbar_title;
    protected QPOSService pos;

    class CalThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.d(TAG, "start");
            while (!isFinished) {
                Log.d(TAG, "running");
                fibonacci(30);
                SystemClock.sleep(50);
            }
            Log.d(TAG, "end");

        }
    }

    public static long fibonacci(long number) {
        if ((number == 0) || (number == 1))
            return number;
        else
            return fibonacci(number - 1) + fibonacci(number - 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mSp = getSharedPreferences("pref", MODE_PRIVATE);
        mCalThreadCount = mSp.getInt("load_thread_count", 0);
        isFinished = false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            txt_toolbar_title = toolbar.findViewById(R.id.txt_toolbar_title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show the left arrow
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setDefaultToolbarColor();
            toolbar.setPadding(0, 0, 0, 0);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onToolbarLinstener();
                }
            });
        }

        mBatteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        btnPrint = findViewById(R.id.print);
        cbContuningPrint = findViewById(R.id.cb_contuning_print);
        cbNeedInterval = findViewById(R.id.cb_need_interval);
        cbAutoFeedLine = findViewById(R.id.cb_line_feed);
        cbAutoFeedLine.setChecked(false);
        tvMessage = findViewById(R.id.message);
        spAlignment = findViewById(R.id.sp_alignment);
        spGrayLevel = findViewById(R.id.sp_grey_level);
        mSpSpeedLevel = findViewById(R.id.sp_speed_level);
        findViewById(R.id.ll_continuous_print).setVisibility(View.INVISIBLE);
        spGrayLevel.setSelection(4);
        mSpSpeedLevel.setSelection(4);
        btnPrint.setEnabled(true);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onPrintStart();
                    enableButton(btnPrint, false);
                    totalPrintDistance = 0;
                    totalPrintTime = 0;
                    print();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        cbContuningPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbNeedInterval.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (!isChecked) {
                    cbNeedInterval.setChecked(false);
                }
            }
        });

        etLoadThreadCount = findViewById(R.id.et_load_thread_count);
        etLoadThreadCount.setText(mCalThreadCount + "");
        etLoadThreadCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mSp.edit().putInt("load_thread_count", Integer.parseInt(s.toString())).apply();
                    ;
                }
            }
        });
        for (int i = 0; i < mCalThreadCount; i++) {
            new CalThread().start();
        }

        initUart(QPOSService.CommunicationMode.UART);//

    }

    private void initUart(QPOSService.CommunicationMode mode) {
        TRACE.d("open");
        //pos=null;

        //implement singleton mode
        pos = QPOSService.getInstance(mode);
        if (pos == null) {
            TRACE.d("CommonActivity pos == null");
            return;
        }
        if (mode == QPOSService.CommunicationMode.USB_OTG_CDC_ACM) {
            pos.setUsbSerialDriver(QPOSService.UsbOTGDriver.CDCACM);
        }
        pos.setD20Trade(true);
        pos.setConext(this);
        //init handler
        MyPosListener listener = new MyPosListener();
        Handler handler = new Handler(Looper.myLooper());
        pos.initListener(handler, listener);
    }

    // public abstract CQPOSService setMyPosListener();


    public abstract void onToolbarLinstener();

    int getLayoutId() {
        return R.layout.activity_print_common;
    }

    private void print() throws RemoteException {
        tvMessage.setText("");
        int re = printTest();
        if (cbAutoFeedLine.isChecked()) {
            // printer.lineFeed(5);
        }
        if (re != 0) {
            return;
        }
    }

    long getDelayPrintTime() {
        return 500;
    }

    abstract int printTest() throws RemoteException;

    void onPrintButtonClick() {
    }

    void onPrintStart() {
    }

    void onTestPrintPause(int reason) {
    }

    void onTestPrintResume(int reason) {
    }

    abstract void onPrintFinished(Hashtable<String, String> result);

    abstract void onPrintError(Hashtable<String, String> result);

    public void enableButton(Button button, boolean enable) {
        button.post(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(enable);
            }
        });
    }

    public void setMessage(String message) {
        tvMessage.post(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinished = true;
    }


    public void setDefaultToolbarColor() {
        setToolbarBgColor(ContextCompat.getColor(this, R.color.eb_col_11));
        //        setToolbarTextColor(ContextCompat.getColor(this,R.color.eb_col_30));
        setToolbarIconColor(ContextCompat.getColor(this, R.color.eb_col_30));
        setStatusBarColor(ContextCompat.getColor(this, R.color.eb_col_11));
    }

    public void setToolbarIconColor(int toolbarIconsColor) {
        if (toolbar != null) {
            final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.SRC_ATOP);//MULTIPLY

            for (int i = 0; i < toolbar.getChildCount(); i++) {
                final View v = toolbar.getChildAt(i);

                //Step 1 : Changing the color of back button (or open drawer button).
                if (v instanceof ImageButton) {
                    //Action Bar back button
                    ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
                }

                if (v instanceof ActionMenuView) {
                    for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {
                        //Step 2: Changing the color of any ActionMenuViews - icons that
                        //are not back button, nor text, nor overflow menu icon.
                        final View innerView = ((ActionMenuView) v).getChildAt(j);

                        if (innerView instanceof ActionMenuItemView) {
                            int drawablesCount = ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                            for (int k = 0; k < drawablesCount; k++) {
                                if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) {
                                    final int finalK = k;

                                    //Important to set the color filter in seperate thread,
                                    //by adding it to the message queue
                                    //Won't work otherwise.
                                    innerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                //Step 3: Changing the color of title and subtitle.
                if (txt_toolbar_title != null) {
                    txt_toolbar_title.setTextColor(toolbarIconsColor);
                }
                toolbar.setTitleTextColor(toolbarIconsColor);
                toolbar.setSubtitleTextColor(toolbarIconsColor);

                //Step 4: Changing the color of the Overflow Menu icon.
                setOverflowButtonColor(this, colorFilter);
            }
        }
    }

    public void setToolbarBgColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }

    public void setStatusBarColor(int color) {
        if (isAboveKITKAT()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //            window.setStatusBarColor(color);
        }
    }

    public boolean isAboveKITKAT() {
        boolean isHigher = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isHigher = true;
        }
        return isHigher;
    }

    private void setOverflowButtonColor(final Activity activity, final PorterDuffColorFilter colorFilter) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                AppCompatImageView overflow = (AppCompatImageView) outViews.get(0);
                overflow.setColorFilter(colorFilter);
                removeOnGlobalLayoutListener(decorView, this);
            }
        });
    }

    private void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public void setTitle(int titleResource) {
        setTitle(getResources().getString(titleResource));
    }

    public void setTitle(String title) {
        if (title != null && !title.equals("")) {
            if (txt_toolbar_title != null) {
                txt_toolbar_title.setText(title);
                toolbar.setTitle("");
            } else if (toolbar != null) {
                toolbar.setTitle(title);
                txt_toolbar_title.setText("");
            }
        }
    }

    class MyPosListener extends CQPOSService {
        @Override
        public void onQposPrintOperateResult(Hashtable<String, String> result) {
            super.onQposPrintOperateResult(result);
            enableButton(btnPrint, true);
            String code = (String) result.get("code");
            if (code.equals("true")) {
                onPrintFinished(result);
            } else {
                onPrintError(result);
            }
        }
    }
}

