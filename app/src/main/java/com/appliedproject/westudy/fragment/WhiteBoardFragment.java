package com.appliedproject.westudy.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.activities.PostActivity;
import com.appliedproject.westudy.activities.QestionActivity;
import com.appliedproject.westudy.adapter.PostAdapter;
import com.appliedproject.westudy.adapter.QuestionAdapter;
import com.appliedproject.westudy.database.PostingEntity;
import com.appliedproject.westudy.database.QuestionEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WhiteBoardFragment extends Fragment {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private List<QuestionEntity> questionList;

    private RecyclerView recyclerView_story;

    private List<String> followingList;

    ProgressBar progressCircular;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_white_board, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(getContext(), questionList);
        recyclerView.setAdapter(questionAdapter);


        progressCircular = view.findViewById(R.id.progress_circular);

        checkFollowing();

        return view;



    }

    private void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readQuestions();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readQuestions() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionEntity question = snapshot.getValue(QuestionEntity.class);
                    for (String id : followingList){
                        if (question.getPublisher().equals(id)){
                            questionList.add(question);
                        }
                    }
                }

                questionAdapter.notifyDataSetChanged();
                progressCircular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(getActivity(), QestionActivity.class));
            }
        });
    }


}