package com.cimctht.servicestation.user.Impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.Menu;

import java.util.List;

public interface MenuServiceImpl {

    JSONArray ajaxLoadTree();

    JSONArray ajaxLoadTreeChecked(String roleid);

    TableEntity queryMenusByIsDeleteAndPmenu(String id, Integer page, Integer limit);

    void treeAddJudge(String id);

    void addMenu(String id,String name,String href,String selectId,Integer type,String icon);

    void editMenu(String id,String name,String href,String selectId,Integer type,String icon);

    void saveMenuSelect(JSONArray data,String id);

    JSONArray ajaxUserLoadTreeChecked(String userid);

    void saveMenuCollect(JSONArray data,String id);

    List<MenuBo> ajaxSelectCollect(String id);

    List<String> loadSearchInfo(String info,String id);

    List<MenuBo> searchAndOpenMenu(String info,String id);

    void rowUp(Menu m);

    void rowDown(Menu m);
}
