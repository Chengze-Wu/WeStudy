package com.appliedproject.westudy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appliedproject.westudy.R;
import com.appliedproject.westudy.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.royrodriguez.transitionbutton.TransitionButton;
import com.royrodriguez.transitionbutton.utils.WindowUtils;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ActivityLoginBinding mBinding;
    private GoogleSignInClient mSignInClient;

    //firebase instance
    private FirebaseAuth mFirebaseAuth;

    //firebase database
    DatabaseReference reference;

    //textview
    EditText email, password;
    //buttons
    private Button btnRegister, btnLogin;




    //animation login button
    //private TransitionButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //animation button
        WindowUtils.makeStatusbarTransparent(this);

        //initialize
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);



        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mFirebaseAuth = FirebaseAuth.getInstance();



        mBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //configure google sign in
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, options);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //register button click listener
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });


        //Login button
        btnLogin = findViewById(R.id.transition_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String strEmail = "marcelo@me.com";//email.getText().toString();
                String strPassword = "123456";//password.getText().toString();


                if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)){
                    Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();


                } else{
                    mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(mFirebaseAuth.getCurrentUser().getUid());

                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pd.dismiss();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                pd.dismiss();
                                            }
                                        });
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Then start the loading animation when the user tap the button
//                btnLogin.startAnimation();
//
//                // Do your networking task or background work here.
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        boolean isSuccessful = true;
//
//                        if (isSuccessful) {
//                            btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
//                                @Override
//                                public void onAnimationStopEnd() {
//
//                                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
//                                    pd.setMessage("Please wait...");
//                                    pd.show();
//
//                                    strEmail = txtEmail.getText().toString();
//                                    strPassword = txtPassword.getText().toString();
//
//
//                                    if (strEmail.equals("") || strPassword.equals("")){
//                                        Log.d("Login Email---------------", strEmail);
//                                        Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
//
//                                        btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
//                                    } else{
//                                        mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPassword)
//                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                                        if(task.isSuccessful()){
//                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
//                                                                    .child(mFirebaseAuth.getCurrentUser().getUid());
//
//                                                            reference.addValueEventListener(new ValueEventListener() {
//                                                                @Override
//                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                    pd.dismiss();
//                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                                    startActivity(intent);
//                                                                    finish();
//                                                                }
//
//                                                                @Override
//                                                                public void onCancelled(@NonNull DatabaseError error) {
//                                                                    pd.dismiss();
//                                                                }
//                                                            });
//                                                        }else{
//                                                            pd.dismiss();
//                                                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                }
//                            });
//                        } else {
//                            btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
//                        }
//                    }
//                }, 2000);
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:"+ account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInWIthCredential:success");
                        String uid = mFirebaseAuth.getUid();
                        registerToDatabase(account,uid);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "signInWithCredential", e);
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //google auth
    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    //register
    private void signUp(){
        Intent signUpIntent = new Intent(this, RegisterActivity.class);
        startActivityForResult(signUpIntent,RC_SIGN_IN);
    }

    private void registerToDatabase(GoogleSignInAccount account, String uid){
        String userID = uid;
        String username = account.getDisplayName().toLowerCase();
        String email = account.getEmail().toLowerCase();
        String photoUrl = account.getPhotoUrl().toString();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userID);
        map.put("username", username.toLowerCase());
        map.put("email", email);
        map.put("photourl", photoUrl);
        map.put("coins", 0);
        reference.setValue(map);
    }
}