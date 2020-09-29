package com.cimctht.servicestation.user.bo;

import com.cimctht.servicestation.user.entity.Menu;

import java.util.List;

public class MenuBo {

    private String datatitle;
    private String dataid;
    private String dataurl;
    private String iconid;
    private List<MenuBo> childrenMenuBo;

    public MenuBo() {
    }

    public MenuBo(Menu menu) {
        this.datatitle = menu.getName();
        this.dataid = menu.getUda2();
        this.dataurl = menu.getHref();
        this.iconid = menu.getUda1();
    }

    public String getDatatitle() {
        return datatitle;
    }

    public void setDatatitle(String datatitle) {
        this.datatitle = datatitle;
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    public List<MenuBo> getChildrenMenuBo() {
        return childrenMenuBo;
    }

    public void setChildrenMenuBo(List<MenuBo> childrenMenuBo) {
        this.childrenMenuBo = childrenMenuBo;
    }

    public String getIconid() {
        return iconid;
    }

    public void setIconid(String iconid) {
        this.iconid = iconid;
    }
}
