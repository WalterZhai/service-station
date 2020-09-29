package com.cimctht.servicestation.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.user.Impl.GroupServiceImpl;
import com.cimctht.servicestation.user.entity.Group;
import com.cimctht.servicestation.user.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class GroupController {

    @Autowired
    private GroupServiceImpl groupServiceImpl;

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping(value = "/group/tableData")
    public TableEntity roleTableData(HttpServletRequest request, String searchName, String searchCode, Integer page, Integer limit) {
        TableEntity table;
        try{
            table = groupServiceImpl.findGroups(searchName,searchCode,page,limit);
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @GetMapping(value = "/group/tableDataDeatil")
    public TableEntity tableDataDeatil(HttpServletRequest request, String selectid, Integer page, Integer limit) {
        TableEntity table;
        try{
            table = groupServiceImpl.findGroupsDeatils(selectid,page,limit);
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/group/addGroup")
    public JsonResult addGroup(HttpServletRequest request, String code, String name, String explain) {
        try{
            groupServiceImpl.addGroup(code,name,explain);
            return new JsonResult("生成成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/group/delGroups")
    public JsonResult delGroups(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
        List<Group> list = JSON.parseArray(arrs,Group.class);
        try{
            EntityUtils.insertDeleteAll(list);
            groupRepository.saveAll(list);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }


    @PostMapping(value = "/group/editGroup")
    public JsonResult editGroup(HttpServletRequest request,String id,String code,String name,String explain) {
        try{
            groupServiceImpl.editGroup(id,code,name,explain);
            return new JsonResult("编辑成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }


    @PostMapping(value = "/group/delGroup")
    public JsonResult delGroup(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Group g = JSON.parseObject(obj,Group.class);
        try{
            EntityUtils.insertDelete(g);
            groupRepository.save(g);
            return new JsonResult("删除成功");
        }catch (Exception ex){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }

    @PostMapping(value = "/group/ajaxLoadTransferGroupRelUser")
    public JsonResult ajaxLoadTransferGroupRelUser(HttpServletRequest request,String groupid) {
        try{
            Map<String,Object> map = groupServiceImpl.ajaxLoadTransferGroupRelUser(groupid);
            return new JsonResult(map);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/group/addSelectUser")
    public JsonResult addSelectUser(HttpServletRequest request,String groupid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            groupServiceImpl.addSelectUser(groupid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/group/delSelectUser")
    public JsonResult delSelectUser(HttpServletRequest request,String groupid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            groupServiceImpl.delSelectUser(groupid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

}
