package com.cimctht.servicestation.user.entity;


import com.cimctht.servicestation.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="SM_ROLE")
public class Role extends BaseEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
