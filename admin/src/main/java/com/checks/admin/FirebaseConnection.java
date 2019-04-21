package com.checks.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class FirebaseConnection implements Connection {

    private FirebaseFirestore store;
    private Context context;

    public FirebaseConnection(Context context) {
        this.context = context;
    }

    public void init(){
        FirebaseApp.initializeApp(context);
        store = FirebaseFirestore.getInstance();
    }

    @Override
    public void logIn(final AdminUser adminUser) {
        System.out.println(adminUser.getUserName());
        store.collection("BankUser").document(adminUser.getUserName().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()) {
                    DocumentSnapshot doc = task.getResult();
                    if(adminUser.getUserName().equals(doc.getString("userName")) && adminUser.getPassword().equals(doc.getString("password"))) {
                        adminUser.setFullName(doc.getString("userName"));
                        System.out.println(adminUser.getUserName() + " " + adminUser.getPassword() + " " + adminUser.getFullName());
                        store.collection("Cashed Checks").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                if(e == null){
//                                    System.out.println(queryDocumentSnapshots.size());
//                                    for(DocumentSnapshot doc : queryDocumentSnapshots){
//                                        User user = doc.get("User", User.class);
//                                        System.out.println(user.getBalance() + " " + user.getFullName());
//                                        Check check = doc.get("Check", Check.class);
//                                        Class<User> rec = (Class<User>) doc.getData().get("User").getClass();
//                                        System.out.println(check.getAmount() + " " + check.getCheckImage());
//                                    }
//                                }
                            }
                        });

                    }
                }else {
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Login Failed, ")
                            .setMessage("Wrong Banck Account Number or Password")
                            .setPositiveButton("Close", null)
                            .show();
                }

            }
        });
    }
}


