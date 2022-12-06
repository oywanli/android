package com.dspread.demoui.activities.mpprint;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.action.printerservice.PrintStyle;
import com.action.printerservice.barcode.Barcode2D;
import com.dspread.demoui.R;
import com.dspread.demoui.activities.OtherActivity;
import com.dspread.demoui.utils.QRCodeUtil;
import com.dspread.demoui.utils.TRACE;
import com.dspread.demoui.view.BitmapPrintLine;
import com.dspread.demoui.view.PrintLine;
import com.dspread.demoui.view.PrinterLayout;
import com.dspread.demoui.view.TextPrintLine;
import com.dspread.xpos.CQPOSService;

import java.util.Hashtable;

public class MPPrintQRCodeActivity extends CommonActivity {
    private EditText etQRCode, etSize;
    private Spinner spErrLevel;
    private Spinner spAlignment;
    private String qrcontent = "";
    private int size = 72;
    private Spinner mSpinnerSpGreyLevel, mSpinnerSpAlignment;
    private int bitMapPosition;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.mp_qrcode_print));
        etQRCode = findViewById(R.id.et_qrcode_content);
        etSize = findViewById(R.id.et_qrcode_size);
        mSpinnerSpGreyLevel = findViewById(R.id.sp_grey_level);
        findViewById(R.id.align_area).setVisibility(View.VISIBLE);
        mSpinnerSpAlignment = findViewById(R.id.sp_alignment);
        mSpinnerSpAlignment.setSelection(1);
        mSpinnerSpAlignment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                if (item.equals("Left")) {
                    setPosition(0);
                } else if (item.equals("Right")) {
                    setPosition(2);
                } else if (item.equals("Center")) {
                    setPosition(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerSpGreyLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                pos.setPrintDensity(Integer.parseInt(item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onToolbarLinstener() {
        finish();

    }

    @Override
    int getLayoutId() {
        return R.layout.activity_mpprint_qrcode;
    }

    @Override
    int printTest() throws RemoteException {
        PrinterLayout printerLayout = new PrinterLayout(MPPrintQRCodeActivity.this);
        int size = Integer.parseInt(getText(etSize));
        Bitmap qrcodeBM = QRCodeUtil.getQrcodeBM(getText(etQRCode), size);
        switch (getPosition()) {
            case 0:
                bitMapPosition = PrintLine.LEFT;
                break;
            case 1:
                bitMapPosition = PrintLine.CENTER;
                break;
            case 2:
                bitMapPosition = PrintLine.RIGHT;
                break;
        }
        BitmapPrintLine bitmapPrintLine2 = new BitmapPrintLine(qrcodeBM, bitMapPosition);
        printerLayout.addBitmap(bitmapPrintLine2);
        TextPrintLine textPrintLine = new TextPrintLine();
        textPrintLine.setContent("\n");
        textPrintLine.setPosition(PrintLine.CENTER);
        printerLayout.addText(textPrintLine);
        Bitmap bitmap = printerLayout.viewToBitmap();
        pos.printQRCode(bitmap); // 二维码
        return 0;
    }

    @Override
    void onPrintFinished(Hashtable<String, String> result) {
        TRACE.d("ssss" + result.toString());
    }

    @Override
    void onPrintError(Hashtable<String, String> result) {

    }

    private String getText(EditText etText) {
        if (etText.getText() != null) {
            return etText.getText().toString().trim();
        } else {
            return "";
        }
    }
}
