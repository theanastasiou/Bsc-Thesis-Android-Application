/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import freel.Notifications;
import freel.NotificationsPK;
import freel.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
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
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author aarodoeht
 */
@Stateless
@Path("freel.notifications")
public class NotificationsFacadeREST extends AbstractFacade<Notifications> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private NotificationsPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;notificationID=notificationIDValue;receiver=receiverValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        freel.NotificationsPK key = new freel.NotificationsPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> notificationID = map.get("notificationID");
        if (notificationID != null && !notificationID.isEmpty()) {
            key.setNotificationID(new java.lang.Integer(notificationID.get(0)));
        }
        java.util.List<String> receiver = map.get("receiver");
        if (receiver != null && !receiver.isEmpty()) {
            key.setReceiver(new java.lang.Integer(receiver.get(0)));
        }
        return key;
    }

    public NotificationsFacadeREST() {
        super(Notifications.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Notifications entity) {
         Query qq= em.createQuery("SELECT MAX(u.notificationsPK) FROM Notifications u");
        int maxid = (int) qq.getSingleResult();
        maxid++;
        System.out.println(maxid);
        //entity.setNotificationsPK(maxid);
      
        super.create(entity);
    }

    @PUT
    @Path("mark/{choice}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void mark(@HeaderParam("Authorization") String token,List<Notifications> notifs,@PathParam("choice") int choice){
        User creator = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", notifs.get(0).getUser().getUserName()).
                getSingleResult();
        if(choice==0)
            for(Notifications n : notifs){
                n.setSeen(true);
                n.setUser(creator);
                em.merge(n);
            }
        else
            for(Notifications n : notifs){
                n.setUser(creator);
                if (!em.contains(n)) 
                    n = em.merge(n);
                em.remove(n);
            }
        em.flush();
    }
    
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Notifications entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        freel.NotificationsPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Notifications find(@PathParam("id") PathSegment id) {
        freel.NotificationsPK key = getPrimaryKey(id);
        return super.find(key);
    }
    
    @GET
    @Path("basedonuser/{userName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Notifications> findbasedonuser(@PathParam("userName") String username) {
        System.out.println("NAIIII "+username);
        Query q = em.createNamedQuery("Notifications.findByReceiver");
        User u = (User) em.createNamedQuery("User.findByUserName").setParameter("userName", username).getSingleResult();        
        q.setParameter("receiver", u.getUserID());
        List<Notifications> usernot = q.getResultList();
        System.out.println("SIZE IS "+usernot.size());
        if(usernot.isEmpty())
            return null;
        return usernot;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Notifications> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Notifications> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
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
