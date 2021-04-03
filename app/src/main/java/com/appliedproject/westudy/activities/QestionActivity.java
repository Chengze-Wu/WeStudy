package com.appliedproject.westudy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appliedproject.westudy.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class QestionActivity extends AppCompatActivity {
    Uri imageUri;
    String myUri = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView imageClose, imageAdded;
    TextView question;
    EditText questionContent;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qestion);

        imageClose = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        question = findViewById(R.id.post);
        description = findViewById(R.id.description);
        questionContent = findViewById(R.id.content);

        storageReference = FirebaseStorage.getInstance().getReference("questions");

        //close button
        imageClose.setOnClickListener(view -> {
            startActivity(new Intent(QestionActivity.this, MainActivity.class));
            finish();
        });

        //post button
        question.setOnClickListener(view -> uploadImage());

        imageAdded.setOnClickListener(view ->{
            CropImage.activity()
                    .setAspectRatio(1,1)
                    .start(QestionActivity.this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Something gone Wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(QestionActivity.this, MainActivity.class));
            finish();
        }
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if(imageUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Questions");
                        String postId = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("questionid", postId);
                        hashMap.put("questionimage", myUri);
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("questioncontent", questionContent.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postId).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(QestionActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(QestionActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(QestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(QestionActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}