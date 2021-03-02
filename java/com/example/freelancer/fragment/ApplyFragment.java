package com.example.freelancer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Applies;
import com.example.freelancer.rest.Notifications;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.User;

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

public class ApplyFragment extends Fragment implements View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String jwt;
    int UserID;
    int projectid;
    TextView tvOwner;
    TextView tvDescription;
    TextView tvTitle;
    Jwt<Header, Claims> untrusted;
    int usrID = 0;

    private MoviesFragment.OnFragmentInteractionListener mListener;

    public ApplyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        projectid = getArguments().getInt("ProjectID");
        System.out.println("ProjectID " + projectid);
        UserID = getArguments().getInt("UserID");
        System.out.println("UserID " + UserID);
        View v = inflater.inflate(R.layout.apply_layout, container, false);

        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        final Jwt<Header, Claims>[] untrusted = new Jwt[]{Jwts.parser().parseClaimsJwt(withoutSignature)};
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        final Project[] myproject = new Project[1];
        final Call<Project> project = restAPI.fetchProject(projectid);
        project.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                myproject[0] = response.body();
                System.out.println("UUUU ");
                System.out.println("USERNAME " + myproject[0].getDescription());
                tvDescription.setText(myproject[0].getDescription());
                tvTitle.setText(myproject[0].getTitle());


            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {

            }
        });
        final RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
        final User[] myuser = new User[1];
        final Call<User> user = restAPII.fetchUser(UserID);
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                myuser[0] = response.body();
                String Owner= myuser[0].getUserName();
                tvOwner.setText(Owner);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        Button secondViewBtn = (Button) v.findViewById(R.id.btnAccept);
        secondViewBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                 int i = jwt.lastIndexOf('.');
                 String withoutSignature = jwt.substring(0, i + 1);
                 untrusted[0] = Jwts.parser().parseClaimsJwt(withoutSignature);
                 Call<Void> call = restAPI.accept(jwt, projectid, UserID);
                 call.enqueue(new Callback<Void>() {
                     @Override
                     public void onResponse(Call<Void> call, final Response<Void> response) {
//                         RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
//                         int i = jwt.lastIndexOf('.');
//                         String withoutSignature = jwt.substring(0, i + 1);
//                         untrusted[0] = Jwts.parser().parseClaimsJwt(withoutSignature);
//                         Call<Project> call1 = restAPII.fetchProject(projectid);
//                         final Project[] newP = {new Project()};
//                         call1.enqueue(new Callback<Project>() {
//                             @Override
//                             public void onResponse(Call<Project> call, Response<Project> response) {
//                                 response.body().setAvailable(2);
//                                 RestAPI restA = RestClient.getStringClient().create(RestAPI.class);
//                                 Call<User> cal1 = restA.fetchUser(UserID);
//                                 newP[0] =response.body();
//                                 final User[] newU = new User[1];
//                                 cal1.enqueue(new Callback<User>() {
//                                  @Override
//                                  public void onResponse(Call<User> call, Response<User> response) {
//                                      newU[0] = response.body();
////                                      newU[0].setPhoto(null);
//                                      System.out.println(newU[0].getName());
//                                      System.out.println("helloo");
//                                      System.out.println(newU[0].getUserName());
//
//                                      newP[0].setUserIDWorker(newU[0]);
////                                      newP[0].getUserIDOwner().setPhoto(null);
//                                      System.out.println("Description"+newP[0].getUserIDWorker().getUserName());
//                                      RestAPI restAP = RestClient.getStringClient().create(RestAPI.class);
//                                      Call<Integer> call1 = restAP.update(newP[0].getProjectID(), newP[0]);
//                                      call1.enqueue(new Callback<Integer>() {
//                                          @Override
//                                          public void onResponse(Call<Integer> call, Response<Integer> response) {
//                                              Toast.makeText(getActivity(), "Your Project is now Running", Toast.LENGTH_SHORT).show();
//                                          }
//
//                                          @Override
//                                          public void onFailure(Call<Integer> call, Throwable t) {
//
//                                          }
//                                      });
//                                  }
//
//                                  @Override
//                                  public void onFailure(Call<User> call, Throwable t) {
//                                      System.out.println("helloo11");
//
//                                  }
//                                });
//
//                             }
//
//                             @Override
//                             public void onFailure (Call <Project> call, Throwable t){
//
//                             }
//
//
//                         });
                     }

                     @Override
                     public void onFailure(Call<Void> call, Throwable t) {
                         Toast.makeText(getActivity(), "Something went wrong try again later.", Toast.LENGTH_SHORT).show();

                     }
                 });
             }
         });

            /*Button secondViewBtn1 = (Button) v.findViewById(R.id.btnDecline);
            secondViewBtn1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                int i = jwt.lastIndexOf('.');
                String withoutSignature = jwt.substring(0, i + 1);
                untrusted[0] = Jwts.parser().parseClaimsJwt(withoutSignature);
                Call<Void> call = restAPI.decline(jwt, projectid, UserID);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getActivity(), "You declined the Application", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong try again later.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            });*/
            return v;

    }

    @Override
    public void onClick(View view) {
    }



    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        tvDescription=getView().findViewById(R.id.tvDescription);
        tvOwner =getView().findViewById(R.id.tvOwner);
        tvTitle=getView().findViewById(R.id.tvTitle);
        tvOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                int text = UserID;
                args.putInt("ProjectID", UserID);
                fragment.setArguments(args);
                replaceFragment(fragment);
            }
        });

    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




}
