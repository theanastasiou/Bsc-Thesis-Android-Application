package com.example.freelancer.rest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.Date;

public class User {

    private int userID;
    private String name;
    private String surname;
    private String userName;
    private String email;
    private int phoneNumber;
    private String country;
    private int typeOfUser;
    private Date dateOfBirth;
    private double earnings;
    private double onlinepresence;
    private String about;
//    private byte[] photo;
    private String password;
    private double averageRating;

    private Collection<Skills> skillsCollection;
    private Collection<Project> projectCollection;

    private Collection<Project> projectCollection1;

    private Collection<Project> projectCollection2;

    private Collection<Chat> chatCollection;

    private Collection<Chat> chatCollection1;

    private Collection<Review> reviewCollection;

    private Collection<Review> reviewCollection1;

    private Collection<Applies> appliesCollection;

    private Collection<Notifications> notificationsCollection;

    public User() {
    }

    public User(Integer userID) {
        this.userID = userID;
    }

    public User(Integer userID, String name, String surname, String userName,String password, String email, int phoneNumber, String country, int typeOfUser, Date dateOfBirth) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.typeOfUser = typeOfUser;
        this.dateOfBirth = dateOfBirth;

    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(int typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

//    public byte[] getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(byte[] photo) {
//        this.photo = photo;
//    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }
    public double getOnlinePresence() {
        return onlinepresence;
    }

    public void setOnlinePresence(double onlinepresence) {
        this.onlinepresence = onlinepresence;
    }

    public Collection<Skills> getSkillsCollection() {
        return skillsCollection;
    }


    public void setSkillsCollection(Collection<Skills> skillsCollection) {
        this.skillsCollection = skillsCollection;
    }


    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    public Collection<Project> getProjectCollection1() {
        return projectCollection1;
    }

    public void setProjectCollection1(Collection<Project> projectCollection1) {
        this.projectCollection1 = projectCollection1;
    }


    public Collection<Project> getProjectCollection2() {
        return projectCollection2;
    }

    public void setProjectCollection2(Collection<Project> projectCollection2) {
        this.projectCollection2 = projectCollection2;
    }


    public Collection<Chat> getChatCollection() {
        return chatCollection;
    }

    public void setChatCollection(Collection<Chat> chatCollection) {
        this.chatCollection = chatCollection;
    }


    public Collection<Chat> getChatCollection1() {
        return chatCollection1;
    }

    public void setChatCollection1(Collection<Chat> chatCollection1) {
        this.chatCollection1 = chatCollection1;
    }


    public Collection<Review> getReviewCollection() {
        return reviewCollection;
    }

    public void setReviewCollection(Collection<Review> reviewCollection) {
        this.reviewCollection = reviewCollection;
    }


    public Collection<Review> getReviewCollection1() {
        return reviewCollection1;
    }

    public void setReviewCollection1(Collection<Review> reviewCollection1) {
        this.reviewCollection1 = reviewCollection1;
    }


    public Collection<Applies> getAppliesCollection() {
        return appliesCollection;
    }

    public void setAppliesCollection(Collection<Applies> appliesCollection) {
        this.appliesCollection = appliesCollection;
    }


    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}