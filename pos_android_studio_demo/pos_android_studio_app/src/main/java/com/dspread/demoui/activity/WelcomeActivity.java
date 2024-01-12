package com.dspread.demoui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.dspread.demoui.R;

public class WelcomeActivity extends AppCompatActivity {
    private Handler handler = new Handler(Looper.myLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.postDelayed(runnable, 100);
    }


}