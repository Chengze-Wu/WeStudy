package com.appliedproject.westudy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.database.UserEntity;
import com.appliedproject.westudy.fragment.DuringStudyFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import java.util.HashMap;

public class CoStudyScope extends AppCompatActivity {

    Button btnStart;
    String timeValue="";
    String userid;
    Intent intent;
    FirebaseUser fuser;
    DatabaseReference reference;

    TextView txtUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_study_scope);

        TextView tvStudyGoal = findViewById(R.id.task_goal);
        TextView tvDescription = findViewById(R.id.description);
        txtUser = findViewById(R.id.shared_user);
        intent = getIntent();
        //Setup Dropdown list
        Spinner spinner = (Spinner) findViewById(R.id.topics_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.topics_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Setup start button
        btnStart = findViewById(R.id.btnStart);
        //Setup shared user
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserEntity user = dataSnapshot.getValue(UserEntity.class);
                txtUser.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Setup croller time
        Croller croller = findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
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
            SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putString("taskid", taskId);
            edt.commit();
            Intent nextIntent = new Intent(this.getApplicationContext(), PostActivity.class);
            startActivity(nextIntent);

        });
    }
}