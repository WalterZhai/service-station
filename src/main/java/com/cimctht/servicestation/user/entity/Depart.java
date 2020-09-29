package com.cimctht.servicestation.user.entity;


import com.cimctht.servicestation.common.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="SM_DEPART")
public class Depart extends BaseEntity{

    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME")
    private String shortNmae;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PID")
    private Depart pdepart;
    @Column(name = "DEPT_PROPERTY")
    private Integer deptProperty;
    @Column(name = "DEPT_TYPE")
    private Integer deptType;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPERVISOR_GID")
    private Employee supervisor;
    @Column(name = "COMMU_ADDRESS")
    private String commuAddress;
    @Column(name = "TELEPHONE")
    private String telephone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "REMARK")
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortNmae() {
        return shortNmae;
    }

    public void setShortNmae(String shortNmae) {
        this.shortNmae = shortNmae;
    }

    public Depart getPdepart() {
        return pdepart;
    }

    public void setPdepart(Depart pdepart) {
        this.pdepart = pdepart;
    }

    public Integer getDeptProperty() {
        return deptProperty;
    }

    public void setDeptProperty(Integer deptProperty) {
        this.deptProperty = deptProperty;
    }

    public Integer getDeptType() {
        return deptType;
    }

    public void setDeptType(Integer deptType) {
        this.deptType = deptType;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public String getCommuAddress() {
        return commuAddress;
    }

    public void setCommuAddress(String commuAddress) {
        this.commuAddress = commuAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
