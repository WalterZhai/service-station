package com.cimctht.servicestation.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.user.Impl.GroupServiceImpl;
import com.cimctht.servicestation.user.entity.Group;
import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.GroupRepository;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupService implements GroupServiceImpl {


    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public TableEntity findGroups(String searchName, String searchCode, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Group> pages = groupRepository.queryGroupsByIsDeleteAndCodeLikeAndNameLikeOrderByCreateDate(0,"%"+searchCode+"%","%"+searchName+"%",pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public TableEntity findGroupsDeatils(String selectid, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<User> pages = userRepository.queryUsersByIsDeleteAndGroupId(selectid,pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void addGroup(String code, String name, String explain) {
        Group g = new Group();
        g.setCode(code);
        g.setName(name);
        g.setExplain(explain);
        EntityUtils.insertBasicInfo(g);
        groupRepository.save(g);
    }

    @Override
    public void editGroup(String id, String code, String name, String explain) {
        Group g = groupRepository.findGroupById(id);
        g.setCode(code);
        g.setName(name);
        g.setExplain(explain);
        EntityUtils.insertBasicInfo(g);
        groupRepository.save(g);
    }

    @Override
    public Map<String, Object> ajaxLoadTransferGroupRelUser(String groupid) {
        Map<String, Object> result = new HashMap<String, Object>();

        //已属于用户组的用户
        List<User> userExist = userRepository.queryUserGroupId(groupid);
        List<User> userAll = userRepository.queryUsersByIsDelete(0);
        //data值
        JSONArray arr = new JSONArray();
        for(User r : userAll){
            JSONObject o = new JSONObject();
            o.put("value",r.getId());
            o.put("title",r.getName());
            o.put("disabled","");
            o.put("checked","");
            arr.add(o);
        }
        result.put("data",arr);
        //value值
        List<String> listids = new ArrayList<String>();
        for(User r : userExist){
            listids.add(r.getId());
        }
        result.put("value",listids);

        return result;
    }

    @Override
    public void addSelectUser(String groupid, JSONArray arr) {
        for(int i=0;i<arr.size();i++){
            groupRepository.addSelectRole(groupid,arr.getJSONObject(i).get("value").toString());
        }
    }

    @Override
    public void delSelectUser(String groupid, JSONArray arr) {
        for(int i=0;i<arr.size();i++){
            groupRepository.delSelectRole(groupid,arr.getJSONObject(i).get("value").toString());
        }
    }


}
