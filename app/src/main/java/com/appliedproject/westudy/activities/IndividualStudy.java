package com.appliedproject.westudy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.fragment.PostFragment;
import com.appliedproject.westudy.fragment.StudyScopeFragment;

public class IndividualStudy extends AppCompatActivity {

    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_study);

        selectedFragment = new StudyScopeFragment();
        if (selectedFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
        }
    }
}