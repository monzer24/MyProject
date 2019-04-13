package com.projects.check;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends Activity {

    Connection conn;
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

        logIn.setOnClickListener(this.login(bankNo.getText().toString(), password.getText().toString()));

    }

    View.OnClickListener login(String bankNo, String password){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conn.logIn(bankNo, password);
            }
        };
    }
}
