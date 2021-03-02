/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author aarodoeht
 */
@Entity
@Table(name = "Skills")
@NamedQueries({
    @NamedQuery(name = "Skills.findAll", query = "SELECT s FROM Skills s"),
    @NamedQuery(name = "Skills.findBySkillID", query = "SELECT s FROM Skills s WHERE s.skillID = :skillID"),
    @NamedQuery(name = "Skills.findByDescription", query = "SELECT s FROM Skills s WHERE s.description = :description")})
public class Skills implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SkillID")
    private Integer skillID;
    @Size(max = 20)
    @Column(name = "Description")
    private String description;
 

    
//    @ManyToMany(mappedBy = "skillsCollection",cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
  //@JsonbTransient
       @ManyToMany(mappedBy = "skillsCollection")

//    @ManyToMany(mappedBy="skillsCollection",cascade = {CascadeType.PERSIST})
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
//    @JsonIgnore
    @JsonbTransient
    @JsonIgnore
    private Set<Project> projectCollection = new HashSet<>();
       
    @ManyToMany(mappedBy = "skillsCollection",cascade=CascadeType.ALL)
    @JsonbTransient
//    @JsonIgnore
    private Set<User> userCollection = new HashSet<>();

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
    
   @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public Set<Project> getProjectCollection() {
        return projectCollection;
    }

    @JsonbTransient
   @JsonIgnore
   @XmlTransient
    public void setProjectCollection(Set<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (skillID != null ? skillID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Skills)) {
            return false;
        }
        Skills other = (Skills) object;
        if ((this.skillID == null && other.skillID != null) || (this.skillID != null && !this.skillID.equals(other.skillID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.Skills[ skillID=" + skillID + " ]";
    }
    
    public void AddProject(Project project){
        projectCollection.add(project);
    }
    
    public void AddUser(User u){
        userCollection.add(u);
    }
    
    
}
