package com.dspread.demoui.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.dspread.demoui.R;

/**
 * @author user
 */
public class HDownloadProgressDialogUtils {

    private static ProgressDialog sHorizontalProgressDialog;

    private HDownloadProgressDialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Displays a horizontal progress bar pop-up
     *
     * @param context
     * @param msg
     * @param isShowSize Whether to display the progress size
     */
    public static void showHorizontalProgressDialog(Context context, String msg, boolean isShowSize) {
        cancel();
        if (sHorizontalProgressDialog == null) {
            sHorizontalProgressDialog = new ProgressDialog(context);
            sHorizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sHorizontalProgressDialog.setCancelable(false);
            if (isShowSize) {
                sHorizontalProgressDialog.setProgressNumberFormat("%2dMB/%1dMB");
            }

        }
        if (!TextUtils.isEmpty(msg)) {
            //sHorizontalProgressDialog.setMessage(msg);
        }
        sHorizontalProgressDialog.setIcon(R.drawable.download_icon);
        sHorizontalProgressDialog.setTitle(msg);
        sHorizontalProgressDialog.show();

    }

    public static void setMax(long total) {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
    }


    public static void cancel() {
        if (sHorizontalProgressDialog != null) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * Set the loading progress
     *
     * @param current Progress
     */
    public static void setProgress(int current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(current);
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * Sets the current file size
     *
     * @param current
     */
    public static void setProgress(long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        //Because the unit is MB
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

    /**
     * Loading
     *
     * @param total   Total size
     * @param current The current size
     */
    public static void onLoading(long total, long current) {
        if (sHorizontalProgressDialog == null) {
            return;
        }
        if (current == 0) {
            sHorizontalProgressDialog.setMax(((int) total) / (1024 * 1024));
        }
        sHorizontalProgressDialog.setProgress(((int) current) / (1024 * 1024));
        if (sHorizontalProgressDialog.getProgress() >= sHorizontalProgressDialog.getMax()) {
            sHorizontalProgressDialog.dismiss();
            sHorizontalProgressDialog = null;
        }
    }

}
