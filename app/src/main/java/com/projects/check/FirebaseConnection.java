package com.projects.check;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseConnection implements Connection<String, Object> {

    FirebaseFirestore store;
    StorageReference storage;
    static String res;
    String docId;
    String userName;
    static boolean flag = true;

    public void initConnection(){
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void uploadImage(byte[] bytes) {
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
                    System.out.println("finished");
                    if (task.isSuccessful()) {
                        res = task.getResult().toString();
                        flag = false;
                        System.out.println(flag + " " + res);

                    }
                }
            });

        }catch (NullPointerException e){

        }
    }

    public static String imageURL(){
        while(flag){}
        return res;
    }

    @Override
    public String addCheck(String url, Map<String ,Object> info) {
        info.put("picture", url);
        System.out.println(info);
        try {
            store.collection("Checks").add(info).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    docId = documentReference.getId();
                    System.out.println(docId);
                }
            });
            while(docId == null);
            System.out.println("Doc ID : " + docId);

            return docId;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public User logIn(User user) {
        Query que = store.collection("Users").whereEqualTo(user.getBankAccountNumber(), "bankNo");
        que.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task != null){
                    for(DocumentSnapshot doc : task.getResult().getDocuments()){
                        if(user.getBankAccountNumber().equals(doc.get("bankNo")) && user.getPassword().equals(doc.get("password"))){
                            user.setFullName(doc.getString("fullName"));
                            user.setPhoneNumber(doc.getString("phoneNumber"));
                            user.setBankBranch(doc.getString("bankBranch"));
                        }
                    }
                }
            }
        });
        return user.getFullName() != null ? user : null;
    }

    @Override
    public boolean signUp(User user) {
        System.out.println(user.toString());
        final boolean[] success = {false};
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", user.getFullName());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("password", user.getPassword());
        userMap.put("bankBranch", user.getBankBranch());
        userMap.put("bankNo", user.getBankAccountNumber());

        store.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                success[0] = true;
                System.out.println(success[0]);
            }
        });
        return success[0];
    }
}
