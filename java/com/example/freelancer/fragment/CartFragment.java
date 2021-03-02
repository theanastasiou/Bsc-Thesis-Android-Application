package com.example.freelancer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.freelancer.R;
import com.example.freelancer.activities.ListItemActivity1;
import com.example.freelancer.activities.ListItemActivity2;
import com.example.freelancer.activities.MainActivity;
import com.example.freelancer.activities.MainActivity1;
import com.example.freelancer.activities.MainActivity2;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
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

public class CartFragment extends Fragment {
    private String jwt;
    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setNavMenuItemThemeColors(int color){
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = R.color.colorAccent;
        int navDefaultIconColor = R.color.colorAccent;

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_profile);
        System.out.println(mi.getTitle());
        mi.setIconTintList(navMenuIconList);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(color), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
//        mi.setItemTextColor(navMenuTextList);
     }

    @Override
    public void onPause() {
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_profile);
        Drawable newIcon = (Drawable)mi.getIcon();
        newIcon.mutate().setColorFilter(Color.parseColor("#F5F5F5"), PorterDuff.Mode.SRC_IN);
        mi.setIcon(newIcon);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#F5F5F5")), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
        super.onPause();
    }

    @Override
    public void onResume(){
        final View view = getView();
        BottomNavigationView m = ((AppCompatActivity)getActivity()).findViewById(R.id.bottom_navigation);
        MenuItem mi = m.getMenu().findItem(R.id.navigation_profile);
        Drawable newIcon = (Drawable)mi.getIcon();
        newIcon.mutate().setColorFilter(Color.parseColor("#D81B60"), PorterDuff.Mode.SRC_IN);
        mi.setIcon(newIcon);
        SpannableString spanString = new SpannableString(mi.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0,     spanString.length(), 0); //fix the color to white
        mi.setTitle(spanString);
        if(getActivity().getClass()== MainActivity1.class) {
            System.out.println("MA1");
            ((MainActivity1) getActivity()).badge();
        }
        else {
            System.out.println("MA2");
            ((MainActivity2) getActivity()).badge();
        }
        final ListView listView= (ListView)view.findViewById(R.id.list);
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<String>> senders = restAPI.findSenders(jwt,untrusted.getBody().getSubject());
        senders.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, final Response<List<String>> response) {
                List<String> values = response.body();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
                        myIntent.putExtra("receiver",response.body().get(position));
                        startActivityForResult(myIntent, 0);

//                        if (position == 0) {
//                            Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                            startActivityForResult(myIntent, 0);
//                        }
//
//                        if (position == 1) {
//                            Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                            startActivityForResult(myIntent, 0);
//                        }
//
//                        if (position == 2) {
//                            Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                            startActivityForResult(myIntent, 0);
//                        }
//
//                        if (position == 3) {
//                            Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                            startActivityForResult(myIntent, 0);
//                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        String[] values = new String[]{"Message 1", "Message 2",
                "Message 3", "Message 4"
        };
//        ListView listView= (ListView)view.findViewById(R.id.list);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (position == 0) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 1) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 2) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 3) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                    startActivityForResult(myIntent, 0);
//                }
//            }
//        });

        return view;
    }
}
