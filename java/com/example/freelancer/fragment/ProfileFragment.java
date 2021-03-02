package com.example.freelancer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.activities.ListItemActivity1;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.ReviewAdapter;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Review;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    TextView  edEmail;
    TextView  edAbout;
    TextView  edSkills;
    TextView  edPhoneNumber;
    TextView  edUserName;
    TextView  edDateofbirth;
    float rt;
    TextView edRating;
    TextView tvname;
    TextView tvsurname;
    TextView  edCountry;
    int projectid;
    int projectidOr;
    RatingBar ratingBar;
    ImageView image;
    String jwt;
    ReviewAdapter adapter;
    Jwt<Header, Claims> untrusted;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        System.out.println(jwt);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        projectid = getArguments().getInt("ProjectID"); //worker
        System.out.println("ProjectID " + projectid);

        projectidOr = getArguments().getInt("ProjectOfficial"); //pid
        //Toast.makeText(getContext(),"Project " + String.valueOf(projectidOr),Toast.LENGTH_SHORT).show();
        final View v = inflater.inflate(R.layout.owner_profile, container, false);
        edCountry=v.findViewById(R.id.edCountry);
//        edCountry.setEnabled(false);
        ratingBar=v.findViewById(R.id.ratingBar3);
        tvname=v.findViewById(R.id.tvName);
       // ratingBar=v.findViewById(R.id.ratingBar);
        tvsurname=v.findViewById(R.id.tvSurname);
        edEmail=v.findViewById(R.id.edEmail);
//        edEmail.setEnabled(false);
        edAbout=v.findViewById(R.id.edAbout);
//        edAbout.setEnabled(false);
        edDateofbirth=v.findViewById(R.id.edDateOfBirth);
//        edDateofbirth.setEnabled(false);
        edPhoneNumber=v.findViewById(R.id.edPhoneNumber2);
//        edPhoneNumber.setEnabled(false);
        edSkills=v.findViewById(R.id.edSkills);
//        edSkills.setEnabled(false);
        edUserName=v.findViewById(R.id.tvUsername);
//        edUserName.setEnabled(false);
        image= v.findViewById(R.id.imageView4);
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
//        final Project[] myproject = new Project[1];
//        final Call<Project> project = restAPI.fetchProject(projectid);
//        project.enqueue(new Callback<Project>() {
//            @Override
//            public void onResponse(Call<Project> call, Response<Project> response) {
//                myproject[0] = response.body();
//                System.out.println("UUUU ");
//                System.out.println("USERNAME " + myproject[0].getDescription());
//
//            }
//
//            @Override
//            public void onFailure(Call<Project> call, Throwable t) {
//
//            }
//        });

//        System.out.println("Projectidd  "+ myproject[0].getProjectID());
        final RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
        Call<User> user = restAPII.fetchUser(projectid);
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, final Response<User> response) {
                final User myuser = response.body();
                System.out.println("UUUU ");
                System.out.println("USERNAME " + myuser.getUserName());
                System.out.println("name  " + myuser.getName());
                System.out.println("surname " + myuser.getSurname());
                System.out.println("country " + myuser.getCountry());
                System.out.println("phone " + myuser.getPhoneNumber());
                edUserName.setText("Username : "+myuser.getUserName());

                String skills="Skills: ";
                if(!myuser.getSkillsCollection().isEmpty()) {
                    for (Skills s : myuser.getSkillsCollection())
                        skills += s.getDescription() + ",";
                    skills = skills.substring(0, skills.length() - 1);
                }
                edSkills.setText(skills);

                tvname.setText("Name : "+myuser.getName());
                tvsurname.setText("Surname : "+myuser.getSurname());
                edEmail.setText("Email : "+myuser.getEmail());
                edAbout.setText("About : "+myuser.getAbout());
                Integer phonenumber = myuser.getPhoneNumber();
                Double avgrating = myuser.getAverageRating();
                System.out.println(avgrating.toString());
                System.out.println(phonenumber);

                double rat = (myuser.getAverageRating());
                float rt = (float)rat;
                System.out.println("RATING:" +rt);
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(rt);System.out.println(phonenumber);
                Date dateofbirth =  myuser.getDateOfBirth();
                Call<String> fPhoto = restAPI.getPhoto(myuser.getUserID());
                fPhoto.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body()==null) {
                            System.out.println("EMPTYY");
                            image.setImageResource(R.drawable.glyph28512);
                        }
                        else{
                            System.out.println("NOT EMPTYY");
                            byte[] encodeByte = Base64.decode(response.body(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            image.setImageBitmap(bitmap);
                        }

                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
                System.out.println(dateofbirth.toString());
               /* SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = null;
                String date=null;
                try {
                    formatter.setLenient(false);
                    date1 = (Date)formatter.parse(String.valueOf(dateofbirth));
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                edDateofbirth.setText("Date Of Birth : "+sdf.format(dateofbirth));edPhoneNumber.setText("Phonenumber : "+phonenumber.toString());
                edCountry.setText("Country : "+myuser.getCountry());
                Button sendmsg=getView().findViewById(R.id.btnUploadImage);
                sendmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent inte = new Intent(view.getContext(), ListItemActivity1.class);
                        inte.putExtra("receiver",response.body().getUserName());
                        startActivityForResult(inte, 0);
                    }
                });
                Button review=getView().findViewById(R.id.btnReview);
                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        // Setting Alert Dialog Title
                        alertDialogBuilder.setTitle("Write a Review!");
                        LayoutInflater inflater1=getLayoutInflater();
                        // Icon Of Alert Dialog
                        final View vi = inflater1.inflate(R.layout.reviewtoast,(ViewGroup)view.findViewById(R.id.custom_toast));
                        alertDialogBuilder.setView(vi);
                        // Setting Alert Dialog Message
                        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // Toast.makeText(getContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //
                                // finish();
                                EditText content = vi.findViewById(R.id.reviewcontent);
                                RatingBar rat = vi.findViewById(R.id.ratingBar);
                                Review newRev = new Review();
                                newRev.setContent(content.getText().toString());
                                newRev.setRate(Double.valueOf(rat.getRating()));
                                //Toast.makeText(getContext(),String.valueOf(rat.getRating()),Toast.LENGTH_SHORT).show();
                                newRev.setReceiver(myuser);
                                int i = jwt.lastIndexOf('.');
                                String withoutSignature = jwt.substring(0, i + 1);
                                untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
                                RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                                System.out.println(projectidOr+" is proj");
//                                newRev.getProjectProjectID().getUserIDWorker().setPhoto(null);
//                                newRev.getProjectProjectID().getUserIDOwner().setPhoto(null);
//                                newRev.getReceiver().setPhoto(null);
//                                newRev.getSender().setPhoto(null);
                                Call<Integer> rev = restAPI.createrev(jwt,untrusted.getBody().getSubject(),projectidOr,newRev);
                                rev.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if(response.body()==1)
                                            Toast.makeText(getActivity(), "Your Review has been sumbited.", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getActivity(), "You can't review this user yet.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Sorry! Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                });
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        final View view = getView();
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.rev);
        final List<Review> list = null;
        adapter = new ReviewAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        System.out.println("calling fun");
        RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
        Call<User> user = restAPII.fetchUser(projectid);
        user.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {

                             RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                             Call<List<Review>> call1 = restAPI.fetchrev(response.body().getUserName());
                             call1.enqueue(new Callback<List<Review>>() {
                                 @Override
                                 public void onResponse(Call<List<Review>> call, final Response<List<Review>> response) {
                                     ReviewAdapter adapter = (ReviewAdapter) listView.getAdapter();
                                     System.out.println("WWWWWWWWWWWWWWWWW");
                                     adapter.insert(response.body());
                                     System.out.println(adapter.getItemCount() + " is size");
                                     adapter.notifyDataSetChanged();

                                 }

                                 @Override
                                 public void onFailure(Call<List<Review>> call, Throwable t) {
                                     System.out.println(t.getMessage());
                                 }
                             });
                         }
                         @Override
                         public void onFailure(Call<User> call, Throwable t) {

                         }
                     });
        super.onResume();
    }

    @Override
    public void onClick(View view) {

    }
}
