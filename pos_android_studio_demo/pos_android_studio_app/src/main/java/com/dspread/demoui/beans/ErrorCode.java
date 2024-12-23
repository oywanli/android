package com.dspread.demoui.beans;


/**
 * 全局错误码枚举 0他999比较特殊，作为成功他未知错误使用。 1-998 HTTP编码保留,禁止使用。HTTP 响应状态码
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 *
 * 业务异常的错误码区间，解决各模块错误码定义，避免重复
 *
 * 一共 10位，分成四段
 *
 * 第一段，1位，类型 1 - 业务级别异常 x - 预留 第二段，3位，系统类型 001 - biz1 002 - biz2 ... - ... 第三段，3位，模块 不限制规则。 一般建议，每个系统里面，可能有多个模块 001 -
 * modue1 002 - modue2 第四段，3 位，错误码 每个模块自增。
 *
 * @author tangyundong
 * @since 2022-10-28 18:31:41
 */
public enum ErrorCode {
    SUCCESS(0, "Success"),
    UNKNOWN(999, "Unknow error"),
    PARAMETER_INVALIDATE(1003000001, "Parameter invalidate"),
    SQL_EXCEPTION(1003000002, "Sql exception"),

//    // 设备信息
    SN_NOT_EXIST_DATABASE(1003000100, "The device sn data does not exist in the database"),
    FILE_NOT_EXIST(1003000102, "file does not exist"),
    TASK_NOT_EXIST(1003000104, "The device task does not exist or is in an illegal state"),
    TASK_CAN_NOT_DEGRADE(1003000105, "Can not degrade"),
    TASK_APK_VERSION_IS_SAME(1003000106, "APK version is same"),
    TASK_VERSION_NOT_MATCH(1003000107, "Version is not match"),
    SN_NOT_EXIST(1003000108, "Cannot find parameter information of sn"),
    DEVICE_NOT_EXIST(1003000109, "The sn does not exist"),
    PLATFORM_ERROR(1003000110, "The device hardware platform version does not support the task file"),
    TASK_RESOURCE_VERSION_IS_SAME(1003000106, "The version is same"),
    CAN_NOT_UPDATE_CUST(1003000107, "Customized packages for different customers cannot update each other"),
    GET_MODEL_ERROR(1003000108, "Get model error"),
    GET_SCHEDULED_TASK_ERROR(1003000109, "Get the next task exception to be executed in the orchestration"),
    S3_RE_UPLOAD_ERROR(1003000110, "No upload exist"),
    POST_INSPECT_INFO_ERROR(1003000111, "The inspection information sent to the server is abnormal."),
    PARSE_TIME_ERROR(1003000112, "Wrong date format"),
    ANALYST_INSPECT_INFO_ERROR(1003000113, "Analyze abnormal equipment inspection information"),
    TASK_NETWORK_STRATEGY_ERROR(1003000114,"The current task network policy is WiFi only, the device network is cellular, and push is not allowed");
    private String msg;
    private int code;

    ErrorCode(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
