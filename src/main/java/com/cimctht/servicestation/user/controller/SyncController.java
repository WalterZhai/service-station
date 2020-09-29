package com.cimctht.servicestation.user.controller;

import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.user.Impl.SyncServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SyncController {

    @Autowired
    private SyncServiceImpl syncServiceImpl;

    @PostMapping(value = "/sync/syncEmployee")
    public JsonResult syncEmployee(HttpServletRequest request) {
        try{
            syncServiceImpl.syncEmployee();
            return new JsonResult("人员同步成功！");
        }catch(Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/sync/syncDepart")
    public JsonResult syncDepart(HttpServletRequest request) {
        try{
            syncServiceImpl.syncDepart();
            return new JsonResult("部门同步成功！");
        }catch(Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }


}
