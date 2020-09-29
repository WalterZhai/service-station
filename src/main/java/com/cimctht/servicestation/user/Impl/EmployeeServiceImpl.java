package com.cimctht.servicestation.user.Impl;

import com.cimctht.servicestation.common.entity.TableEntity;
import com.cimctht.servicestation.user.entity.Employee;

import java.util.List;

public interface EmployeeServiceImpl {

    TableEntity findEmployeesByIsDeleteAndCodeLikeAndNameLike(String name, String code, Integer page, Integer limit);

    void genEmployees(List<Employee> list);

    TableEntity employeeTableData(String id, Integer page, Integer limit);
}
