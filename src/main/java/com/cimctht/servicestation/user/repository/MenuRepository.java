package com.cimctht.servicestation.user.repository;

import com.cimctht.servicestation.user.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,String> {

    List<Menu> queryMenusByLeveAndIsDelete(Integer leve, Integer isDelete);

    @Query(nativeQuery = true, value =" select distinct t.menu_id from SM_ROLE_REL_MENU t left join SM_MENU m on t.menu_id=m.gid where t.role_id in (:roleidsList) and m.is_delete=0 and m.type=0 ")
    List<String> queryMenuidByRoleids(@Param("roleidsList") List<String> roleidsList);

    List<Menu> queryMenusByLeveAndIsDeleteAndIdInOrderBySeq(Integer leve, Integer isDelete, List<String> ids);

    List<Menu> queryMenusByLeveAndIsDeleteAndIdInAndPmenuOrderBySeq(Integer leve, Integer isDelete, List<String> ids, Menu menu);

    List<Menu> queryMenusByLeveAndIsDeleteOrderBySeq(Integer leve, Integer isDelete);

    List<Menu> queryMenusByLeveAndIsDeleteAndPmenuOrderBySeq(Integer leve, Integer isDelete,Menu menu);

    Page<Menu> queryMenusByIsDeleteAndPmenuOrderBySeq(Integer isDelete, Menu menu, Pageable pageable);

    Menu queryMenuById(String id);

    @Query(nativeQuery = true, value =" select nvl(max(t.seq),0) from SM_MENU t where t.leve=:leve and t.pid=:pid ")
    Integer queryMaxSeqByPidAndLeve(@Param("leve") Integer leve,@Param("pid") String pid);

    @Query(nativeQuery = true, value =" select nvl(max(t.seq),0) from SM_MENU t where t.leve=:leve and pid is null ")
    Integer queryMaxSeqByLeve(@Param("leve") Integer leve);

    @Query(nativeQuery = true, value =" select count(*) from SM_MENU t where t.uda2=:selectid ")
    Integer queryCtByUda2(@Param("selectid") String selectid);

    @Query(nativeQuery = true, value =" select distinct t.menu_id from SM_ROLE_REL_MENU t left join SM_MENU m on t.menu_id=m.gid where t.role_id=:roleid and m.is_delete=0 ")
    List<String> queryMenuidByRoleid(@Param("roleid") String roleid);

    @Query(nativeQuery = true, value =" select m.gid from SM_USER u left join sm_role_rel_user rru on u.gid=rru.user_id left join sm_role r on rru.role_id=r.gid and r.is_delete=0 left join sm_role_rel_menu rrm on rru.role_id=rrm.role_id left join sm_menu m on rrm.menu_id=m.gid and m.is_delete=0 where u.gid=:userid ")
    List<String> queryMenuidByUserid(@Param("userid") String userid);

    @Query(nativeQuery = true, value =" select t.menu_id from SM_USER_COLLECT_MENU t left join sm_menu m on t.menu_id=m.gid where t.user_id=:userid and m.is_delete=0 ")
    List<String> queryMenuidByUserCollect(@Param("userid") String userid);

    List<Menu> queryMenusByIdIn(List<String> ids);

    @Query(nativeQuery = true, value =" with rt as " +
            "(select m.* from SM_USER u " +
            "left join sm_role_rel_user rru on u.gid=rru.user_id " +
            "left join sm_role r on rru.role_id=r.gid and r.is_delete=0 " +
            "left join sm_role_rel_menu rrm on rrm.role_id=r.gid " +
            "left join sm_menu m on rrm.menu_id=m.gid and m.is_delete=0 " +
            "where u.gid=:id) " +
            "select t.* from rt t where (t.name||'('||t.uda2||')') like :name  and t.is_delete=0 and t.href is not null")
    List<Menu> queryListName(String id,String name);

    Menu queryMenuByLeveAndSeqAndPmenu(Integer leve, Integer seq, Menu m);

}
