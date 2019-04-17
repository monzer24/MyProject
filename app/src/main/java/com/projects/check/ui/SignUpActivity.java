package com.projects.check.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.projects.check.database.FirebaseConnection;
import com.projects.check.R;
import com.projects.check.model.User;

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
                connection.signUp(user);

            }
        };
    }
}
