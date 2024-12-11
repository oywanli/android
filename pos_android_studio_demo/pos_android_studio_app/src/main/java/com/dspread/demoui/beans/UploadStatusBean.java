package com.dspread.demoui.beans;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/11 15:19]
 * @updateRemark : [说明本次修改内容]
 */
public class UploadStatusBean {

    private String description;
    private String operateTime;
    private Integer status;
    private Integer subStatus;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Integer subStatus) {
        this.subStatus = subStatus;
    }
}
