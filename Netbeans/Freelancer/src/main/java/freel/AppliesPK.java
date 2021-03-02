/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aarodoeht
 */
@Embeddable
public class AppliesPK implements Serializable {
   
    @Basic(optional = false)
    @NotNull
    @Column(name = "User_UserID", insertable=false, updatable=false )
    private int userUserID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Project_ProjectID", insertable=false, updatable=false )
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userUserID;
        hash += (int) projectProjectID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppliesPK)) {
            return false;
        }
        AppliesPK other = (AppliesPK) object;
        if (this.userUserID != other.userUserID) {
            return false;
        }
        if (this.projectProjectID != other.projectProjectID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.AppliesPK[ userUserID=" + userUserID + ", projectProjectID=" + projectProjectID + " ]";
    }
    
}
