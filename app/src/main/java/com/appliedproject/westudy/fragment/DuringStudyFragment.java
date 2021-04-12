package com.appliedproject.westudy.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.activities.MainActivity;
import com.appliedproject.westudy.activities.PostActivity;
import com.appliedproject.westudy.database.PostingEntity;
import com.appliedproject.westudy.database.TaskEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class DuringStudyFragment extends Fragment {
    TextView tvTimer;
    private List<TaskEntity> taskList;
    private TaskEntity task;
    private int timeScope;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_during_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initial task list
        taskList = new ArrayList<>();
        //retrieve task id from previous fragment
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String id = pref.getString("taskid", "empty");
        //setup timer textview
        tvTimer = view.findViewById(R.id.textview_timer);
        //get time scope from database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks").child(id);
        String key = reference.child(id).child("tasktime").getKey();
        //int keyValue = Integer.parseInt(key);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                task = dataSnapshot.getValue(TaskEntity.class);
                //tvTimer.setText(task.getTasktime());
                timeScope = Integer.parseInt(task.getTasktime());
                //tvTimer.setText(timeScope);
                initialTimer(timeScope);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void initialTimer(int timeLength){
        long duration = TimeUnit.MINUTES.toMillis(timeLength);

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                //Set converted string to textview
                tvTimer.setText(sDuration);
            }

            @Override
            public void onFinish() {
                //hide timer
                tvTimer.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Congratulations! Study Task Finished!", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                initialDialog(builder);
            }
        }.start();
    }

    private void initialDialog(AlertDialog.Builder builder){
        // Build an AlertDialog

        // Set a title for alert dialog
        builder.setTitle("Share your work!");

        // Ask the final question
        builder.setMessage("Would you like to share your works with others?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface

        //Change the style of button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            }
        });
        dialog.show();
    }

    private void transferTask(String taskId){
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("taskid", taskId);
        edt.commit();
    }

}