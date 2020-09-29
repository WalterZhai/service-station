package com.cimctht.servicestation.message.Impl;

import com.alibaba.fastjson.JSONArray;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.message.entity.Message;
import com.cimctht.servicestation.user.entity.User;

import java.util.List;
import java.util.Map;

public interface MessageServiceImpl {

    TableEntity findMessages(String searchTitle, Integer sendType,Integer isSend, Integer page, Integer limit);

    Map<String,Object> laodTrans();

    void addMessageInfo(String title, String content, Integer type, JSONArray arr);

    void editMessage(String id,String title,String content);

    void sendMessage(List<Message> list);

    void cancelMessage(List<Message> list);

    TableEntity tableDataMessageInfo(User user,Integer page,Integer limit);

    void messageReadAll(User user);

}
