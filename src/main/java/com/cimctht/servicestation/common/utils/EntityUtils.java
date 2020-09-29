package com.cimctht.servicestation.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cimctht.servicestation.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class EntityUtils {

    static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);


    /**
     * 删除标识添加
     * @param o
     * @return
     */
    public static void insertDelete(Object o){
        delEntity(o);
    }

    /**
     * 删除标识添加(数组)
     * @param list
     */
    public static void insertDeleteAll(List list) {
        if(list.size()==0) {
            return;
        }
        for(int i=0;i<list.size();i++) {
            insertDelete(list.get(i));
        }
    }

    /**
     * 写入实体基本信息
     * @param o
     * @return
     */
    public static void insertBasicInfo(Object o){
        if(judgeIsNewEntity(o)) {
            modifyEntity(o);
        }else {
            newEntity(o);
        }
    }

    /**
     * 写入实体基本信息(数组)
     * @param list
     */
    public static void insertBasicInfoAll(List list) {
        if(list.size()==0) {
            return;
        }
        for(int i=0;i<list.size();i++) {
            insertBasicInfo(list.get(i));
        }
    }

    /**
     * 判断Entity是否有gid，有gid则是更新，没有gid则是新增
     *
     * @param o
     * @return
     */
    public static boolean judgeIsNewEntity(Object o) {
        Object id = getFieldValueByName("id", o);
        if (id == null)
            return false;
        else if ("".equals(id.toString()))
            return false;
        else if (id.toString().length() > 0)
            return true;
        else
            return false;
    }

    /**
     * 新建时自动填Entity信息
     *
     * @param o
     * @return
     */
    public static Object delEntity(Object o) {
        //获得session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //获得用户
        User bu = (User) session.getAttribute("user");
        //填值
        Date dt = new Date();
        Field f;
        try {
            f = o.getClass().getSuperclass().getDeclaredField("isDelete");
            f.setAccessible(true);
            f.set(o, 1);
            f = o.getClass().getSuperclass().getDeclaredField("modifyDate");
            f.setAccessible(true);
            f.set(o, dt);
            f = o.getClass().getSuperclass().getDeclaredField("modifyId");
            f.setAccessible(true);
            f.set(o, bu.getLoginName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("delEntity方法出现错误！" + ex.getMessage());
        }
        return o;
    }

    /**
     * 新建时自动填Entity信息
     *
     * @param o
     * @return
     */
    public static Object newEntity(Object o) {
        //获得session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //获得用户
        User bu = (User) session.getAttribute("user");
        //填值
        Date dt = new Date();
        Field f;
        try {
            f = o.getClass().getSuperclass().getDeclaredField("createDate");
            f.setAccessible(true);
            f.set(o, dt);
            f = o.getClass().getSuperclass().getDeclaredField("createId");
            f.setAccessible(true);
            f.set(o, bu.getLoginName());
            f = o.getClass().getSuperclass().getDeclaredField("modifyDate");
            f.setAccessible(true);
            f.set(o, dt);
            f = o.getClass().getSuperclass().getDeclaredField("modifyId");
            f.setAccessible(true);
            f.set(o, bu.getLoginName());
            f = o.getClass().getSuperclass().getDeclaredField("isDelete");
            f.setAccessible(true);
            f.set(o, 0);
//            f = o.getClass().getSuperclass().getDeclaredField("gid");
//            f.setAccessible(true);
//            f.set(o, StringUtils.generteUUID());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("newEntity方法出现错误！" + ex.getMessage());
        }
        return o;
    }

    /**
     * 修改时自动填Entity信息
     *
     * @param o
     * @return
     */
    public static Object modifyEntity(Object o) {
        //获得session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //获得用户
        User bu = (User) session.getAttribute("user");
        //填值
        Date dt = new Date();
        Field f;
        try {
            f = o.getClass().getSuperclass().getDeclaredField("modifyDate");
            f.setAccessible(true);
            f.set(o, dt);
            f = o.getClass().getSuperclass().getDeclaredField("modifyId");
            f.setAccessible(true);
            f.set(o, bu.getLoginName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("modifyEntity方法出现错误！" + ex.getMessage());
        }
        return o;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static List<Map<String, Object>> getFiledInfo(Object o) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(o.getClass().getDeclaredFields()));

        /**
         * 如果存在父类，获取父类的属性值，类型，名称并添加到一起
         */
        Class sc = o.getClass().getSuperclass();
        if (sc != null) {
            fields.addAll(Arrays.asList(sc.getDeclaredFields()));
        }
        for (Field field : fields) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("type", field.getType().toString());
            infoMap.put("name", field.getName());
            infoMap.put("value", getFieldValueByName(field.getName(), o));
            list.add(infoMap);
        }
        return list;
    }

}
