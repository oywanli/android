package com.dspread.demoui.activities.serialprint;

import java.util.List;

public class PrintSUtil {

    private static  PrintSUtil instence;

    private List<PrintSettingBean> list;

    public static PrintSUtil getInstance(){
        if(instence == null){
            instence = new PrintSUtil();
        }
        return instence;
    }

    public void setList(List<PrintSettingBean> list) {
        this.list = list;
    }

    public List<PrintSettingBean> getList() {
        return list;
    }
}
