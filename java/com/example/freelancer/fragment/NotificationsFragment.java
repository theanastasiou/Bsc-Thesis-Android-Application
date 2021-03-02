package com.example.freelancer.fragment;

import android.app.Notification;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.activities.ListItemActivity1;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.NotificationAdapter;
import com.example.freelancer.helper.ProjectAdapter;
import com.example.freelancer.rest.Notifications;
import com.example.freelancer.rest.Project;

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
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String jwt;
    Jwt<Header, Claims> untrusted;
    MenuItem markRead;
    MenuItem clearAll;
    ArrayList<Notifications> notifs;
    NotificationAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        notifs=new ArrayList<>();
        setHasOptionsMenu(true);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        System.out.println("NOT fragment in");

    }

    private void mark(){
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        for(Notifications n : notifs) {
//            n.getUser().setPhoto(null);
            if(n.getApplies()!=null) {
//                n.getApplies().getProject().getUserIDWorker().setPhoto(null);
//                n.getApplies().getProject().getUserIDOwner().setPhoto(null);
//                n.getApplies().getUser().setPhoto(null);
            }
            else if (n.getReviewReviewID()!=null){
//                n.getReviewReviewID().getSender().setPhoto(null);
//                n.getReviewReviewID().getReceiver().setPhoto(null);
//                n.getReviewReviewID().getProjectProjectID().getUserIDOwner().setPhoto(null);
//                n.getReviewReviewID().getProjectProjectID().getUserIDWorker().setPhoto(null);
            }
            else{
//                n.getChatMessageID().getUserRec().setPhoto(null);
//                n.getChatMessageID().getUserSend().setPhoto(null);
            }
        }
        Call<Void> mark = restAPI.markOrClear(jwt,notifs,0);
        mark.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(),"Marked as read",Toast.LENGTH_SHORT).show();
                notifs.clear();
                onResume();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void clear(){
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        for(Notifications n : notifs) {
//            n.getUser().setPhoto(null);
            if(n.getApplies()!=null) {
//                n.getApplies().getProject().getUserIDWorker().setPhoto(null);
//                n.getApplies().getProject().getUserIDOwner().setPhoto(null);
//                n.getApplies().getUser().setPhoto(null);
            }
            else if (n.getReviewReviewID()!=null){
//                n.getReviewReviewID().getSender().setPhoto(null);
//                n.getReviewReviewID().getReceiver().setPhoto(null);
//                n.getReviewReviewID().getProjectProjectID().getUserIDOwner().setPhoto(null);
//                n.getReviewReviewID().getProjectProjectID().getUserIDWorker().setPhoto(null);
            }
            else{
//                n.getChatMessageID().getUserRec().setPhoto(null);
//                n.getChatMessageID().getUserSend().setPhoto(null);
            }
        }
        Call<Void> mark = restAPI.markOrClear(jwt,notifs,1);
        mark.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(),"Cleared",Toast.LENGTH_SHORT).show();
                notifs.clear();
                onResume();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_mark_all_read:
                mark();
                return true;
            case R.id.action_clear_notifications:
                clear();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notifications,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onViewCreated(View view,Bundle savedInstanceState){
        markRead = view.findViewById(R.id.action_mark_all_read);
        clearAll = view.findViewById(R.id.action_clear_notifications);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not, container, false);
    }

    @Override
    public void onResume() {
        final View view = getView();
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.not);
        final List<Notifications> list = null;
        adapter = new NotificationAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        System.out.println("calling fun");
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        Call<List<Notifications>> call = restAPI.fetchnot(untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, final Response<List<Notifications>> response) {
                NotificationAdapter adapter = (NotificationAdapter) listView.getAdapter();
                System.out.println("WWWWWWWWWWWWWWWWW");
                adapter.insert(response.body());
                System.out.println(adapter.getItemCount()+ " is size");
                if(response.body()!=null)
                    notifs.addAll (response.body());
                adapter.setOnItemClickListener(new NotificationAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        if (response.body() != null) {
                            if (response.body().get(position).getApplies() != null) {
                                //Toast.makeText(getActivity(),"Apply",Toast.LENGTH_SHORT).show();
                                Fragment fragment = null;
                                fragment = new ApplyFragment(); // next fragment for post a project
                                Bundle args = new Bundle();
                                int projectid = response.body().get(position).getApplies().getProject().getProjectID();
                                int userid = response.body().get(position).getApplies().getUser().getUserID();
                                System.out.println(projectid);
                                args.putInt("ProjectID", projectid);
                                args.putInt("UserID",userid);
                                fragment.setArguments(args);
                                replaceFragment(fragment);

                            }
                            if (response.body().get(position).getChatMessageID() != null) {
                                //Toast.makeText(getActivity(),"Chat",Toast.LENGTH_SHORT).show();
                                Intent inte = new Intent(view.getContext(), ListItemActivity1.class);
                                inte.putExtra("receiver",response.body().get(position).getChatMessageID().getUserSend().getUserName());
                                startActivityForResult(inte, 0);

                            }
                            if (response.body().get(position).getReviewReviewID() != null) {
                                //Toast.makeText(getActivity(),"Review",Toast.LENGTH_SHORT).show();
                                PhotosFragment photosFragment = new PhotosFragment();
                                replaceFragment(photosFragment);

                            }
                        }
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {
                System.out.println(t.getMessage());
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
