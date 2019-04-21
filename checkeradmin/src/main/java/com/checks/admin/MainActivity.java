package com.checks.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.R;

public class MainActivity extends AppCompatActivity {

    EditText accountNo;
    EditText password;
    Button login;
    private FirebaseConnection connection;
    private User user;
    private View.OnClickListener logIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user = new User();
            user.setUserName(accountNo.getText().toString());
            user.setPassword(password.getText().toString());
            connection.logIn(user);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new FirebaseConnection(this);
        connection.init();

        accountNo = findViewById(R.id.bank_no);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        user  = new User();
        login.setOnClickListener(logIn);
    }
}
