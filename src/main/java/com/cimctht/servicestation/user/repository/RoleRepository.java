package com.cimctht.servicestation.user.repository;

import com.cimctht.servicestation.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,String> {

    @Query(nativeQuery = true, value =" select r.* from sm_role r left join sm_role_rel_user ru on r.gid=ru.role_id left join sm_user u on ru.user_id=u.gid where r.is_delete=0 and u.is_delete=0 and u.login_name=:userid ")
    List<Role> queryRolesByUserLoginname(@Param("userid") String userid);

    Page<Role> findRolesByIsDeleteAndCodeLikeAndNameLikeOrderByCode(Integer isDelete, String code, String name, Pageable pageable);

    Role findRoleById(String id);

    List<Role> findRolesByIsDelete(Integer isDelete);

    @Query(nativeQuery = true, value =" select r.* from SM_ROLE_REL_USER ru left join sm_role r on ru.role_id=r.gid where r.is_delete=0 and ru.user_id=:userid ")
    List<Role> queryRoleUserid(@Param("userid") String userid);

    List<Role> queryRolesByIsDelete(Integer isDelete);
}
