package com.appliedproject.westudy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.activities.QestionActivity;
import com.appliedproject.westudy.database.PostingEntity;
import com.appliedproject.westudy.database.QuestionEntity;
import com.appliedproject.westudy.database.UserEntity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    public Context mContext;
    public List<QuestionEntity> mQuestions;

    private FirebaseUser firebaseUser;

    public QuestionAdapter(Context mContext, List<QuestionEntity> mQuestions){
        this.mContext = mContext;
        this.mQuestions = mQuestions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_item, parent, false);
        return new QuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        QuestionEntity question = mQuestions.get(position);

        if(question.getQuestionimage().equals("")){
            holder.questionImage.setVisibility(View.GONE);
        }else{
            holder.questionImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(question.getQuestionimage()).into(holder.questionImage);
        }


        if(question.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(question.getDescription());
        }
        holder.questionContent.setText(question.getQuestioncontent());


        publisherInfo(holder.imageProfile, holder.username, holder.publisher, question.getPublisher());

    }

    private void publisherInfo(ImageView imageProfile, TextView username, TextView publisher, String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserEntity user = snapshot.getValue(UserEntity.class);
                Glide.with(mContext).load(user.getPhotourl()).into(imageProfile);
                publisher.setText(user.getUsername());
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageProfile, questionImage;
        public TextView username, likes, publisher, description, replys, questionContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.imgProfile);
            questionImage = itemView.findViewById(R.id.imgQuestion);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            replys = itemView.findViewById(R.id.replys);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.question_description);
            questionContent = itemView.findViewById(R.id.question_content);
        }


    }
}
