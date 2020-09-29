package com.cimctht.servicestation.user.service;

import com.cimctht.servicestation.common.constant.ThtConstant;
import com.cimctht.servicestation.common.exception.UnimaxException;
import com.cimctht.servicestation.common.utils.StringUtils;
import com.cimctht.servicestation.common.utils.TimeUtils;
import com.cimctht.servicestation.user.Impl.SyncServiceImpl;
import com.cimctht.servicestation.user.bo.DepartSyncBo;
import com.cimctht.servicestation.user.entity.Depart;
import com.cimctht.servicestation.user.entity.Employee;
import com.cimctht.servicestation.user.repository.DepartRepository;
import com.cimctht.servicestation.user.repository.EmployeeRepository;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import java.util.*;

@Service
public class SyncService implements SyncServiceImpl {

    static final Logger logger = LoggerFactory.getLogger(SyncService.class);

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HttpServletRequest request;


    @Override
    public void syncEmployee(){

        //加载已有用户，转为Map，以集团工号作为key，实体作为value
        List<Employee>  listEmps = employeeRepository.findAll();
        Map<String,Employee> map = new HashMap<String,Employee>();
        for(Employee e : listEmps){
            map.put(e.getUda1(),e);
        }

        try{

            // 使用RPC方式调用WebService
            RPCServiceClient serviceClient = new RPCServiceClient();

            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(ThtConstant.HRMS_EMPLOYEE);
            Options options = serviceClient.getOptions();
            // 确定目标服务地址
            options.setTo(targetEPR);
            // 确定调用方法
            options.setAction("soap:Body");

            options.setTimeOutInMilliSeconds(20);

            QName qname = new QName("http://10.33.1.39/hrms", "Body");
            // 指定getPrice方法的参数值
            Object[] parameters = new Object[] {};

            // 指定getPrice方法返回值的数据类型的Class对象
            Class[] returnTypes = new Class[] { String.class };



            // 调用方法一 传递参数，调用服务，获取服务返回结果集
            OMElement element = serviceClient.invokeBlocking(qname, parameters);
            Iterator it = element.getChildrenWithLocalName("record");
            List<Employee> list = new ArrayList<Employee>();
            while (it.hasNext()) {
                // 转成字符串
                String xmlStr = it.next().toString();
                Document doc = DocumentHelper.parseText(xmlStr);
                // 获得document中的根节点
                String uParam;
                Element rootElement = doc.getRootElement();
                uParam = rootElement.elementTextTrim("EMPLOYEE_CODE");
                Employee u;
                if(map.containsKey(uParam)){
                    u = map.get(uParam);
                }else{
                    u = new Employee();
                }
                uParam = rootElement.elementTextTrim("COMPANY_CODE");
                u.setCode(uParam);
                uParam = rootElement.elementTextTrim("EMPLOYEE_NAME");
                u.setName(uParam);
                uParam = rootElement.elementTextTrim("EMPLOYEE_CODE");
                u.setUda1(uParam);
                uParam = rootElement.elementTextTrim("MOBIL");
                u.setMobile(uParam);
                uParam = rootElement.elementTextTrim("SEX_NAME");
                if("男".equals(uParam)){
                    u.setSex(0);
                }else{
                    u.setSex(1);
                }
                uParam = rootElement.elementTextTrim("CERTIFICATE_ID");
                u.setIdCardNum(uParam);
                uParam = rootElement.elementTextTrim("BORN_DATE");
                u.setBirthday(TimeUtils.convertStringToDate(uParam,"yyyy-MM-dd hh:mm:ss"));
                uParam = rootElement.elementTextTrim("HOME_TOWN");
                u.setAddress(uParam);
                uParam = rootElement.elementTextTrim("OFFICE_PHONE");
                u.setOfficeTele(uParam);
                uParam = rootElement.elementTextTrim("EMAIL");
                u.setEmail(uParam);
                uParam = rootElement.elementTextTrim("FIRST_WORK_DATE");
                u.setCareerBeginDate(TimeUtils.convertStringToDate(uParam,"yyyy-MM-dd hh:mm:ss"));
                uParam = rootElement.elementTextTrim("JOIN_DATE");
                u.setJoinCompanyDate(TimeUtils.convertStringToDate(uParam,"yyyy-MM-dd hh:mm:ss"));
                uParam = rootElement.elementTextTrim("UNIT_ID");
                Depart d = departRepository.findDepartByCode(uParam);
                u.setDepart(d);

                Date date = new Date();
                if(StringUtils.isEmpty(u.getId())){
                    u.setCreateId("admin");
                    u.setCreateDate(date);
                    u.setModifyId("admin");
                    u.setModifyDate(date);
                    u.setIsDelete(0);
                }else{
                    u.setModifyId("admin");
                    u.setModifyDate(date);
                }

                list.add(u);
            }
            employeeRepository.saveAll(list);
            logger.info("人员信息同步成功！");
        }catch (Exception e){
            throw new UnimaxException(e.getMessage());
        }
    }

    @Override
    public void syncDepart() {

        //加载已有部门，转为Map，以unit_id作为key，实体作为value
        List<Depart>  listDeparts = departRepository.findAll();
        Map<String,Depart> map = new HashMap<String,Depart>();
        for(Depart d : listDeparts){
            map.put(d.getCode(),d);
        }

        try{
            // 使用RPC方式调用WebService
            RPCServiceClient serviceClient = new RPCServiceClient();

            // 指定调用WebService的URL
            EndpointReference targetEPR = new EndpointReference(ThtConstant.HRMS_DEPART);
            Options options = serviceClient.getOptions();
            // 确定目标服务地址
            options.setTo(targetEPR);
            // 确定调用方法
            options.setAction("soap:Body");

            options.setTimeOutInMilliSeconds(20);

            QName qname = new QName("http://10.33.1.39/hrms", "Body");
            // 指定getPrice方法的参数值
            Object[] parameters = new Object[] {};

            // 指定getPrice方法返回值的数据类型的Class对象
            Class[] returnTypes = new Class[] { String.class };



            // 调用方法一 传递参数，调用服务，获取服务返回结果集
            OMElement element = serviceClient.invokeBlocking(qname, parameters);
            Iterator it = element.getChildrenWithLocalName("record");
            List<Depart> list = new ArrayList<Depart>();
            List<DepartSyncBo> listbo = new ArrayList<DepartSyncBo>();
            while (it.hasNext()) {
                // 转成字符串
                String xmlStr = it.next().toString();
                Document doc = DocumentHelper.parseText(xmlStr);
                // 获得document中的根节点
                String uParam;
                Element rootElement = doc.getRootElement();
                uParam = rootElement.elementTextTrim("UNIT_ID");
                Depart d;
                DepartSyncBo bo = new DepartSyncBo();
                if(map.containsKey(uParam)){
                    d = map.get(uParam);
                }else{
                    d = new Depart();
                }
                uParam = rootElement.elementTextTrim("UNIT_ID");
                d.setCode(uParam);
                bo.setChildCode(uParam);
                uParam = rootElement.elementTextTrim("UNIT_NAME");
                d.setName(uParam);
                uParam = rootElement.elementTextTrim("PARENT_ID");
                bo.setParentCode(uParam);
//                Depart p = new Depart();
//                p.setId(uParam);
//                d.setPdepart(p);
                uParam = rootElement.elementTextTrim("MANAGER_POSITION_NAME");
                d.setUda1(uParam);

                Date date = new Date();
                if(StringUtils.isEmpty(d.getId())){
                    d.setCreateId("admin");
                    d.setCreateDate(date);
                    d.setModifyId("admin");
                    d.setModifyDate(date);
                    d.setIsDelete(0);
                }else{
                    d.setModifyId("admin");
                    d.setModifyDate(date);
                }
                list.add(d);
                listbo.add(bo);
            }
            departRepository.saveAll(list);
            //填写上级部门
            for(DepartSyncBo b : listbo){
                if(!StringUtils.isEmpty(b.getParentCode())){
                    Depart d =  departRepository.findDepartByCode(b.getChildCode());
                    if(d.getPdepart()!=null){
                        if(!d.getPdepart().getCode().equals(b.getParentCode())){
                            Depart p = departRepository.findDepartByCode(b.getParentCode());
                            d.setPdepart(p);
                            departRepository.save(d);
                        }
                    }else{
                        Depart p = departRepository.findDepartByCode(b.getParentCode());
                        d.setPdepart(p);
                        departRepository.save(d);
                    }
                }
            }
            logger.info("部门信息同步成功！");
        }catch (Exception e){
            throw new UnimaxException(e.getMessage());
        }
    }


}
