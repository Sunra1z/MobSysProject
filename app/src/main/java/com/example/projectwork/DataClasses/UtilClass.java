package com.example.projectwork.DataClasses;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UtilClass {
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


    public static StorageReference getCurrentProfilePicStorageRef(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(currentFirebaseUser.getUid());
    }

    public static Task<Uri> uploadProfilePic(Uri imageUri, String userId){
        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profile_pic").child(userId);
        return profilePicRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return profilePicRef.getDownloadUrl();
                });
    }
}
