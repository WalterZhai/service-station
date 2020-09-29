package com.cimctht.servicestation.common.entity;

public class JsonResult {
    private static final int SUCCESS = 1;
    private static final int ERROR = 0;
    /**
     * 状态
     */
    private int state;
    /**
     * 对应状态的消息
     */
    private String message;
    /**
     * 具体业务数据
     */
    private Object data;

    /**
     * 此构造方法应用于data为null的场景
     */
    public JsonResult() {
        this.state = SUCCESS;//1
        this.message = "OK";
    }

    /**
     * 有具体业务数据返回时,使用此构造方法
     */
    public JsonResult(Object data) {
        this();
        this.data = data;
    }

    /**
     * 出现异常以后要调用此方法封装异常信息
     */
    public JsonResult(Throwable t) {
        this.state = ERROR;//0
        this.message = t.getMessage();
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public void setState(int state) {
        this.state = state;
    }
}
