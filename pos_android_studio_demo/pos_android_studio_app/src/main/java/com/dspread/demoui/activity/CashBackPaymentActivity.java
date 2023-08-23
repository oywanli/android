package com.dspread.demoui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.MoneyUtil;
import com.dspread.demoui.utils.SharedPreferencesUtil;

public class CashBackPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView inputMoneyYuanText, inputMoneyFenText;
    private long cashbackAmounts = 0;
    private Button mConfirm;
    private TextView mAmount;
    private RelativeLayout mRLayouttitle;
    private ImageView mIvbacktitle;
    private TextView mTvtitle;
    private String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.fragment_input_money);
        initView();
    }

    protected void initView() {
        mRLayouttitle = findViewById(R.id.il_title);
        mTvtitle = findViewById(R.id.tv_title);
        mIvbacktitle = findViewById(R.id.iv_back_title);
        mRLayouttitle.setVisibility(View.VISIBLE);
        mTvtitle.setText(getString(R.string.input_cashback));
        mIvbacktitle.setOnClickListener(this);
        inputMoneyYuanText = findViewById(R.id.inputMoneyYuanText);
        inputMoneyFenText = findViewById(R.id.inputMoneyFenText);
        Button btn_num00 = findViewById(R.id.btn_num00);
        btn_num00.setOnClickListener(this);
        Button btn_num0 = findViewById(R.id.btn_num0);
        btn_num0.setOnClickListener(this);
        Button btn_num1 = findViewById(R.id.btn_num1);
        btn_num1.setOnClickListener(this);
        Button btn_num2 = findViewById(R.id.btn_num2);
        btn_num2.setOnClickListener(this);
        Button btn_num3 = findViewById(R.id.btn_num3);
        btn_num3.setOnClickListener(this);
        Button btn_num4 = findViewById(R.id.btn_num4);
        btn_num4.setOnClickListener(this);
        Button btn_num5 = findViewById(R.id.btn_num5);
        btn_num5.setOnClickListener(this);
        Button btn_num6 = findViewById(R.id.btn_num6);
        btn_num6.setOnClickListener(this);
        Button btn_num7 = findViewById(R.id.btn_num7);
        btn_num7.setOnClickListener(this);
        Button btn_num8 = findViewById(R.id.btn_num8);
        btn_num8.setOnClickListener(this);
        Button btn_num9 = findViewById(R.id.btn_num9);

        btn_num9.setOnClickListener(this);
        // 清除
        ImageView btn_num_clear = findViewById(R.id.btn_num_clear);
        btn_num_clear.setOnClickListener(this);
        mConfirm = findViewById(R.id.btn_confirm);
        mConfirm.setOnClickListener(this);
        mAmount = findViewById(R.id.tv_amount);
    }

    @Override
    public void onResume() {
        super.onResume();
        cashbackAmounts = 0;
        inputMoneySetText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_num0:
            case R.id.btn_num1:
            case R.id.btn_num2:
            case R.id.btn_num3:
            case R.id.btn_num4:
            case R.id.btn_num5:
            case R.id.btn_num6:
            case R.id.btn_num7:
            case R.id.btn_num8:
            case R.id.btn_num9:

                if ((cashbackAmounts + "").length() < 12) {
                    cashbackAmounts = Long.parseLong(cashbackAmounts + ((Button) v).getText().toString());

                    inputMoneySetText();
                }
                break;

            case R.id.btn_num00:

                if ((cashbackAmounts + "").length() < 11) {
                    cashbackAmounts = Long.parseLong(cashbackAmounts + ((Button) v).getText().toString());

                    inputMoneySetText();
                }

                break;

            case R.id.btn_num_clear:
                if (cashbackAmounts > 0) {
                    cashbackAmounts = cashbackAmounts / 10;
                    inputMoneySetText();
                }
                break;
            case R.id.btn_confirm:
                if (cashbackAmounts > 0) {
                    String amount = getIntent().getStringExtra("amount");
                    String inputMoney = getIntent().getStringExtra("inputMoney");
                    SharedPreferencesUtil connectType = SharedPreferencesUtil.getmInstance();
                    String conType = (String) connectType.get(CashBackPaymentActivity.this, "conType", "");

                    if (conType != null && "uart".equals(conType)) {
                        Intent intent = new Intent(CashBackPaymentActivity.this, PaymentActivity.class);
                        intent.putExtra("amount", amount);
                        String inputMoneyString = String.valueOf(inputMoney);
                        intent.putExtra("inputMoney", inputMoneyString);
                        intent.putExtra("paytype", "CASHBACK");
                        String inputcashbackMoney = String.valueOf(cashbackAmounts);
                        intent.putExtra("cashbackAmounts", inputcashbackMoney);
                        intent.putExtra("connect_type", 2);
                        startActivity(intent);
                        finish();
                    } else if (conType != null && "usb".equals(conType)) {
                        Intent intent = new Intent(CashBackPaymentActivity.this, PaymentActivity.class);
                        intent.putExtra("amount", amount);
                        String inputMoneyString = String.valueOf(inputMoney);
                        intent.putExtra("inputMoney", inputMoneyString);
                        intent.putExtra("paytype", "CASHBACK");
                        String inputcashbackMoney = String.valueOf(cashbackAmounts);
                        intent.putExtra("cashbackAmounts", inputcashbackMoney);
                        intent.putExtra("conType", conType);
                        intent.putExtra("connect_type", 3);
                        startActivity(intent);
                        finish();
                    } else if (conType != null && "blue".equals(conType)) {//blue
                        Intent intent = new Intent(CashBackPaymentActivity.this, PaymentActivity.class);
                        intent.putExtra("amount", amount);
                        String inputMoneyString = String.valueOf(inputMoney);
                        intent.putExtra("inputMoney", inputMoneyString);
                        intent.putExtra("paytype", "CASHBACK");
                        String inputcashbackMoney = String.valueOf(cashbackAmounts);
                        intent.putExtra("cashbackAmounts", inputcashbackMoney);
                        intent.putExtra("connect_type", 1);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(CashBackPaymentActivity.this, getString(R.string.set_amount), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back_title:
                finish();
                break;
            default:
                break;
        }
    }

    private void inputMoneySetText() {
        String inputMoneyString = MoneyUtil.fen2yuan(cashbackAmounts);
        inputMoneyYuanText.setText(inputMoneyString.substring(0, inputMoneyString.length() - 2));
        inputMoneyFenText.setText(inputMoneyString.substring(inputMoneyString.length() - 2));
        amount = "¥" + inputMoneyString.substring(0, inputMoneyString.length() - 2) + inputMoneyString.substring(inputMoneyString.length() - 2);
        mAmount.setText(amount);

    }
}