package com.dspread.demoui.activity.printer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
public class PrinterAlertDialog {
    public static void showAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error"); //
        builder.setMessage("D70 initialization failed");
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 关闭弹窗
            }
        });
        AlertDialog dialog = builder.create();
        // 设置为点击外部不能消失
        dialog.setCancelable(false); // 返回键不可关闭
        dialog.setCanceledOnTouchOutside(false); // 点击外部不可关闭
        // 显示弹窗
        dialog.show();
    }
}
