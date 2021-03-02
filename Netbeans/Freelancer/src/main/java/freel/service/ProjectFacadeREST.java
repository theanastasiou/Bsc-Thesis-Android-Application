/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import freel.Project;
import freel.Skills;
import freel.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
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
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import static utilities.KeyHolder.key;

/**
 *
 * @author aarodoeht
 */
@Stateless
@Path("freel.project")
public class ProjectFacadeREST extends AbstractFacade<Project> {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ProjectFacadeREST() {
        super(Project.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(@HeaderParam("Authorization") String token,Project entity) {
         System.out.println("ok");
         System.out.println(entity.getPaymentType());
         Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token.trim()).getBody();
        User creator = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getUserIDOwner().getUserName()).
                getSingleResult();
        entity.setUserIDOwner(creator);
        System.out.println(claims.getSubject());
         Query qq= em.createQuery("SELECT MAX(u.projectID) FROM Project u");
        Integer maxid =(Integer) qq.getSingleResult();
        if(maxid== null)
            maxid=0;
        else 
            maxid++;
        
        System.out.println(maxid);
        entity.setProjectID(maxid);
        System.out.println(entity.getUserIDOwner().getUserID());  
        
        
        
        //em.createQuery("Select count(b) ....).setParameter().getResultList();
   
  /*  String queryString ="SELECT TypeOfUser FROM User where Project.userIDOwner = User.UserID ";
    TypedQuery<User> query = em.createQuery(queryString, User.class);
    query.setParameter("typeOfUser", typeOfUser);
    List result =  query.getResultList();*/
   
    Query q = em.createQuery("SELECT u.typeOfUser FROM User u where u.userID = :userID");
    q.setParameter("userID", entity.getUserIDOwner().getUserID());
    int typeofuser = (int) q.getSingleResult();
    System.out.println(typeofuser);
    if( typeofuser!=1){
//           System.out.println("Error : Type of user : 0 : Works ");
           return;
      }
      
      Set<Skills> projectskills = entity.getSkillsCollection();
      entity.setSkillsCollection(new HashSet<Skills>());
      System.out.println(projectskills + " IS SIZE");
      super.create(entity);
      em.flush();
//      return;
      for(Skills s:projectskills){
        try{
            Skills skill=(Skills) em.createNamedQuery("Skills.findByDescription").setParameter("description",s.getDescription()).getSingleResult();
            if(skill.getProjectCollection()==null)
                skill.setProjectCollection(new HashSet<Project>());
            System.out.println("SLLSLSLSLSLSLSLS");
            skill.AddProject(entity);
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
            s.setProjectCollection(new HashSet<Project>());
            s.AddProject(entity);
            entity.AddSkill(s);
//            Query qq1;
//            System.out.println("MAXID is "+maxid1);
//            s.setSkillID(maxid1);
            em.merge(s); //an iparxi apla to ananeoeni 
            em.merge(entity);
            em.flush(); 
        }
//            entity.AddSkill(s);
//            if()
//            s.AddProject(entity);
//            em.merge(s);
//            em.merge(entity);
//            em.flush();
      }
      

      
    

       //// "SELECT TypeOfUser FROM User where Project.userIDOwner = User.UserID ")
      
//        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Project entity) {
        System.out.println("EDIT!!!!!!!!!");
        Project p = (Project) em.createNamedQuery("Project.findByProjectID").setParameter("projectID", id).getSingleResult();
        User creator = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getUserIDOwner().getUserName()).
                getSingleResult();
        p.setUserIDOwner(creator);
        User creator2 = (User) em.createNamedQuery("User.findByUserName").
                setParameter("userName", entity.getUserIDWorker().getUserName()).
                getSingleResult();
        p.setUserIDWorker(creator2);
        System.out.println(creator2.getUserName()+ " is the worker");
        p.setAvailable(entity.getAvailable());
        Set<Skills> skillsCollection = new  HashSet<>();
        for(Skills s : entity.getSkillsCollection()){
            skillsCollection.add(s);
//            em.merge(s);
//            em.flush();
        }
        p.setSkillsCollection(skillsCollection);
        super.edit(p);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Project find(@PathParam("id") Integer id) {
        System.out.println("projectid " + super.find(id).getProjectID());
        System.out.println("projectid " + super.find(id).getUserIDOwner().getUserName());

        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> findAll() {
        return super.findAll();
    }

    @GET
    @Path("/find")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> findActive() {
        List<Project> list = findAll();
        List<Project> rlist = new ArrayList<>();
        for(Project p : list)
            if(p.getAvailable()==0)
                rlist.add(p);
        System.out.println("FINDALL "+rlist.size());
        return rlist;
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("runningprojectsforworker/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> ShowRunninProjectsW(@PathParam("userid") String userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.userIDWorker.userName = :userworker AND u.available =2 ");
        qq.setParameter("userworker", userid);
        List<Project> userchat = qq.getResultList();
        System.out.println("RUN "+userchat);
        if(userchat.isEmpty())
            return null;
        return userchat;
    }    
    
    @GET
    @Path("runningprojectsforemployer/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> ShowRunninProjectsE(@PathParam("userid") String userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.userIDOwner.userName = :userowner AND (u.available = 0 OR u.available = 2) ");
        qq.setParameter("userowner", userid);
        List<Project> userchat = qq.getResultList();
        System.out.println("RUN "+userchat);
        if(userchat.isEmpty())
            return null;
        return userchat;
    }    
    
     @GET
    @Path("searchprojects/{key}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> SearchProject(@PathParam("key") String key) {
       
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.title LIKE '%" + key + "%'");
        List<Project> projects  =  (List<Project>) qq.getResultList();
        return projects;
    }
    @GET
    @Path("pastprojectsforemployer/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> ShowPastProjectsE(@PathParam("userid")String userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.userIDOwner.userName = :userowner AND u.available = 1 ");
        qq.setParameter("userowner", userid);
        List<Project> userchat = qq.getResultList();
        System.out.println("PAST "+ userchat);
        if(userchat.isEmpty())
            return null;
        return userchat;
    }   
    
    @GET
    @Path("projectskills/{projectid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public  Collection<Skills> ShowProjectSkills(@PathParam("projectid") int projectid) {
       // System.out.println(projectid);
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.projectID = :projectID ");
        qq.setParameter("projectID", projectid);
        Project project = (Project) qq.getSingleResult();
        Collection<Skills> skills = (Collection<Skills>) project.getSkillsCollection();
        System.out.println(skills);
        if (!skills.isEmpty()){
            return null;
        }
        return skills;
    }
    
     @GET
    @Path("pastprojectsforworker/{userid}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> ShowPastProjectsW(@PathParam("userid") String userid) {
        System.out.println(userid);
        Query qq= em.createQuery("SELECT u FROM Project u WHERE u.userIDWorker.userName = :userIDWorker AND u.available = 1 ");
        qq.setParameter("userIDWorker", userid);
        List<Project> userchat = qq.getResultList();
        System.out.println(userchat);
        if(userchat.isEmpty())
            return null;
        return userchat;
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
    
    //try generic DataModel
    
    @GET //item similarity projects boolean
    @Path("/recommendCU/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> recCU(@PathParam("userid") int userid) throws TasteException{
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("fl");
        MySQLBooleanPrefJDBCDataModel dam = new MySQLBooleanPrefJDBCDataModel(dataSource,"Project","UserIDWorker","Category",null);
        ReloadFromJDBCDataModel dm = new ReloadFromJDBCDataModel(dam);
        ItemSimilarity similarity = new TanimotoCoefficientSimilarity(dm);
        int noRecs = 5;
        Recommender recommender =new GenericBooleanPrefItemBasedRecommender(dm, similarity);
        List<RecommendedItem> list = recommender.recommend(userid, noRecs);
        List<Project> res = new ArrayList<>();
        for(RecommendedItem ri : list){
            System.out.println(ri.getItemID());
            List<Project> temp = em.createQuery("Select p from Project p "
                    + "where p.available=0 and p.category=:c")
                    .setParameter("c", String.valueOf(ri.getItemID()))
                    .getResultList();
            res.addAll(temp.subList(0, 1));
            if (res.size()>=20)
                break;
        }
        return res;
    }

    @GET //user similarity projects boolean
    @Path("/recommendCUB/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> recCUB(@PathParam("userid") int userid) throws TasteException{
        int noRecs = 5;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("fl");
        MySQLBooleanPrefJDBCDataModel dam = new MySQLBooleanPrefJDBCDataModel(dataSource,"Project","UserIDWorker","Category",null);
        ReloadFromJDBCDataModel dm = new ReloadFromJDBCDataModel(dam);
        UserSimilarity similarity = new TanimotoCoefficientSimilarity(dm);
        UserNeighborhood neighbor = new NearestNUserNeighborhood (2, similarity, dm);
        Recommender recommender =new GenericBooleanPrefUserBasedRecommender(dm,neighbor, similarity);
        List<RecommendedItem> list = recommender.recommend(userid, noRecs);
        List<Project> res = new ArrayList<>();
        for(RecommendedItem ri : list){
            System.out.println(ri.getItemID());
            List<Project> temp = em.createQuery("Select p from Project p "
                    + "where p.available=0 and p.category=:c")
                    .setParameter("c", String.valueOf(ri.getItemID()))
                    .getResultList();
            res.addAll(temp.subList(0, 1));
            if (res.size()>=20)
                break;
        }
        return res;
    }
    
    /*@GET //item similarity project
    @Path("/recommendC")
//    @Produces(MediaType.APPLICATION_JSON)
    public void recC(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("fl");
        ItemBasedRecommender recommender = null;
        try {
            JDBCDataModel dm = new MySQLJDBCDataModel(dataSource,"Project","UserIDWorker","ProjectID","Category",null);
            ReloadFromJDBCDataModel dataModel = new ReloadFromJDBCDataModel(dm);
            ItemSimilarity similarity = new EuclideanDistanceSimilarity(dm);
            System.out.println(similarity.itemSimilarity(965,1021));
            System.out.println(dataModel.getNumItems());
            System.out.println(dataModel.getNumUsers());
//            AllSimilarItemsCandidateItemsStrategy candidateStrategy =
//                new AllSimilarItemsCandidateItemsStrategy(similarity);
            recommender = new GenericItemBasedRecommender(dataModel,
                similarity, candidateStrategy, candidateStrategy);
            Recommender cachingRecommender = new CachingRecommender(recommender);
            List<RecommendedItem> l = cachingRecommender.recommend(6, 10);
            System.out.println(l);
//            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
//            double score = evaluator.evaluate(recommender, null, dataModel, 0.7, 1.0);
//            System.out.println("RMSE: " + score);
        } catch (Exception e1) {
            e1.printStackTrace();
//            System.exit(-1);
        }
//        recommender.refresh(null);
        List<RecommendedItem> r = null;
        try {
            r = recommender.mostSimilarItems(965, 10);
        } catch (TasteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println(r);
    }

    @GET //item similarity reviews
    @Path("/recommendM2")
//    @Produces(MediaType.APPLICATION_JSON)
    public void recc2(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("fl");
        ItemBasedRecommender recommender = null;
        try {
            JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,"Review","Sender","Receiver","Rate",null);
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
//            AllSimilarItemsCandidateItemsStrategy candidateStrategy =
//                new AllSimilarItemsCandidateItemsStrategy(similarity);
            recommender = new GenericItemBasedRecommender(dataModel,
                similarity, candidateStrategy, candidateStrategy);
//            RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
//            double score = evaluator.evaluate(recommender, null, dataModel, 0.7, 1.0);
//            System.out.println("RMSE: " + score);
        } catch (Exception e1) {
            e1.printStackTrace();
//            System.exit(-1);
        }
        recommender.refresh(null);
        List<RecommendedItem> r = null;
        try {
            r = recommender.mostSimilarItems(23, 5);
        } catch (TasteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.println(r);
    }
    
    @GET //user similarity review
    @Path("/recommendM")
    @Produces(MediaType.APPLICATION_JSON)
    public void recommendedM() throws TasteException, TasteException, TasteException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("fl");
        JDBCDataModel dm = new MySQLJDBCDataModel(dataSource,"Review","Sender","Receiver","Rate",null);
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dm);
        System.out.println(similarity.userSimilarity(7, 23));
        UserNeighborhood neighbor = new NearestNUserNeighborhood(5,similarity,dm);
        System.out.println("b");
        Recommender recommender = new GenericUserBasedRecommender(dm, neighbor, similarity);
        List<RecommendedItem> list = recommender.recommend(23, 5);
        System.out.println("c " +list.size());
        for (RecommendedItem ri : list) {
            System.out.println("SDSDSDS");
            System.out.println(ri);
        }
    }*/
}
