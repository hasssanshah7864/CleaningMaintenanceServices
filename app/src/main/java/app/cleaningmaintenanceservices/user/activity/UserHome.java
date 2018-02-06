package app.cleaningmaintenanceservices.user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.common.activity.Login;
import app.cleaningmaintenanceservices.helper.Utils;
import app.cleaningmaintenanceservices.model.MDFeaturedServices;
import app.cleaningmaintenanceservices.model.MDTestimonial;
import app.cleaningmaintenanceservices.user.adapter.ViewPagerAdapter;

public class UserHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView userImage;
    TextView username, userPhone;
    ArrayList<String> viewPagerImages;
    ViewPager viewPager;
    LinearLayout indicatorDotsLinearLayout;
    ViewPagerAdapter adapter;
    private ImageView[] indicatorDots;
    int imagesCounter = -1;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();


        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        for (int i = 0; i < indicatorDots.length; i++) {
                            indicatorDots[i].setImageResource(R.drawable.unselected_dot);
                        }
                        indicatorDots[position].setImageResource(R.drawable.selected_dot);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(imagesCounter + 1 == indicatorDots.length){
                    imagesCounter = -1;
                    viewPager.setCurrentItem(++imagesCounter);
                }
                else {
                    viewPager.setCurrentItem(++imagesCounter);
                }
                handler.postDelayed(this,1500);
            }
        },1500);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.user_nav_home:
                break;

            case R.id.user_nav_history:
                break;

            case R.id.user_nav_address_book:

                intent = new Intent(this,AddressBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.user_nav_notification:
                break;

            case R.id.user_nav_profile:

                if(preferences.getBoolean("isLogin",false)){
                    intent = new Intent(this, UserProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("whichLogin","loginForProfile");
                    startActivity(intent);
                }

                break;

            case R.id.user_nav_testimonial:
                break;

            case R.id.user_nav_faq:
                break;

            case R.id.user_nav_terms_condition:
                break;

            case R.id.user_nav_about:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void init(){
        userImage = findViewById(R.id.userNavigationDrawerProfileImage);
        username = findViewById(R.id.userNavigationDrawerName);
        userPhone = findViewById(R.id.userNavigationDrawerPhone);

        Utils.loadImg(this, userImage, Utils.user.image, false, false);
        username.setText(Utils.user.full_name);
        userPhone.setText(Utils.user.mobile);

        viewPager = findViewById(R.id.userMainViewpager);
        indicatorDotsLinearLayout = findViewById(R.id.userMainViewpagerpagerDotsIndicator);
        viewPagerImages = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        for(MDTestimonial testimonial : Utils.settings.top_testimonials){
            viewPagerImages.add(testimonial.image);
        }
        adapter = new ViewPagerAdapter(viewPagerImages,this);
        viewPager.setAdapter(adapter);
        setupPagerIndicatorDots();

    }

    public void setupPagerIndicatorDots(){
        indicatorDots = new ImageView[viewPagerImages.size()];

        for(int i = 0 ; i < indicatorDots.length ; i++){
            indicatorDots[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            indicatorDots[i].setLayoutParams(params);
            indicatorDots[i].setImageResource(R.drawable.unselected_dot);
            indicatorDotsLinearLayout.addView(indicatorDots[i]);
            indicatorDotsLinearLayout.bringToFront();
        }
        indicatorDots[0].setImageResource(R.drawable.selected_dot);
    }
}
