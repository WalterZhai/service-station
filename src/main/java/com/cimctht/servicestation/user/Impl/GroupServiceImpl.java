package com.cimctht.servicestation.user.Impl;

import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.TableEntity;

import java.util.Map;

public interface GroupServiceImpl {

    TableEntity findGroups(String searchName,String searchCode,Integer page,Integer limit);

    TableEntity findGroupsDeatils(String selectid,Integer page,Integer limit);

    void addGroup(String code, String name, String explain);

    void editGroup(String id,String code, String name, String explain);

    Map<String,Object> ajaxLoadTransferGroupRelUser(String groupid);

    void addSelectUser(String groupid, JSONArray arr);

    void delSelectUser(String groupid, JSONArray arr);
}
