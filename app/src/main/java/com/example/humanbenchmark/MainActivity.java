package com.example.humanbenchmark;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.humanbenchmark.ui.dashboard.DashboardFragment;
import com.example.humanbenchmark.ui.notifications.NotificationsFragment;
import com.example.humanbenchmark.util.ActivityUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Fragment mCurrentFragment;
    private List<Fragment> mFragmentList;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private Context mContext;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    try {
                        switchFragment(mCurrentFragment, mFragmentList.get(1));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return true;
                case R.id.navigation_notifications:
                    try {
                        switchFragment(mCurrentFragment, mFragmentList.get(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext= getApplicationContext();

//        viewPager = findViewById(R.id.viewpager);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NotificationsFragment());
        mFragmentList.add(new DashboardFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.nav_host_fragment);
        mCurrentFragment = mFragmentList.get(0);
//        MyFragmentPagerAdapter mAdapter = new MyFragmentPagerAdapter(this, mFragmentList);
//        viewPager.setAdapter(mAdapter);
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                if (menuItem != null) {
//                    menuItem.setChecked(false);
//                } else {
//                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
//                }
//                menuItem = bottomNavigationView.getMenu().getItem(position);
//                menuItem.setChecked(true);
//            }
//        });
//        bottomNavigationView = findViewById(R.id.nav_view);
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.navigation_home:
//                                viewPager.setCurrentItem(0);
//                                break;
//                            case R.id.navigation_dashboard:
//                                viewPager.setCurrentItem(1);
//                                break;
//                            case R.id.navigation_notifications:
//                                viewPager.setCurrentItem(2);
//                                break;
//                        }
//                        return false;
//                    }
//                });


    }
    public void switchFragment(Fragment from, Fragment to) throws ParseException {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().
                    beginTransaction();
            if (!to.isAdded()) {
                Log.i("LOGCAT","to is added not true");
//                transaction.hide(from).add(R.id.nav_host_fragment, to).commit();
                transaction.replace(R.id.nav_host_fragment,to).commit();
            } else {
                transaction.replace(R.id.nav_host_fragment,to).commit();
//                transaction.hide(from).show(to).commit();
            }
        }
    }
}