package com.example.freelancer.rest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.Date;

public class Chat {
    private Integer messageID;
    private String content;
    private Date date;
    private User userSend;
    private User userRec;
    private boolean seen;
    private Collection<Notifications> notificationsCollection;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Chat() {
    }

    public Chat(Integer messageID) {
        this.messageID = messageID;
    }

    public Chat(Integer messageID, Date date) {
        this.messageID = messageID;
        this.date = date;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getDate() {
        return date;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserSend() {
        return userSend;
    }

    public void setUserSend(User userSend) {
        this.userSend = userSend;
    }

    public User getUserRec() {
        return userRec;
    }

    public void setUserRec(User userRec) {
        this.userRec = userRec;
    }


    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }
}
