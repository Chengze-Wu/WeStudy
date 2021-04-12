package com.appliedproject.westudy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.appliedproject.westudy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import java.util.HashMap;


public class StudyScopeFragment extends Fragment {
    Button btnStart;
    String timeValue="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_scope, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initial Views
        TextView tvStudyGoal = view.findViewById(R.id.task_goal);
        TextView tvDescription = view.findViewById(R.id.description);


        //Setup start button
        btnStart = view.findViewById(R.id.btnStart);
        //Setup Dropdown list
        Spinner spinner = (Spinner) view.findViewById(R.id.topics_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.topics_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Setup croller time
        Croller croller = view.findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.WHITE);
        croller.setMax(36);
        croller.setStartOffset(45);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
        croller.setIsContinuous(false);

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                // use the progress
                String timeScope = progress * 5 + " minutes";
                timeValue = timeScope.replaceAll("[^0-9]", "");
                croller.setLabel(timeScope);
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
                // tracking started
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
                // tracking stopped
            }
        });

        btnStart.setOnClickListener(View->{
            //SharedPreferences.Editor editor = this.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            //need tobe changed to task id from firebase
//            editor.putString("taskid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//            editor.apply();

            //get Value from view
            String taskGoal = tvStudyGoal.getText().toString();
            String description = tvDescription.getText().toString();


            //insert task into database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");
            String taskId = reference.push().getKey();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("taskid", taskId);
            hashMap.put("taskgoal", taskGoal);
            hashMap.put("description", description);
            hashMap.put("tasktime", timeValue);
            hashMap.put("topic", spinner.getSelectedItem().toString());
            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("status", 0);

            reference.child(taskId).setValue(hashMap);

            // direct to during study fragment
            DuringStudyFragment nextFrag= new DuringStudyFragment();
            //intent task id to next fragment
            SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putString("taskid", taskId);
            edt.commit();
            //intent next fragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();

        });

    }

    private void addTask(){

    }



}