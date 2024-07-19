package com.example.projectwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectwork.DataClasses.TaskDataClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TaskUploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button uploadButton;
    EditText Article, Desc, Price;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_upload);

        uploadImage = findViewById(R.id.ImageViewAddPhoto);
        uploadButton = findViewById(R.id.buttonUploadTask);
        Article = findViewById(R.id.edit_text_taskArticle);
        Desc = findViewById(R.id.edit_text_taskDesc);
        Price = findViewById(R.id.edit_text_taskPrice);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            uploadImage.setBackgroundResource(R.drawable.green_border);
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(TaskUploadActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = Article.getText().toString();
                String desc = Desc.getText().toString();
                String price = Price.getText().toString();

                if (title.isEmpty() || desc.isEmpty() || price.isEmpty() || uri == null){
                    Toast.makeText(TaskUploadActivity.this, "Please enter all fields and images", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveData();
            }
        });
    }

    public void saveData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskUploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData() {
        String title = Article.getText().toString();
        String desc = Desc.getText().toString();
        String price = Price.getText().toString();

        TaskDataClass taskDataClass = new TaskDataClass(title, desc, imageURL, price);

        // Get the upload user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the number of tasks the user has already created
        DatabaseReference userTasksRef = FirebaseDatabase.getInstance().getReference("Tasks").child(userId);
        userTasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           long taskNumber = dataSnapshot.getChildrenCount();

                           // Unique task id combining user id and task number
                           String taskId = userId + "_" + (taskNumber + 1);

                           userTasksRef.setValue(taskDataClass)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){
                                               Toast.makeText(TaskUploadActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                                               finish();
                                           }
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(TaskUploadActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                           Log.d("DB ERROR", e.getMessage().toString());
                                       }
                                   });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                        Log.e("Database", "ERROR");
                    }
                });
    }
}