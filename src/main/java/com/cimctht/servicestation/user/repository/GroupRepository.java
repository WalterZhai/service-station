package com.cimctht.servicestation.user.repository;

import com.cimctht.servicestation.user.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface GroupRepository extends JpaRepository<Group,String> {

    Page<Group> queryGroupsByIsDeleteAndCodeLikeAndNameLikeOrderByCreateDate(Integer isDelete, String code, String name, Pageable pageable);

    Group findGroupById(String id);

    @Modifying
    @Query(value = "insert into SM_GROUP_RLE_USER (group_id,User_Id) values (?1,?2)",nativeQuery = true)
    int addSelectRole(String groupid,String userid);

    @Modifying
    @Query(value = "delete SM_GROUP_RLE_USER where group_id=?1 and user_id=?2",nativeQuery = true)
    int delSelectRole(String groupid,String userid);

    List<Group> queryGroupsByIsDelete(Integer isDelete);
}
