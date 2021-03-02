package com.example.freelancer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.ProjectAdapter;
import com.example.freelancer.helper.ProjectAdapterS;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.User;

import java.util.ArrayList;
import java.util.List;

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
public class Saved extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProjectAdapterS adapter;
    String jwt;
    Jwt<Header, Claims> untrusted;

    public Saved() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=null;
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        v = inflater.inflate(R.layout.fragment_saved, container, false);

        return v;
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

    public void resume(){
        onResume();
    }

    @Override
    public void onResume() {
        final View view = getView();
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_card);
        System.out.println(mi.getTitle());
        Drawable newIcon = (Drawable)mi.getIcon();
        newIcon.mutate().setColorFilter(Color.parseColor("#D81B60"), PorterDuff.Mode.SRC_IN);
        mi.setIcon(newIcon);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.allProjects);
        final List<Project> list = null;
        adapter = new ProjectAdapterS(view.getContext(), list,jwt,this);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
//                DividerItemDecoration.VERTICAL);
//        listView.addItemDecoration(mDividerItemDecoration);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<User> call = restAPI.findUsername(untrusted.getBody().getSubject());
        final List<Project>[] savedProject = new List[1];
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, final Response<User> response) {
                savedProject[0] = (List<Project>) response.body().getProjectCollection();
                ProjectAdapterS adapter = (ProjectAdapterS) listView.getAdapter();
                System.out.println("WWWWWWWWWWWWWWWWW");
                adapter.insert(savedProject[0]);
                System.out.println(adapter.getItemCount()+ " is size");
                adapter.setOnItemClickListener(new ProjectAdapterS.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if(response.body()!=null) {
                            System.out.println("onItemClick position: " + savedProject[0].get(position).getProjectID());
                            Fragment fragment = null;
                            String typeofuser = (String) untrusted.getBody().get("type");
                            System.out.println("TYPEOFUSER " + typeofuser);
                            if(Integer.valueOf(typeofuser)==2)
                                fragment = new ViewProject(); // next fragment for post a project
                            if(Integer.valueOf(typeofuser)==1)
                                fragment = new ViewProjectHire(); // next fragment for post a project
                            Bundle args = new Bundle();
                            int projectid = savedProject[0].get(position).getProjectID();
                            System.out.println(projectid);
                            args.putInt("ProjectID", projectid);
                            fragment.setArguments(args);
                            replaceFragment(fragment);
                        }
                    }

                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        super.onResume();
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
