package com.dspread.demoui.activity;

import static com.dspread.demoui.activity.BaseApplication.pos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dspread.demoui.R;
import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.ui.dialog.Mydialog;
import com.dspread.demoui.utils.TRACE;
import com.dspread.xpos.QPOSService;

public class SuccessActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView ivBackTitle;
    private TextView tvTitle;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_success);
        initView();
    }

    private void initView() {
        ivBackTitle = findViewById(R.id.iv_back_title);
        tvTitle = findViewById(R.id.tv_title);
        tvInfo = findViewById(R.id.tv_info);
        String paytment = getIntent().getStringExtra("paytment");
        String tradeResut = getIntent().getStringExtra("tradeResut");
        String posinfo = getIntent().getStringExtra("posinfo");
        if("payment".equals(paytment)){
            tvTitle.setText(getString(R.string.transaction_approved));
            tvInfo.setText(tradeResut);
            initInfo();
        }
        if ("posid".equals(posinfo)) {
            tvTitle.setText(getString(R.string.get_pos_id));
            tvInfo.setText(Constants.transData.getPosId());
            initInfo();
        } else if ("posinfo".equals(posinfo)) {
            tvTitle.setText(getString(R.string.get_info));
            tvInfo.setText(Constants.transData.getPosInfo());
            initInfo();
        } else if ("updatekey".equals(posinfo)) {
            tvTitle.setText(getString(R.string.get_update_key));
            tvInfo.setText(Constants.transData.getUpdateCheckValue());
            initInfo();
        } else if ("keycheckvalue".equals(posinfo)) {
            tvTitle.setText(getString(R.string.get_key_checkvalue));
            tvInfo.setText(Constants.transData.getKeyCheckValue());
            initInfo();
        }
        ivBackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                initInfo();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("onDestroy","onDestroy");
        initInfo();
    }

    public void initInfo() {
        if (Constants.transData.getPosId() != null&& !"".equals(Constants.transData.getPosId())) {
            Constants.transData.setPosId("");
        }
        if (Constants.transData.getPosInfo() != null&& !"".equals(Constants.transData.getPosInfo())) {
            Constants.transData.setPosInfo("");
        }
        if (Constants.transData.getPayment() != null&& !"".equals(Constants.transData.getPayment())) {
            Constants.transData.setPayment("");
        }
        if (Constants.transData.getSN() != null&& !"".equals(Constants.transData.getSN())) {
            Constants.transData.setSN("");
        }
        if (Constants.transData.getCashbackAmounts() != null&& !"".equals(Constants.transData.getCashbackAmounts() )) {
            Constants.transData.setCashbackAmounts("");
        }
        if (Constants.transData.getUpdateCheckValue() != null&& !"".equals(Constants.transData.getUpdateCheckValue())) {
            Constants.transData.setUpdateCheckValue("");

        }
        if (Constants.transData.getKeyCheckValue() != null&& !"".equals(Constants.transData.getKeyCheckValue())) {
            Constants.transData.setKeyCheckValue("");
        }
        if (Constants.transData.getInputMoney() != null&& !"".equals(Constants.transData.getInputMoney())) {
            Constants.transData.setInputMoney("");
        }
        if (Constants.transData.getPayType() != null&& !"".equals(Constants.transData.getPayType() )) {
            Constants.transData.setPayType("");
        }

    }
}