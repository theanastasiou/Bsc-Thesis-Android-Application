package com.example.freelancer.rest;
import java.util.Collection;

public class Applies {

    protected AppliesPK appliesPK;
    private String status;
    private Project project;
    private User user;
    private Collection<Notifications> notificationsCollection;

    public Applies() {
    }

    public Applies(AppliesPK appliesPK) {
        this.appliesPK = appliesPK;
    }

    public Applies(AppliesPK appliesPK, String status) {
        this.appliesPK = appliesPK;
        this.status = status;
    }

    public Applies(int userUserID, int projectProjectID) {
        this.appliesPK = new AppliesPK(userUserID, projectProjectID);
    }

    public AppliesPK getAppliesPK() {
        return appliesPK;
    }

    public void setAppliesPK(AppliesPK appliesPK) {
        this.appliesPK = appliesPK;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

}
