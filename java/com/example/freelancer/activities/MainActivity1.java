package com.example.freelancer.activities;

import android.app.Notification;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.fragment.CartFragment;
import com.example.freelancer.fragment.HomeFragment;
import com.example.freelancer.fragment.MoviesFragment;
import com.example.freelancer.fragment.MyProjectsFragment;
import com.example.freelancer.fragment.NotificationsFragment;
import com.example.freelancer.fragment.PhotosFragment;
import com.example.freelancer.fragment.PostProject;
import com.example.freelancer.fragment.ProfileFragment;
import com.example.freelancer.fragment.SettingsFragment;
import com.example.freelancer.other.CircleTransform;
import com.example.freelancer.helper.BottomNavigationBehavior;
import com.example.freelancer.rest.Notifications;

import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {



    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ActionBar toolbarr;
    private ActionBar toolbaR;
    Jwt<Header, Claims> untrusted;
    ArrayAdapter<String> adapter;

    private Button button;
    private View notificationBadge;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_LOGOUT= "logout";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    String jwt=null;

    private BottomNavigationView bottomNavigationView;

    @Override
    public  void onResume(){

        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<Long> unread = restAPI.unread(jwt,untrusted.getBody().getSubject());
        unread.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if(response.body()>0)
                    addBadgeView();
                else
                    removeBadgeView();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {

            }
        });
        Call<List<Notifications>> call = restAPI.fetchnot(untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                if(response.body()!=null)
                    for(Notifications n : response.body())
                        if(!n.isSeen()){
                            navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
                            break;
                        }
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {

            }
        });
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        System.out.println("reti jwt on click = " + restoredText);
        jwt=restoredText;
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(" ");
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
            setTitle(" ");
        }

       // toolbaR = getSupportActionBar();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
       //bottomNavigationView.setOnNavigationItemSelectedListener();
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //button = findViewById(R.id.button);
        //button.setOnClickListener(new View.OnClickListener() {
        //bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // attaching bottom sheet behaviour - hide / show on scroll

        /*CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());*/



    }

    public void badge(){
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        Jwt<Header, Claims> untrusted;
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<Long> unread = restAPI.unread(jwt,untrusted.getBody().getSubject());
        unread.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if(response.body()>0)
                    addBadgeView();
                else{
                    System.out.println("EMPTY NOTIFS");
                    removeBadgeView();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {

            }
        });
    }
    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        System.out.println("hiiiiiiiiiiiiiiiii");
       /* txtName.setText("Welcome");
        txtName.setTextColor(this.getResources().getColor(R.color.Black));*/
        txtWebsite.setText(untrusted.getBody().getSubject());
        txtWebsite.setTextColor(this.getResources().getColor(R.color.Black));

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
//                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();


            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                 getHomeFragment();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private boolean getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
                return true;
            case 1:
                // photos
                PhotosFragment photosFragment = new PhotosFragment();
                loadFragment(photosFragment);
                return true;
            case 2:
                // movies fragment
               MyProjectsFragment myProjectFragment = new MyProjectsFragment();
                loadFragment(myProjectFragment);
                return true;
            case 3:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                loadFragment(notificationsFragment);
                return true;
            case 4:
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Intent ma = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ma);
                return true;
            default:
                loadFragment(new HomeFragment());
                return true;
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_logout:
                        navItemIndex=4;
                        CURRENT_TAG = TAG_LOGOUT;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.app_bar_search).setVisible(true);
//        MenuItem item = menu.findItem(R.id.app_bar_search);

//        SearchView searchView = (SearchView)item.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//
//                return false;
//            }
//        });

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }*/

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
//            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
//            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }
        if(  actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);

    }

    private void removeBadgeView() {
        System.out.println("remove btch1");
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);

//        notificationBadge = LayoutInflater.from(this).inflate(R.layout.custom_notification_layout, menuView, false);
//        notificationBadge.setVisibility(View.GONE);
        itemView.removeView(itemView.getChildAt(2));
    }


    private void addBadgeView() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);

        notificationBadge = LayoutInflater.from(this).inflate(R.layout.custom_notification_layout, menuView, false);

        itemView.addView(notificationBadge);
    }

    private void refreshBadgeView() {
        boolean badgeIsVisible = notificationBadge.getVisibility() != View.VISIBLE;
        notificationBadge.setVisibility(badgeIsVisible ? View.VISIBLE : View.GONE);
        button.setText(badgeIsVisible ? "Hide badge" : " Show badge");
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_profile:
                    toolbar.setTitle("Messages");
                    CartFragment fragment = new CartFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_card:
                    toolbar.setTitle("New Project");
                    MoviesFragment fragmentt = new MoviesFragment();
                    loadFragment(fragmentt);
                    return true;
            }

            return false;
        }
    };


    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.add(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}