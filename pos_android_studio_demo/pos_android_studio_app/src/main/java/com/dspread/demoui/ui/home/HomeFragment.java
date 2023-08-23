package com.dspread.demoui.ui.home;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dspread.demoui.R;
import com.dspread.demoui.ui.dialog.Mydialog;
import com.dspread.demoui.utils.MoneyUtil;
import com.dspread.demoui.utils.MyListener;


public class HomeFragment extends Fragment implements View.OnClickListener {

    //    private FragmentHomeBinding binding;
    private TextView inputMoneyYuanText, inputMoneyFenText;
    private long inputMoney = 0;
    private Bundle bundle;
    private Button mConfirm;
    private TextView mAmount;
    private String amount = "";

    View view;
    MyListener myListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//         myListener = (MyListener) getActivity();
//        myListener.sendValue(getString(R.string.menu_payment));

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_input_money, null);
        myListener = (MyListener) getActivity();
//        myListener.sendValue(getString(R.string.menu_payment));
        getActivity().setTitle(getString(R.string.menu_payment));
        initView(view);
        initData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.add(0, 1, 0, "posinfo");
        menu.add(0, 2, 0, "posid");
        super.onCreateOptionsMenu(menu, inflater);
    }


    protected void initView(View view) {
        inputMoneyYuanText = view.findViewById(R.id.inputMoneyYuanText);
        inputMoneyFenText = view.findViewById(R.id.inputMoneyFenText);
        Button btn_num00 = view.findViewById(R.id.btn_num00);
        btn_num00.setOnClickListener(this);
        Button btn_num0 = view.findViewById(R.id.btn_num0);
        btn_num0.setOnClickListener(this);
        Button btn_num1 = view.findViewById(R.id.btn_num1);
        btn_num1.setOnClickListener(this);
        Button btn_num2 = view.findViewById(R.id.btn_num2);
        btn_num2.setOnClickListener(this);
        Button btn_num3 = view.findViewById(R.id.btn_num3);
        btn_num3.setOnClickListener(this);
        Button btn_num4 = view.findViewById(R.id.btn_num4);
        btn_num4.setOnClickListener(this);
        Button btn_num5 = view.findViewById(R.id.btn_num5);
        btn_num5.setOnClickListener(this);
        Button btn_num6 = view.findViewById(R.id.btn_num6);
        btn_num6.setOnClickListener(this);
        Button btn_num7 = view.findViewById(R.id.btn_num7);
        btn_num7.setOnClickListener(this);
        Button btn_num8 = view.findViewById(R.id.btn_num8);
        btn_num8.setOnClickListener(this);
        Button btn_num9 = view.findViewById(R.id.btn_num9);

        btn_num9.setOnClickListener(this);
        // 清除
        ImageView btn_num_clear = view.findViewById(R.id.btn_num_clear);
        btn_num_clear.setOnClickListener(this);
        mConfirm = view.findViewById(R.id.btn_confirm);
        mConfirm.setOnClickListener(this);
        mAmount = view.findViewById(R.id.tv_amount);
    }

    protected void initData() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
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

                if ((inputMoney + "").length() < 12) {
                    inputMoney = Long.parseLong(inputMoney + ((Button) v).getText().toString());

                    inputMoneySetText();
                }
                break;

            case R.id.btn_num00:

                if ((inputMoney + "").length() < 11) {
                    inputMoney = Long.parseLong(inputMoney + ((Button) v).getText().toString());

                    inputMoneySetText();
                }

                break;

            case R.id.btn_num_clear:
                if (inputMoney > 0) {
                    inputMoney = inputMoney / 10;
                    inputMoneySetText();
                }
                break;
            case R.id.btn_confirm:
                if (inputMoney > 0) {
                    if (!canshow){
                        return;
                    }
                    canshow=false;
                    showTimer.start();
                    Mydialog.showDialog(getActivity(), amount, inputMoney, data);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.set_amount), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void inputMoneySetText() {
        String inputMoneyString = MoneyUtil.fen2yuan(inputMoney);
        inputMoneyYuanText.setText(inputMoneyString.substring(0, inputMoneyString.length() - 2));
        inputMoneyFenText.setText(inputMoneyString.substring(inputMoneyString.length() - 2));
        amount = "¥" + inputMoneyString.substring(0, inputMoneyString.length() - 2) + inputMoneyString.substring(inputMoneyString.length() - 2);
        mAmount.setText(amount);

    }

    @Override
    public void onResume() {
        super.onResume();
        inputMoney = 0;
        inputMoneySetText();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private static int LOG_MAXLENGTH = 2000;

    public static void logshow(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;

        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) { // 剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private String[] data = {"GOODS", "SERVICES", "CASH", "CASHBACK", "INQUIRY",
            "TRANSFER", "ADMIN", "CASHDEPOSIT",
            "PAYMENT", "PBOCLOG||ECQ_INQUIRE_LOG", "SALE",
            "PREAUTH", "ECQ_DESIGNATED_LOAD", "ECQ_UNDESIGNATED_LOAD",
            "ECQ_CASH_LOAD", "ECQ_CASH_LOAD_VOID", "CHANGE_PIN", "REFOUND", "SALES_NEW"};




    private boolean canshow=true;
    private CountDownTimer showTimer = new CountDownTimer(500,500){
        @Override
        public void onTick(long millisUntilFinished) {
        }
        @Override
        public void onFinish() {
            canshow = true;
        }

    };

}

