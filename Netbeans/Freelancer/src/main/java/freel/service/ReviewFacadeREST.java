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
import freel.Project;
import freel.Review;
import freel.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
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
@Path("freel.review")
public class ReviewFacadeREST extends AbstractFacade<Review> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ReviewFacadeREST() {
        super(Review.class);
    }

    @POST
    @Path("/{sender}/{projectid}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public int create(@HeaderParam("Authorization") String token,@PathParam("sender") String username,@PathParam("projectid") Integer projectid, Review entity) {
       System.out.println("Review");
       Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token.trim()).getBody();
      
        System.out.println(claims.getSubject());
        Query q= em.createQuery("SELECT MAX(u.reviewID) FROM Review u");
        Integer maxid =(Integer) q.getSingleResult();
        if(maxid== null)
            maxid=0;
        else 
            maxid++;
        System.out.println("Maxid" + maxid);
        entity.setReviewID(maxid); 
        User sender = (User) em.createNamedQuery("User.findByUserName").setParameter("userName",username).getSingleResult();
        Project prj = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", projectid).getSingleResult();
        entity.setProjectProjectID(prj);
        entity.setSender(sender);
        System.out.println("Available"+ prj.getAvailable());
//        Query qq= em.createQuery("SELECT u.projectID FROM Project u where u.userIDOwner.userID = :userIDOwner ");
//        qq.setParameter("userIDOwner", entity.getReceiver().getUserID());
//        List<Applies> typeofuserr = qq.getResultList();
//      
//       //gia kathe stoixeio sto typeofuserr tha kanw to sigkekrimeno query me projectID to kathe 
//       //an yparxei esto kai ena p na exei san apotelesma RUNNING/ACCEPTED tote mporei n stilei minima
//        int size = typeofuserr.size();
//        int counter=0;
//        for(int i=0;i<size;i++)
//        {
//        Query qqq= em.createQuery("SELECT u.status FROM Applies u where u.user.userID = :userID and u.project.projectID = :projectID");
//        qqq.setParameter("userID", entity.getSender().getUserID());
//        qqq.setParameter("projectID",typeofuserr.get(i));
//        List<String> typeofuser = qqq.getResultList();
//        System.out.println(typeofuser);
//            int siz = typeofuser.size();
//
//            for(int j=0;j<siz;j++)
//            {
//                System.out.println(typeofuser.get(j));
//                //(string1 == string2) it returns 0. 
//                if("Accepted".equals(typeofuser.get(j))){
//                    counter++;
//                }
//            }
//            System.out.println(counter);
//               
//        }
//      
       //gia kathe stoixeio sto typeofuserr tha kanw to sigkekrimeno query me projectID to kathe 
       //an yparxei esto kai ena p na exei san apotelesma RUNNING/ACCEPTED tote mporei n stilei minima
//        int size = typeofuserr.size();
//        int counter=0;
//        boolean canSend = false;
//        for(Applies a:typeofuserr)
//            if(a.getStatus().equals("Running")||a.getStatus().equals("Accepted")){
//                canSend = true;
//                break;
//        }
//        
//        if(counter==0){
//            System.out.println("Error : Cant Add Review : counter = 0 ");
//            return;
//        }
//        

        System.out.println("ProjectID"+ prj.getProjectID());
        System.out.println("ProjectOwner"+ prj.getUserIDOwner().getUserName());
        System.out.println("ProjectOwner"+ prj.getUserIDWorker().getUserName());
        Query query = em.createQuery("Select count(r) from Review r where r.projectProjectID=:pid and r.receiver= :rec and r.sender= :sender");
        query.setParameter("pid", prj);
        query.setParameter("rec", entity.getReceiver());
        query.setParameter("sender", sender);
        long resq = query.getFirstResult();
        System.out.println("resq is "+resq);
        if(resq!=0)
            return 0;
        Project p = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", entity.getProjectProjectID().getProjectID()).getSingleResult();
        entity.setProjectProjectID(p);
        User creator2 = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getReceiver().getUserName()).
                getSingleResult();
        entity.setReceiver(creator2);
        User creator = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getSender().getUserName()).
                getSingleResult();
        entity.setSender(creator);
        if((prj.getAvailable()==1)&&(prj.getUserIDOwner().getUserName().toLowerCase().equals(username.toLowerCase())||prj.getUserIDWorker().getUserName().toLowerCase().equals(username.toLowerCase()))){
            super.create(entity);
            double rating = (double) em.createQuery("Select avg(u.rate) "
                    + "from Review u where u.receiver = :uname")
                    .setParameter("uname",entity.getReceiver()).getSingleResult();
            entity.getReceiver().setAverageRating(rating);
            em.merge(entity.getReceiver());
            Notifications n= new Notifications();
            NotificationsPK npk = new NotificationsPK();
            npk.setReceiver(entity.getReceiver().getUserID());
            Query qq1 = em.createQuery("SELECT MAX(u.notificationsPK.notificationID) FROM Notifications u");
            int maxid1 = (int) qq1.getSingleResult();
            maxid1++;
            npk.setNotificationID(maxid1);
            n.setSeen(false);
            n.setNotificationsPK(npk);
            n.setReviewReviewID(entity);
            n.setDate(new Date());
            em.persist(n);
            em.flush();
            return 1;
        }
        return 0;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Review entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("basedonuser/{userName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Review> ShowReviews(@PathParam("userName") String username) {
        Query qq= em.createQuery("SELECT u FROM Review u WHERE u.receiver.userName = :userrec");
        qq.setParameter("userrec", username);
        List<Review> userchat = qq.getResultList();
        System.out.println(userchat);
        if(userchat.isEmpty())
            return null;
        return userchat;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Review find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Review> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Review> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
