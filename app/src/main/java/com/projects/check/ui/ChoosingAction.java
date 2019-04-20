package com.projects.check.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.projects.check.R;
import com.projects.check.model.User;

public class ChoosingAction extends Activity {

    private TextView name;
    private Button send;
    private Button receive;
    private Button logout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        send = findViewById(R.id.send);
        receive = findViewById(R.id.receive);
        name = findViewById(R.id.user);
        logout = findViewById(R.id.logout);
        user = (User) getIntent().getSerializableExtra("user");

        name.setText("Welcome\n" + user.getFullName());

        send.setOnClickListener(this.sendActicity());
        receive.setOnClickListener(this.receiveActivity());
        logout.setOnClickListener(this.out());
    }

    private View.OnClickListener out() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoosingAction.this, LogInActivity.class));
                finish();
            }
        };
    }

    private View.OnClickListener receiveActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChoosingAction.this, RetrieveCheck.class);
                in.putExtra("user", user);
                startActivity(in);
                finish();
            }
        };
    }

    private View.OnClickListener sendActicity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ChoosingAction.this, CameraActivity.class);
                in.putExtra("user", user);
                startActivity(in);
                finish();
            }
        };
    }
}
