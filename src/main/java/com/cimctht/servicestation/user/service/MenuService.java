package com.cimctht.servicestation.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.common.utils.StringUtils;
import com.cimctht.servicestation.user.Impl.MenuServiceImpl;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.Employee;
import com.cimctht.servicestation.user.entity.Menu;
import com.cimctht.servicestation.user.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class MenuService implements MenuServiceImpl {

    @Autowired
    private MenuRepository menuRepository;


    @PersistenceContext(unitName = "unimaxPersistenceUnit")
    private EntityManager unimaxEntityManager;


    @Override
    public JSONArray ajaxLoadTree() {
        JSONArray resultArr = new JSONArray();
        JSONObject mtop = new JSONObject();
        mtop.put("title","<h3><i class='layui-icon layui-icon-align-left'>&nbsp;</i>菜单树</h3>");
        mtop.put("id","");
        //返回的JSONArr
        JSONArray arr = new JSONArray();
        List<Menu> listTop = menuRepository.queryMenusByLeveAndIsDeleteOrderBySeq(1,0);
        for(Menu top : listTop){
            JSONObject jsono = new JSONObject();
            jsono.put("title",top.getName());
            jsono.put("id",top.getId());
            List<Menu> listChildren = menuRepository.queryMenusByLeveAndIsDeleteAndPmenuOrderBySeq(2,0,top);

            if(listChildren.size()>0){
                JSONArray arrChild = new JSONArray();
                for(Menu child : listChildren){
                    JSONObject jsonoChild = new JSONObject();
                    jsonoChild.put("title",child.getName());
                    jsonoChild.put("id",child.getId());
                    arrChild.add(jsonoChild);
                }
                jsono.put("children",arrChild);
            }
            arr.add(jsono);
        }

        mtop.put("children",arr);
        resultArr.add(mtop);
        return resultArr;
    }

    @Override
    public TableEntity queryMenusByIsDeleteAndPmenu(String id, Integer page, Integer limit) {
        Menu u = menuRepository.queryMenuById(id);
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Menu> pages = menuRepository.queryMenusByIsDeleteAndPmenuOrderBySeq(0,u,pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void treeAddJudge(String id) {
        Menu u = menuRepository.queryMenuById(id);
        if(u!=null && u.getLeve()==2 ){
            throw new UnimaxException("只能在顶层和一级菜单下添加菜单！");
        }
    }

    @Override
    public void addMenu(String id, String name, String href, String selectId,Integer type,String icon) {
        Menu u = new Menu();
        u.setName(name);
        u.setHref(href);
        u.setUda2(selectId);
        Integer judge = menuRepository.queryCtByUda2(selectId);
        if(judge>0){
            throw  new UnimaxException("SelectID已存在！");
        }
        u.setType(type);
        if(!StringUtils.isEmpty(id)){
            Menu p = menuRepository.queryMenuById(id);
            u.setPmenu(p);
            u.setLeve(p.getLeve()+1);
            Integer max = menuRepository.queryMaxSeqByPidAndLeve(2,p.getId());
            u.setSeq(max+1);
        }else{
            u.setLeve(1);
            Integer max = menuRepository.queryMaxSeqByLeve(1);
            u.setSeq(max+1);
        }
        u.setUda1(icon);
        EntityUtils.insertBasicInfo(u);
        menuRepository.save(u);
    }

    @Override
    public void editMenu(String id, String name, String href, String selectId, Integer type,String icon) {
        Menu u = menuRepository.queryMenuById(id);
        u.setName(name);
        u.setHref(href);
        u.setUda2(selectId);
        if(!selectId.equals(u.getUda2())){
            Integer judge = menuRepository.queryCtByUda2(selectId);
            if(judge>0){
                throw  new UnimaxException("SelectID已存在！");
            }
        }

        u.setType(type);
        u.setUda1(icon);
        EntityUtils.insertBasicInfo(u);
        menuRepository.save(u);
    }

    //保存菜单选择结果
    @Modifying
    @Override
    public void saveMenuSelect(JSONArray data,String id) {
        //现有的菜单
        List<String> listExistMenuids = menuRepository.queryMenuidByRoleid(id);
        //需要添加的菜单
        List<String> listAddMenuids = new ArrayList<>();
        //需要删除的菜单
        List<String> listDelMenuids = new ArrayList<>();
        //传进来的菜单
        List<String> listInMenuids = new ArrayList<>();

        //先保存传进来的菜单
        listInMenuids = getIdIntoListMenuids(data,listInMenuids);

        //比较是否有新增的
        for(String s1 : listInMenuids){
            if(!listExistMenuids.contains(s1)){
                listAddMenuids.add(s1);
            }
        }

        //比较是否有删除的
        for(String s1 : listExistMenuids){
            if(!listInMenuids.contains(s1)){
                listDelMenuids.add(s1);
            }
        }

        //添加新增的
        for(String s1 : listAddMenuids){
            unimaxEntityManager.createNativeQuery(" insert into SM_ROLE_REL_MENU (role_id,menu_id) values (?,?) ").setParameter(1,id).setParameter(2,s1).executeUpdate();
        }

        //删除要删除的
        for(String s1 : listDelMenuids){
            unimaxEntityManager.createNativeQuery(" delete SM_ROLE_REL_MENU where menu_id = ? and role_id = ? ").setParameter(1,s1).setParameter(2,id).executeUpdate();
        }

    }

    public List<String> getIdIntoListMenuids(JSONArray data,List<String> listInMenuids){
        for(int i=0;i<data.size();i++) {
            //有id加入list
            if(!StringUtils.isEmpty(data.getJSONObject(i).get("id").toString())){
                listInMenuids.add(data.getJSONObject(i).get("id").toString());
            }
            //有子菜单递归
            JSONArray children = (JSONArray) data.getJSONObject(i).get("children");
            if(children!=null && children.size()>0){
                getIdIntoListMenuids(children,listInMenuids);
            }
        }
        return listInMenuids;
    }


    @Override
    public JSONArray ajaxLoadTreeChecked(String roleid) {
        //获得角色关联的菜单的id
        List<String> listMenuids = menuRepository.queryMenuidByRoleid(roleid);
        JSONArray resultArr = new JSONArray();
        JSONObject mtop = new JSONObject();
        mtop.put("title","<h3><i class='layui-icon layui-icon-align-left'>&nbsp;</i>菜单树</h3>");
        mtop.put("id","");
        mtop.put("spread",true);
        //返回的JSONArr
        JSONArray arr = new JSONArray();
        List<Menu> listTop = menuRepository.queryMenusByLeveAndIsDeleteOrderBySeq(1,0);
        for(Menu top : listTop){
            JSONObject jsono = new JSONObject();
            jsono.put("title",top.getName());
            jsono.put("id",top.getId());
//            if(listMenuids.contains(top.getId())){
//                jsono.put("checked",true);
//            }
            jsono.put("spread",true);
            List<Menu> listChildren = menuRepository.queryMenusByLeveAndIsDeleteAndPmenuOrderBySeq(2,0,top);

            if(listChildren.size()>0){
                JSONArray arrChild = new JSONArray();
                for(Menu child : listChildren){
                    JSONObject jsonoChild = new JSONObject();
                    jsonoChild.put("title",child.getName());
                    jsonoChild.put("id",child.getId());
                    if(listMenuids.contains(child.getId())){
                        jsonoChild.put("checked",true);
                    }
                    jsonoChild.put("spread",true);
                    arrChild.add(jsonoChild);
                }
                jsono.put("children",arrChild);
            }
            arr.add(jsono);
        }

        mtop.put("children",arr);
        resultArr.add(mtop);
        return resultArr;
    }



    @Override
    public JSONArray ajaxUserLoadTreeChecked(String userid) {
        //获得用户能访问的菜单
        List<String> listMenuids = menuRepository.queryMenuidByUserid(userid);
        //获得用户已收藏的菜单
        List<String> listCollectids = menuRepository.queryMenuidByUserCollect(userid);

        JSONArray resultArr = new JSONArray();
        JSONObject mtop = new JSONObject();
        mtop.put("title","<h3><i class='layui-icon layui-icon-align-left'>&nbsp;</i>菜单树</h3>");
        mtop.put("id","");
        mtop.put("spread",true);
        //返回的JSONArr
        JSONArray arr = new JSONArray();
        List<Menu> listTop = menuRepository.queryMenusByLeveAndIsDeleteOrderBySeq(1,0);
        for(Menu top : listTop){
            if(listMenuids.contains(top.getId())){
                JSONObject jsono = new JSONObject();
                jsono.put("title",top.getName());
                jsono.put("id",top.getId());
                jsono.put("spread",true);
                List<Menu> listChildren = menuRepository.queryMenusByLeveAndIsDeleteAndPmenuOrderBySeq(2,0,top);

                if(listChildren.size()>0){
                    JSONArray arrChild = new JSONArray();
                    for(Menu child : listChildren){
                        if(listMenuids.contains(child.getId())){
                            JSONObject jsonoChild = new JSONObject();
                            jsonoChild.put("title",child.getName());
                            jsonoChild.put("id",child.getId());
                            if(listCollectids.contains(child.getId())){
                                jsonoChild.put("checked",true);
                            }
                            jsonoChild.put("spread",true);
                            arrChild.add(jsonoChild);
                        }
                    }
                    jsono.put("children",arrChild);
                }
                arr.add(jsono);
            }
        }

        mtop.put("children",arr);
        resultArr.add(mtop);
        return resultArr;
    }


    //保存收藏的菜单
    @Modifying
    @Override
    public void saveMenuCollect(JSONArray data,String id) {
        //目前已收藏的菜单
        List<String> listExistMenuids = menuRepository.queryMenuidByUserCollect(id);
        //需要添加的菜单
        List<String> listAddMenuids = new ArrayList<>();
        //需要删除的菜单
        List<String> listDelMenuids = new ArrayList<>();
        //传进来的菜单(总)
        List<String> listInMenuids = new ArrayList<>();
        //传进来的菜单(有url的)
        List<String> listInMenuidsLast = new ArrayList<>();

        //先保存传进来的菜单
        listInMenuids = getIdIntoListMenuids(data,listInMenuids);

        //将没有url的删除
        for(String s : listInMenuids){
            Menu m = menuRepository.queryMenuById(s);
            if(!StringUtils.isEmpty(m.getHref())){
                listInMenuidsLast.add(s);
            }
        }

        //比较是否有新增的
        for(String s1 : listInMenuidsLast){
            if(!listExistMenuids.contains(s1)){
                listAddMenuids.add(s1);
            }
        }

        //比较是否有删除的
        for(String s1 : listExistMenuids){
            if(!listInMenuidsLast.contains(s1)){
                listDelMenuids.add(s1);
            }
        }

        //添加新增的
        for(String s1 : listAddMenuids){
            unimaxEntityManager.createNativeQuery(" insert into SM_USER_COLLECT_MENU (user_id,menu_id) values (?,?) ").setParameter(1,id).setParameter(2,s1).executeUpdate();
        }

        //删除要删除的
        for(String s1 : listDelMenuids){
            unimaxEntityManager.createNativeQuery(" delete SM_USER_COLLECT_MENU where user_id=? and menu_id = ? ").setParameter(1,id).setParameter(2,s1).executeUpdate();
        }

    }

    @Override
    public List<MenuBo> ajaxSelectCollect(String id) {
        List<String> listExistMenuids = menuRepository.queryMenuidByUserCollect(id);
        List<Menu> listmenus = menuRepository.queryMenusByIdIn(listExistMenuids);
        List<MenuBo> listbos = new ArrayList<MenuBo>();
        for(Menu m : listmenus){
            listbos.add(new MenuBo(m));
        }
        return listbos;
    }

    @Override
    public List<String> loadSearchInfo(String info,String id) {
        List<Menu> menus = menuRepository.queryListName(id,"%"+info+"%");
        List<String> list = new ArrayList<String>();
        for(Menu m : menus){
            list.add(m.getName()+"("+m.getUda2()+")");
        }
        return list;
    }

    @Override
    public List<MenuBo> searchAndOpenMenu(String info, String id) {
        List<Menu> menus = menuRepository.queryListName(id,"%"+info+"%");
        List<MenuBo> list = new ArrayList<MenuBo>();
        for(Menu m : menus){
            list.add(new MenuBo(m));
        }
        return list;
    }

    @Override
    public void rowUp(Menu m) {
        Menu up = menuRepository.queryMenuByLeveAndSeqAndPmenu(m.getLeve(),m.getSeq()-1,m.getPmenu());
        if(up!=null){
            up.setSeq(m.getSeq());
            m.setSeq(m.getSeq()-1);
            menuRepository.save(up);
            menuRepository.save(m);
        }
    }

    @Override
    public void rowDown(Menu m) {
        Menu down = menuRepository.queryMenuByLeveAndSeqAndPmenu(m.getLeve(),m.getSeq()+1,m.getPmenu());
        if(down!=null){
            down.setSeq(m.getSeq());
            m.setSeq(m.getSeq()+1);
            menuRepository.save(down);
            menuRepository.save(m);
        }
    }


}
