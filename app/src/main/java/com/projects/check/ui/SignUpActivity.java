package com.projects.check.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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
    private View.OnClickListener signUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(fullName.getText().toString().equals("") && phoneNumber.getText().toString().equals("") && password.getText().toString().equals("") && accountNumber.getText().toString().equals("") && bankBranch.getSelectedItemPosition() == 0){
                Toast.makeText(SignUpActivity.this, "You have empty fields, please fill them out", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = new User();
            user.setFullName(fullName.getText().toString().trim());
            user.setPhoneNumber(phoneNumber.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.setBankAccountNumber(accountNumber.getText().toString().trim());
            user.setBankBranch(bankBranch.getSelectedItem().toString().trim());
            connection.signUp(user);
        }
    };

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

        signup.setOnClickListener(this.signUp);

    }

}
