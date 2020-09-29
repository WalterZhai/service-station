package com.cimctht.servicestation.user.Impl;

import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.user.entity.Employee;

import java.util.List;

public interface DepartServiceImpl {

    JSONArray ajaxLoadTree();

    TableEntity queryMenusByIsDeleteAndPmenu(String id,Integer page,Integer limit);

    void addDeaprt(String id,String code,String name,String uda1);

    void editDeaprt(String id,String code,String name,String uda1);

}
