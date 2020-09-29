package com.cimctht.servicestation.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.StringUtils;
import com.cimctht.servicestation.user.Impl.MenuServiceImpl;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.Menu;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuServiceImpl menuServiceImpl;

    @PostMapping(value = "/menu/ajaxLoadTree")
    public JsonResult ajaxLoadTree(HttpServletRequest request) {
        try{
            JSONArray array = menuServiceImpl.ajaxLoadTree();
            return new JsonResult(array);
        }catch (Exception e){
            return new JsonResult(new UnimaxException("加载树形菜单失败!"));
        }
    }

    @PostMapping(value = "/menu/ajaxLoadTreeChecked")
    public JsonResult ajaxLoadTreeChecked(HttpServletRequest request,String roleid) {
        try{
            JSONArray array = menuServiceImpl.ajaxLoadTreeChecked(roleid);
            return new JsonResult(array);
        }catch (Exception e){
            return new JsonResult(new UnimaxException("加载树形菜单失败!"));
        }
    }

    @GetMapping(value = "/menu/tableData")
    public TableEntity menuTableData(HttpServletRequest request, String id, String page, String limit) {
        TableEntity table;
        try{
            table = menuServiceImpl.queryMenusByIsDeleteAndPmenu(id,Integer.parseInt(page),Integer.parseInt(limit));
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/menu/treeAddJudge")
    public JsonResult treeAddJudge(HttpServletRequest request,String id) {
        try{
            menuServiceImpl.treeAddJudge(id);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/addMenu")
    public JsonResult addMenu(HttpServletRequest request,String id,String name,String href,String selectId,Integer type,String icon) {
        try{
            menuServiceImpl.addMenu(id,name,href,selectId,type,icon);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/editMenu")
    public JsonResult editMenu(HttpServletRequest request,String id,String name,String href,String selectId,Integer type,String icon) {
        try{
            menuServiceImpl.editMenu(id,name,href,selectId,type,icon);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/delMenu")
    public JsonResult delMenu(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Menu u = JSON.parseObject(obj,Menu.class);
        try{
            EntityUtils.insertDelete(u);
            menuRepository.save(u);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }


    @PostMapping(value = "/menu/saveMenuSelect")
    public JsonResult saveMenuSelect(HttpServletRequest request,String id) {
        String treeData = request.getParameter("treeData");
        JSONArray trees = JSON.parseArray(treeData);
        try{
            menuServiceImpl.saveMenuSelect(trees,id);
            return new JsonResult("保存成功!");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/ajaxUserLoadTreeChecked")
    public JsonResult ajaxUserLoadTreeChecked(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        try{
            JSONArray array = menuServiceImpl.ajaxUserLoadTreeChecked(u.getId());
            return new JsonResult(array);
        }catch (Exception e){
            return new JsonResult(new UnimaxException("加载树形菜单失败!"));
        }
    }

    @PostMapping(value = "/menu/saveMenuCollect")
    public JsonResult saveMenuCollect(HttpServletRequest request) {
        String treeData = request.getParameter("treeData");
        JSONArray trees = JSON.parseArray(treeData);
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        try{
            menuServiceImpl.saveMenuCollect(trees,u.getId());
            return new JsonResult("保存成功!");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/ajaxSelectCollect")
    public JsonResult ajaxSelectCollect(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        try{
            List<MenuBo> menus = menuServiceImpl.ajaxSelectCollect(u.getId());
            return new JsonResult(menus);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/loadSearchInfo")
    public JsonResult loadSearchInfo(HttpServletRequest request,String info) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        try{
            List<String> list = menuServiceImpl.loadSearchInfo(info,u.getId());
            return new JsonResult(list);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/searchAndOpenMenu")
    public JsonResult searchAndOpenMenu(HttpServletRequest request,String info) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        try{
            List<MenuBo> list = menuServiceImpl.searchAndOpenMenu(info,u.getId());
            return new JsonResult(list);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/menu/rowUp")
    public JsonResult rowUp(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Menu u = JSON.parseObject(obj,Menu.class);
        try{
            menuServiceImpl.rowUp(u);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException("移动失败"));
        }
    }

    @PostMapping(value = "/menu/rowDown")
    public JsonResult rowDown(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Menu u = JSON.parseObject(obj,Menu.class);
        try{
            menuServiceImpl.rowDown(u);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException("移动失败"));
        }
    }

}
