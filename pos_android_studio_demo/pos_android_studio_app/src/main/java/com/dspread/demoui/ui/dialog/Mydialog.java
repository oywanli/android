package com.dspread.demoui.ui.dialog;

import static com.xuexiang.xutil.resource.ResUtils.getString;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dspread.demoui.R;
import com.dspread.demoui.activity.InputCashBack;
import com.dspread.demoui.activity.PaymentActivity;
import com.dspread.demoui.utils.MoneyUtil;
import com.dspread.demoui.utils.MyListener;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.Utils;
import com.dspread.demoui.widget.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class Mydialog {
    public interface OnMyClickListener {
        void onCencel();

        void onConfirm();
    }

    /**
     * 加载框
     */
    public static Dialog Ldialog;

    public static void loading(Activity mContext, String msg) {
        Ldialog = new Dialog(mContext);
        Ldialog.setContentView(R.layout.processing_dialog);
        Button confirmButton = Ldialog.findViewById(R.id.confirmButton);
        TextView messageTextView = Ldialog.findViewById(R.id.msgTextView);
        messageTextView.setText(msg);
        messageTextView.setTextSize(20);
        Window dialogWindow = Ldialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        Ldialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        lp.dimAmount = 0.4f;
        dialogWindow.setAttributes(lp);
        Ldialog.setCanceledOnTouchOutside(false);
        Ldialog.setCancelable(false);
        if (!mContext.isFinishing()) {
            Ldialog.show();
        }
    }

    /*
     *错误信息提示框
     */
    public static AlertDialog ErrorDialog;

    public static void ErrorDialog(Activity mContext, String msg, OnMyClickListener listener) {
        View view = View.inflate(mContext, R.layout.alert_dialog, null);
        Button mbtnConfirm = view.findViewById(R.id.btnConfirm);
        TextView mtvInfo = view.findViewById(R.id.tvInfo);
        mtvInfo.setText(msg);
        Button mbtnCancel = view.findViewById(R.id.btnCancel);
        mbtnCancel.setVisibility(View.GONE);
        mbtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onConfirm();
                } else {
                    ErrorDialog.dismiss();
                    if (!msg.equals(mContext.getString(R.string.bad_swipe))) {
                        mContext.finish();
                    }
                }
            }
        });

        ErrorDialog = new AlertDialog.Builder(mContext).create();      //创建AlertDialog对象
        ErrorDialog.setCanceledOnTouchOutside(false);
        ErrorDialog.setCancelable(false);
        if (!mContext.isFinishing()) {
            ErrorDialog.show();
        }
        //显示对话框
        Window window = ErrorDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null); // 重设background
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //设置正面按钮
        Display d = wm.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = ErrorDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        ErrorDialog.getWindow().setAttributes(p); //设置生效
        window.setContentView(view);

    }

    /**
     * 提示对话框
     */
    public static AlertDialog TalertDialog;

    public static void TalertDialog(Activity mContext, String msg, OnMyClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.alert_dialog, null);
        View viewv = view.findViewById(R.id.view_v);
        viewv.setVisibility(View.VISIBLE);
        TextView mtvInfo = view.findViewById(R.id.tvInfo);
        mtvInfo.setText(msg);
        Button mbtnConfirm = view.findViewById(R.id.btnConfirm);
        Button mbtnCancel = view.findViewById(R.id.btnCancel);
        mbtnCancel.setVisibility(View.VISIBLE);
        mbtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm();
            }
        });
        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCencel();
            }
        });

        TalertDialog = builder.create();      //
        if (!mContext.isFinishing()) {
            TalertDialog.show();
        }
        Window window = TalertDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null); // 重设background
        window.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = TalertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        TalertDialog.getWindow().setAttributes(p); //设置生效
        TalertDialog.setCanceledOnTouchOutside(true);
        window.setContentView(view);
    }


    public static AlertDialog PalertDialog;
    private static RecyclerView rvlist;
    private static String transactionTypeString = "GOODS";
    private static MyAdapter myAdapter;

    public static void showDialog(Activity mContext, String amount, long inputMoney, String[] data) {

        PalertDialog = new AlertDialog.Builder(mContext).create();
        if (!mContext.isFinishing()) {
            PalertDialog.show();
        }
        Window window = PalertDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null); // 重设background
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = PalertDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); //高度设置为屏幕的0.6
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        PalertDialog.getWindow().setAttributes(p); //设置生效
        View view = View.inflate(mContext, R.layout.paytype_dialog_view, null);
        rvlist = view.findViewById(R.id.rv_list);
        rvlist.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));
        myAdapter = new MyAdapter(getArrayList(data));
        rvlist.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String content) {
                if (Utils.islistFastClick()) {
                    SharedPreferencesUtil connectType = SharedPreferencesUtil.getmInstance();
                    String conType = (String) connectType.get(mContext, "conType", "");
                    if (content.equals("CASHBACK")) {
                        Intent intent = new Intent(mContext, InputCashBack.class);
                        intent.putExtra("amount", amount);
                        String inputMoneyString = String.valueOf(inputMoney);
                        intent.putExtra("inputMoney", inputMoneyString);
                        intent.putExtra("paytype", "CASHBACK");
                        intent.putExtra("connect_type", 2);
                        mContext.startActivity(intent);
                    } else {
                        if (conType != null && conType.equals("uart")) {
                            transactionTypeString = content;
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            intent.putExtra("amount", amount);
                            String inputMoneyString = String.valueOf(inputMoney);
                            intent.putExtra("inputMoney", inputMoneyString);
                            intent.putExtra("paytype", transactionTypeString);
                            intent.putExtra("connect_type", 2);
                            mContext.startActivity(intent);
                        } else if (conType != null && conType.equals("usb")) {
                            transactionTypeString = content;
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            intent.putExtra("amount", amount);
                            String inputMoneyString = String.valueOf(inputMoney);
                            intent.putExtra("inputMoney", inputMoneyString);
                            intent.putExtra("paytype", transactionTypeString);
                            intent.putExtra("conType", conType);
                            intent.putExtra("connect_type", 3);
                            mContext.startActivity(intent);
                        } else if (conType != null && conType.equals("blue")) {//blue
                            transactionTypeString = content;
                            Intent intent = new Intent(mContext, PaymentActivity.class);
                            intent.putExtra("amount", amount);
                            String inputMoneyString = String.valueOf(inputMoney);
                            intent.putExtra("inputMoney", inputMoneyString);
                            intent.putExtra("paytype", transactionTypeString);
                            intent.putExtra("connect_type", 1);
                            mContext.startActivity(intent);
                        }
                    }
                    PalertDialog.dismiss();

                }
            }
        });
        window.setContentView(view);
        PalertDialog.setCanceledOnTouchOutside(true);
        PalertDialog.setCancelable(true);
    }

    private static List<String> getArrayList(String[] data) {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            dataList.add(data[i]);
        }
        return dataList;
    }

    //交易确认信息
    public static AlertDialog onlingDialog;

    public static void onlingDialog(Activity mContext, boolean isPinCanceled, OnMyClickListener listener) {
        View view = View.inflate(mContext, R.layout.processing_dialog, null);
        TextView textView = view.findViewById(R.id.messageTextView);
        LinearLayout btnlayout = view.findViewById(R.id.btn_layout);
        btnlayout.setVisibility(View.VISIBLE);
        textView.setTextSize(20);
        if (isPinCanceled) {
            textView.setText(R.string.replied_failed);
        } else {
            textView.setText(R.string.replied_success);
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button cancelButton = view.findViewById(R.id.cancelButton);
        view.findViewById(R.id.confirmButton).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onConfirm();
                    }
                });

        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCencel();

                    }
                });
        onlingDialog = new AlertDialog.Builder(mContext).create();      //创建AlertDialog对象
        onlingDialog.setCanceledOnTouchOutside(false);
        onlingDialog.setCancelable(false);
        if (!mContext.isFinishing()) {
            onlingDialog.show();
        }
        Window window = onlingDialog.getWindow();
        window.setWindowAnimations(R.style.popupAnimation);
        window.setBackgroundDrawable(null); // 重设background
        window.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = onlingDialog.getWindow().getAttributes(); //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.3); //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 1); //宽度设置为屏幕的1

        onlingDialog.getWindow().setAttributes(p); //设置生效
        window.setContentView(view);
    }

}
