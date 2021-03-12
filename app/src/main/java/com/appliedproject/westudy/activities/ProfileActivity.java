package com.appliedproject.westudy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.utils.BottomNavigationHelper;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class ProfileActivity extends AppCompatActivity {
    private Context mContext = ProfileActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void setupBottomNavigationView(){
        AnimatedBottomBar bottomBar = (AnimatedBottomBar)findViewById(R.id.bottomNavViewBar);
        bottomBar.setSelectedTabType(AnimatedBottomBar.TabType.TEXT);
        BottomNavigationHelper.setupBottomNavigationView(bottomBar);
        BottomNavigationHelper.enableNavigation(mContext, bottomBar);


    }
}