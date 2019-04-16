package com.projects.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

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

        logIn.setOnClickListener(this.login(bankNo.getText().toString(), password.getText().toString()));
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

    View.OnClickListener login(String bankNo, String password){
        User user = new User();
        if(bankNo != null && password != null) {
            user.setBankAccountNumber(bankNo);
            user.setPassword(password);
        }else{
            Toast.makeText(LogInActivity.this, "Account number or password fields can not be empty, Please fill them and try again", Toast.LENGTH_SHORT).show();
            return null;
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection.logIn(user).getFullName() != null){
                    Toast.makeText(LogInActivity.this, "Welcome Mr. " + user.getFullName(), Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LogInActivity.this, ChooseingAction.class);
                    in.putExtra("user", (Serializable) user);
                    startActivity(in);
                }else{
                    Toast.makeText(LogInActivity.this, "Wrong account number or password, please check them out and try again", Toast.LENGTH_SHORT).show();
                }
            }

        };
    }
}
