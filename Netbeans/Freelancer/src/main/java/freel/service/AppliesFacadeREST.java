/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import freel.Applies;
import freel.AppliesPK;
import freel.Notifications;
import freel.NotificationsPK;
import freel.Project;
import freel.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import static utilities.KeyHolder.key;

/**
 *
 * @author aarodoeht
 */
@Stateless
@Path("freel.applies")
public class AppliesFacadeREST extends AbstractFacade<Applies> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private AppliesPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;userUserID=userUserIDValue;projectProjectID=projectProjectIDValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        freel.AppliesPK key = new freel.AppliesPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> userUserID = map.get("userUserID");
        if (userUserID != null && !userUserID.isEmpty()) {
            System.out.println("FOUND");
            key.setUserUserID(new java.lang.Integer(userUserID.get(0)));
        }
        java.util.List<String> projectProjectID = map.get("projectProjectID");
        if (projectProjectID != null && !projectProjectID.isEmpty()) {
            key.setProjectProjectID(new java.lang.Integer(projectProjectID.get(0)));
        }
        return key;
    }

    public AppliesFacadeREST() {
        super(Applies.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public int create(@HeaderParam("Authorization") String token,Applies entity) {
        System.out.println("LETS CREATE");
        System.out.println("uname " + entity.getUser().getUserName());
        System.out.println("u id " + entity.getUser().getUserID());
        System.out.println("u id pk " + entity.getAppliesPK().getUserUserID());
        System.out.println("prj id " + entity.getProject().getProjectID());
        System.out.println("prj id pk " + entity.getAppliesPK().getProjectProjectID());
        Applies apply = new Applies();
        User u = em.find(User.class, entity.getUser().getUserID());
        apply.setUser(u);
        Project p = em.find(Project.class, entity.getProject().getProjectID());
        apply.setProject(p);
        apply.setAppliesPK(entity.getAppliesPK());
        apply.setStatus(entity.getStatus());
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token.trim()).getBody();
      
        System.out.println(claims.getSubject());
//        System.out.println(entity.getUser().getUserName());
        Query qq= em.createQuery("SELECT u.typeOfUser FROM User u where u.userName = :userName ");
        qq.setParameter("userName", claims.getSubject());
        int typeofuserr = (int) qq.getSingleResult();
        System.out.println(typeofuserr);
        if(typeofuserr == 1){
            System.out.println("Error : Cant Apply : typeofuser 1 : Hires ");
            return 1;
        }
        Applies a;
        try{
            a = (Applies) em.createNamedQuery("Applies.findByPK")
                .setParameter("userUserID", apply.getAppliesPK().getUserUserID())
                .setParameter("projectProjectID", apply.getAppliesPK().getProjectProjectID())
                .getSingleResult();
            return 2;
        }
        catch(NoResultException e){
            super.create(apply);
            Notifications n= new Notifications();
            NotificationsPK npk = new NotificationsPK();
            npk.setReceiver(apply.getProject().getUserIDOwner().getUserID());
            Query qq1 = em.createQuery("SELECT MAX(u.notificationsPK.notificationID) FROM Notifications u");
            Integer maxid =(Integer) qq1.getSingleResult();
            if(maxid== null)
                maxid=0;
            else 
                maxid++;
            npk.setNotificationID(maxid);
            n.setNotificationsPK(npk);
            n.setSeen(false);
            n.setApplies(apply);
            n.setDate(new Date());
            em.persist(n);
            em.flush();
            return 0;
        }
        
    }

    @PUT
    @Path("accept/{id}/{aID}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@HeaderParam("Authorization") String token,@PathParam("id") int project,@PathParam("aID") int ApplicantID) {
        System.out.println("RERERERERE");
        Applies a = new Applies();
        AppliesPK pk = new AppliesPK();
        pk.setProjectProjectID(project);
        pk.setUserUserID(ApplicantID);
        Applies aa=this.find(pk);
        aa.setStatus("Accepted");
        Project p = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", project).getSingleResult();
        p.setAvailable(2);
        User creator2 = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", aa.getUser().getUserName()).
                getSingleResult();
        p.setUserIDWorker(creator2);
//        User u = em.find(User.class,ApplicantID);
//        aa.setUser(u);
//        Project p = em.find(Project.class, project);
//        aa.setProject(p);
//        em.detach(u);
//        em.merge(aa);
//        em.merge(u);
//        em.flush();
        super.edit(aa);
    }

    @DELETE
    @Path("decline/{id}")
    public void remove(@HeaderParam("Authorization") String token,@PathParam("id") int project,int ApplicantID) {
        AppliesPK pk = new AppliesPK();
        pk.setProjectProjectID(project);
        pk.setUserUserID(ApplicantID);
        Applies a=this.find(pk);
        User u = em.find(User.class,ApplicantID);
        a.setUser(u);
        Project p = em.find(Project.class, project);
        a.setProject(p);
        super.remove(a);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Applies find(@PathParam("id") PathSegment id) {
        System.out.println(id.getMatrixParameters().get("userUserID"));
        freel.AppliesPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Applies> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Applies> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
