package com.projects.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends Activity {

    EditText fullName;
    EditText phoneNumber;
    EditText password;
    Spinner bankBranch;
    EditText accountNumber;
    Button signup;
    private FirebaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        fullName = findViewById(R.id.fullname);
        phoneNumber = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        bankBranch = findViewById(R.id.bank);
        accountNumber = findViewById(R.id.account);
        signup = findViewById(R.id.signUp);

        connection = new FirebaseConnection(this);
        connection.initConnection();

        signup.setOnClickListener(this.signUp());

    }

    private View.OnClickListener signUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();

                user.setFullName(fullName.getText().toString());
                user.setPhoneNumber(phoneNumber.getText().toString());
                user.setPassword(password.getText().toString());
                user.setBankAccountNumber(accountNumber.getText().toString());
                user.setBankBranch(bankBranch.getSelectedItem().toString());
                System.out.println("user is that :  " + user.getBankAccountNumber() + " " + user.getPassword() + " " + user.getFullName() + " " + user.getBankBranch() + "  " + user.getPhoneNumber());
                boolean flag = connection.signUp(user);
                System.out.println(flag);
                if(flag){
                    Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(in);
                }
            }
        };
    }
}
