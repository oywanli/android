package com.dspread.demoui.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dspread.demoui.ui.dialog.HDownloadProgressDialogUtils;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.file.FileUtils;

import java.io.File;

/**
 * @author user
 */
public class UpdateAppHelper {

    @Permission(PermissionConsts.STORAGE)
    public static void useApkDownLoadFunction(Context mContext, String downloadUrl) {
        XUpdate.newBuild(mContext)
                .apkCacheDir(PathUtils.getAppExtCachePath())
                .build()
                .download(downloadUrl, new OnFileDownloadListener() {
                    @Override
                    public void onStart() {
                        deleteFile(new File(PathUtils.getAppExtCachePath()));
                        HDownloadProgressDialogUtils.showHorizontalProgressDialog(mContext, "Downloading", false);
                    }

                    @Override
                    public void onProgress(float progress, long total) {
                        HDownloadProgressDialogUtils.setProgress(Math.round(progress * 100));
                    }

                    @Override
                    public boolean onCompleted(File file) {
                        TRACE.d("download：" + FileUtils.getFileByPath(file.getPath()));
                        HDownloadProgressDialogUtils.cancel();
                        _XUpdate.startInstallApk(mContext, FileUtils.getFileByPath(file.getPath()));
                        return false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        HDownloadProgressDialogUtils.cancel();
                    }
                });
    }


    public static Boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        File[] files = file.listFiles();

        for (File f : files) {

            if (f.isDirectory()) {

                deleteFile(f);
            } else {

                f.delete();

            }
        }
        return true;
    }

    /**
     * Gets the version number
     *
     * @return The version number of the current app
     */
    public static String getPackageVersionName(Context context, String pkgName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            //PackageManager.GET_CONFIGURATIONS
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static int getPackageVersionCode(Context context, String pkgName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(pkgName, 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
