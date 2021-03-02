/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


/**
 *
 * @author aarodoeht
 */
@Entity
@Table(name = "Notifications")

@NamedQueries({
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n"),
    @NamedQuery(name = "Notifications.findByDate", query = "SELECT n FROM Notifications n WHERE n.date = :date"),
    @NamedQuery(name = "Notifications.findByNotificationID", query = "SELECT n FROM Notifications n WHERE n.notificationsPK.notificationID = :notificationID"),
    @NamedQuery(name = "Notifications.findByReceiver", query = "SELECT n FROM Notifications n WHERE n.notificationsPK.receiver = :receiver  ORDER BY n.date DESC")})
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotificationsPK notificationsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumns({
        @JoinColumn(name = "Applies_User_UserID", referencedColumnName = "User_UserID"),
        @JoinColumn(name = "Applies_Project_ProjectID", referencedColumnName = "Project_ProjectID")})
    @ManyToOne
    private Applies applies;
    @JoinColumn(name = "Chat_MessageID", referencedColumnName = "MessageID")
    @ManyToOne
    private Chat chatMessageID;
    @JoinColumn(name = "Review_ReviewID", referencedColumnName = "ReviewID")
    @ManyToOne
    private Review reviewReviewID;
    @JoinColumn(name = "Receiver", referencedColumnName = "UserID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @Column(name = "Seen")
    private boolean seen;

    public Notifications() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Notifications(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    public Notifications(NotificationsPK notificationsPK, Date date) {
        this.notificationsPK = notificationsPK;
        this.date = date;
    }

    public Notifications(int notificationID, int receiver) {
        this.notificationsPK = new NotificationsPK(notificationID, receiver);
    }

    public NotificationsPK getNotificationsPK() {
        return notificationsPK;
    }

    public void setNotificationsPK(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Applies getApplies() {
        return applies;
    }

    public void setApplies(Applies applies) {
        this.applies = applies;
    }

    public Chat getChatMessageID() {
        return chatMessageID;
    }

    public void setChatMessageID(Chat chatMessageID) {
        this.chatMessageID = chatMessageID;
    }

    public Review getReviewReviewID() {
        return reviewReviewID;
    }

    public void setReviewReviewID(Review reviewReviewID) {
        this.reviewReviewID = reviewReviewID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationsPK != null ? notificationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.notificationsPK == null && other.notificationsPK != null) || (this.notificationsPK != null && !this.notificationsPK.equals(other.notificationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.Notifications[ notificationsPK=" + notificationsPK + " ]";
    }
    
}
