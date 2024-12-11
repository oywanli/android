package com.dspread.demoui.beans;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/10 15:40]
 * @updateRemark : [说明本次修改内容]
 */
public class GetDownloadFileBean {

    private Integer code;
    private DataDTO data;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataDTO {
        private String fullUrl;
        private String fullHash;
        private Object diffUrl;
        private Object diffHash;

        public String getFullUrl() {
            return fullUrl;
        }

        public void setFullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
        }

        public String getFullHash() {
            return fullHash;
        }

        public void setFullHash(String fullHash) {
            this.fullHash = fullHash;
        }

        public Object getDiffUrl() {
            return diffUrl;
        }

        public void setDiffUrl(Object diffUrl) {
            this.diffUrl = diffUrl;
        }

        public Object getDiffHash() {
            return diffHash;
        }

        public void setDiffHash(Object diffHash) {
            this.diffHash = diffHash;
        }
    }
}
