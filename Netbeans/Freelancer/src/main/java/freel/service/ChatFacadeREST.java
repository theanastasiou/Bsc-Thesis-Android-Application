/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import freel.Applies;
import freel.Chat;
import freel.Notifications;
import freel.NotificationsPK;
import freel.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static utilities.KeyHolder.key;

/**
 *
 * @author aarodoeht
 */
@Stateless
@Path("freel.chat")
public class ChatFacadeREST extends AbstractFacade<Chat> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ChatFacadeREST() {
        super(Chat.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@HeaderParam("Authorization") String token, Chat entity){
       // Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token.trim()).getBody();
      
        System.out.println(claims.getSubject());
       
        System.out.println("KK");
        User creator = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getUserRec().getUserName()).
                getSingleResult();
        entity.setUserRec(creator);
        User creator2 = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getUserSend().getUserName()).
                getSingleResult();
        entity.setUserSend(creator2);
        Query q= em.createQuery("SELECT MAX(u.messageID) FROM Chat u");
        int maxid = (int) q.getSingleResult();
        maxid++;
        System.out.println(maxid);
        entity.setSeen(false);
        entity.setMessageID(maxid);
//        Query qq= em.createQuery("SELECT u.projectID FROM Project u where u.userIDOwner.userID = :userIDOwner ");
//        qq.setParameter("userIDOwner", entity.getUserRec().getUserID());
//        List<Applies> typeofuserr = qq.getResultList();
        Query qk = em.createQuery("SELECT a FROM Applies a,Project p where a.appliesPK.projectProjectID = p.projectID and a.appliesPK.userUserID = :user and p.userIDOwner = :owner");
        qk.setParameter("user", entity.getUserSend().getUserID());
        qk.setParameter("owner", entity.getUserRec());
        List<Applies> typeofuserr = qk.getResultList();
       //gia kathe stoixeio sto typeofuserr tha kanw to sigkekrimeno query me projectID to kathe 
       //an yparxei esto kai ena p na exei san apotelesma RUNNING/ACCEPTED tote mporei n stilei minima
        int size = typeofuserr.size();
        int counter=0;
        boolean canSend = false;
        for(Applies a:typeofuserr)
            if(a.getStatus().equals("Running")||a.getStatus().equals("Accepted")){
                canSend = true;
                break;
        }
        if(!canSend){
            System.out.println("O koloke");
            Query qk1 = em.createQuery("SELECT a FROM Applies a,Project p where a.appliesPK.projectProjectID = p.projectID and a.appliesPK.userUserID = :user and p.userIDOwner = :owner");
            qk1.setParameter("user", entity.getUserRec().getUserID());
            qk1.setParameter("owner", entity.getUserSend());
            List<Applies> typeofuse = qk1.getResultList();
            for(Applies a:typeofuse)
                if(a.getStatus().equals("Running")||a.getStatus().equals("Accepted")){
                    System.out.println("VRIKE");
                    canSend = true;
                    break;
                }
        }
        
//        for(int i=0;i<size;i++)
//        {
//            System.out.println(typeofuserr.get(i));
//            Query qqq= em.createQuery("SELECT u.status FROM Applies u where u.user.userID = :userID and u.project.projectID = :projectID");
//            qqq.setParameter("userID", entity.getUserSend().getUserID());
//            qqq.setParameter("projectID",typeofuserr.get(i));
//            List<String> typeofuser = qqq.getResultList();
//            System.out.println(typeofuser);
//            int siz = typeofuser.size();
//            for(int j=0;j<siz;j++)
//            {
//                System.out.println(typeofuser.get(j));
//                //(string1 == string2) it returns 0. 
//                if(("Running".equals(typeofuser.get(j))) ||("Accepted".equals(typeofuser.get(j)))){
//                    counter++;
//                }
//            }
//            System.out.println(counter);
//        }
//       
//        if(counter==0){
//           System.out.println("Error : Cant Send Message : counter = 0 ");
//           return;
//        }
       
      
/*Query q = em.createQuery("SELECT u.status FROM Applies u where u.appliesPK.userUserID = :userUserID and u.appliesPK.projectProjectID =: projectProjectID");
      q.setParameter("userUserID", entity.getUserSend().getUserID());
       q.setParameter("userUserIDOwner", entity.getUserRec().getUserID());
        String typeofuser = (String) q.getSingleResult();
       System.out.println(typeofuser);*/
     /* if( typeofuser!=1){
           System.out.println("Error : Type of user : 0 : Works ");
           return;
      }*/
        if(canSend){
            super.create(entity);
            Notifications n= new Notifications();
            NotificationsPK npk = new NotificationsPK();
            npk.setReceiver(entity.getUserRec().getUserID());
            Query qq1 = em.createQuery("SELECT MAX(u.notificationsPK.notificationID) FROM Notifications u");
            int maxid1 = (int) qq1.getSingleResult();
            maxid1++;
            npk.setNotificationID(maxid1);
            n.setNotificationsPK(npk);
            n.setSeen(false);
            n.setChatMessageID(entity);
            n.setDate(new Date());
            em.persist(n);
            em.flush();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Chat entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Chat find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("msgsToFrom/{uname}/{uname2}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Chat> findMsgs(@HeaderParam("Authorization") String token,@PathParam("uname") String uname,@PathParam("uname2") String uname2) {
        System.out.println("MPIKERERERERE");
//        User u =(User) em.createNamedQuery("User.findByUserName").setParameter("userName", uname).getSingleResult();
//        User u2=(User) em.createNamedQuery("User.findByUserName").setParameter("userName", uname2).getSingleResult();
        Query q = em.createQuery("Select m from Chat m where (m.userRec.userName = :u1 and m.userSend.userName = :u2) or (m.userRec.userName = :u2 and m.userSend.userName = :u1) ORDER BY m.date ASC");
        q.setParameter("u1", uname);
        q.setParameter("u2", uname2);
//        System.out.println(u.getChatCollection().size());
//        System.out.println(u.getChatCollection1().size());
//        List<Chat> messages = new ArrayList<>();
//        for(Chat c : u.getChatCollection1())
//            if(c.getUserSend().equals(u2))
//                messages.add(c);
//        for(Chat c : u.getChatCollection())
//            if(c.getUserRec().equals(u2))
//                messages.add(c);
//        return messages;
        List<Chat> msgs=q.getResultList();
        for(Chat c:msgs){
            System.out.println(c.isSeen());
            if(c.getUserRec().getUserName().equals(uname)&&!c.isSeen()){
                c.setSeen(true);
                em.merge(c);
            }
        }
        em.flush();
        return msgs;
    }
    
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Chat> findAll() {
        return super.findAll();
    }

    @GET
    @Path("sendersTo/{username}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<String> findSndrsTo(@HeaderParam("Authorization") String token, @PathParam("username") String username) {
        User receiver = (User) em.createNamedQuery("User.findByUserName").setParameter("userName", username).getSingleResult();
        Collection<Chat> msgs = receiver.getChatCollection1();
        Collection<Chat> msgs2 = receiver.getChatCollection();
        System.out.println(msgs.size()+" EDWDWW "+username);
        List<String> senders = new ArrayList<>();
        for(Chat c:msgs)
            if(!senders.contains(c.getUserSend().getUserName()))
                senders.add(c.getUserSend().getUserName());
        for(Chat c:msgs2)
            if(!senders.contains(c.getUserRec().getUserName()))
                senders.add(c.getUserRec().getUserName());
        return senders;
    }   
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Chat> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("messegesperuser/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Chat> ShowMessages(@PathParam("userid") int userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM Chat u WHERE u.userSend.userID = :usersend");
        qq.setParameter("usersend", userid);
        List<Chat> userchat = qq.getResultList();
        System.out.println(userchat);
        return userchat;
    }
    
    @GET
    @Path("count/{uname}")
    @Produces(MediaType.TEXT_PLAIN)    
    public long count(@HeaderParam("Authorization") String token, @PathParam("uname") String uname) {
        Query q = em.createQuery("Select count(m) from Chat m where m.userRec.userName = :u1 and m.seen = false");
        q.setParameter("u1", uname);
        long num = (long) q.getSingleResult();
        System.out.println(num+" is num");
        return num;
    }

    
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
