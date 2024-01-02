package com.dspread.demoui.utils;


import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dspread.demoui.ui.dialog.Mydialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.http.HttpRequest;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2023/10/9 16:16]
 * @updateRemark : [upload the log to dingding]
 */
public class DingTalkTest {
    public static void main(String[] args){

        try {

//rebot webhook

            String dingUrl = "https://oapi.dingtalk.com/robot/send?access_token=83e8afc691a1199c70bb471ec46d50099e6dd078ce10223bbcc56c0485cb5cc3";

//is or not to push everyone

            boolean isAtAll = false;

//all mobile list that push message

//            List mobileList = Lists.newArrayList();

//dingding test content

            String content = "issues: test reboot！";

//build the request msg

            String reqStr = buildReqStr(content, isAtAll);

//push msg(https)

            String result = postJson(dingUrl, reqStr);

            System.out.println("result == " + result);

        }catch (Exception e){

            e.printStackTrace();

        }
    }

    /**

     * build the request body

     * @param content

     * @return

     */

    public static String buildReqStr(String content, boolean isAtAll) {
        Map<String,String> contentMap = new HashMap<>();

//        contentMap.put("content", content);
        contentMap.put("title","issues");
        contentMap.put("text",content);

        Map atMap = new HashMap<>();

        atMap.put("isAtAll", isAtAll);

//        atMap.put("atMobiles", mobileList);

        Map reqMap = new HashMap<>();

        reqMap.put("msgtype", "markdown");

        reqMap.put("markdown", contentMap);

        reqMap.put("at", atMap);

        return JSON.toJSONString(reqMap);
//        return "";
    }

    private static final int timeout = 10000;
    public static String postJson(String url, String reqStr) {
        String body = null;
        try {
            body = HttpRequest.post(url).body(reqStr).timeout(timeout).execute().body();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;

    }

}
