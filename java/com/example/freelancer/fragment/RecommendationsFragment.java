package com.example.freelancer.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.ProjectAdapter;
import com.example.freelancer.rest.Project;

import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RecommendationsFragment extends Fragment {

    private String jwt;
    Jwt<Header, Claims> untrusted;
    ProjectAdapter adapter;
    int userid;
    public RecommendationsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        setHasOptionsMenu(true);
        userid=getArguments().getInt("userid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=null;
        v = inflater.inflate(R.layout.fragment_rec, container, false);
        return v;
    }

    @Override
    public void onResume() {
        final View view = getView();
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.allProjects);
        final List<Project> list = null;
        adapter = new ProjectAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<Project>> call = restAPI.recProj(userid);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, final Response<List<Project>> response) {
                ProjectAdapter adapter = (ProjectAdapter) listView.getAdapter();
//                System.out.println("WWWWWWWWWWWWWWWWW");
                adapter.insert(response.body());
                System.out.println(adapter.getItemCount()+ " is size");
                adapter.setOnItemClickListener(new ProjectAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if(response.body()!=null) {
                            System.out.println("onItemClick position: " + response.body().get(position).getProjectID());
                            Fragment fragment = null;
                            String typeofuser = (String) untrusted.getBody().get("type");
                            System.out.println("TYPEOFUSER " + typeofuser);
                            if(Integer.valueOf(typeofuser)==2)
                                fragment = new ViewProject(); // next fragment for post a project
                            if(Integer.valueOf(typeofuser)==1)
                                fragment = new ViewProjectHire(); // next fragment for post a project
                            Bundle args = new Bundle();
                            int projectid = response.body().get(position).getProjectID();
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
            public void onFailure(Call<List<Project>> call, Throwable t) {

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

}
