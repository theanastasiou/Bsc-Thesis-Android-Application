/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import freel.Project;
import freel.Skills;
import freel.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

/**
 *
 * @author aarodoeht
 */
@Stateless
@Path("freel.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @GET
    @Path("/checkUname/{uname}")
    public boolean checkUName(@PathParam("uname") String uname){
        List<User> user2= em.createNamedQuery("User.findByUserName").setParameter( "userName", uname).getResultList() ; 
        if(!user2.isEmpty()){
           System.out.println("ERror");
           return false;
        }
        return true;
    }
    
    @GET
    @Path("/checkEmail/{email}")
    public boolean checkEmail(@PathParam("email") String email){
        List<User> user1= em.createNamedQuery("User.findByEmail").setParameter( "email", email).getResultList() ; 
        if(!user1.isEmpty()){
           System.out.println("ERror");
           return false;
        }
        return true;
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public int create1(User entity) {
         System.out.println("S");
         System.out.println(entity.getDateOfBirth());
         
        Query qq= em.createQuery("SELECT MAX(u.userID) FROM User u");
        Integer maxid =(Integer) qq.getSingleResult();
        if(maxid== null)
            maxid=0;
        else 
            maxid++;
        System.out.println(maxid);
        entity.setUserID(maxid);
      List<User> user1= em.createNamedQuery("User.findByEmail").setParameter( "email", entity.getEmail()).getResultList() ; 
        if(!user1.isEmpty()){
           System.out.println("ERror");
           return 1;
        }
        List<User> user2= em.createNamedQuery("User.findByUserName").setParameter( "userName", entity.getUserName()).getResultList() ; 
        if(!user2.isEmpty()){
           System.out.println("ERror");
           return 2;
        }
        Set<Skills> projectskills = entity.getSkillsCollection();
        entity.setSkillsCollection(new HashSet<Skills>());
        super.create(entity);
        em.flush();
        for(Skills s:projectskills){
            try{
                Skills skill=(Skills) em.createNamedQuery("Skills.findByDescription").setParameter("description",s.getDescription()).getSingleResult();
                if(skill.getProjectCollection()==null)
                    skill.setProjectCollection(new HashSet<Project>());
                System.out.println("SLLSLSLSLSLSLSLS");
                skill.AddUser(entity);
                entity.AddSkill(skill);
    //            s=skill;
                em.merge(skill); //an iparxi apla to ananeoeni 
                em.merge(entity);
                em.flush(); //stelnei tis allages sti vasi 
            }
            catch(NoResultException e){
                Query qq1 = em.createQuery("SELECT MAX(u.skillID) FROM Skills u");
                Integer maxid1 =(Integer) qq1.getSingleResult();
                if(maxid1== null){
                    maxid1=0;
                }
                else{
                    System.out.println(maxid1);
                    maxid1++;
                }
                s.setSkillID(maxid1);
                s.setUserCollection(new HashSet<User>());
                s.AddUser(entity);
                entity.AddSkill(s);
    //            Query qq1;
    //            System.out.println("MAXID is "+maxid1);
    //            s.setSkillID(maxid1);
                em.merge(s); //an iparxi apla to ananeoeni 
                em.merge(entity);
                em.flush(); 
            }
        }
        return 0;
    }
    
    @POST
    @Path("upload/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void uploadImg(@PathParam("id") Integer id,String img,@HeaderParam("Authorization") String jwt) {
        User u = find(id);
        u.setPhoto(img.getBytes());
        em.merge(u);
    }
            
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, User entity,@HeaderParam("Authorization") String jwt) {
        User u = find(id);
        u.setAbout(entity.getAbout());
        u.setCountry(entity.getCountry());
        u.setEmail(entity.getEmail());
        u.setDateOfBirth(entity.getDateOfBirth());
        u.setPhoneNumber(entity.getPhoneNumber());
        System.out.println("EDIING Prof");
        if(u.getPhoto()!=null)
            u.setPhoto(find(id).getPhoto());
//        System.out.println("DOB "+entity.getDateOfBirth());
        Set<Skills> projectskills = entity.getSkillsCollection();
        u.setSkillsCollection(new HashSet<Skills>());
        
        super.edit(u);
        em.flush();
        for(Skills s:projectskills){
            try{
                Skills skill=(Skills) em.createNamedQuery("Skills.findByDescription").setParameter("description",s.getDescription()).getSingleResult();
                if(skill.getProjectCollection()==null)
                    skill.setProjectCollection(new HashSet<Project>());
                System.out.println("SLLSLSLSLSLSLSLS");
                skill.AddUser(u);
                u.AddSkill(skill);
    //            s=skill;
                em.merge(skill); //an iparxi apla to ananeoeni 
                em.merge(u);
                em.flush(); //stelnei tis allages sti vasi 
            }
            catch(NoResultException e){
                Query qq1 = em.createQuery("SELECT MAX(u.skillID) FROM Skills u");
                Integer maxid1 =(Integer) qq1.getSingleResult();
                if(maxid1== null){
                    maxid1=0;
                }
                else{
                    System.out.println(maxid1);
                    maxid1++;
                }
                s.setSkillID(maxid1);
                s.setUserCollection(new HashSet<User>());
                s.AddUser(u);
                u.AddSkill(s);
    //            Query qq1;
    //            System.out.println("MAXID is "+maxid1);
    //            s.setSkillID(maxid1);
                em.merge(s); //an iparxi apla to ananeoeni 
                em.merge(u);
                em.flush(); 
            }
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User find(@PathParam("id") Integer id) {
        User u = super.find(id);
        System.out.println(u.getUserID());
        return u;
  }
    
    @GET
    @Path("photo/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String findS(@PathParam("id") Integer id) {
        byte[] res = super.find(id).getPhoto();
        if(res == null)
            return null;
        System.out.println("RETRIEVING PHOTO");
        return new String(res);
    }
    
    @GET
    @Path("/findbyusername/{username}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User findbyUsername(@PathParam("username") String id) {
        System.out.println(id+" IS UNAME");
        Query q  =  em.createNamedQuery("User.findByUserName").setParameter("userName", id);
        User u = (User) q.getSingleResult();
        System.out.println("username " + u.getUserName());
        return u;
    }
    

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("userskills/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public  Collection<Skills> ShowUserSkills(@PathParam("userid") int userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM User u WHERE u.userID = :userID ");
        qq.setParameter("userID", userid);
        User user  = (User) qq.getSingleResult();
        Collection<Skills> skills = (List<Skills>) user.getSkillsCollection();
        System.out.println(skills);
         if(!skills.isEmpty())
            return null;
        return skills;
    }
    
    
  @GET
    @Path("searchuser/{key}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> SearchUser(@PathParam("key") String key) {
       
        //Query qq= em.createQuery("SELECT u FROM User u WHERE u.userName LIKE '%" + key + "%'");
        Query qq= em.createQuery("SELECT u FROM User u"
                +" WHERE (u.userName LIKE '%" + key + "%'"
                + "or u.name LIKE '%" + key + "%' "
                + "or u.surname LIKE '%" + key + "%')");

        List<User> users  =  (List<User>) qq.getResultList();
         if(!users.isEmpty())
            return null;
        return users;
    }
    
   
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("unsavePrj/{uname}")
    public void rmvProject(@HeaderParam("Authorization") String jwt,@PathParam("uname") String uname,Integer prjID){
        User u = this.findbyUsername(uname);
        Project p = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", prjID).getSingleResult();
        u.rmvProject(p);
        em.merge(u);
        em.flush();
    }
    
    @POST
    @Path("savePrj/{uname}")
    public int addProject(@HeaderParam("Authorization") String jwt,@PathParam("uname") String uname,Integer prjID){
        User u = this.findbyUsername(uname);
        Project p = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", prjID).getSingleResult();
        if(u.getProjectCollection().contains(p))
            return 0;
        u.AddProject(p);
        em.merge(u);
        em.flush();
        return 1;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    
}
