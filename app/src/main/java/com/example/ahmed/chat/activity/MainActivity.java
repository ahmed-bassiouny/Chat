package com.example.ahmed.chat.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapters.ViewPagerAdapter;
import com.example.ahmed.chat.fragments.HomeFragment;
import com.example.ahmed.chat.fragments.OlderChatFragment;
import com.example.ahmed.chat.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView navigation;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handleEvent();
        setupViewPager();
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void handleEvent() {
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.navigation_dashboard:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.navigation_notifications:
                                viewPager.setCurrentItem(2);
                                return true;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProfileFragment.getInstance());
        adapter.addFragment(HomeFragment.getInstance());
        adapter.addFragment(OlderChatFragment.getInstance());
        viewPager.setAdapter(adapter);
    }
}
