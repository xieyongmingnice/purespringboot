package com.phhc.sso.enums;

/**
 * 返回信息枚举类
 * @author xieym
 * @date 2019/8/29
 * @since
 **/
public enum ResponseEnum {
    /**
     * desc ： 10 开头权限问题
     *         2&20 开头 数据请求成功&无数据
     *         3&30 开头 重定向
     *         4&40 开头 客户端请求&请求数据有误
     *         5&50 开头 服务器异常&服务器异常相关提示
     */
    // 请求成功 正常返回的枚举  2开头
    SUCCESS(true, "200", "操作成功", "操作成功"),
    // 系统错误，5开头
    SYS_ERROR(false, "500", "服务器错误", "服务器开小差了~")
    ;
    ResponseEnum(boolean status, String code, String message, String userMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
    }

    /**
     * 业务状态
     */
    private boolean status;
    /**
     * 业务状态码
     */
    private String code;
    /**
     * 状态消息（内部使用）
     */
    private String message;
    /**
     * 用户消息（前端展示、提醒）
     */
    private String userMessage;

    public boolean getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
