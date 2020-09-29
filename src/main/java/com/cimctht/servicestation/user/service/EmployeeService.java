package com.cimctht.servicestation.user.service;

import com.cimctht.servicestation.common.constant.ThtConstant;
import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.common.utils.EntityUtils;
import com.cimctht.servicestation.common.utils.MathsUtils;
import com.cimctht.servicestation.common.utils.TimeUtils;
import com.cimctht.servicestation.user.Impl.EmployeeServiceImpl;
import com.cimctht.servicestation.user.entity.Depart;
import com.cimctht.servicestation.user.entity.Employee;
import com.cimctht.servicestation.user.entity.User;
import com.cimctht.servicestation.user.repository.DepartRepository;
import com.cimctht.servicestation.user.repository.EmployeeRepository;
import com.cimctht.servicestation.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService implements EmployeeServiceImpl {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartRepository departRepository;

    @Override
    public TableEntity findEmployeesByIsDeleteAndCodeLikeAndNameLike(String name, String code, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Employee> pages = employeeRepository.findEmployeesByIsDeleteAndCodeLikeAndNameLike(0,"%"+code+"%","%"+name+"%",pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

    @Override
    public void genEmployees(List<Employee> list) {
        List<User> listU = new ArrayList<User>();
        for(Employee emp : list){
            User u = new User();
            u.setName(emp.getName());
            u.setLoginName(emp.getCode());
            u.setPassword(ThtConstant.PASSWORD);
            u.setIsLocked(0);
            u.setEmail(emp.getEmail());
            u.setMobile(emp.getMobile());
            u.setEffectiveDate(TimeUtils.convertStringToDate("2000-01-01","yyyy-MM-dd"));
            u.setExpirationDate(TimeUtils.convertStringToDate("3000-01-01","yyyy-MM-dd"));
            EntityUtils.insertBasicInfo(u);
            listU.add(u);
        }
        userRepository.saveAll(listU);
    }

    @Override
    public TableEntity employeeTableData(String id, Integer page, Integer limit) {
        Depart d = departRepository.findDepartById(id);
        Pageable pageable = PageRequest.of(page-1,limit);
        Page<Employee> pages = employeeRepository.findEmployeesByIsDeleteAndDepart(0,d,pageable);
        return new TableEntity(pages.getContent(), MathsUtils.convertLong2BigDecimal(pages.getTotalElements()));
    }

}
