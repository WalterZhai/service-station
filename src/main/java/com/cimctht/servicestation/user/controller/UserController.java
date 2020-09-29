package com.cimctht.servicestation.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.user.Impl.UserServiceImpl;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView= new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession();
        Principal principal = request.getUserPrincipal();
        User user = userService.queryUserByLoginNameAndIsDelete(principal.getName(),0);
        List<String> listRoleCodes = new ArrayList<String>();
        for(GrantedAuthority auth : authentication.getAuthorities()) {
            listRoleCodes.add(auth.getAuthority());
        }
        session.setAttribute("user",user);
        session.setAttribute("roles",listRoleCodes);
        List<MenuBo> menus = userService.queryMenusForLogin(user.getLoginName());
        ModelMap Model = new ModelMap().addAttribute("menus", menus);
        ModelAndView modelAndView= new ModelAndView("index",Model);
        logger.info(user.getName()+" 登录成功！");
        return modelAndView;
    }

    @GetMapping(value="/loginout")
    public ModelAndView loginout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        logger.info(user.getName()+" 登出成功！");
        session.invalidate();
        ModelAndView modelAndView= new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping(value = "/user/tableData")
    public TableEntity userTableData(HttpServletRequest request,String searchName,String page,String limit) {
        TableEntity table;
        try{
            table = userService.queryUsersByLikeName(searchName,Integer.parseInt(page),Integer.parseInt(limit));
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/user/lockUser")
    public JsonResult lockUser(HttpServletRequest request,String id,String isLocked) {
        try{
            userService.updateIsLockedById(id,Integer.parseInt(isLocked));
            return new JsonResult("修改成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("修改失败"));
        }
    }

    @PostMapping(value = "/user/addUser")
    public JsonResult addUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String loginName = request.getParameter("loginName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        try{
            userService.saveOneUser(name,loginName,email,mobile);
            return new JsonResult("添加成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("添加失败"));
        }
    }

    @PostMapping(value = "/user/delUsers")
    public JsonResult delUsers(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
         List<User> list = JSON.parseArray(arrs,User.class);
        try{
            EntityUtils.insertDeleteAll(list);
            userRepository.saveAll(list);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }

    @PostMapping(value = "/user/editUser")
    public JsonResult editUser(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String loginName = request.getParameter("loginName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        try{
            userService.editOneUser(id,name,loginName,email,mobile);
            return new JsonResult("修改成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("修改失败"));
        }
    }

    @PostMapping(value = "/user/delUser")
    public JsonResult delUser(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        User u = JSON.parseObject(obj,User.class);
        try{
            EntityUtils.insertDelete(u);
            userRepository.save(u);
            return new JsonResult("删除成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("删除失败"));
        }
    }

    @PostMapping(value = "/user/editBaseInfo")
    public JsonResult editBaseInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        try{
            User u = userService.editBaseInfo(id,name,email,mobile);
            request.getSession().setAttribute("user",u);
            return new JsonResult("修改成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException("修改失败"));
        }
    }


    @PostMapping(value = "/user/editPassword")
    public JsonResult editPassword(HttpServletRequest request) {
        String pwd1 = request.getParameter("pwd1");
        String pwd2 = request.getParameter("pwd2");
        String pwd3 = request.getParameter("pwd3");
        try{
            userService.editPassword(pwd1,pwd2,pwd3);
            return new JsonResult("修改成功");
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/user/ajaxLoadTransferUserRelRole")
    public JsonResult ajaxLoadTransferUserRelRole(HttpServletRequest request,String userid) {
        try{
            Map<String,Object> map = userService.ajaxLoadTransferUserRelRole(userid);
            return new JsonResult(map);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/user/addSelectRole")
    public JsonResult addSelectRole(HttpServletRequest request,String userid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            userService.addSelectRole(userid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/user/delSelectRole")
    public JsonResult delSelectRole(HttpServletRequest request,String userid,String data) {
        JSONArray arr = JSON.parseArray(data);
        try{
            userService.delSelectRole(userid,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @GetMapping(value = "/user/onlineUserData")
    public TableEntity onlineUserData(HttpServletRequest request,String page,String limit) {
        TableEntity table;
        try{
            table = userService.onlineUserData(Integer.parseInt(page),Integer.parseInt(limit));
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }


}
