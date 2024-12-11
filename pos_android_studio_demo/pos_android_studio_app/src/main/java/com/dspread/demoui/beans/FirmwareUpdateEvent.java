package com.dspread.demoui.beans;

import com.dspread.xpos.QPOSService;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/10 18:03]
 * @updateRemark : [说明本次修改内容]
 */
public class FirmwareUpdateEvent {
    public final byte[] data;

    public FirmwareUpdateEvent(byte[] data) {
        this.data = data;
    }
}
