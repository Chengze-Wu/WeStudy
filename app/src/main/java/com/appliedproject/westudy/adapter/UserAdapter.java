package com.appliedproject.westudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.activities.MainActivity;
import com.appliedproject.westudy.activities.MessageActivity;
import com.appliedproject.westudy.database.UserEntity;
import com.appliedproject.westudy.fragment.ProfileFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<UserEntity> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<UserEntity> mUsers, boolean isFragment){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final UserEntity user = mUsers.get(position);

        holder.btnFollow.setVisibility(View.VISIBLE);


        holder.txtUsername.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        Glide.with(mContext).load(user.getPhotourl()).into(holder.imgProfile);
        isFollowing(user.getId(), holder.btnFollow);


        if (user.getId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                Log.d("USER ID ------------", intent.getStringExtra("userid"));
                mContext.startActivity(intent);
            }

        });

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btnFollow.getText().equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    addNotification(user.getId());
                } else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }

    private void addNotification(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtUsername;
        public TextView email;
        public CircleImageView imgProfile;
        public Button btnFollow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }

    private void isFollowing(String uid, Button btn){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(uid).exists()){
                    btn.setText("following");
                }else {
                    btn.setText("follow");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
