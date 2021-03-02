package com.example.freelancer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.activities.MainActivity;
import com.example.freelancer.activities.MainActivity1;
import com.example.freelancer.activities.MainActivity2;
import com.example.freelancer.activities.PostProjectActivity;
import com.example.freelancer.activities.SignUpStep2;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

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
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostProject extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText Descr;
    EditText iTitle;
    RadioGroup iPayment;
    EditText iBudget;
    Spinner spinner2;
    String text;
    String iCoin;
    EditText iSkills;
    RadioButton rd;
    String iCategory;
    String jwt;

    private OnFragmentInteractionListener mListener;

    public PostProject() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        System.out.println("okeypost");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iCategory = getArguments().getString("Spinner");
        switch(iCategory){
            case "Website & IT":
                iCategory="0";
                break;
            case "Mobile":
                iCategory="1";
                break;
            case "Art & Design":
                iCategory="2";
                break;
            case "Data Entry":
                iCategory="3";
                break;
            case "Software Dev":
                iCategory="4";
                break;
            case "Writing":
                iCategory="5";
                break;
            case "Business":
                iCategory="6";
                break;
            case "Sales":
                iCategory="7";
                break;
            default:
                break;
        }
        System.out.println("SPinner " + iCategory);

        View v = inflater.inflate(R.layout.postproject1, container, false);
        String[] descriptionData = {"Start", "Finish"};
        StateProgressBar stateProgressBar = (StateProgressBar) v.findViewById(R.id.state);
        stateProgressBar.setStateDescriptionData(descriptionData);
        String[] values =
                {"Euro", "GDB", "INR", "AUD", "CAD", "SGD"};
        spinner2 = (Spinner) v.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter);


        Button secondViewBtn = (Button) v.findViewById(R.id.btnPostProject);

        secondViewBtn.setOnClickListener(this);

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Descr = getView().findViewById(R.id.etDescription);
        iTitle = getView().findViewById(R.id.edTitle);
        iSkills = getView().findViewById(R.id.etSkills);
        iBudget = getView().findViewById(R.id.etBudget);
        //iCoin =  spinner2.getSelectedItem().toString();
        //System.out.println("COIN:"+ iCoin);
        iPayment = (RadioGroup) getView().findViewById(R.id.radioGroup2);
        iSkills = getView().findViewById(R.id.etSkills);
        String iDescription = Descr.getText().toString();
        System.out.println(iDescription);

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
    public void onPause() {
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_card);
        Drawable newIcon = (Drawable)mi.getIcon();
        newIcon.mutate().setColorFilter(Color.parseColor("#F5F5F5"), PorterDuff.Mode.SRC_IN);
        mi.setIcon(newIcon);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#F5F5F5")), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
        super.onPause();
    }


    @Override
    public void onResume() {
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_card);
        System.out.println(mi.getTitle());
        Drawable newIcon = (Drawable)mi.getIcon();
        newIcon.mutate().setColorFilter(Color.parseColor("#D81B60"), PorterDuff.Mode.SRC_IN);
        mi.setIcon(newIcon);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        int selectedId = iPayment.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        rd = (RadioButton) getView().findViewById(selectedId);
        String paymenttype=null;
        if (rd.getText().equals("Fixed Price"))
            paymenttype = "Fixed Price";
        if (rd.getText().equals("Hourly"))
            paymenttype = "Hourly ";

        //Toast.makeText(getView().getContext(), rd.getText().toString(),Toast.LENGTH_SHORT).show();

        iCoin =  spinner2.getSelectedItem().toString();
        System.out.println("COIN" + iCoin);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String[] params = {currentDateandTime,iTitle.getText().toString(),paymenttype,Descr.getText().toString(),iCoin.toString(),iBudget.getText().toString(),iCategory,iSkills.getText().toString()};

        Project newProject = new Project();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            formatter.setLenient(false);
            date1 = (Date)formatter.parse(params[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        System.out.println("Date "+ date1);
        newProject.setDate(date1);
        newProject.setTitle(params[1]);
        newProject.setPaymentType(params[2]);
        System.out.println("Paymenttype " + newProject.getPaymentType());

        //Toast.makeText(getView().getContext(), params[2]+" lalalala ",Toast.LENGTH_SHORT).show();
        newProject.setDescription(params[3]);
        newProject.setCoin(params[4]);
        newProject.setBudget(Float.parseFloat(params[5]));
        newProject.setCategory(params[6]);
        newProject.setAvailable(0);
        newProject.setProjectID(0);
        User meUser = new User();
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        meUser.setUserName(untrusted.getBody().getSubject());
        newProject.setUserIDOwner(meUser);

        StringTokenizer multiTokenizer = new StringTokenizer(iSkills.getText().toString(), ",");

        Collection<Skills> projectskills=new ArrayList<>();
        int j=0;
        while (multiTokenizer.hasMoreTokens())
        {
            Skills newskill = new Skills();
            newskill.setSkillID(j++);
            newskill.setDescription(multiTokenizer.nextToken().trim());
            projectskills.add(newskill);

        }
        newProject.setSkillsCollection(projectskills);
//        newProject.getUserIDOwner().setPhoto(null);
//        newProject.getUserIDWorker().setPhoto(null);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<Void> call = (Call<Void>) restAPI.create(jwt,newProject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(),"Your Project is now Available",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                //Toast.makeText(getActivity(),throwable.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        String typeofuser = (String) untrusted.getBody().get("type");
        Intent ma =null;
        if (Integer.valueOf(typeofuser)==1)
            ma =new Intent(getContext(), MainActivity1.class);
        if (Integer.valueOf(typeofuser)==2)
            ma =new Intent(getContext(), MainActivity2.class);
        startActivity(ma);
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
