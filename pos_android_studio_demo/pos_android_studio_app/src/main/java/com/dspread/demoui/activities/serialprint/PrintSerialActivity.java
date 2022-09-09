package com.dspread.demoui.activities.serialprint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.action.printerservice.ActionPrinter;
import com.dspread.demoui.R;
import com.dspread.demoui.activities.BaseActivity;

public class PrintSerialActivity extends BaseActivity implements View.OnClickListener{

    private TextView textPrint_text,imagePrint_text,barPrint_text,eqPrint_text,setting_text,codeChage_text;
    private ImageView textPrint_image,imagePrint_image,barPrint_image,eqPrint_image,setting_image,codeChage_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.print_Test);
        initView();
        ActionPrinter.getInstance(getApplicationContext()).bind();
    }

    private void initView() {
        textPrint_text=(TextView) findViewById(R.id.textPrint_text);
        imagePrint_text=(TextView) findViewById(R.id.imagePrint_text);
        barPrint_text=(TextView) findViewById(R.id.barPrint_text);
        eqPrint_text=(TextView) findViewById(R.id.eqPrint_text);
        setting_text=(TextView) findViewById(R.id.setting_text);
        codeChage_text=(TextView) findViewById(R.id.codeChage_text);

        textPrint_image=(ImageView) findViewById(R.id.textPrint_image);
        imagePrint_image=(ImageView) findViewById(R.id.imagePrint_image);
        barPrint_image=(ImageView) findViewById(R.id.barPrint_image);
        eqPrint_image=(ImageView) findViewById(R.id.eqPrint_image);
        setting_image=(ImageView) findViewById(R.id.setting_image);
        codeChage_image=(ImageView) findViewById(R.id.codeChage_image);


        textPrint_text.setOnClickListener(this);
        imagePrint_text.setOnClickListener(this);
        barPrint_text.setOnClickListener(this);
        eqPrint_text.setOnClickListener(this);
        setting_text.setOnClickListener(this);
        codeChage_text.setOnClickListener(this);

        textPrint_image.setOnClickListener(this);
        imagePrint_image.setOnClickListener(this);
        barPrint_image.setOnClickListener(this);
        eqPrint_image.setOnClickListener(this);
        setting_image.setOnClickListener(this);
        codeChage_image.setOnClickListener(this);
    }

    @Override
    public void onToolbarLinstener() {
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_serial_print;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId()== R.id.setting_image||v.getId()== R.id.setting_text){
            Intent intent= new Intent(this,PrintSetting.class);
            startActivity(intent);
            return;
        }
        Intent intent= new Intent(this,PrintContentActivity.class);
        switch (v.getId()) {
            //打印文本
            case R.id.textPrint_text:
            case R.id.textPrint_image:
                intent.putExtra("type", PrintType.key.PRINT_TEXT);
                intent.putExtra("title", R.string.str_printtext);
                break;
            //打印图片
            case R.id.imagePrint_text:
            case R.id.imagePrint_image:
                intent.putExtra("type", PrintType.key.PRINT_IMAGE);
                intent.putExtra("title", R.string.str_printimg);
                break;
            //打印一维条形码维码
            case R.id.barPrint_text:
            case R.id.barPrint_image:
                intent.putExtra("type", PrintType.key.PRINT_BARCODE);
                intent.putExtra("title", R.string.str_printbarcode);
                break;
            //二维码
            case R.id.eqPrint_text:
            case R.id.eqPrint_image:
                intent.putExtra("type", PrintType.key.PRINT_QR_CODE);
                intent.putExtra("title", R.string.str_printqrcode);
                break;
            //设置
            //传输code
            case R.id.codeChage_text:
            case R.id.codeChage_image:
                intent.putExtra("type", PrintType.key.PRINT_BLACK_MARK);
                intent.putExtra("title", R.string.code_transformation);
                break;
            default:
                break;
        }
        startActivity(intent);

    }

}