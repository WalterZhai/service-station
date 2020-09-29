package com.cimctht.servicestation.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cimctht.servicestation.common.constant.ThtConstant;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.common.utils.TimeUtils;
import com.cimctht.servicestation.user.Impl.UserServiceImpl;
import com.cimctht.servicestation.user.bo.MenuBo;
import com.cimctht.servicestation.user.entity.Menu;
import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.MenuRepository;
import com.cimctht.servicestation.user.repository.RoleRepository;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private HttpServletRequest request;

    @PersistenceContext(unitName = "unimaxPersistenceUnit")
    private EntityManager unimaxEntityManager;


    @Override
    public User queryUserByLoginNameAndIsDelete(String loginName, Integer isDelete) {
        return userRepository.queryUserByLoginNameAndIsDelete(loginName,0);
    }

    @Override
    public List<MenuBo> queryMenusForLogin(String loginName) {
        //User user = userRepository.queryUserByLoginNameAndIsDelete(loginName,0);
        List<Role> listRoles = roleRepository.queryRolesByUserLoginname(loginName);
        List<String> listRoleids = new ArrayList<String>();
        for(Role r : listRoles){
            listRoleids.add(r.getId());
        }
        List<String> listMenuIds = menuRepository.queryMenuidByRoleids(listRoleids);
        List<Menu> listMenu = menuRepository.queryMenusByLeveAndIsDeleteAndIdInOrderBySeq(1,0,listMenuIds);

        //到前台的结果
        List<MenuBo> listMenuBo = new ArrayList<MenuBo>();
        for(Menu menu : listMenu){
            MenuBo bo = new MenuBo(menu);
            //找子菜单
            List<Menu> listChild = menuRepository.queryMenusByLeveAndIsDeleteAndIdInAndPmenuOrderBySeq(2,0,listMenuIds,menu);
            List<MenuBo> listChildBo = new ArrayList<MenuBo>();
            for(Menu c : listChild){
                MenuBo child = new MenuBo(c);
                listChildBo.add(child);
            }
            bo.setChildrenMenuBo(listChildBo);
            listMenuBo.add(bo);
        }
        return listMenuBo;
    }

    @Override
    public List<User> queryUsers() {
        List<User> list = userRepository.queryUsersByIsDelete(0);
        return list;
    }

    @Override
    public TableEntity queryUsersByLikeName(String name,Integer page,Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<User> pages = userRepository.findUsersByIsDeleteAndNameLike(0,"%"+name+"%",pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void updateIsLockedById(String id, Integer isLocked) {
        User u = userRepository.queryUserById(id);
        u.setIsLocked(isLocked);
        userRepository.save(u);
    }

    @Override
    public void saveOneUser(String name, String loginName, String email, String moblie) {
        User u = new User();
        u.setName(name);
        u.setLoginName(loginName);
        u.setPassword(ThtConstant.PASSWORD);
        u.setIsLocked(0);
        u.setEmail(email);
        u.setMobile(moblie);
        u.setEffectiveDate(TimeUtils.convertStringToDate("2000-01-01","yyyy-MM-dd"));
        u.setExpirationDate(TimeUtils.convertStringToDate("3000-01-01","yyyy-MM-dd"));
        EntityUtils.insertBasicInfo(u);
        userRepository.save(u);
    }

    @Override
    public void editOneUser(String id, String name, String loginName, String email, String moblie) {
        User u = userRepository.queryUserById(id);
        u.setName(name);
        u.setLoginName(loginName);
        u.setEmail(email);
        u.setMobile(moblie);
        EntityUtils.insertBasicInfo(u);
        userRepository.save(u);
    }

    @Override
    public User editBaseInfo(String id, String name, String email, String moblie) {
        User u = userRepository.queryUserById(id);
        u.setName(name);
        u.setEmail(email);
        u.setMobile(moblie);
        EntityUtils.insertBasicInfo(u);
        userRepository.save(u);
        return u;
    }

    @Override
    public void editPassword(String pwd1, String pwd2, String pwd3) {
        if(!pwd2.equals(pwd3)){
            throw new UnimaxException("新密码与确认密码不一致!");
        }
        User u = (User) request.getSession().getAttribute("user");
        if(!pwd1.equals(u.getPassword())){
            throw new UnimaxException("登录密码错误!");
        }
        u.setPassword(pwd2);
        EntityUtils.insertBasicInfo(u);
        userRepository.save(u);
        request.getSession().setAttribute("user",u);
    }

    @Override
    public Map<String, Object> ajaxLoadTransferUserRelRole(String userid) {
        Map<String, Object> result = new HashMap<String, Object>();

        //已属于用户的角色
        List<Role> roleExist = roleRepository.queryRoleUserid(userid);
        List<Role> roleAll = roleRepository.findRolesByIsDelete(0);
        //data值
        JSONArray arr = new JSONArray();
        for(Role r : roleAll){
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
        for(Role r : roleExist){
            listids.add(r.getId());
        }
        result.put("value",listids);

        return result;
    }

    @Override
    public void addSelectRole(String userid, JSONArray arr) {
        for(int i=0;i<arr.size();i++){
            userRepository.addSelectRole(arr.getJSONObject(i).get("value").toString(),userid);
        }
    }

    @Override
    public void delSelectRole(String userid, JSONArray arr) {
        for(int i=0;i<arr.size();i++){
            userRepository.delSelectRole(arr.getJSONObject(i).get("value").toString(),userid);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableEntity onlineUserData(Integer page, Integer limit) {
        String sql = "select u.name,u.login_name as loginName,t.creation_time as createTime,t.last_access_time as lastAccessTime from SPRING_SESSION t left join sm_user u on t.principal_name=u.login_name";
        Query query = unimaxEntityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> resultList = query.getResultList();
        return new TableEntity(resultList, MathsUtils.convertInteger2BigDecimal(resultList.size()));
    }

}
