package com.appliedproject.westudy.explore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.utils.BottomNavigationHelper;
import com.google.android.material.tabs.TabLayout;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class ExploreActivity extends AppCompatActivity {
    private static final String TAG = "ExploreEActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = ExploreActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();
        setupViewPager();
    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestFragment()); //index 0
        adapter.addFragment(new TrendingFragment()); //index 1
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_name);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        AnimatedBottomBar bottomBar = (AnimatedBottomBar)findViewById(R.id.bottomNavViewBar);
        bottomBar.setSelectedTabType(AnimatedBottomBar.TabType.TEXT);
        BottomNavigationHelper.setupBottomNavigationView(bottomBar);
        BottomNavigationHelper.enableNavigation(mContext, bottomBar);


    }
}
