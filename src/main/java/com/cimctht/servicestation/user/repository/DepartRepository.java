package com.cimctht.servicestation.user.repository;

import com.cimctht.servicestation.user.entity.Depart;
import com.cimctht.servicestation.user.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartRepository extends JpaRepository<Depart,String> {

    Depart findDepartByCode(String code);

    List<Depart> queryDepartsBypdepartAndIsDelete(Depart d,Integer isDelete);

    Depart findDepartById(String id);

    Page<Depart> queryDepartsByIsDeleteAndPdepartOrderByCode(Integer isDelete, Depart d, Pageable pageable);

}
