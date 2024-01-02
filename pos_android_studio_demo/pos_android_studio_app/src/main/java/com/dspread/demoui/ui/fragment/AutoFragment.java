package com.dspread.demoui.ui.fragment;

import static com.dspread.demoui.activity.BaseApplication.pos;
import static com.dspread.demoui.utils.Utils.open;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
    private  View view;
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
        connectType = SharedPreferencesUtil.getmInstance(getActivity());
        conType = (String) connectType.get("conType", "");
        if (conType != null && "uart".equals(conType)) {
            open(QPOSService.CommunicationMode.UART_SERVICE, getActivity());
        }
        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getApplicationInstance=getActivity();
                etSubstr = etSub.getText().toString();
                if (etSubstr != null && !"".equals(etSubstr)) {
                    totalRecord = Integer.parseInt(etSubstr);
                    Constants.transData.setInputMoney("1000");
                    Constants.transData.setPayType(transactionTypeString);
                    Constants.transData.setPayment("payment");
                    Constants.transData.setAutoTrade("autoTrade");
                    pos.getQposId();
                } else {
                    Toast.makeText(getActivity(), "请输入测试次数", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.w("onResume","Constants.transData.getSub() ="+Constants.transData.getSub());
        Log.w("onResume","Constants.transData.getSuccessSub()  ="+Constants.transData.getSuccessSub());
        Log.w("onResume","record ="+record);
        Log.w("totalRecord","totalRecord ="+totalRecord);
        BaseApplication.getApplicationInstance=getActivity();
        record = Constants.transData.getSub();
        if (Constants.transData.getSub() != 0 && !"".equals(Constants.transData.getSub())){
            sub.setText(Constants.transData.getSub()+"");
        }
        if (Constants.transData.getSuccessSub() != 0 && !"".equals(Constants.transData.getSuccessSub())){
            sucesssub.setText(Constants.transData.getSuccessSub()+"");
        }
        if (Constants.transData.getFialSub() != 0 && !"".equals(Constants.transData.getFialSub())){
            fialsub.setText(Constants.transData.getFialSub()+"");
        }
        if (record != 0 && !"".equals(record)){
            if (totalRecord==record){
                Toast.makeText(getActivity(), "交易完成", Toast.LENGTH_SHORT).show();
                initInfo();
            }else{
                Log.w("transData","transData");
                transactionTypeString = "GOODS";
                Constants.transData.setInputMoney("1000");
                Constants.transData.setPayType(transactionTypeString);
                Constants.transData.setPayment("payment");
                Constants.transData.setAutoTrade("autoTrade");
                pos.getQposId();
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        conType = (String) connectType.get("conType", "");
        Log.w("onHiddenChanged", "onHiddenChanged==" + conType);
        if (!hidden) {
            if (conType != null && "uart".equals(conType)) {
                open(QPOSService.CommunicationMode.UART_SERVICE, getActivity());
            }
        } else {
            if (conType != null && "uart".equals(conType)) {
                initInfo();
                pos.closeUart();
            }

        }
    }
    public void initInfo(){
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