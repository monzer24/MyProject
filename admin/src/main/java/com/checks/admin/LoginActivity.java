package com.checks.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    EditText accountNo;
    EditText password;
    Button login;
    private FirebaseConnection connection;
    private AdminUser adminUser;
    private View.OnClickListener logIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adminUser = new AdminUser();
            adminUser.setUserName(accountNo.getText().toString());
            adminUser.setPassword(password.getText().toString());
            connection.logIn(adminUser);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connection = new FirebaseConnection(this);
        connection.init();

        accountNo = findViewById(R.id.bank_no);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        adminUser = new AdminUser();
        login.setOnClickListener(logIn);
    }
}
