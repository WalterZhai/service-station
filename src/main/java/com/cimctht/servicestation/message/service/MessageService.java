package com.cimctht.servicestation.message.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.common.utils.StringUtils;
import com.cimctht.servicestation.message.Impl.MessageServiceImpl;
import com.cimctht.servicestation.message.entity.Message;
import com.cimctht.servicestation.message.entity.MessageInfo;
import com.cimctht.servicestation.message.repository.MessageInfoRepository;
import com.cimctht.servicestation.message.repository.MessageRepository;
import com.cimctht.servicestation.user.entity.Group;
import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.GroupRepository;
import com.cimctht.servicestation.user.repository.RoleRepository;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService implements MessageServiceImpl {

    @Autowired
    private MessageInfoRepository messageInfoRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PersistenceContext(unitName = "unimaxPersistenceUnit")
    private EntityManager unimaxEntityManager;

    @Override
    public TableEntity findMessages(String searchTitle, Integer sendType,Integer isSend, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        StringBuffer sql = new StringBuffer();
        sql.append(" select t.* from MSG_MAIN t where t.is_delete=0");
        sql.append(" and t.mes_title like '%"+searchTitle+"%' ");
        if(sendType!=null){
            sql.append(" and t.is_type="+sendType);
        }
        if(isSend!=null){
            sql.append(" and t.is_send="+isSend);
        }
        sql.append(" order by t.create_date desc ");
        Query query = unimaxEntityManager.createNativeQuery(sql.toString(),Message.class);
        int all = query.getResultList().size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Message> meses = query.getResultList();
        return new TableEntity(meses, MathsUtils.convertInteger2BigDecimal(all));
    }

    @Override
    public Map<String, Object> laodTrans() {
        Map<String, Object> result = new HashMap<String, Object>();

        //加载所有角色
        List<Role> listRoles = roleRepository.queryRolesByIsDelete(0);
        //加载所有用户
        List<User> listUsers = userRepository.queryUsersByIsDelete(0);
        //加载所有用户组
        List<Group> listGroups = groupRepository.queryGroupsByIsDelete(0);

        //角色data值
        JSONArray arr1 = new JSONArray();
        for(Role r : listRoles){
            JSONObject o = new JSONObject();
            o.put("value",r.getId());
            o.put("title",r.getName());
            o.put("disabled","");
            o.put("checked","");
            arr1.add(o);
        }
        result.put("roles",arr1);

        //角色data值
        JSONArray arr2 = new JSONArray();
        for(User r : listUsers){
            JSONObject o = new JSONObject();
            o.put("value",r.getId());
            o.put("title",r.getName());
            o.put("disabled","");
            o.put("checked","");
            arr2.add(o);
        }
        result.put("users",arr2);

        //角色data值
        JSONArray arr3 = new JSONArray();
        for(Group r : listGroups){
            JSONObject o = new JSONObject();
            o.put("value",r.getId());
            o.put("title",r.getName());
            o.put("disabled","");
            o.put("checked","");
            arr3.add(o);
        }
        result.put("groups",arr3);

        return result;
    }

    @Override
    public void addMessageInfo(String title, String content, Integer type, JSONArray arr) {
        List<Message> list = new ArrayList<Message>();
        if(type==1){
            Message mes = new Message();
            mes.setMesTitle(title);
            mes.setMesContent(content);
            mes.setIsType(type);
            mes.setIsSend(0);
            mes.setTypeExplain("所有用户");
            list.add(mes);
        }else{
            if(arr.size()!=0){
                for(int i =0;i<arr.size();i++){
                    Message mes = new Message();
                    mes.setMesTitle(title);
                    mes.setMesContent(content);
                    mes.setIsType(type);
                    mes.setIsSend(0);
                    mes.setTypeExplain(arr.getJSONObject(i).get("title").toString());
                    mes.setTypeId(arr.getJSONObject(i).get("value").toString());
                    list.add(mes);
                }
            }
        }
        EntityUtils.insertBasicInfoAll(list);
        messageRepository.saveAll(list);
    }

    @Override
    public void editMessage(String id, String title, String content) {
        Message message = messageRepository.queryMessageById(id);
        message.setMesTitle(title);
        message.setMesContent(content);
        EntityUtils.insertBasicInfo(message);
        messageRepository.save(message);
    }

    @Override
    public void sendMessage(List<Message> list) {
        List<MessageInfo> result = new ArrayList<MessageInfo>();
        for(Message message : list){
            if(message.getIsSend()==1){
                continue;
            }
            List<User> users = new ArrayList<User>();
            if(message.getIsType()==1){
                users = userRepository.queryUsersByIsDelete(0);
            }else if(message.getIsType()==2){
                users = userRepository.queryUserRoleId(message.getTypeId());
            }else if(message.getIsType()==3){
                users.add(userRepository.queryUserById(message.getTypeId()));
            }else if(message.getIsType()==4){
                users = userRepository.queryUserGroupId(message.getTypeId());
            }
            for(User u : users){
                MessageInfo info = new MessageInfo();
                info.setUser(u);
                info.setIsRead(0);
                info.setMessage(message);
                result.add(info);
            }
            message.setIsSend(1);
        }
        EntityUtils.insertBasicInfoAll(result);
        messageInfoRepository.saveAll(result);
        EntityUtils.insertBasicInfoAll(list);
        messageRepository.saveAll(list);
    }

    @Override
    public void cancelMessage(List<Message> list) {
        for(Message message : list){
            if(message.getIsSend()==0){
                continue;
            }
            List<MessageInfo> dels =  messageInfoRepository.queryMessageInfosByIsDeleteAndMessage(0,message);
            EntityUtils.insertDeleteAll(dels);
            messageInfoRepository.saveAll(dels);
            message.setIsSend(0);
        }
        EntityUtils.insertBasicInfoAll(list);
        messageRepository.saveAll(list);
    }

    @Override
    public TableEntity tableDataMessageInfo(User user,Integer page,Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<MessageInfo> pages = messageInfoRepository.queryMessageInfosByIsDeleteAndUserOrderByIsReadAscCreateDateDesc(0,user,pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void messageReadAll(User user) {
        messageInfoRepository.updateIsReadByUserId(user.getId());
    }


}
