/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aarodoeht
 */
@Entity
@Table(name = "User")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findCreds", query = "SELECT u from User u WHERE u.userName = :uname and u.password = :pass"),
    @NamedQuery(name = "User.findByUserID", query = "SELECT u FROM User u WHERE u.userID = :userID"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByUserName", query = "SELECT u FROM User u WHERE u.userName = :userName"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPhoneNumber", query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "User.findByCountry", query = "SELECT u FROM User u WHERE u.country = :country"),
    @NamedQuery(name = "User.findByAverageRating", query = "SELECT u FROM User u WHERE u.averageRating = :averageRating"),
    @NamedQuery(name = "User.findByTypeOfUser", query = "SELECT u FROM User u WHERE u.typeOfUser = :typeOfUser"),
    @NamedQuery(name = "User.findByDateOfBirth", query = "SELECT u FROM User u WHERE u.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "User.findByPhoto", query = "SELECT u FROM User u WHERE u.photo = :photo")})
public class User implements Serializable {

    @Size(max = 45)
    @Column(name = "Password")
    private String password;
    @Size(max = 45)
    @Column(name = "About")
    private String about;
    @Column(name = "Earnings")
    private Double earnings;
    @Column(name = "OnlinePresence")
    private Double onlinepresence;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserID")
    private Integer userID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UserName")
    private String userName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PhoneNumber")
    private int phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Country")
    private String country;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AverageRating")
    private Double averageRating;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TypeOfUser")
    private int typeOfUser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateOfBirth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Column(name = "Photo")
//    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @JsonbTransient
    private byte[] photo;
  
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="User_has_Skills", 
        joinColumns = {@JoinColumn(name = "User_UserID")},
        inverseJoinColumns = {@JoinColumn(name = "Skills_SkillID")})
    private Set<Skills> skillsCollection = new HashSet<>();
//    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "userCollection")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Saved", joinColumns = {
        @JoinColumn(name = "User_UserID", referencedColumnName = "UserID")}, inverseJoinColumns = {
        @JoinColumn(name = "Project_ProjectID", referencedColumnName = "ProjectID")})
//     @ManyToMany(cascade = CascadeType.ALL)
    private Set<Project> projectCollection= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userIDOwner")
    @JsonbTransient
    private Set<Project> projectCollection1= new HashSet<>();
    @OneToMany(mappedBy = "userIDWorker")
    @JsonbTransient
    private Set<Project> projectCollection2= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userSend")
    @JsonbTransient
    private Set<Chat> chatCollection= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userRec")
    @JsonbTransient
    private Set<Chat> chatCollection1= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    @JsonbTransient
    private Set<Review> reviewCollection= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    @JsonbTransient
    private Set<Review> reviewCollection1= new HashSet<>();
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
    @JsonbTransient
    private Set<Applies> appliesCollection= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonbTransient
    private Set<Notifications> notificationsCollection= new HashSet<>();

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
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.typeOfUser = typeOfUser;
        this.dateOfBirth = dateOfBirth;
         this.password = password;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }   
    
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
//    @JsonIgnore
//    @JsonbTransient
    public byte[] getPhoto() {
        return photo;
    }
    
//    @JsonIgnore
    @JsonbTransient
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
    
   public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public void setOnlinepresence(Double onlinepresence) {
        this.onlinepresence = onlinepresence;
    }

      public Double getOnlinePresence() {
        return onlinepresence;
    }


    public Set<Skills> getSkillsCollection() {
        return skillsCollection;
    }

    public void setSkillsCollection(Set<Skills> skillsCollection) {
        this.skillsCollection = skillsCollection;
    }

   
    public Set<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Set<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Project> getProjectCollection1() {
        return projectCollection1;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setProjectCollection1(Set<Project> projectCollection1) {
        this.projectCollection1 = projectCollection1;
    }

   @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Project> getProjectCollection2() {
        return projectCollection2;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setProjectCollection2(Set<Project> projectCollection2) {
        this.projectCollection2 = projectCollection2;
    }

   @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Chat> getChatCollection() {
        return chatCollection;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setChatCollection(Set<Chat> chatCollection) {
        this.chatCollection = chatCollection;
    }

 @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Chat> getChatCollection1() {
        return chatCollection1;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setChatCollection1(Set<Chat> chatCollection1) {
        this.chatCollection1 = chatCollection1;
    }

  @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Review> getReviewCollection() {
        return reviewCollection;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setReviewCollection(Set<Review> reviewCollection) {
        this.reviewCollection = reviewCollection;
    }

    @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Review> getReviewCollection1() {
        return reviewCollection1;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setReviewCollection1(Set<Review> reviewCollection1) {
        this.reviewCollection1 = reviewCollection1;
    }

//   @JsonbTransient
//   @JsonIgnore
//   @XmlTransient
    public Set<Applies> getAppliesCollection() {
        return appliesCollection;
    }
//@JsonbTransient
//   @JsonIgnore
//   @XmlTransient
    public void setAppliesCollection(Set<Applies> appliesCollection) {
        this.appliesCollection = appliesCollection;
    }

   @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setNotificationsCollection(Set<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userID != null ? userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userID == null && other.userID != null) || (this.userID != null && !this.userID.equals(other.userID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.User[ userID=" + userID + " ]";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void AddSkill(Skills s){
        this.skillsCollection.add(s);
    }
    
    public void AddProject(Project p){
        this.projectCollection.add(p);
    }
   
    public void rmvProject(Project p){
        this.projectCollection.remove(p);
    }
   
}
