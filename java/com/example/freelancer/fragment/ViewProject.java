package com.example.freelancer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Applies;
import com.example.freelancer.rest.AppliesPK;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProject extends Fragment implements View.OnClickListener  {

    private OnFragmentInteractionListener mListener;
    String jwt;
    int projectid;
    TextView tvOwner;
    TextView tvDate;
    TextView tvDescription;
    TextView tvBudget;
    TextView tvCoin;
    TextView tvType;
    TextView tvTitle;
    TextView tvPaymentType;
    TextView tvSkills;
    int usrID = 0;

    public ViewProject() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        projectid = getArguments().getInt("ProjectID");
        System.out.println("ProjectID " + projectid);

        final View v = inflater.inflate(R.layout.project_view, container, false);

        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        final Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        final Project[] myproject = new Project[1];
        final Call<Project> project = restAPI.fetchProject(projectid);
        project.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                myproject[0] = response.body();
                myproject[0] = response.body();
                System.out.println("UUUU ");
                System.out.println("USERNAME " + myproject[0].getDescription());
                Float budget = myproject[0].getBudget();
                tvBudget.setText("Budget : " +budget.toString());
                tvCoin.setText("Coin : " + myproject[0].getCoin());
                tvDescription.setText("Description : " +myproject[0].getDescription());
                String Owner= myproject[0].getUserIDOwner().getUserName();
                usrID = myproject[0].getUserIDOwner().getUserID();
                tvOwner.setText("Posted By : " +Owner);
                tvPaymentType.setText("Payment Type : "+myproject[0].getPaymentType());
                tvTitle.setText("Title : " + myproject[0].getTitle());
                tvTitle.setTypeface(null, Typeface.BOLD);
                switch (myproject[0].getCategory()){
                    case "0":
                        tvType.setText("Category : "+"Website & IT");
                        break;
                    case "1":
                        tvType.setText("Category : "+"Mobile");
                        break;
                    case "2":
                        tvType.setText("Category : "+"Art & Design");
                        break;
                    case "3":
                        tvType.setText("Category : "+"Data Entry");
                        break;
                    case "4":
                        tvType.setText("Category : "+"Software Dev");
                        break;
                    case "5":
                        tvType.setText("Category : "+"Writing");
                        break;
                    case "6":
                        tvType.setText("Category : "+"Business");
                        break;
                    case "7":
                        tvType.setText("Category : "+"Sales");
                        break;
                    default:
                        break;
                }
                String skills="";
                for(Skills s:myproject[0].getSkillsCollection())
                    skills+=s.getDescription()+" ";
                tvSkills.setText("Skills : "+skills);
//                tvWorker.setText(myproject[0].getUserIDWorker().getUserID());
                Date dateofbirth =  myproject[0].getDate();
                System.out.println(dateofbirth.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tvDate.setText("Posted On:" + sdf.format(dateofbirth));
//                worker=myproject[0].getUserIDWorker().getUserID();
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {

            }
        });
        Button btnSave = (Button) v.findViewById(R.id.btnSaved);
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("CLICKED SAVE "+projectid);
                final RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
                Call<Integer> save = restAPII.savePrj(jwt,untrusted.getBody().getSubject(),projectid);
                save.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.body()==1)
                            Toast.makeText(getContext(),"Project saved",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getContext(),"Project already saved",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(getContext(),"Project could not be saved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btnApply = (Button) v.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
                final Applies newapp = new Applies();
                final AppliesPK newPK = new AppliesPK();
//                myproject[0].getUserIDOwner().setPhoto(null);
                newPK.setProjectProjectID(myproject[0].getProjectID());
                newapp.setProject(myproject[0]);
                Call<User> user = restAPII.findUsername(untrusted.getBody().getSubject());
                System.out.println("Username used "+ untrusted.getBody().getSubject()+ " for prjct "+myproject[0].getProjectID());
                user.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        System.out.println("user response "+response.body().getUserID());
//                        response.body().setPhoto(null);
                        newapp.setUser(response.body());
                        newPK.setUserUserID(response.body().getUserID());
                        newapp.setStatus("Running");
                        System.out.println("APPLIES PK IS "+newPK.getUserUserID());
                        newapp.setAppliesPK(newPK);
//                        newapp.getUser().setPhoto(null);
//                        newapp.getProject().getUserIDOwner().setPhoto(null);
//                        newapp.getProject().getUserIDWorker().setPhoto(null);
                        RestAPI restAPIII = RestClient.getStringClient().create(RestAPI.class);
                        Call<Integer> newapply = restAPIII.create(jwt,newapp);
                        newapply.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.body()==1)
                                    Toast.makeText(getActivity(),"Wrong type of user",Toast.LENGTH_SHORT).show();
                                else if(response.body()==2)
                                    Toast.makeText(getActivity(),"You have already applied to this project",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(),"Your application was submitted",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });


            }
        });
//        Button btnSaved = (Button) v.findViewById(R.id.btnSaved);
//        btnSaved.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Saved save = new Saved();
//                }
//        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tvBudget=getView().findViewById(R.id.tvBudget);
        tvCoin=getView().findViewById(R.id.tvCoin);
        tvType=getView().findViewById(R.id.tvCategory);
        tvDate=getView().findViewById(R.id.tvDate);
        tvDescription=getView().findViewById(R.id.tvDescription);
        tvPaymentType=getView().findViewById(R.id.tvPaymentType);
        tvOwner =getView().findViewById(R.id.tvOwner);
        tvTitle=getView().findViewById(R.id.tvTitle);
        tvSkills = getView().findViewById(R.id.tvSkills);
        tvOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                int text = projectid;
                System.out.println("Txt is "+usrID);
                args.putInt("ProjectID", usrID);
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

    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
