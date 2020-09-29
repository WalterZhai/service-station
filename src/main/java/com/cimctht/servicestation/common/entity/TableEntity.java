package com.cimctht.servicestation.common.entity;

import java.math.BigDecimal;
import java.util.List;

public class TableEntity {
    private String code;
    private String msg;
    private BigDecimal count;
    private Object data;

    public TableEntity() {
    }

    public TableEntity(Object datas,BigDecimal count) {
        this();
        this.code = "0";
        this.msg = "";
        this.count = count;
        this.data = datas;
    }

    public TableEntity(Throwable t) {
        this();
        this.code = "1";
        this.msg = t.getMessage();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
