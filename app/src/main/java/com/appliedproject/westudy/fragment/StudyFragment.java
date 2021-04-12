package com.appliedproject.westudy.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.activities.CoStudy;
import com.appliedproject.westudy.activities.IndividualStudy;
import com.google.android.material.slider.RangeSlider;


public class StudyFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnIndividual = view.findViewById(R.id.btnStudyIndividual);
        Button btnCoStudy = view.findViewById(R.id.btnStudyColla);
        btnIndividual.setOnClickListener(View->{
            Intent intent = new Intent(view.getContext(), IndividualStudy.class);
            startActivity(intent);
        });
        btnCoStudy.setOnClickListener(View-> {
            Intent intent = new Intent(view.getContext(), CoStudy.class);
            startActivity(intent);
        });

    }
}