package com.cimctht.servicestation.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.user.Impl.RoleServiceImpl;
import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/role/tableData")
    public TableEntity roleTableData(HttpServletRequest request, String searchName, String searchCode, String page, String limit) {
        TableEntity table;
        try{
            table = roleServiceImpl.findRolesByIsDeleteAndCodeLikeAndNameLike(searchName,searchCode,Integer.parseInt(page),Integer.parseInt(limit));
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/role/delRoles")
    public JsonResult delRoles(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
        List<Role> list = JSON.parseArray(arrs,Role.class);
        try{
            EntityUtils.insertDeleteAll(list);
            roleRepository.saveAll(list);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }

    @PostMapping(value = "/role/delRole")
    public JsonResult delRole(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Role r = JSON.parseObject(obj,Role.class);
        try{
            EntityUtils.insertDelete(r);
            roleRepository.save(r);
            return new JsonResult("删除成功");
        }catch (Exception ex){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }


    @PostMapping(value = "/role/addRole")
    public JsonResult addRole(HttpServletRequest request,String code,String name) {
        try{
            roleServiceImpl.addRole(code,name);
            return new JsonResult("生成成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }


    @PostMapping(value = "/role/editRole")
    public JsonResult editRole(HttpServletRequest request,String id,String code,String name) {
        try{
            roleServiceImpl.editRole(id,code,name);
            return new JsonResult("生成成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/role/ajaxLoadTransferRoleRelUser")
    public JsonResult ajaxLoadTransferRoleRelUser(HttpServletRequest request,String roleid) {
        try{
            Map<String,Object> map = roleServiceImpl.ajaxLoadTransferRoleRelUser(roleid);
            return new JsonResult(map);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/role/addSelectUser")
    public JsonResult addSelectUser(HttpServletRequest request,String roleid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            roleServiceImpl.addSelectUser(roleid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/role/delSelectUser")
    public JsonResult delSelectUser(HttpServletRequest request,String roleid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            roleServiceImpl.delSelectUser(roleid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

}
