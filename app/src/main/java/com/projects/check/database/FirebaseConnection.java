package com.projects.check.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projects.check.R;
import com.projects.check.model.Check;
import com.projects.check.model.User;
import com.projects.check.ui.ChoosingAction;
import com.projects.check.ui.LogInActivity;
import com.projects.check.ui.RetrieveCheck;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseConnection implements Connection<String, Object> {

    private FirebaseFirestore store;
    private StorageReference storage;
    Context context;
    String res;
    static ProgressDialog dia;

    public FirebaseConnection(Context context) {
        this.context = context;
    }

    public void initConnection(){
        store = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void uploadImage(byte[] bytes, Map<String, Object> info, User user) {
        StorageReference ref = storage.child("images/" + new Date().toString());
        UploadTask uploadTask = ref.putBytes(bytes); // start uploading
        System.out.println(Double.parseDouble(user.getBalance()) <= Double.parseDouble((String) info.get("amount")));

        if(Double.parseDouble((String) info.get("amount")) <= Double.parseDouble(user.getBalance())){

            dia = new ProgressDialog(context);
            dia.setMessage("Uploading ...");
            dia.setCancelable(false);
            dia.setButton( "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadTask.cancel();
                }
            });
            dia.show();
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
                        addCheck(res, info, user);
                    }
                }
            });
        }else{
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.x)
                    .setTitle("Failed !")
                    .setMessage("Your balance is " + user.getBalance() + ", not enough to cash this check, please check out your balance your the check amount")
                    .setNegativeButton("OK", null)
                    .show();
        }
    }

    @Override
    public String addCheck(String url, Map<String ,Object> info, User user) {
        info.put("picture", url);
        info.put("Sender", user);
        String docId = (String) info.get("id");
        try {
            store.collection("Checks").document(docId).set(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dia.dismiss();
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Successful !")
                            .setMessage("Check ID : " + docId + " Copy it and send it to the recipient")
                            .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData data = ClipData.newPlainText(docId, docId);
                                    clip.setPrimaryClip(data);
                                    Toast.makeText(context, docId + "has been Copied to clip board", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(context, ChoosingAction.class);
                                    in.putExtra("user", user);
                                    context.startActivity(in);
                                    ((Activity)context).finish();
                                }
                            }).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.x)
                            .setTitle("Failed !")
                            .setMessage("A failure happened and the check has not been uploaded")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addCheck(url, info, user);
                                }
                            }).show();
                }
            });
            return docId;
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public void logIn(User user) {
         store.collection("Users").document(user.getBankAccountNumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task != null){
                    DocumentSnapshot doc = task.getResult();
                    if(user.getBankAccountNumber().equals(doc.get("accountNo")) && user.getPassword().equals(doc.get("password"))){
                        user.setFullName(doc.getString("name"));
                        user.setPhoneNumber(doc.getString("phoneNo"));
                        user.setBankBranch(doc.getString("branch"));
                        user.setBalance(doc.getString("balance"));
                        user.setSsn(doc.getString("ssn"));
                        Intent in = new Intent(context, ChoosingAction.class);
                        Toast.makeText(context, "Logged in Successfully" + user.getFullName(), Toast.LENGTH_SHORT).show();
                        in.putExtra("user", user);
                        context.startActivity(in);
                        ((Activity)context).finish();
                    }else{
                        new AlertDialog.Builder(context)
                                .setIcon(R.drawable.check)
                                .setTitle("Login Failed, ")
                                .setMessage("Wrong Banck Account Number or Password")
                                .setPositiveButton("Close", null)
                                .show();
                    }
                }else{
                    new AlertDialog.Builder(context)
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
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.check)
                        .setTitle("Login Failed")
                        .setMessage("User with Bank Account Number " + user.getBankAccountNumber() + " is not exist")
                        .setPositiveButton("close", null)
                        .show();
            }
        });
    }

    @Override
    public void signUp(User user) {
        store.collection("Bank").document(user.getBankAccountNumber()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                try {
                    if (doc != null && user.getBankAccountNumber().contains(doc.getString("accountNo").trim()) && user.getBankBranch().equals(doc.getString("branch").trim())) {
                        System.out.println(user.getBankAccountNumber().length() + " " + doc.getString("accountNo").trim().length() + "_" + user.getBankBranch().length() + " " + doc.getString("branch").trim().length());
                        System.out.println("true");
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("name", user.getFullName());
                        userMap.put("accountNo", user.getBankAccountNumber());
                        userMap.put("branch", user.getBankBranch());
                        userMap.put("phoneNo", user.getPhoneNumber());
                        userMap.put("password", user.getPassword());
                        userMap.put("ssn", doc.getString("ssn"));
                        userMap.put("balance", doc.getString("balance"));
                        store.collection("Users").document(user.getBankAccountNumber()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(context, LogInActivity.class);
                                context.startActivity(in);
                                ((Activity) context).finish();
                            }
                        });
                    }
                }catch (NullPointerException e){
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Failed !")
                            .setMessage("This user does not exist in this bank, Please visit the bank to make one")
                            .setPositiveButton("Ok", null)
                            .show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.check)
                        .setTitle("Failed !")
                        .setMessage("This user does not exist in this bank, Please visit the bank to make one")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent in = new Intent(context, ChoosingAction.class);
                                in.putExtra("user", user);
                                context.startActivity(in);
                                ((Activity)context).finish();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void retrieveCheck(String id, User user){
        store.collection("Checks").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if(doc != null && user.getFullName().equals(doc.getString("recipientName").trim()) && user.getBankBranch().equals(doc.getString("bankBranch"))) {
                    Check check = new Check();
                    check.setCheckId(id);
                    check.setSenderName(doc.getString("senderName").trim());
                    check.setRecipientName(doc.getString("recipientName").trim());
                    check.setAmount(doc.getString("amount").trim());
                    check.setBankBranch(doc.getString("bankBranch").trim());
                    check.setCheckImage(doc.getString("picture").trim());
                    check.setCheckDate(doc.getString("date").trim());

                    Intent in = new Intent(context, RetrieveCheck.class);
                    in.putExtra("check", check);
                    in.putExtra("user", user);
                    context.startActivity(in);
                    ((Activity)context).finish();
                }else{
                    Toast.makeText(context, "No check for your with this ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void cashCheck(String id, User user) {
        store.collection("Checks").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sendToBank(documentSnapshot, user);
            }
        });
    }

    private void sendToBank(DocumentSnapshot doc, User user){
        Map<String, Object> bankData = new HashMap<>();
        bankData.put("Recipient User", user);
        bankData.put("Check", doc.getData());
        bankData.put("id", doc.getId());
        store.collection("Cashed Checks").document(doc.getId()).set(bankData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                store.collection("Checks").document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new AlertDialog.Builder(context)
                                .setIcon(R.drawable.check)
                                .setTitle("Successful !")
                                .setMessage("This check Has been sent to the bank, Please wait for bank confirmation")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent in = new Intent(context, ChoosingAction.class);
                                        in.putExtra("user", user);
                                        context.startActivity(in);
                                        ((Activity)context).finish();
                                    }
                                })
                                .show();
                    }
                });
            }
        });
    }
}
