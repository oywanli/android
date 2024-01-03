package com.dspread.demoui.activity;

import static com.dspread.demoui.activity.BaseApplication.getApplicationInstance;
import static com.dspread.demoui.activity.BaseApplication.pos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dspread.demoui.R;
import com.dspread.demoui.beans.Constants;
import com.dspread.xpos.QPOSService;

public class PaymentUartActivity extends AppCompatActivity {

    private ImageView ivBackTitle;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_payment_uart);
        initView();
    }

    private void initView() {
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("SN:" + Constants.transData.getSN());
        BaseApplication.getApplicationInstance = this;
        pos.setCardTradeMode(QPOSService.CardTradeMode.SWIPE_TAP_INSERT_CARD_NOTUP);
        pos.doTrade(20);
        ivBackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("autoTrade".equals(Constants.transData.getAutoTrade())) {
                    Constants.transData.setAutoTrade("StopTrade");
                }
                pos.cancelTrade();
                finish();
                initInfo();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initInfo() {
        if (Constants.transData.getPosId() != null && !"".equals(Constants.transData.getPosId())) {
            Constants.transData.setPosId("");
        }
        if (Constants.transData.getPosInfo() != null && !"".equals(Constants.transData.getPosInfo())) {
            Constants.transData.setPosInfo("");
        }
        if (Constants.transData.getPayment() != null && !"".equals(Constants.transData.getPayment())) {
            Constants.transData.setPayment("");
        }
//        if (Constants.transData.getSN() != null && !"".equals(Constants.transData.getSN())) {
//            Constants.transData.setSN("");
//        }
        if (Constants.transData.getCashbackAmounts() != null && !"".equals(Constants.transData.getCashbackAmounts())) {
            Constants.transData.setCashbackAmounts("");
        }
        if (Constants.transData.getUpdateCheckValue() != null && !"".equals(Constants.transData.getUpdateCheckValue())) {
            Constants.transData.setUpdateCheckValue("");

        }
        if (Constants.transData.getKeyCheckValue() != null && !"".equals(Constants.transData.getKeyCheckValue())) {
            Constants.transData.setKeyCheckValue("");
        }
        if (Constants.transData.getInputMoney() != null && !"".equals(Constants.transData.getInputMoney())) {
            Constants.transData.setInputMoney("");
        }
        if (Constants.transData.getPayType() != null && !"".equals(Constants.transData.getPayType())) {
            Constants.transData.setPayType("");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("autoTrade".equals(Constants.transData.getAutoTrade())) {
                Constants.transData.setAutoTrade("StopTrade");
            }
            pos.cancelTrade();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}