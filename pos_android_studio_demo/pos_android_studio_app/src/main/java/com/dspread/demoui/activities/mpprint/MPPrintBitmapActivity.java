package com.dspread.demoui.activities.mpprint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dspread.demoui.R;
import com.dspread.demoui.utils.TRACE;

import androidx.annotation.RequiresApi;

public class MPPrintBitmapActivity extends CommonActivity {
    private ImageView iv;
    private Spinner mSpinnerSpGreyLevel;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.gray_level_area).setVisibility(View.GONE);
        iv = findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        iv.setImageBitmap(bitmap);
//        printTest();
        findViewById(R.id.align_area).setVisibility(View.INVISIBLE);
        findViewById(R.id.gray_level_area).setVisibility(View.INVISIBLE);
        mSpinnerSpGreyLevel = findViewById(R.id.sp_grey_level);
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
        return R.layout.activity_print_bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    int printTest() throws RemoteException {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        pos.printBitmap(bitmap);

        return 0;
    }

    @Override
    void onPrintFinished(boolean isSuccess, String status) {
        if (status != null) {
            TRACE.d("ssss" + status);
        }

    }

    @Override
    void onPrintError(boolean isSuccess, String status) {

    }

}
