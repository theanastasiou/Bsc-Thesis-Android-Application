package com.example.freelancer.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.freelancer.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinner;

    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        String[] descriptionData = {"Start", "Finish"};
        StateProgressBar stateProgressBar = (StateProgressBar) v.findViewById(R.id.state);
        stateProgressBar.setStateDescriptionData(descriptionData);
//        HorizontalStepView setpview5 = (HorizontalStepView) v.findViewById(R.id.step_view);
//        List<StepBean> stepsBeanList = new ArrayList<>();
//        StepBean stepBean0 = new StepBean("Step 1",0);
//        StepBean stepBean1 = new StepBean("Step 2",-1);
//        StepBean stepBean2 = new StepBean("出发",1);
//        StepBean stepBean3 = new StepBean("Step 1",0);
//        StepBean stepBean4 = new StepBean("Step 2",-1);
//        stepsBeanList.add(stepBean0);
//        stepsBeanList.add(stepBean1);
////        stepsBeanList.add(stepBean2);
////        stepsBeanList.add(stepBean3);
////        stepsBeanList.add(stepBean4);
//        setpview5
//                .setStepViewTexts(stepsBeanList)//总步骤
//                .setTextSize(12)//set textSize
//                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.Black))//设置StepsViewIndicator未完成线的颜色
//                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.Black))//设置StepsView text未完成线的颜色
//                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
//                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
        String[] values =
                {"Website & IT", "Mobile", "Art & Design", "Data Entry", "Software Dev", "Writing", "Business", "Sales",};
        spinner= (Spinner) v.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        Button secondViewBtn = (Button) v.findViewById(R.id.btnNext);

        secondViewBtn.setOnClickListener(this);
        return v;

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
    public void onClick(View view) {
        Fragment fragment = null;
        fragment = new PostProject(); // next fragment for post a project
        Bundle args = new Bundle();
        String text = spinner.getSelectedItem().toString();
        System.out.println(text);
        args.putString("Spinner", text);
        fragment.setArguments(args);
        //Inflate the fragment
       // getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        replaceFragment(fragment);
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
