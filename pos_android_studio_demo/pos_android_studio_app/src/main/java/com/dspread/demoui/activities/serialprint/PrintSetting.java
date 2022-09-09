package com.dspread.demoui.activities.serialprint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.dspread.demoui.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PrintSetting extends BaseActivity {

    private ListView lv;

    private List<PrintSettingBean> list;

    private PrintAdapter adapter;
    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(this.getResources().getString(R.string.print_setting));
        initData();
        initView();
    }

    private void initData() {
        if(PrintSUtil.getInstance().getList()!=null){
            list = PrintSUtil.getInstance().getList();
            return;
        }
        list = new ArrayList<>();
        list.add(new PrintSettingBean(this.getResources().getString(R.string.bottom_spacing),"5", R.string.bottom_spacing));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.text_alignment),null, R.string.text_alignment));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.bold_text),null, R.string.bold_text));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.text_underline),null, R.string.text_underline));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.Line_spacing),null, R.string.Line_spacing));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.text_spacing),null, R.string.text_spacing));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.font_size),null, R.string.font_size));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.grayscale_values),null, R.string.grayscale_values));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.Pictures_and_black_marks_Grayscale_values),null, R.string.Pictures_and_black_marks_Grayscale_values));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.Barcode_Type),null, R.string.Barcode_Type));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.Barcode_width),null, R.string.Barcode_width));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.Barcode_height),null, R.string.Barcode_height));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.QR_code_error_correction_level),null, R.string.QR_code_error_correction_level));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.QR_code_type),null, R.string.QR_code_type));
        list.add(new PrintSettingBean(this.getResources().getString(R.string.QR_code_size),null, R.string.QR_code_size));
        PrintSUtil.getInstance().setList(list);
    }



    private void sDialog(int position){
        ad = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit,null,true);
        EditText edit = view.findViewById(R.id.edit_did);
        edit.setInputType(InputType.TYPE_CLASS_PHONE);
        ad.setView(view);
        ad.setTitle(list.get(position).getName());
        ad.setPositiveButton(getApplicationContext().getString(R.string.select_dialog_confirm), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),list.get(position).getName(),Toast.LENGTH_SHORT).show();
                String str = list.get(position).getName();
                int id = list.get(position).getId();
                list.set(position,new PrintSettingBean(str,edit.getText().toString(),id));
                PrintSUtil.getInstance().setList(list);
                adapter.notifyDataSetChanged();
              //  EditText editText = editText
            }}
        );
        ad.setNegativeButton(getApplicationContext().getString(R.string.select_dialog_cancel), new DialogInterface.OnClickListener() {
            /**设置取消监听时间，关闭当前dilaog**/
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create().show();
    }


    private void textAlDialog(int position){
        ad = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select,null,true);
        RadioGroup mRadioGroup = view.findViewById(R.id.radioGroup1);
        RadioButton radio = view.findViewById(R.id.radio0);
        RadioButton radio1 = view.findViewById(R.id.radio1);
        RadioButton radio2 = view.findViewById(R.id.radio2);
        RadioButton radio3 = view.findViewById(R.id.radio3);
        RadioButton radio4 = view.findViewById(R.id.radio4);
        RadioButton radio5 = view.findViewById(R.id.radio5);
        RadioButton radio6 = view.findViewById(R.id.radio6);
        if(list.get(position).getName().equals(this.getResources().getString(R.string.text_alignment))){
            radio.setText(R.string.select_dialog_normal);
            radio1.setText(R.string.select_dialog_center);
            radio2.setText(R.string.select_dialog_align_opposite);
            radio3.setVisibility(View.GONE);
            radio4.setVisibility(View.GONE);
            radio5.setVisibility(View.GONE);
            radio6.setVisibility(View.GONE);
        }else if(list.get(position).getName().equals(this.getResources().getString(R.string.Barcode_Type))){
            radio.setText("CODE_128");
            radio1.setText("CODABAR");
            radio2.setText("CODE_39");
            radio3.setText("EAN_8");
            radio4.setText("EAN_13");
            radio5.setText("UPC_A");
            radio6.setText("UPC_E");
        }else if(list.get(position).getName().equals(this.getResources().getString(R.string.QR_code_error_correction_level))){
            radio.setText("L");
            radio1.setText("M");
            radio2.setText("Q");
            radio3.setText("H");
            radio4.setVisibility(View.GONE);
            radio5.setVisibility(View.GONE);
            radio6.setVisibility(View.GONE);
        }else if(list.get(position).getName().equals(this.getResources().getString(R.string.QR_code_type))){
            radio.setText("QR_CODE");
            radio1.setText("AZTEC");
            radio2.setText("DATA_MATRIX");
            radio3.setText("MAXICODE");
            radio4.setText("PDF_417");
            radio5.setVisibility(View.GONE);
            radio6.setVisibility(View.GONE);
        }
        ad.setView(view);
        ad.setTitle(list.get(position).getName());
        ad.setPositiveButton(getApplicationContext().getString(R.string.select_dialog_confirm), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String value = "";
                switch(mRadioGroup.getCheckedRadioButtonId()){
                    case R.id.radio0:
                        value = radio.getText().toString();
                        break;
                    case R.id.radio1:
                        value = radio1.getText().toString();
                        break;
                    case R.id.radio2:
                        value = radio2.getText().toString();
                        break;
                    case R.id.radio3:
                        value = radio3.getText().toString();
                        break;
                    case R.id.radio4:
                        value = radio4.getText().toString();
                        break;
                    case R.id.radio5:
                        value = radio5.getText().toString();
                        break;
                    case R.id.radio6:
                        value = radio6.getText().toString();
                        break;
                }
                Toast.makeText(getApplicationContext(),radio.getText().toString(),Toast.LENGTH_SHORT).show();
                String str = list.get(position).getName();
                int id = list.get(position).getId();
                list.set(position,new PrintSettingBean(str,value,id));
                PrintSUtil.getInstance().setList(list);
                adapter.notifyDataSetChanged();
                //  EditText editText = editText
            }}
        );
        ad.setNegativeButton(getApplicationContext().getString(R.string.select_dialog_cancel), new DialogInterface.OnClickListener() {
            /**设置取消监听时间，关闭当前dilaog**/
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.create().show();
    }


    private void initView() {
        lv = findViewById(R.id.print_listview);
        adapter = new PrintAdapter(this,list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.text_alignment))||
                        list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.QR_code_type))||
                        list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.QR_code_error_correction_level))||
                        list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.Barcode_Type))) {
                    textAlDialog(position);
                }else if(list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.bold_text))||
                        list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.text_underline))){
                    selectDialog(position);
                }else{
                    sDialog(position);
                }
            }
        });
    }

    private void selectDialog(int position) {
        ad = new AlertDialog.Builder(this);

        ad.setTitle(list.get(position).getName());
        if(list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.bold_text))){

            ad.setMessage(getApplicationContext().getString(R.string.select_dialog_bold));
        }else if(list.get(position).getName().equals(getApplicationContext().getResources().getString(R.string.text_underline))){
            ad.setMessage(getApplicationContext().getString(R.string.select_dialog_text_underline));
        }
        ad.setPositiveButton(getApplicationContext().getString(R.string.select_dialog_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String str = list.get(position).getName();
                int id = list.get(position).getId();
                list.set(position,new PrintSettingBean(str,"true",id));
                PrintSUtil.getInstance().setList(list);
                adapter.notifyDataSetChanged();
                //  EditText editText = editText
            }}
        );
        ad.setNegativeButton(getApplicationContext().getString(R.string.select_dialog_no), new DialogInterface.OnClickListener() {
            /**设置取消监听时间，关闭当前dilaog**/
            public void onClick(DialogInterface dialog, int which) {
                String str = list.get(position).getName();
                int id = list.get(position).getId();
                list.set(position,new PrintSettingBean(str,"",id));
                PrintSUtil.getInstance().setList(list);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        ad.create().show();
    }

    @Override
    public void onToolbarLinstener() {
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_serial_printsetting;
    }
}
