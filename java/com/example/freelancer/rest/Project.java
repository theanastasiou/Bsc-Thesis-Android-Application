package com.example.freelancer.rest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.Date;

public class Project {
    private Integer projectID;
    private Date date;
    private String title;
    private String description;
    private int available;
    private float budget;
    private String category;
    private String coin;
    private String paymenttype;
    private Collection<User> userCollection;
    private Collection<Skills> skillsCollection;
    private User userIDOwner;
    private User userIDWorker;
    private Collection<Review> reviewCollection;
    private Collection<Applies> appliesCollection;

    public Project() {
    }

    public Project(Integer projectID) {
        this.projectID = projectID;
    }

    public Project(Integer projectID, Date date, String title, String description, int available, String paymenttype,String category,float budget,String coin) {
        this.projectID = projectID;
        this.budget = budget;
        this.category = category;
        this.coin = coin;
        this.paymenttype = paymenttype;
        this.date = date;
        this.title = title;
        this.description = description;
        this.available = available;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getDate() {
        return date;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public void setDate(Date date) {
        this.date = date;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentType() {
        return paymenttype;
    }

    public void setPaymentType(String paymenttype) {
        this.paymenttype = paymenttype;
    }


    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public float getBudget(){
        return budget;
    }

    public void setBudget(float budget) {
        this.budget= budget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Collection<Skills> getSkillsCollection() {
        return skillsCollection;
    }

    public void setSkillsCollection(Collection<Skills> skillsCollection) {
        this.skillsCollection = skillsCollection;
    }

    public User getUserIDOwner() {
        return userIDOwner;
    }

    public void setUserIDOwner(User userIDOwner) {
        this.userIDOwner = userIDOwner;
    }

    public User getUserIDWorker() {
        return userIDWorker;
    }

    public void setUserIDWorker(User userIDWorker) {
        this.userIDWorker = userIDWorker;
    }

    public Collection<Review> getReviewCollection() {
        return reviewCollection;
    }

    public void setReviewCollection(Collection<Review> reviewCollection) {
        this.reviewCollection = reviewCollection;
    }

    public Collection<Applies> getAppliesCollection() {
        return appliesCollection;
    }

    public void setAppliesCollection(Collection<Applies> appliesCollection) {
        this.appliesCollection = appliesCollection;
    }
}
