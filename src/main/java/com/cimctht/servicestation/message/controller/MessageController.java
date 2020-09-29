package com.cimctht.servicestation.message.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.JsonResult;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.message.Impl.MessageServiceImpl;
import com.cimctht.servicestation.message.entity.Message;
import com.cimctht.servicestation.message.entity.MessageInfo;
import com.cimctht.servicestation.message.repository.MessageInfoRepository;
import com.cimctht.servicestation.message.repository.MessageRepository;
import com.cimctht.servicestation.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private MessageServiceImpl messageServiceImpl;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageInfoRepository messageInfoRepository;

    @GetMapping(value = "/message/tableData")
    public TableEntity messageTableData(HttpServletRequest request, String searchTitle, Integer sendType, Integer isSend, Integer page, Integer limit) {
        TableEntity table;
        try{
            table = messageServiceImpl.findMessages(searchTitle,sendType, isSend,page,limit);
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/message/laodTrans")
    public JsonResult laodTrans(HttpServletRequest request) {
        try{
            Map<String,Object> map = messageServiceImpl.laodTrans();
            return new JsonResult(map);
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }


    @PostMapping(value = "/message/addMessageInfo")
    public JsonResult addMessageInfo(HttpServletRequest request,Integer type) {
        String title = request.getParameter("frm[title]");
        String content = request.getParameter("frm[content]");
        String data = request.getParameter("data");
        JSONArray arr = JSON.parseArray(data);
        try{
            messageServiceImpl.addMessageInfo(title,content,type,arr);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/delMessages")
    public JsonResult delMessages(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
        List<Message> list = JSON.parseArray(arrs, Message.class);
        try{
            EntityUtils.insertDeleteAll(list);
            messageRepository.saveAll(list);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/delMessage")
    public JsonResult delMessage(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        Message message = JSON.parseObject(obj,Message.class);
        try{
            EntityUtils.insertDelete(message);
            messageRepository.save(message);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/editMessage")
    public JsonResult editMessage(HttpServletRequest request,String id,String title,String content) {
        try{
            messageServiceImpl.editMessage(id,title,content);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/sendMessage")
    public JsonResult sendMessage(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
        List<Message> list = JSON.parseArray(arrs, Message.class);
        try{
            messageServiceImpl.sendMessage(list);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/cancelMessage")
    public JsonResult cancelMessage(HttpServletRequest request) {
        String arrs = request.getParameter("arrs");
        List<Message> list = JSON.parseArray(arrs, Message.class);
        try{
            messageServiceImpl.cancelMessage(list);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/inspectMessage")
    public JsonResult inspectMessage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        try{
            return new JsonResult(messageInfoRepository.queryMessageInfosCountByUserId(user.getId()));
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }


    @GetMapping(value = "/message/tableDataMessageInfo")
    public TableEntity tableDataMessageInfo(HttpServletRequest request,Integer page,Integer limit) {
        User user = (User) request.getSession().getAttribute("user");
        TableEntity table;
        try{
            table = messageServiceImpl.tableDataMessageInfo(user,page,limit);
        }catch (Exception e){
            table = new TableEntity(e);
        }
        return table;
    }

    @PostMapping(value = "/message/messageRead")
    public JsonResult messageRead(HttpServletRequest request) {
        String obj = request.getParameter("obj");
        MessageInfo messageInfo = JSON.parseObject(obj, MessageInfo.class);
        try{
            if(messageInfo.getIsRead()==0){
                messageInfo.setIsRead(1);
                EntityUtils.insertBasicInfo(messageInfo);
                messageInfoRepository.save(messageInfo);
            }
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

    @PostMapping(value = "/message/messageReadAll")
    public JsonResult messageReadAll(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        try{
            messageServiceImpl.messageReadAll(user);
            return new JsonResult();
        }catch (Exception e){
            return new JsonResult(new UnimaxException(e.getMessage()));
        }
    }

}
