package com.example.freelancer.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Notifications {

    private Date date;
    private Applies applies;
    private Chat chatMessageID;
    private Review reviewReviewID;
    private User user;
    private boolean seen;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    protected NotificationsPK notificationsPK;

    public Notifications() {
    }

    public Notifications(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    public Notifications(NotificationsPK notificationsPK, Date date) {
        this.notificationsPK = notificationsPK;
        this.date = date;
    }

    public Notifications(int notificationID, int receiver) {
        this.notificationsPK = new NotificationsPK(notificationID, receiver);
    }

    public NotificationsPK getNotificationsPK() {
        return notificationsPK;
    }

    public void setNotificationsPK(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getDate() {
        return date;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public void setDate(Date date) {
        this.date = date;
    }

    public Applies getApplies() {
        return applies;
    }

    public void setApplies(Applies applies) {
        this.applies = applies;
    }

    public Chat getChatMessageID() {
        return chatMessageID;
    }

    public void setChatMessageID(Chat chatMessageID) {
        this.chatMessageID = chatMessageID;
    }

    public Review getReviewReviewID() {
        return reviewReviewID;
    }

    public void setReviewReviewID(Review reviewReviewID) {
        this.reviewReviewID = reviewReviewID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
