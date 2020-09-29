package com.cimctht.servicestation.user.entity;


import com.cimctht.servicestation.common.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="SM_EMPLOYEE")
public class Employee extends BaseEntity {

    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "USED_NAME")
    private String usedName;
    @Column(name = "SEX")
    private Integer sex;
    @Column(name = "ID_CARD_NUM")
    private String idCardNum;
    @Column(name = "SOC_CARD_NUM")
    private String socCardNum;
    @Column(name = "BIRTHDAY")
    private Date birthday;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ZIPCODE")
    private String zipcode;
    @Column(name = "OFFICE_TELE")
    private String officeTele;
    @Column(name = "HOME_TELE")
    private String homeTele;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CAREER_BEGIN_DATE")
    private Date careerBeginDate;
    @Column(name = "JOIN_COMPANY_DATE")
    private Date joinCompanyDate;
    @Column(name = "TRAINING_CONTENT")
    private String trainingContent;
    @Column(name = "TRAINING_OK")
    private Integer trainingOk;
    @Column(name = "QULIFIED_OK")
    private Integer qulifiedOk;
    @Column(name = "REMARK")
    private String remark;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPART_ID")
    private Depart depart;


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

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getSocCardNum() {
        return socCardNum;
    }

    public void setSocCardNum(String socCardNum) {
        this.socCardNum = socCardNum;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getOfficeTele() {
        return officeTele;
    }

    public void setOfficeTele(String officeTele) {
        this.officeTele = officeTele;
    }

    public String getHomeTele() {
        return homeTele;
    }

    public void setHomeTele(String homeTele) {
        this.homeTele = homeTele;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCareerBeginDate() {
        return careerBeginDate;
    }

    public void setCareerBeginDate(Date careerBeginDate) {
        this.careerBeginDate = careerBeginDate;
    }

    public Date getJoinCompanyDate() {
        return joinCompanyDate;
    }

    public void setJoinCompanyDate(Date joinCompanyDate) {
        this.joinCompanyDate = joinCompanyDate;
    }

    public String getTrainingContent() {
        return trainingContent;
    }

    public void setTrainingContent(String trainingContent) {
        this.trainingContent = trainingContent;
    }

    public Integer getTrainingOk() {
        return trainingOk;
    }

    public void setTrainingOk(Integer trainingOk) {
        this.trainingOk = trainingOk;
    }

    public Integer getQulifiedOk() {
        return qulifiedOk;
    }

    public void setQulifiedOk(Integer qulifiedOk) {
        this.qulifiedOk = qulifiedOk;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }
}
