/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freel.service;

import freel.Login;
import freel.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Stateless
@Path("freel.login")
public class LoginFacadeREST {

    @PersistenceContext(unitName = "com.mycompany_Freelancer_war_1.0-SNAPSHOTPU")
    private EntityManager em;


    
    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String login(final Login login) {
        System.out.println("WTF");
        System.out.println(login.getUsername());
        Login logged = this.login(login.getUsername(), login.getPassword());
        if (logged != null) {
            String token = issueToken(login.getUsername());
            System.out.println(token);
            return token;
        }
        else {
            System.out.println("KSSJSJSJSJ");
            return "not";
        }
    }
    
    private String issueToken(String username) {
            Key key = utilities.KeyHolder.key;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMillis = nowMillis + 300000L;
            Date exp = new Date(expMillis);
            Query q = em.createNamedQuery("User.findByUserName");
        q.setParameter("userName", username);
        User u = (User) q.getSingleResult();
//        q.setParameter("pass", password);
            String jws = Jwts.builder()
                        .setSubject(username)
                        .claim("type",String.valueOf(u.getTypeOfUser()))
                        .setIssuedAt(now)
                        .signWith(SignatureAlgorithm.HS512, key)
                        .setExpiration(exp)
                        .compact();
         
            return jws;
    }
    
    private Login login(String username, String password)
    {   
        System.out.println(username);
        System.out.println(password);
        Login login = null;
        System.out.println("Dd");
        Query q = em.createNamedQuery("User.findByUserName");
        q.setParameter("userName", username);
//        q.setParameter("pass", password);
        User logins;
        try {
            logins =  (User) q.getSingleResult();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        if (logins!=null)
        {
            System.out.println("Uname: "+password+"Pw: "+logins.getPassword());
            if(logins.getPassword().equals(password)){
                login = new Login();
                login.setPassword(logins.getPassword());
                login.setUsername(logins.getUserName());
            }
        }
        return login;    
    }
    

    
}
