package com.cimctht.servicestation.user.Impl;

import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.user.entity.Employee;

import java.util.List;
import java.util.Map;

public interface RoleServiceImpl {

    TableEntity findRolesByIsDeleteAndCodeLikeAndNameLike(String name, String code, Integer page, Integer limit);

    void addRole(String code,String name);

    void editRole(String id,String code,String name);

    Map<String,Object> ajaxLoadTransferRoleRelUser(String roleid);

    void addSelectUser(String roleid, JSONArray arr);

    void delSelectUser(String roleid,JSONArray arr);
}
