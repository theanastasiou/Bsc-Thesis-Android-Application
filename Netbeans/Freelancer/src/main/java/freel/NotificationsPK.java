/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aarodoeht
 */
@Embeddable
public class NotificationsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "NotificationID")
    private int notificationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Receiver")
    private int receiver;

    public NotificationsPK() {
    }

    public NotificationsPK(int notificationID, int receiver) {
        this.notificationID = notificationID;
        this.receiver = receiver;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) notificationID;
        hash += (int) receiver;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationsPK)) {
            return false;
        }
        NotificationsPK other = (NotificationsPK) object;
        if (this.notificationID != other.notificationID) {
            return false;
        }
        if (this.receiver != other.receiver) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.NotificationsPK[ notificationID=" + notificationID + ", receiver=" + receiver + " ]";
    }
    
}
