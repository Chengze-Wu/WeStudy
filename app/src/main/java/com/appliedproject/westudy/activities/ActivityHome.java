package com.appliedproject.westudy.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.utils.BottomNavigationHelper;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class ActivityHome extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = ActivityHome.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBottomNavigationView();

        getSupportActionBar().hide();

    }

    private void setupBottomNavigationView(){
        AnimatedBottomBar bottomBar = (AnimatedBottomBar)findViewById(R.id.bottomNavViewBar);
        bottomBar.setSelectedTabType(AnimatedBottomBar.TabType.TEXT);
        BottomNavigationHelper.setupBottomNavigationView(bottomBar);
        BottomNavigationHelper.enableNavigation(mContext, bottomBar);


    }
}