package com.cimctht.servicestation.message.repository;

import com.cimctht.servicestation.message.entity.Message;
import com.cimctht.servicestation.message.entity.MessageInfo;
import com.cimctht.servicestation.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MessageInfoRepository  extends JpaRepository<MessageInfo,String> {

    List<MessageInfo> queryMessageInfosByIsDeleteAndMessage(Integer isDelete, Message message);

    @Query(nativeQuery = true, value =" select count(*) from MSG_INFO t where t.user_id=?1 and t.is_delete=0 ")
    Integer queryMessageInfosCountByUserId(String userid);

    Page<MessageInfo> queryMessageInfosByIsDeleteAndUserOrderByIsReadAscCreateDateDesc(Integer isDelete, User user, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value =" update MSG_INFO t set t.is_read=1 where t.user_id=?1 and t.is_delete=0 ")
    Integer updateIsReadByUserId(String userid);
}
