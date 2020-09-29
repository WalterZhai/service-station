package com.cimctht.servicestation.user.repository;

import com.cimctht.servicestation.user.entity.Role;
import com.cimctht.servicestation.user.entity.User;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User,String> {

    User queryUserByLoginNameAndIsDelete(String loginName, Integer isDelete);

    List<User> queryUsersByIsDelete(Integer isDelete);

    User queryUserById(String id);

    //List<User> findUsersByNameLike(String name);

    Page<User> findUsersByIsDeleteAndNameLike(Integer isDelete, String name, Pageable pageable);

    User queryUserByLoginNameAndIsDeleteAndIsLocked(String loginName, Integer isDelete, Integer isLocked);

    @Modifying
    @Query(value = "insert into SM_ROLE_REL_USER (role_id,User_Id) values (?1,?2)",nativeQuery = true)
    int addSelectRole(String roleid,String userid);

    @Modifying
    @Query(value = "delete sm_role_rel_user where role_id=?1 and user_id=?2",nativeQuery = true)
    int delSelectRole(String roleid,String userid);

    @Query(nativeQuery = true, value =" select u.* from SM_ROLE_REL_USER ru left join sm_user u on ru.user_id=u.gid where u.is_delete=0 and ru.role_id=?1 ")
    List<User> queryUserRoleId(String roleid);

    @Query(nativeQuery = true, value =" select u.* from SPRING_SESSION t left join sm_user u on t.principal_name=u.login_name ")
    Page<User> queryOnlineUser(Pageable pageable);

    @Query(nativeQuery = true, value =" select u.* from sm_user u left join SM_GROUP_RLE_USER gru on u.gid=gru.user_id where gru.group_id=?1 and u.is_delete=0 ")
    Page<User> queryUsersByIsDeleteAndGroupId(String selectid, Pageable pageable);

    @Query(nativeQuery = true, value =" select u.* from SM_GROUP_RLE_USER ru left join sm_user u on ru.user_id=u.gid where u.is_delete=0 and ru.group_id=?1 ")
    List<User> queryUserGroupId(String groupid);

}
