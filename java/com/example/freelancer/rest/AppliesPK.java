package com.example.freelancer.rest;

public class AppliesPK {
    private int userUserID;
    private int projectProjectID;

    public AppliesPK() {
    }

    public AppliesPK(int userUserID, int projectProjectID) {
        this.userUserID = userUserID;
        this.projectProjectID = projectProjectID;
    }

    public int getUserUserID() {
        return userUserID;
    }

    public void setUserUserID(int userUserID) {
        this.userUserID = userUserID;
    }

    public int getProjectProjectID() {
        return projectProjectID;
    }

    public void setProjectProjectID(int projectProjectID) {
        this.projectProjectID = projectProjectID;
    }
}