package com.example.checkeradmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText accountNo;
    EditText password;
    Button login;
    private FirebaseConnection connection;
    private User user;
    private View.OnClickListener logIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
        login.setOnClickListener(logIn);
    }
}
