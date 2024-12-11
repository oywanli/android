package com.dspread.demoui.http;

import android.content.Context;
import android.widget.Toast;

import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.beans.FirmwareUpdateEvent;
import com.dspread.demoui.beans.GetDownloadFileBean;
import com.dspread.demoui.beans.GlobalErrorEvent;
import com.dspread.demoui.beans.TerminalHeartBean;
import com.dspread.demoui.beans.UploadStatusBean;
import com.dspread.demoui.ui.dialog.HDownloadProgressDialogUtils;
import com.dspread.demoui.utils.SharedPreferencesUtil;
import com.dspread.demoui.utils.TRACE;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/9 17:14]
 * @updateRemark : [说明本次修改内容]
 */
public class DownloadFirmwareAPI {
    private static Context mContext;
    private static SharedPreferencesUtil preferencesUtil;
    private static DownloadFirmwareAPI downloadFirmwareAPI;
    public static DownloadFirmwareAPI getInstance(Context context){
        mContext = context;
        preferencesUtil = SharedPreferencesUtil.getInstance(mContext);
        if(downloadFirmwareAPI == null){
            downloadFirmwareAPI = new DownloadFirmwareAPI();
        }
        return downloadFirmwareAPI;
    }
    private String taskId;

    public void checkHeartBeat(String sn){
        taskId = (String) preferencesUtil.get("taskId","");
        if(!"".equals(taskId)){
            getDownloadFileAPI(sn,taskId);
            return;
        }
        OkGo.<String>post(String.format(Constants.TerminalHeartBeatUrl, sn))
        .tag(this)
        .execute(new StringCallback() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                HDownloadProgressDialogUtils.showHorizontalProgressDialog(mContext, "Downloading", false);
            }

            @Override
            public void onSuccess(Response<String> response) {
                TRACE.i("response = "+response.body());
                Gson gson = new Gson();
                TerminalHeartBean heartBean = gson.fromJson(response.body(),TerminalHeartBean.class);
                if(heartBean.getData() != null){
                    if(heartBean.getData().getPushTasks() != null && heartBean.getData().getPushTasks().get(0) != null){
                        taskId = heartBean.getData().getPushTasks().get(0).getTaskId();
                        if(taskId != null){
                            preferencesUtil.put("taskId",taskId);
                            getDownloadFileAPI(sn,taskId);
                        }
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                HDownloadProgressDialogUtils.cancel();
            }
        });

    }

    private void getDownloadFileAPI(String sn, String taskId){
        OkGo.<String>get(String.format(Constants.GetTaskFileDownloadUrl, sn,taskId))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        HDownloadProgressDialogUtils.showHorizontalProgressDialog(mContext, "Start Downloading", false);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        TRACE.i("download file response = "+response.body());
                        Gson gson = new Gson();
                        GetDownloadFileBean downloadFileBean = gson.fromJson(response.body(),GetDownloadFileBean.class);
                        if(downloadFileBean.getData() != null){
                            String fullUrl = downloadFileBean.getData().getFullUrl();
                            if(fullUrl != null){
                                downloadFile(fullUrl);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        HDownloadProgressDialogUtils.cancel();
                    }
                });
    }

    private void downloadFile(String url){
        OkGo.<File>get(url)
                .tag(this) // 设置请求tag，用于取消下载任务
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        // 下载成功
                        File downloadedFile = response.body();
                        byte[] fileBytes = fileToByteArray(downloadedFile);
                        if(fileBytes != null){
                            EventBus.getDefault().post(new FirmwareUpdateEvent(fileBytes));
                        }
                        TRACE.d("下载成功: " + downloadedFile.getAbsolutePath());
                        HDownloadProgressDialogUtils.cancel();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        HDownloadProgressDialogUtils.cancel();
                        // 下载失败
                        Throwable e = response.getException();
                        TRACE.d("下载失败: " + e.getMessage());
                    }

                    @Override
                    public void downloadProgress(com.lzy.okgo.model.Progress progress) {
                        // 更新下载进度
                        int progressPercentage = (int) (progress.fraction * 100);
                        TRACE.d("Download Progress: " + progressPercentage + "%");
                        HDownloadProgressDialogUtils.setProgress(Math.round(progressPercentage));
                    }
                });
    }

    public void uploadStatus(String sn, String taskId, UploadStatusBean statusBean){
        Gson gson = new Gson();
        String jsonData = gson.toJson(statusBean);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        OkGo.<String>post(String.format(Constants.UpdateTaskLogUrl, sn,taskId))
                .tag(this)
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        HDownloadProgressDialogUtils.showHorizontalProgressDialog(mContext, "Start Uploading Device Status", false);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        TRACE.i("uploadStatus file response = "+response.body());
                        Toast.makeText(mContext,"Upload success!",Toast.LENGTH_LONG).show();
                        HDownloadProgressDialogUtils.cancel();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        HDownloadProgressDialogUtils.cancel();
                    }
                });
    }

    /**
     * 将文件转换为字节数组
     *
     * @param file 下载的文件
     * @return 字节数组
     */
    private byte[] fileToByteArray(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
