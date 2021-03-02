/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author aarodoeht
 */
@Entity
@Table(name = "Project")
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p"),
    @NamedQuery(name = "Project.findByProjectID", query = "SELECT p FROM Project p WHERE p.projectID = :projectID"),
    @NamedQuery(name = "Project.findByDate", query = "SELECT p FROM Project p WHERE p.date = :date"),
    @NamedQuery(name = "Project.findByTitle", query = "SELECT p FROM Project p WHERE p.title = :title"),
    @NamedQuery(name = "Project.findByDescription", query = "SELECT p FROM Project p WHERE p.description = :description"),
    @NamedQuery(name = "Project.findByAvailable", query = "SELECT p FROM Project p WHERE p.available = :available")})

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ProjectID")
    private Integer projectID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Description")
    private String description;
    
//    @Basic(optional = false)
//    @Size(min = 1, max = 25)
    @Column(name = "PaymentType")
    private String paymenttype;
//    @Basic(optional = false)
//   
//    @Size(min = 1, max = 45)
    @Column(name = "Category")
    private String category;
    
//    @Basic(optional = false) 
    @Column(name = "Budget")
    private float budget;
    
//     @Basic(optional = false)
//    @Size(min = 1, max = 15)
    @Column(name = "Coin")
    private String coin; 
     
    @Basic(optional = false)
    @NotNull
    @Column(name = "Available")
    private int available;
//    @JoinTable(name = "Saved", joinColumns = {
//        @JoinColumn(name = "Project_ProjectID", referencedColumnName = "ProjectID")}, inverseJoinColumns = {
//        @JoinColumn(name = "User_UserID", referencedColumnName = "UserID")})
//     @ManyToMany(cascade = CascadeType.ALL)
    @JsonbTransient
    @JsonIgnore
    @ManyToMany(mappedBy="projectCollection")
  private Set<User> userCollection= new HashSet<>();
//    @JoinTable(name = "Project_has_Skills", joinColumns = {
//        @JoinColumn(name = "Project_ProjectID", referencedColumnName = "ProjectID")}, inverseJoinColumns = {
//        @JoinColumn(name = "Skills_SkillID", referencedColumnName = "SkillID")})
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
//     @JsonbTransient
     @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
     @JoinTable(name="Project_has_Skills", 
        joinColumns = {@JoinColumn(name = "Project_ProjectID")},
        inverseJoinColumns = {@JoinColumn(name = "Skills_SkillID")})
//    @ManyToMany(cascade = {CascadeType.PERSIST})
//    @JsonIgnore
   
    private Set<Skills> skillsCollection = new HashSet<>();
    @JoinColumn(name = "UserIDOwner", referencedColumnName = "UserID")
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    //@JsonbTransient
    private User userIDOwner;
    @JoinColumn(name = "UserIDWorker", referencedColumnName = "UserID")
    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonbTransient
//    @JsonIgnore
    private User userIDWorker;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectProjectID")
    @JsonbTransient
    @JsonIgnore
    private Set<Review> reviewCollection= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    @JsonbTransient
    private Set<Applies> appliesCollection= new HashSet<>();

    public Project() {
    }

    public Project(Integer projectID) {
        this.projectID = projectID;
    }

    public Project(Integer projectID, Date date, String title, String description, int available,String paymenttype,String category,float budget,String coin) {
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
    
    
    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }
    
    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
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

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<User> getUserCollection() {
        return userCollection;
    }
    
@JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setUserCollection(Set<User> userCollection) {
        this.userCollection = userCollection;
    }

    public Set<Skills> getSkillsCollection() {
        return skillsCollection;
    }

    public void setSkillsCollection(Set<Skills> skillsCollection) {
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
//@JsonbTransient
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectID != null ? projectID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.projectID == null && other.projectID != null) || (this.projectID != null && !this.projectID.equals(other.projectID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.Project[ projectID=" + projectID + " ]";
    }
    
    public void AddSkill(Skills skill){
            skillsCollection.add(skill);
    }
    
}
