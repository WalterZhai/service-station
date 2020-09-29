package com.cimctht.servicestation.user.Impl;

import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserServiceImpl {

    User queryUserByLoginNameAndIsDelete(String loginName,Integer isDelete);

    List<MenuBo> queryMenusForLogin(String loginName);

    List<User> queryUsers();

    TableEntity queryUsersByLikeName(String name, Integer page, Integer limit);

    void updateIsLockedById(String id,Integer isLocked);

    void saveOneUser(String name,String loginName,String email,String moblie);

    void editOneUser(String id,String name,String loginName,String email,String moblie);

    User editBaseInfo(String id,String name,String email,String moblie);

    void editPassword(String pwd1,String pwd2,String pwd3);

    Map<String,Object> ajaxLoadTransferUserRelRole(String userid);

    void addSelectRole(String userid,JSONArray arr);

    void delSelectRole(String userid,JSONArray arr);

    TableEntity onlineUserData(Integer page, Integer limit);
}
