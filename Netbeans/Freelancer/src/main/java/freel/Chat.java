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
import javax.persistence.ManyToOne;
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
@Table(name = "Chat")

@NamedQueries({
    @NamedQuery(name = "Chat.findAll", query = "SELECT c FROM Chat c"),
    @NamedQuery(name = "Chat.findByMessageID", query = "SELECT c FROM Chat c WHERE c.messageID = :messageID"),
    @NamedQuery(name = "Chat.findByContent", query = "SELECT c FROM Chat c WHERE c.content = :content"),
    @NamedQuery(name = "Chat.findByDate", query = "SELECT c FROM Chat c WHERE c.date = :date")})
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MessageID")
    private Integer messageID;
    @Size(max = 200)
    @Column(name = "Content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "User_Send", referencedColumnName = "UserID")
    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private User userSend;
    @JoinColumn(name = "User_Rec", referencedColumnName = "UserID")
    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private User userRec;
    @OneToMany(mappedBy = "chatMessageID")
    @JsonbTransient
    @JsonIgnore
    private Set<Notifications> notificationsCollection= new HashSet<>();
    @Column(name = "Seen")
    private boolean seen;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Chat() {
    }

    public Chat(Integer messageID) {
        this.messageID = messageID;
    }

    public Chat(Integer messageID, Date date,boolean s) {
        this.messageID = messageID;
        this.date = date;
        this.seen=s;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserSend() {
        return userSend;
    }

    public void setUserSend(User userSend) {
        this.userSend = userSend;
    }

    public User getUserRec() {
        return userRec;
    }

    public void setUserRec(User userRec) {
        this.userRec = userRec;
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
        hash += (messageID != null ? messageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chat)) {
            return false;
        }
        Chat other = (Chat) object;
        if ((this.messageID == null && other.messageID != null) || (this.messageID != null && !this.messageID.equals(other.messageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "freel.Chat[ messageID=" + messageID + " ]";
    }
    
}
