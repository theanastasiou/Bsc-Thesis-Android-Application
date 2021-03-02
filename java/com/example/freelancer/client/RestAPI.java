package com.example.freelancer.client;

import com.example.freelancer.rest.Applies;
import com.example.freelancer.rest.Chat;
import com.example.freelancer.rest.Login;
import com.example.freelancer.rest.Notifications;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Review;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestAPI {

    /*@GET("user")
    @Headers("Accept: application/json")
    Call<List<User>> getAllUsers();

    @GET("user/{userId}")
    @Headers("Accept: application/json")
    Call<User> getUser(@Path("userId") int userId);

    @POST("images")
    @Multipart
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );*/

   /*POST("login/login")
    Call<Login> login(@Body Login login);*/

   /* @GET("login/{username}/{password}")
    Call<Login> login(@Body Login login);
*/

   @GET("freel.review/basedonuser/{username}")
   Call<List<Review>> fetchrev (@Path("username") String username);

   @POST("freel.review/{sender}/{projectid}")
   Call<Integer> createrev(@Header ("Authorization") String token,@Path("sender") String username, @Path("projectid")Integer projectid, @Body Review rev);

   @GET("freel.chat/count/{uname}")
   Call<Long> unread(@Header ("Authorization") String token,@Path("uname") String uname );

   @PUT("freel.notifications/mark/{choice}")
   Call<Void> markOrClear(@Header ("Authorization") String token,@Body List<Notifications> notifs,@Path("choice") int choice);

   @PUT("freel.applies/accept/{id}/{aID}")
   Call<Void> accept(@Header ("Authorization") String token, @Path("id") Integer id,@Path("aID") Integer user);

   @DELETE("freel.applies/decline/{id}")
   Call<Void> decline(@Header ("Authorization") String token, @Path("id") Integer id,@Body Integer user);

   @GET("freel.chat/msgsToFrom/{uname}/{uname2}")
   Call<List<Chat>> findMessages(@Header ("Authorization") String token, @Path("uname") String usr,@Path("uname2") String usr2);

   @POST("freel.chat")
   Call<Void> sendMsg(@Header ("Authorization") String token,@Body Chat msg);

   @GET("freel.chat/sendersTo/{username}")
   Call<List<String>> findSenders(@Header ("Authorization") String token, @Path("username") String usr);

   @GET("freel.project/find")
   Call<List<Project>> findProjects();

   @GET("freel.project/runningprojectsforemployer/{userid}")
   Call<List<Project>> findRunningProjectsE(@Path("userid") String userid);

   @GET("freel.project/pastprojectsforemployer/{userid}")
   Call<List<Project>> findDoneProjectsE(@Path("userid") String userid);

   @GET("freel.project/runningprojectsforworker/{userid}")
   Call<List<Project>> findRunningProjectsW(@Path("userid") String userid);

   @GET("freel.project/pastprojectsforworker/{userid}")
   Call<List<Project>> findDoneProjectsW(@Path("userid") String userid);

   @GET("freel.user/{id}")
   Call<User> fetchUser(@Path("id") Integer usr);

   @GET("freel.notifications/basedonuser/{userName}")
   Call<List<Notifications>> fetchnot(@Path("userName") String usr);

   @GET("freel.project/{id}")
   Call<Project> fetchProject(@Path("id") Integer id);

   @GET("freel.user/findbyusername/{username}")
   Call<User> findUsername(@Path("username") String usr);

   @GET("freel.user/checkUname/{uname}")
   Call<Boolean> checkUname(@Path("uname") String uname);

   @GET("freel.user/checkEmail/{email}")
   Call<Boolean> checkEmail(@Path("email") String email);

   @POST("freel.project")
   Call<Void> create(@Header("Authorization") String token, @Body Project newProject);

   @POST("freel.applies")
   Call<Integer> create(@Header("Authorization") String token, @Body Applies newApply);

   @POST("freel.user/savePrj/{uname}")
   Call<Integer> savePrj(@Header("Authorization") String token, @Path("uname") String usr,@Body Integer prj);

   @POST("freel.user/unsavePrj/{uname}")
   Call<Void> unsavePrj(@Header("Authorization") String token, @Path("uname") String usr,@Body Integer prj);

   @POST("freel.user")
   Call<Integer> create1(@Body User newuser);

   @PUT("freel.project/{id}")
   Call<Integer> update(@Path("id") Integer id,@Body Project newuser );

   @POST("freel.user/upload/{id}")
   Call<Void> upload(@Path("id") Integer id,@Body String img,@Header("Authorization") String jwt);

   @PUT("freel.user/{id}")
   Call<Integer> update(@Path("id") Integer id,@Body User newuser,@Header("Authorization") String jwt);

   @GET("freel.user/photo/{id}")
   Call<String> getPhoto(@Path("id") Integer id);

   @POST("freel.login/login")
   Call<String> login(@Body Login login);

   @GET("freel.project/recommendCUB/{userid}")
   Call<List<Project>> recProj(@Path("userid") Integer userid);

}