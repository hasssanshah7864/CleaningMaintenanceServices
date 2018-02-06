package app.cleaningmaintenanceservices.owner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.booking.rtlviewpager.RtlViewPager;

import app.cleaningmaintenanceservices.R;
import app.cleaningmaintenanceservices.owner.fragment.HomeMain;

public class OwnerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ViewPager viewPager;

    ImageView notification,calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_owner_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarownerHome);
        setSupportActionBar(toolbar);
       // toolbar.setTitle(R.string.title_activity_owner_home);
       /* notification = findViewById(R.id.fragOwnerHomeMainBookingNotification);
        calender = findViewById(R.id.fragOwnerHomeMainBookingCalender);
        calender.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(OwnerHome.this, "Calender", Toast.LENGTH_SHORT).show();
        }
            });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OwnerHome.this, "Notification", Toast.LENGTH_SHORT).show();
            }
        });*/

        viewPager = (RtlViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomPager(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private class CustomPager extends FragmentPagerAdapter {

        CustomPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return new HomeMain();

                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.owner_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_calender) {
            Toast.makeText(this, "Calender", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.manage_cleaners) {
            startActivity(new Intent(this,MangeCleaners.class));

        } else if (id == R.id.reviews) {
            startActivity(new Intent(this,Reviews.class));

        } else if (id == R.id.clients) {
            startActivity(new Intent(this,Clients.class));

        } else if (id == R.id.profile) {

        } else if (id == R.id.beaprofessional) {

        }
        else if (id == R.id.faq) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
