package com.example.freelancer.rest;
import java.util.Collection;

public class Skills {

    private int skillID;
    private String description;
    private Collection<User> userCollection;
    private Collection<Project> projectCollection;

    public Skills() {
    }

    public Skills(Integer skillID) {
        this.skillID = skillID;
    }

    public Integer getSkillID() {
        return skillID;
    }

    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }


    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;}

}
