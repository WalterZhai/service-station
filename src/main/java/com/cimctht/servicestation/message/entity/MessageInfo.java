package com.cimctht.servicestation.message.entity;

import com.cimctht.servicestation.common.entity.BaseEntity;
import com.cimctht.servicestation.user.entity.User;

import javax.persistence.*;

@Entity
@Table(name="MSG_INFO")
public class MessageInfo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @Column(name = "IS_READ")
    private Integer isRead;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    private Message message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
