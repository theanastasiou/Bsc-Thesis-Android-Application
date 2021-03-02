package com.example.freelancer.rest;

public class NotificationsPK {
    private int notificationID;
    private int receiver;

    public NotificationsPK() {
    }

    public NotificationsPK(int notificationID, int receiver) {
        this.notificationID = notificationID;
        this.receiver = receiver;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }
}
