package com.example.checkeradmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConnection implements Connection {

    private FirebaseFirestore store;
    private Context context;

    public FirebaseConnection(Context context) {
        this.context = context;
    }

    public void init(){
        store = FirebaseFirestore.getInstance();
    }

    @Override
    public void logIn(final User user) {
        store.collection("BankUsers").document(user.getUserName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    DocumentSnapshot doc = task.getResult();
                    user.setFullName(doc.getString("fullName").trim());
                    System.out.println(user.getFullName() + " "+ user.getUserName());
                    Intent in = new Intent(context, CheckChecks.class);
                    in.putExtra("user", user);
                    context.startActivity(in);
                }catch (NullPointerException e){
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.check)
                            .setTitle("Login Failed, ")
                            .setMessage("Wrong Bank Account Number or Password")
                            .setPositiveButton("Close", null)
                            .show();
                }
            }
        });
    }
}
