package com.cimctht.servicestation.user.entity;


import com.cimctht.servicestation.common.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="SM_MENU")
public class Menu extends BaseEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "HREF")
    private String href;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PID")
    private Menu pmenu;
    @Column(name = "LEVE")
    private Integer leve;
    @Column(name = "SEQ")
    private Integer seq;
    @Column(name = "TYPE")
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Menu getPmenu() {
        return pmenu;
    }

    public void setPmenu(Menu pmenu) {
        this.pmenu = pmenu;
    }

    public Integer getLeve() {
        return leve;
    }

    public void setLeve(Integer leve) {
        this.leve = leve;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
