package com.projects.check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.UUID;

public class FirebaseConnection implements Connection<String, Object> {

    private FirebaseFirestore store;
    private StorageReference storage;
    Context context;
    String res;

    public FirebaseConnection(Context context) {
        this.context = context;
        System.out.println(this.context);
    }

    public void initConnection(){
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void uploadImage(byte[] bytes, Map<String, Object> info) {
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
                        addCheck(res, info);
                    }
                }
            });
    }

    @Override
    public String addCheck(String url, Map<String ,Object> info) {
        info.put("picture", url);
        System.out.println(info);
        String s = new UUID(2, 2).randomUUID().toString();
        final String docId = s.substring(s.length() - 12, s.length()).toUpperCase();
        try {
            store.collection("Checks").document(docId).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Dialog d = new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Successful !")
                            .setMessage("Check ID : " + docId + " Copy it and send it to the recipient")
                            .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData data = ClipData.newPlainText(docId, docId);
                                    clip.setPrimaryClip(data);
                                    System.out.println(docId);
                                    Toast.makeText(context, docId + "has been Copied to clip board", Toast.LENGTH_SHORT).show();
//                                    (context).startActivity(new Intent(context, ChoosingAction.class));
                                }
                            }).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Dialog d = new AlertDialog.Builder(context)
                            .setIcon(R.drawable.x)
                            .setTitle("Failed !")
                            .setMessage("A failure happened and the check has not been uploaded")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addCheck(url, info);
                                }
                            }).show();
                }
            });
            System.out.println("Doc ID : " + docId);
            return docId;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public void logIn(User user) {
        Query que = store.collection("Users").whereEqualTo("bankNo", user.getBankAccountNumber());
        que.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                System.out.println("here");
                if(task != null && task.getResult().size() != 0){
                    for(DocumentSnapshot doc : task.getResult().getDocuments()){
                        System.out.println(doc.getString("bankNo"));
                        if(user.getBankAccountNumber().equals(doc.get("bankNo")) && user.getPassword().equals(doc.get("password"))){
                            user.setFullName(doc.getString("fullName"));
                            user.setPhoneNumber(doc.getString("phoneNumber"));
                            user.setBankBranch(doc.getString("bankBranch"));
                            Intent in = new Intent(context, ChoosingAction.class);
                            in.putExtra("user", user);
                            context.startActivity(in);
                        }else{
                            Dialog d = new AlertDialog.Builder(context)
                                    .setIcon(R.drawable.check)
                                    .setTitle("Login Failed, ")
                                    .setMessage("Wrong Banck Account Number or Password")
                                    .setPositiveButton("Close", null)
                                    .show();
                        }
                    }
                }else{
                    System.out.println("elsing");
                    Dialog d = new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Login Failed")
                            .setMessage("User with Bank Account Number " + user.getBankAccountNumber() + " is not exist")
                            .setPositiveButton("close", null)
                            .show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Dialog d = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.check)
                        .setTitle("Login Failed")
                        .setMessage("User with Bank Account Number " + user.getBankAccountNumber() + " is not exist")
                        .setPositiveButton("close", null)
                        .show();
            }
        });
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
                Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(context, LogInActivity.class);
                context.startActivity(in);
            }
        });
        return success[0];
    }

    @Override
    public void retrieveCheck(String id, User user){
        store.collection("Checks").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if(user.getFullName().equals(doc.getString("recipientName"))) {
                    Check check = new Check();
                    check.setSenderName(doc.getString("senderName"));
                    check.setRecipientName(doc.getString("recipientName"));
                    check.setAmount(doc.getString("amount"));
                    check.setBankBranch(doc.getString("bankBranch"));
                    check.setCheckImage(doc.getString("picture"));
                    check.setCheckDate(doc.getString("date"));
                    Intent in = new Intent(context, RetrieveCheck.class);
                    in.putExtra("check", check);
                    context.startActivity(in);
                }
            }
        });
    }
}
