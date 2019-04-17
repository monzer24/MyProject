package com.projects.check.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.projects.check.database.FirebaseConnection;
import com.projects.check.R;
import com.projects.check.model.User;

public class LogInActivity extends Activity {

    FirebaseConnection connection;
    EditText bankNo;
    EditText password;
    Button logIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        bankNo = findViewById(R.id.bank_no);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_up);

        connection = new FirebaseConnection(this);
        connection.initConnection();

        logIn.setOnClickListener(this.login());
        signUp.setOnClickListener(this.toSignUp());

    }

    private View.OnClickListener toSignUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        };
    }

    View.OnClickListener login() {
        if (bankNo.getText() == null && password.getText() == null) {
            Toast.makeText(LogInActivity.this, "Account number or password fields can not be empty, Please fill them and try again", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.setBankAccountNumber(bankNo.getText().toString());
                    user.setPassword(password.getText().toString());
                    connection.logIn(user);
                }

            };
        }
    }
}
