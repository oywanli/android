package com.dspread.demoui.http;

import com.dspread.demoui.beans.Constants;
import com.dspread.demoui.utils.TRACE;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import okhttp3.Call;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/9 17:14]
 * @updateRemark : [说明本次修改内容]
 */
public class DownloadFirmwareAPI {

    public void  checkHeartBeat(String sn){
        OkGo.<String>post(String.format(Constants.TerminalHeartBeatUrl, sn))
        .tag(this)
        .execute(new StringCallback() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response<String> response) {
                TRACE.i("response = "+response.body());
            }

            @Override
            public void onError(Response<String> response) {

            }
        });

    }
}
