package com.projects.check;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Date;
import java.util.Map;

public class FirebaseConnection implements Connection<String, Object> {

    FirebaseFirestore store;
    StorageReference storage;
    String res;
    String docId;

    public void initConnection(){
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public String uploadImage(byte[] bytes) {
        try {
            final StorageReference ref = storage.child("images/" + new Date().toString());
            UploadTask uploadTask = ref.putBytes(bytes); // start uploading

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        res = task.getResult().toString();
                    }
                }
            });
            return res;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public String addCheck(String imageURL, Map<String ,Object> info) {
        System.out.println(info);
        try {
            store.collection("Checks").add(info).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    docId = documentReference.getId();
                    System.out.println(docId);
                }
            });
            System.out.println("Doc ID : " + docId);
            return docId;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public boolean logIn(String bankNo, String password) {
        return false;
    }

    @Override
    public boolean signUp(User user) {
        return false;
    }
}
