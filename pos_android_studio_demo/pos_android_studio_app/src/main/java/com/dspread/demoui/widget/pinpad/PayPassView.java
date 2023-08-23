package com.dspread.demoui.widget.pinpad;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dspread.demoui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义支付密码组件
 */

public class PayPassView extends RelativeLayout {
    private Activity mContext;//上下文
    private GridView mGridView; //支付键盘
    private String strPass = "";//保存密码
    private TextView[] mTvPass;//密码数字控件
    private List<Integer> listNumber;//1,2,3---0
    private View mPassLayout;//布局
    private boolean isRandom;
    private EditText mEtinputpin;

    /**
     * 按钮对外接口
     */
    public static interface OnPayClickListener {
        //        void onPassFinish(String password);
//        void onPayClose();
//        void onPayForget();
        void onCencel();

        void onPaypass();

        void onConfirm(String password);
    }

    private OnPayClickListener mPayClickListener;

    public void setPayClickListener(OnPayClickListener listener) {
        mPayClickListener = listener;
    }

    public PayPassView(Context context) {
        super(context);
    }

    //在布局文件中使用的时候调用,多个样式文件
    public PayPassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //在布局文件中使用的时候调用
    public PayPassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;

        initView();//初始化
        this.addView(mPassLayout); //将子布局添加到父容器,才显示控件
    }

    /**
     * 初始化
     */
    private void initView() {
        mPassLayout = LayoutInflater.from(mContext).inflate(R.layout.view_paypass_layout, null);

        mEtinputpin = mPassLayout.findViewById(R.id.et_inputpin);
        mGridView = mPassLayout.findViewById(R.id.gv_pass);


        //初始化数据
        initData();
    }

    /**
     * isRandom是否开启随机数
     */
    private void initData() {
        if (isRandom) {
            listNumber = new ArrayList<>();
            listNumber.clear();
            for (int i = 0; i <= 10; i++) {
                listNumber.add(i);
            }
            //此方法是打乱顺序
            Collections.shuffle(listNumber);
            for (int i = 0; i <= 10; i++) {
                if (listNumber.get(i) == 10) {
                    listNumber.remove(i);
                    listNumber.add(9, 10);
                }
            }
            listNumber.add(R.mipmap.ic_pay_del0);
            listNumber.add(R.mipmap.ic_pay_del0);
            listNumber.add(R.mipmap.ic_pay_del0);
            listNumber.add(R.mipmap.ic_pay_del0);
        } else {
            listNumber = new ArrayList<>();
            listNumber.clear();
            for (int i = 1; i <= 9; i++) {
                listNumber.add(i);
            }
            listNumber.add(10);
            listNumber.add(0);
            listNumber.add(R.mipmap.ic_pay_del0);

        }
        mGridView.setAdapter(adapter);
    }


    /**
     * GridView的适配器
     */

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return listNumber.size();
        }

        @Override
        public Object getItem(int position) {
            return listNumber.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_paypass_gridview_item, null);
                holder = new ViewHolder();
                holder.btnNumber = (TextView) convertView.findViewById(R.id.btNumber);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //-------------设置数据----------------
            holder.btnNumber.setText(listNumber.get(position) + "");
            if (position == 10) {
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));
            }
            if (position == 9) {
                holder.btnNumber.setText(R.string.delete);
                holder.btnNumber.setTextSize(15);
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));
            }
            if (position == 11) {
                holder.btnNumber.setText("");
                holder.btnNumber.setBackgroundResource(listNumber.get(position));
            }
            if (position == 12) {
                holder.btnNumber.setText(R.string.bypass);
                holder.btnNumber.setTextSize(15);
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));

            }
            if (position == 13) {
                holder.btnNumber.setText(R.string.select_dialog_cancel);
                holder.btnNumber.setTextSize(15);
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));

            }

            if (position == 14) {
                holder.btnNumber.setText(R.string.select_dialog_confirm);
                holder.btnNumber.setTextSize(15);
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));
            }

            //监听事件----------------------------
            if (position == 11) {
                holder.btnNumber.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (position == 11) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del1);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del1);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del0);
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
            holder.btnNumber.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < 11 && position != 9) {//0-9按钮
                        if (strPass.length() == 12) {
                            return;
                        } else {
                            strPass = strPass + listNumber.get(position);//得到当前数字并累加
                            mEtinputpin.setText(strPass);
                        }
                    } else if (position == 11) {//删除
                        if (strPass.length() > 0) {
                            strPass = strPass.substring(0, strPass.length() - 1);//删除一位
                            mEtinputpin.setText(strPass);
                        }
                    }
                    if (position == 9) {//删除所有
                        if (strPass.length() > 0) {
                            strPass = "";//删除所有
                            mEtinputpin.setText("");
                        }
                    } else if (position == 12) {//paypass
                        mPayClickListener.onPaypass();
                    } else if (position == 13) {//cancel
                        mPayClickListener.onCencel();
                    } else if (position == 14) {//confirm
                        mPayClickListener.onConfirm(strPass);
                    }
                }
            });

            return convertView;
        }
    };

    static class ViewHolder {
        public TextView btnNumber;
    }

    /***
     * 设置随机数
     * @param israndom
     */
    public PayPassView setRandomNumber(boolean israndom) {
        isRandom = israndom;
        initData();
        adapter.notifyDataSetChanged();
        return this;
    }


}
