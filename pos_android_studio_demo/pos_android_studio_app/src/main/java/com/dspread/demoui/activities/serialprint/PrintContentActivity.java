package com.dspread.demoui.activities.serialprint;

import static com.action.printerservice.PrintStyle.FontStyle.BOLD;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.action.printerservice.ActionPrinter;
import com.action.printerservice.PrintStyle;
import com.action.printerservice.barcode.Barcode1D;
import com.action.printerservice.barcode.Barcode2D;
import com.dspread.demoui.R;
import com.dspread.demoui.activities.BaseActivity;

import java.util.List;

public class PrintContentActivity extends BaseActivity {

    private Button bt_init,bt_print;
    private EditText et_input_1;
    private ImageView print_image;
    private int type;
    private int imagegraythreshold;
    private String barcode;
    private int barheight;
    private int barwidth;
    private String qrcodeerror;
    private String qrcodetype;
    private int qrsize;
    private int btmsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type",0);
        setTitle(this.getResources().getString(getIntent().getIntExtra("title", R.string.print_Test)));
        initView();
        printer = ActionPrinter.getInstance(getApplicationContext());
        try {
            initPrintSetting();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initPrintSetting() throws RemoteException {
        if(PrintSUtil.getInstance().getList()!=null){
            List<PrintSettingBean> list = PrintSUtil.getInstance().getList();
            for (PrintSettingBean bean : list){
                 if(bean.getValue()==null||bean.getValue()==""){
                     continue;
                 }
                switch (bean.getId()){
                    case R.string.text_alignment:
                        int type = 0;
                        if(bean.getValue().equals(getApplicationContext().getResources().getString(R.string.select_dialog_normal))){
                            type = PrintStyle.Alignment.NORMAL;
                        }else if(bean.getValue().equals(getApplicationContext().getResources().getString(R.string.select_dialog_center))){
                            type = PrintStyle.Alignment.CENTER;
                        }else if(bean.getValue().equals(getApplicationContext().getResources().getString(R.string.select_dialog_align_opposite))){
                            type = PrintStyle.Alignment.ALIGN_OPPOSITE;
                        }
                        printer.setPrintStyle(PrintStyle.Key.ALIGNMENT,type);
                        break;
                    case R.string.bold_text:
                        printer.setPrintStyle(PrintStyle.Key.FONT_STYLE,BOLD);
                        break;
                    case R.string.text_underline:
                        printer.setPrintStyle(PrintStyle.Key.TEXT_UNDER_LINE,1);
                        break;
                    case R.string.Line_spacing:
                        printer.setPrintStyle(PrintStyle.Key.LINE_SPACING,Integer.valueOf(bean.getValue()));
                        break;
                    case R.string.text_spacing:
                        printer.setPrintStyle(PrintStyle.Key.WORD_SPACING ,Integer.valueOf(bean.getValue()));
                        break;
                    case R.string.font_size:
                        printer.setPrintStyle(PrintStyle.Key.FONT_SIZE ,Integer.valueOf(bean.getValue()));
                        break;
                    case R.string.grayscale_values:
                        printer.setParameter(1,Integer.valueOf(bean.getValue()));
                        break;
                    case R.string.Pictures_and_black_marks_Grayscale_values:
                        imagegraythreshold = Integer.valueOf(bean.getValue());
                        break;
                    case R.string.Barcode_Type:
                        barcode = bean.getValue();
                        break;
                    case R.string.bottom_spacing:
                        btmsp =  Integer.valueOf(bean.getValue());
                        break;
                    case R.string.Barcode_width:
                        barwidth = Integer.valueOf(bean.getValue());
                        break;
                    case R.string.Barcode_height:
                        barheight = Integer.valueOf(bean.getValue());
                        break;
                    case R.string.QR_code_error_correction_level:
                        qrcodeerror = bean.getValue();
                        break;
                    case R.string.QR_code_type:
                        qrcodetype = bean.getValue();
                        break;
                    case R.string.QR_code_size:
                        qrsize = Integer.valueOf(bean.getValue());
                        break;
                }
            }
        }
    }

    private Button btn;

    private Button btn1;

    private void initView() {
        btn = findViewById(R.id.bt_stop);
        btn.setOnClickListener(onclickListener);;
        et_input_1 = findViewById(R.id.et_input_1);;
        btn1 = findViewById(R.id.bt_print);
        btn1.setOnClickListener(onclickListener);;
        print_image = findViewById(R.id.print_image);;
        switch (type){
            case PrintType.key.PRINT_TEXT:
                et_input_1.setText("English Français gründen にほんご ภาษาไทย 한국어 简体中文 繁體中文 12345 ");
                print_image.setVisibility(View.GONE);
                break;
            case PrintType.key.PRINT_IMAGE:
                et_input_1.setVisibility(View.GONE);
                break;
            case PrintType.key.PRINT_BARCODE:
                print_image.setVisibility(View.GONE);
                break;
            case PrintType.key.PRINT_QR_CODE:
                print_image.setVisibility(View.GONE);
                break;
            case PrintType.key.PRINT_BLACK_MARK:
                print_image.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }

    private ActionPrinter printer;

    View.OnClickListener onclickListener = new View.OnClickListener(){

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_stop:
                    try {
                        printer.clearBuffer();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.bt_print:
                    try {
                        try {
                            initPrint();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if(printer == null){
                            return;
                        }
                        switch(type){
                            case PrintType.key.PRINT_TEXT:
                            case PrintType.key.PRINT_QR_CODE:
                            case PrintType.key.PRINT_BARCODE:
                                if(et_input_1.getText()==null||et_input_1.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(),"no Data",Toast.LENGTH_SHORT).show();
                                }
                            case PrintType.key.PRINT_IMAGE:
                            case PrintType.key.PRINT_BLACK_MARK:
                            default:
                                print();
                                break;

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void print() throws RemoteException {
//        int re = printTest();

        printer.lineFeed(btmsp==0?5:btmsp);
        printer.print(new MyPrinterCallback() {

            @Override
            public void onPrintStart() throws RemoteException {
                super.onPrintStart();
                enableButton(btn1, true);
            }

            @Override
            public void onPrintFinish(int height) throws RemoteException {
                super.onPrintFinish(height);
                enableButton(btn, true);
            }

            @Override
            public void onError(int error, String message) throws RemoteException {
                super.onError(error, message);
                enableButton(btn, true);
            }

        });
    }

    public void enableButton(Button button, boolean enable) {
        button.post(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(enable);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void initPrint() throws RemoteException {
        switch (type){
            case PrintType.key.PRINT_TEXT:
//                et_input_1.setText("にほんご ภาษาไทย 한국어 Français gründen 简体中文 繁體中文 12345 ");
                printer.addText(et_input_1.getText().toString());
                break;
            case PrintType.key.PRINT_IMAGE:
                et_input_1.setVisibility(View.GONE);
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
                Bitmap bitmap = drawableToBitmap(drawable);

                printer.addBitmap(bitmap, imagegraythreshold==0?50:imagegraythreshold);
                break;
            case PrintType.key.PRINT_BARCODE:
                print_image.setVisibility(View.GONE);
                printer.add1DBarcode(et_input_1.getText().toString(), barcode==null?Barcode1D.CODE_128.name():barcode,barwidth==0?72:barwidth,barheight==0?10:barheight);
                break;
            case PrintType.key.PRINT_QR_CODE:
                print_image.setVisibility(View.GONE);
                printer.add2DBarcode(et_input_1.getText().toString(), qrcodetype==null?Barcode2D.QR_CODE.name():qrcodetype,qrsize==0?70:qrsize,qrcodeerror==null?Barcode2D.ErrorLevel.H.name():qrcodeerror);
                break;
            case PrintType.key.PRINT_BLACK_MARK:
                print_image.setVisibility(View.GONE);
                Bitmap bitmap1 = Bitmap.createBitmap(384, 72, Bitmap.Config.ALPHA_8);
                Canvas canvas = new Canvas(bitmap1);
                Paint paint = new TextPaint();
                paint.setAlpha(255);
                paint.setStyle(Paint.Style.FILL);
                RectF rectF = new RectF(0, 0, 384, 72);
                canvas.drawRect(rectF, paint);  //以上是创建灰色bitmap图像
                printer.addBitmap(bitmap1, imagegraythreshold==0?50:imagegraythreshold);
                break;
            default:
                return;
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        //取drawable的宽高
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //创建对应的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //创建对应的bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把drawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public Bitmap resizeImage(Bitmap bitmap, int width, int height)
    {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        float scaleWidth = ((float) width) / bmpWidth;
        float scaleHeight = ((float) height) / bmpHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @Override
    public void onToolbarLinstener() {
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_serial_print_text;
    }
}
