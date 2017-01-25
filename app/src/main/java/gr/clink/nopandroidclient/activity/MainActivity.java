package gr.clink.nopandroidclient.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.fragment.CategoriesFragment;
import gr.clink.nopandroidclient.fragment.HomeFragment;
import gr.clink.nopandroidclient.fragment.MoviesFragment;
import gr.clink.nopandroidclient.fragment.NotificationsFragment;
import gr.clink.nopandroidclient.fragment.ProfileFragment;
import gr.clink.nopandroidclient.fragment.SettingsFragment;
import gr.clink.nopandroidclient.model.UserInformation;
import gr.clink.nopandroidclient.other.CircleTransform;
import gr.clink.nopandroidclient.other.TypefaceSpan;

public class  MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "http://creativecan.com/wp-content/uploads/2013/04/aperture-science-logo_thumb.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_PROFILE = "profile";
    private static  String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private enum DrawerMenuType {
        Standard,
        User
    }

    private DrawerMenuType menuType;

    public void navViewClicked(){
        if(!UserInformation.getInstance().isAuthenticated()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            //TODO: USER PROFILE
            if (menuType == DrawerMenuType.Standard){
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer_user);
                menuType = DrawerMenuType.User;
            }else{
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                menuType = DrawerMenuType.Standard;
            }
        }
    }

    private void resetUserVisuals(){
        if (UserInformation.getInstance().isAuthenticated()){
            txtName.setText(UserInformation.getInstance().getName());
        }else{
            txtName.setText(getString(R.string.action_sign_in_short).toUpperCase());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // name, website
        resetUserVisuals();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        menuType = DrawerMenuType.Standard;

        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navViewClicked();
            }
        });


        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
       // Loading profile image // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
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

            // show or hide the fab button
            toggleFab();
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
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        if (menuType == DrawerMenuType.Standard){
            switch (navItemIndex) {
                case 0:
                    // home
                    return new HomeFragment();
                case 1:
                    // categories fragment
                    return new CategoriesFragment();
                case 2:
                    // movies fragment
                    return new MoviesFragment();
                case 3:
                    // notifications fragment
                    return new NotificationsFragment();
                case 4:
                    // settings fragment
                    return new SettingsFragment();
                default:
                    return new HomeFragment();
            }
        }else{
            switch (navItemIndex) {
                case 0:
                    // profile
                    return new ProfileFragment();
                case 1:
                    //logout
                default:
                    return new HomeFragment();
            }
        }
    }

    private void setToolbarTitle() {
        SpannableString s = new SpannableString(activityTitles[navItemIndex]);
        s.setSpan(new TypefaceSpan(this, "RobotoSlab300.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
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
                if( menuType == DrawerMenuType.Standard) {
                    switch (menuItem.getItemId()) {
                        //Replacing the main content with ContentFragment Which is our Inbox View;
                        case R.id.home:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_HOME;
                            break;
                        case R.id.nav_categories:
                            navItemIndex = 1;
                            CURRENT_TAG = TAG_CATEGORIES;
                            break;
                        case R.id.nav_movies:
                            navItemIndex = 2;
                            CURRENT_TAG = TAG_MOVIES;
                            break;
                        case R.id.nav_notifications:
                            navItemIndex = 3;
                            CURRENT_TAG = TAG_NOTIFICATIONS;
                            break;
                        case R.id.nav_settings:
                            navItemIndex = 4;
                            CURRENT_TAG = TAG_SETTINGS;
                            break;
                        case R.id.nav_about_us:
                            // launch new intent instead of loading fragment
                            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                            drawer.closeDrawers();
                            return true;
                        case R.id.nav_privacy_policy:
                            // launch new intent instead of loading fragment
                            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                            drawer.closeDrawers();
                            return true;
                        default:
                            navItemIndex = 0;
                    }
                }else{
                    switch (menuItem.getItemId()) {
                        //Replacing the main content with ContentFragment Which is our Inbox View;
                        case R.id.home:
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_PROFILE;
                            break;
                        case R.id.nav_categories:
                            UserInformation.getInstance().logOut();
                            resetUserVisuals();
                            drawer.closeDrawers();

                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.activity_main_drawer);
                            menuType = DrawerMenuType.Standard;
                            CURRENT_TAG = TAG_HOME;
                            return true;
                        default:
                            navItemIndex = 0;
                    }
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




        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
