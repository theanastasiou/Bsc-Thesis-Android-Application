package com.example.freelancer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.ProjectAdapter;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.User;

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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProjectAdapter adapter;
    String jwt;
    Jwt<Header, Claims> untrusted;
    private OnFragmentInteractionListener mListener;
    TextView tvUsername;
    Button btnRec;

        public HomeFragment() {
            // Required empty public constructor
        }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        v = inflater.inflate(R.layout.fragment_home, container, false);
        tvUsername=v.findViewById(R.id.etUsername);
        btnRec = v.findViewById(R.id.btnRecommended);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestAPI rapi =  RestClient.getStringClient().create(RestAPI.class);
                Call<User> call = rapi.findUsername(untrusted.getBody().getSubject());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Bundle args = new Bundle();
                        args.putInt("userid", response.body().getUserID());
                        Fragment fragment = new RecommendationsFragment();
                        fragment.setArguments(args);
                        replaceFragment(fragment);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);

//        SearchView sv=(SearchView) MenuItem.getActionView(search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                System.out.println(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        final View view = getView();

        tvUsername.setText("Welcome " + untrusted.getBody().getSubject() + " ! ");
        if(untrusted.getBody().get("type").equals(String.valueOf(1)))
            btnRec.setVisibility(View.GONE);
        tvUsername.setTypeface(null, Typeface.BOLD);
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.allProjects);
        final List<Project> list = null;
        adapter = new ProjectAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
//                DividerItemDecoration.VERTICAL);
//        listView.addItemDecoration(mDividerItemDecoration);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<Project>> call = restAPI.findProjects();
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
                System.out.println(t.getMessage());
            }
        });
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
