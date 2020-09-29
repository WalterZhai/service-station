package com.cimctht.servicestation.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8685228744146236922L;
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "gid")
    protected String id;
    @Column(name = "CREATE_DATE")
    protected Date createDate;
    @Column(name = "CREATE_ID")
    protected String createId;
    @Column(name = "MODIFY_DATE")
    protected Date modifyDate;
    @Column(name = "MODIFY_ID")
    protected String modifyId;
    @Column(name = "IS_DELETE")
    protected int isDelete;
    @Column(name = "UDA1")
    protected String uda1;
    @Column(name = "UDA2")
    protected String uda2;
    @Column(name = "UDA3")
    protected String uda3;
    @Column(name = "UDA4")
    protected String uda4;
    @Column(name = "UDA5")
    protected String uda5;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getUda1() {
        return uda1;
    }

    public void setUda1(String uda1) {
        this.uda1 = uda1;
    }

    public String getUda2() {
        return uda2;
    }

    public void setUda2(String uda2) {
        this.uda2 = uda2;
    }

    public String getUda3() {
        return uda3;
    }

    public void setUda3(String uda3) {
        this.uda3 = uda3;
    }

    public String getUda4() {
        return uda4;
    }

    public void setUda4(String uda4) {
        this.uda4 = uda4;
    }

    public String getUda5() {
        return uda5;
    }

    public void setUda5(String uda5) {
        this.uda5 = uda5;
    }
}
