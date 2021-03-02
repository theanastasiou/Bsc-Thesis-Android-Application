package com.example.freelancer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.freelancer.R;
import com.example.freelancer.activities.ListItemActivity1;
import com.example.freelancer.activities.ListItemActivity2;
import com.example.freelancer.activities.TabBar;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String jwt;
    Jwt<Header, Claims> untrusted;

    private OnFragmentInteractionListener mListener;

    public DoneProjectFragment() {
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doneproject, container, false);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        final View view = getView();
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.allProjects);
        final List<Project> list = null;
        ProjectAdapter adapter = new ProjectAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
//                DividerItemDecoration.VERTICAL);
//        listView.addItemDecoration(mDividerItemDecoration);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        //ifftype call 1 or call 2
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        final Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        String typeofuser = (String) untrusted.getBody().get("type");
        System.out.println("TYPEOFUSER on DOne " + typeofuser);
        Call<List<Project>> call =null ;
        if(Integer.valueOf(typeofuser)==1)
            call = restAPI.findDoneProjectsE(untrusted.getBody().getSubject());
        if(Integer.valueOf(typeofuser)==2)
            call = restAPI.findDoneProjectsW(untrusted.getBody().getSubject());

        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, final Response<List<Project>> response) {
                ProjectAdapter adapter = (ProjectAdapter) listView.getAdapter();
//                System.out.println("WWWWWWWWWWWWWWWWW");
                adapter.insert(response.body());
                System.out.println(adapter.getItemCount()+ " is done size");
                adapter.setOnItemClickListener(new ProjectAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if(response.body()!=null) {
                            System.out.println("onItemClicked done position: " + response.body().get(position).getProjectID());
                            Fragment fragment = null;
                            String typeofuser = (String) untrusted.getBody().get("type");
//                            System.out.println("TYPEOFUSER " + typeofuser);
                            if(Integer.valueOf(typeofuser)==2)
                                fragment = new ViewProjectHire(); // next fragment for post a project
                            if(Integer.valueOf(typeofuser)==1){
//                                System.out.println("OKKK");
                                fragment = new ViewProjectHire(); // next fragment for post a project

                                 }
                            Bundle args = new Bundle();
                            int projectid = response.body().get(position).getProjectID();
//                            System.out.println(projectid);
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


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
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
