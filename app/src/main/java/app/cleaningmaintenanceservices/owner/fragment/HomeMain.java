package app.cleaningmaintenanceservices.owner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booking.rtlviewpager.RtlViewPager;

import app.cleaningmaintenanceservices.R;

public class HomeMain extends Fragment {

    View v;

    TabLayout tabLayout;
    ViewPager viewPager;

    HomeMainBooking pending, confirmed, finished, cancelled;

    public static boolean needRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_owner_home_main, container, false);

        tabLayout = v.findViewById(R.id.tabLayout);
        viewPager = (RtlViewPager) v.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.upcoming));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.pending));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.completed));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cancelled));

        populateFrags();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(needRefresh){

            populateFrags();
            needRefresh = false;
        }
    }

    public void populateFrags() {

        viewPager.setAdapter(new CustomPager(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private class CustomPager extends FragmentPagerAdapter {

        CustomPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle b = new Bundle();

            switch (position) {
                case 0:

                    pending = new HomeMainBooking();
                    b.putString("type", "pending");
                    pending.setArguments(b);
                    return pending;

                case 1:

                    confirmed = new HomeMainBooking();
                    b.putString("type", "confirmed");
                    confirmed.setArguments(b);
                    return confirmed;

                case 2:

                    finished = new HomeMainBooking();
                    b.putString("type", "finished");
                    finished.setArguments(b);
                    return finished;

                case 3:

                    cancelled = new HomeMainBooking();
                    b.putString("type", "cancelled");
                    cancelled.setArguments(b);
                    return cancelled;

                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
