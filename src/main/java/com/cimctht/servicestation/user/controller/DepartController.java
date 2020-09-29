package com.cimctht.servicestation.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.user.Impl.DepartServiceImpl;
import com.cimctht.servicestation.user.entity.Depart;
import com.cimctht.servicestation.user.entity.Menu;
import com.cimctht.servicestation.user.repository.DepartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DepartController {

    @Autowired
    private DepartServiceImpl departServiceImpl;

    @Autowired
    private DepartRepository departRepository;


    @PostMapping(value = "/depart/ajaxLoadTree")
    public JsonResult ajaxLoadTree(HttpServletRequest request) {
        try{
            JSONArray array = departServiceImpl.ajaxLoadTree();
            return new JsonResult(array);
        }catch (Exception e){
            return new JsonResult(new UnimaxException("加载树形菜单失败!"));
        }
    }

    @GetMapping(value = "/depart/tableData")
    public TableEntity departTableData(HttpServletRequest request, String id, Integer page, Integer limit) {
        TableEntity table;
        try{
            table = departServiceImpl.queryMenusByIsDeleteAndPmenu(id,page,limit);
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/depart/addDeaprt")
    public JsonResult addDeaprt(HttpServletRequest request,String id,String code,String name,String uda1) {
        try{
            departServiceImpl.addDeaprt(id,code,name,uda1);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/depart/editDeaprt")
    public JsonResult editDeaprt(HttpServletRequest request,String id,String code,String name,String uda1) {
        try{
            departServiceImpl.editDeaprt(id,code,name,uda1);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/depart/delDepart")
    public JsonResult delDepart(HttpServletRequest request) {
        try{
            String obj = request.getParameter("obj");
            Depart d = JSON.parseObject(obj, Depart.class);
            EntityUtils.insertDelete(d);
            departRepository.save(d);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/depart/ajaxSetPdepart")
    public JsonResult ajaxSetPdepart(HttpServletRequest request,String id,String selectid) {
        try{
            Depart d = departRepository.findDepartById(id);
            Depart p = departRepository.findDepartById(selectid);
            d.setPdepart(p);
            departRepository.save(d);
            return new JsonResult("更换成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

}
