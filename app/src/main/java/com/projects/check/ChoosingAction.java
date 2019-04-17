package com.projects.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosingAction extends Activity {

    private TextView name;
    private Button send;
    private Button receive;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        name = findViewById(R.id.user);
        user = (User) getIntent().getSerializableExtra("user");
        Toast.makeText(this, "Logged in Successfully" + user.getFullName(), Toast.LENGTH_SHORT).show();

        name.setText("Welcome\n" + user.getFullName());
        System.out.println(user.getBankAccountNumber() + " " + user.getPhoneNumber() + " " + user.getBankBranch() + " " + user.getFullName() + " " + user.getPassword());

        send.setOnClickListener(this.sendActicity());
        receive.setOnClickListener(this.receiveActivity());
    }

    private View.OnClickListener receiveActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChoosingAction.this, RetrieveCheck.class);
                in.putExtra("user", user);
                startActivity(in);
            }
        };
    }

    private View.OnClickListener sendActicity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChoosingAction.this, MainActivity.class);
                in.putExtra("user", user);
                startActivity(in);
            }
        };
    }
}
