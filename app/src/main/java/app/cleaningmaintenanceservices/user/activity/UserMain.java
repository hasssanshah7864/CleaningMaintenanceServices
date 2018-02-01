package app.cleaningmaintenanceservices.user.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class UserMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView userImage;
    TextView username, userPhone;
    ArrayList<String> viewPagerImages;
    ViewPager viewPager;
    LinearLayout indicatorDotsLinearLayout;
    private ImageView[] indicatorDots;
    int imagesCounter = -1;

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

        switch (id){
            case R.id.user_nav_home:
                break;

            case R.id.user_nav_history:
                break;

            case R.id.user_nav_address_book:

                Intent intent = new Intent(this,AddressBook.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;

            case R.id.user_nav_notification:
                break;

            case R.id.user_nav_profile:
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

        viewPager = findViewById(R.id.userMainViewpager);
        indicatorDotsLinearLayout = findViewById(R.id.userMainViewpagerpagerDotsIndicator);
        viewPagerImages = new ArrayList<>();
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
    }
}
