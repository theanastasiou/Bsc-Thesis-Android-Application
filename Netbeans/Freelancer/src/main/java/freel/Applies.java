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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author aarodoeht
 */
@Entity
@Table(name = "Applies")
@NamedQueries({
    @NamedQuery(name = "Applies.findAll", query = "SELECT a FROM Applies a"),
    @NamedQuery(name = "Applies.findByUserUserID", query = "SELECT a FROM Applies a WHERE a.appliesPK.userUserID = :userUserID"),
    @NamedQuery(name = "Applies.findByPK", query = "SELECT a FROM Applies a WHERE a.appliesPK.userUserID = :userUserID and a.appliesPK.projectProjectID = :projectProjectID"),
    @NamedQuery(name = "Applies.findByProjectProjectID", query = "SELECT a FROM Applies a WHERE a.appliesPK.projectProjectID = :projectProjectID"),
    @NamedQuery(name = "Applies.findByStatus", query = "SELECT a FROM Applies a WHERE a.status = :status")})
public class Applies implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AppliesPK appliesPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Status")
    private String status;    
    @JoinColumn(name = "Project_ProjectID",referencedColumnName="ProjectID" , updatable=false)
    @ManyToOne(optional = false)
    private Project project;
    @JoinColumn(name = "User_UserID",referencedColumnName="UserID" , updatable=false)
    @ManyToOne(optional = false)
    private User user;
    @OneToMany(mappedBy = "applies")
    @JsonbTransient
    private Set<Notifications> notificationsCollection= new HashSet<>();

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
        hash += (appliesPK != null ? appliesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Applies)) {
            return false;
        }
        Applies other = (Applies) object;
        if ((this.appliesPK == null && other.appliesPK != null) || (this.appliesPK != null && !this.appliesPK.equals(other.appliesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.Applies[ appliesPK=" + appliesPK + " ]";
    }
    
}
