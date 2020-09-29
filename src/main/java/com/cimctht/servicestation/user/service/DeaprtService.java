package com.cimctht.servicestation.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.constant.ThtConstant;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.common.utils.TimeUtils;
import com.cimctht.servicestation.user.Impl.DepartServiceImpl;
import com.cimctht.servicestation.user.Impl.EmployeeServiceImpl;
import com.cimctht.servicestation.user.entity.Depart;
import com.cimctht.servicestation.user.entity.Employee;
import com.cimctht.servicestation.user.entity.Menu;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.DepartRepository;
import com.cimctht.servicestation.user.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeaprtService implements DepartServiceImpl {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartRepository departRepository;


    @Override
    public JSONArray ajaxLoadTree() {
        JSONArray resultArr = new JSONArray();
        List<Depart> list = departRepository.queryDepartsBypdepartAndIsDelete(null,0);
        for(Depart d : list){
            JSONObject o = new JSONObject();
            o = recursionDepart(o,d);
            resultArr.add(o);
        }
        return resultArr;
    }

    //递归找儿子
    public JSONObject recursionDepart(JSONObject o,Depart d){
        o.put("title",d.getName());
        o.put("id",d.getId());
        List<Depart> list = departRepository.queryDepartsBypdepartAndIsDelete(d,0);
        if(list.size()>0){
            JSONArray arr = new JSONArray();
            for(Depart dchild : list){
                JSONObject oo = new JSONObject();
                oo.put("title",dchild.getName());
                oo.put("id",dchild.getId());
                oo = recursionDepart(oo,dchild);
                arr.add(oo);
            }
            o.put("children",arr);
        }
        return o;
    }

    @Override
    public TableEntity queryMenusByIsDeleteAndPmenu(String id, Integer page, Integer limit) {
        Depart d = departRepository.findDepartById(id);
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Depart> pages = departRepository.queryDepartsByIsDeleteAndPdepartOrderByCode(0,d,pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void addDeaprt(String id, String code, String name, String uda1) {
        Depart p = departRepository.findDepartById(id);
        Depart d = new Depart();
        d.setCode(code);
        d.setName(name);
        d.setUda1(uda1);
        d.setPdepart(p);
        EntityUtils.insertBasicInfo(d);
        departRepository.save(d);
    }

    @Override
    public void editDeaprt(String id, String code, String name, String uda1) {
        Depart d = departRepository.findDepartById(id);
        d.setCode(code);
        d.setName(name);
        d.setUda1(uda1);
        EntityUtils.insertBasicInfo(d);
        departRepository.save(d);
    }


}
