package com.cimctht.servicestation.message.entity;

import com.cimctht.servicestation.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="MSG_MAIN")
public class Message extends BaseEntity {

    @Column(name = "MES_TITLE")
    private String mesTitle;
    @Column(name = "MES_CONTENT")
    private String mesContent;
    @Column(name = "IS_TYPE")
    private Integer isType;
    @Column(name = "TYPE_EXPLAIN")
    private String typeExplain;
    @Column(name = "IS_SEND")
    private Integer isSend;
    @Column(name = "TYPE_ID")
    private String typeId;

    public String getMesTitle() {
        return mesTitle;
    }

    public void setMesTitle(String mesTitle) {
        this.mesTitle = mesTitle;
    }

    public String getMesContent() {
        return mesContent;
    }

    public void setMesContent(String mesContent) {
        this.mesContent = mesContent;
    }

    public Integer getIsType() {
        return isType;
    }

    public void setIsType(Integer isType) {
        this.isType = isType;
    }

    public String getTypeExplain() {
        return typeExplain;
    }

    public void setTypeExplain(String typeExplain) {
        this.typeExplain = typeExplain;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
