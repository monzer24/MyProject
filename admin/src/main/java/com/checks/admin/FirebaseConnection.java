package com.checks.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                                if(e == null){
                                    List<Check> checks = new ArrayList<>();
                                    System.out.println(queryDocumentSnapshots.size());
                                    for(DocumentSnapshot doc : queryDocumentSnapshots){
                                        User user = doc.get("Recipient User", User.class);
                                        System.out.println(user.getBalance() + " " + user.getFullName());
                                        Check check = doc.get("Check", Check.class);
                                        check.setRecipient(user);
                                        check.setSender(getUSer((Map<String, Object>) doc.get("Check")));
                                        checks.add(check);
                                    }
                                    Intent in = new Intent(context, CashedChecks.class);
                                    System.out.println(checks.size());
                                    in.putExtra("checks", (Serializable) checks);
                                    context.startActivity(in);
                                }
                            }
                            User getUSer(Map<String, Object> checkMap){
                                Map<String, String> map = (Map<String, String>) checkMap.get("Sender");
                                System.out.println(map);
                                 for(String s : map.values()){
                                    System.out.println(s);
                                }
                                User user = new User();
                                user.setBankAccountNumber(map.get("bankAccountNumber"));
                                user.setFullName(map.get("fullName"));
                                user.setBalance(map.get("balance")); // Double.parseDouble((String) info.get("amount"))
                                user.setBankBranch(map.get("bankBranch"));
                                user.setPassword(map.get("password"));
                                user.setPhoneNumber("phoneNumber");
                                user.setSsn(map.get("ssn"));
                                return user;
                            }
                        });

                    }
                }else {//jj
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


