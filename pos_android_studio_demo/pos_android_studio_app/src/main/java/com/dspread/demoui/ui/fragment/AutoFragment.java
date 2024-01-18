package com.dspread.demoui.ui.fragment;

import static com.dspread.demoui.activity.BaseApplication.getApplicationInstance;
import static com.dspread.demoui.activity.BaseApplication.pos;
import static com.dspread.demoui.utils.Utils.open;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dspread.demoui.R;
import com.dspread.demoui.activity.BaseApplication;
import com.dspread.demoui.activity.PaymentUartActivity;
import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.utils.MoneyUtil;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.TitleUpdateListener;
import com.dspread.xpos.QPOSService;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AutoFragment extends Fragment {

    private TitleUpdateListener myListener;
    private TextView sub;
    private TextView sucesssub;
    private TextView fialsub;
    private EditText etSub;
    private Button btnTrade;
    private String etSubstr;
    private int totalRecord;
    private int record;
    String transactionTypeString = "GOODS";
    private View view;
    SharedPreferencesUtil connectType;
    String conType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (TitleUpdateListener) getActivity();
        myListener.sendValue(getString(R.string.device_info));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_auto, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        sub = view.findViewById(R.id.sub);
        sucesssub = view.findViewById(R.id.sucesssub);
        fialsub = view.findViewById(R.id.fialsub);
        etSub = view.findViewById(R.id.et_sub);
        btnTrade = view.findViewById(R.id.btn_trade);

        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getApplicationInstance = getActivity();
                etSubstr = etSub.getText().toString();
                if (etSubstr != null && !"".equals(etSubstr)) {
                    sub.setText("0");
                    sucesssub.setText("0");
                    fialsub.setText("0");
                    totalRecord = Integer.parseInt(etSubstr);
                    Constants.transData.setInputMoney("1000");
                    Constants.transData.setPayType(transactionTypeString);
                    Constants.transData.setPayment("payment");
                    Constants.transData.setAutoTrade("autoTrade");
                    Intent intent = new Intent(getApplicationInstance, PaymentUartActivity.class);
                    getApplicationInstance.startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请输入测试次数", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        Log.w("onResume","onResume");
        super.onResume();
        if (Constants.transData.getAutoTrade() != null && "StopTrade".equals(Constants.transData.getAutoTrade())) {
            Constants.transData.setAutoTrade("");
            initInfo();
            Toast.makeText(getActivity(), "停止交易", Toast.LENGTH_SHORT).show();
        } else {
            BaseApplication.getApplicationInstance = getActivity();
            record = Constants.transData.getSub();
            if (Constants.transData.getSub() != 0 && !"".equals(Constants.transData.getSub())) {
                sub.setText(Constants.transData.getSub() + "");
            }
            if (Constants.transData.getSuccessSub() != 0 && !"".equals(Constants.transData.getSuccessSub())) {
                sucesssub.setText(Constants.transData.getSuccessSub() + "");
            }
            if (Constants.transData.getFialSub() != 0 && !"".equals(Constants.transData.getFialSub())) {
                fialsub.setText(Constants.transData.getFialSub() + "");
            }
            if (record != 0 && !"".equals(record)) {
                if (totalRecord == record || record > totalRecord) {
                    Toast.makeText(getActivity(), "交易完成", Toast.LENGTH_SHORT).show();
                    initInfo();
                } else {
                    Log.w("transData", "transData");
                    transactionTypeString = "GOODS";
                    Constants.transData.setInputMoney("1000");
                    Constants.transData.setPayType(transactionTypeString);
                    Constants.transData.setPayment("payment");
                    Constants.transData.setAutoTrade("autoTrade");
                    Intent intent = new Intent(getApplicationInstance, PaymentUartActivity.class);
                    getApplicationInstance.startActivity(intent);
                }

            }
        }
    }


    public void initInfo() {
        Constants.transData.setInputMoney("");
        Constants.transData.setPayType("");
        Constants.transData.setCashbackAmounts("");
        Constants.transData.setPayment("");
        Constants.transData.setAutoTrade("");
        Constants.transData.setSuccessSub(0);
        Constants.transData.setFialSub(0);
        Constants.transData.setSub(0);
    }

}