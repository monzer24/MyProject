package com.projects.check;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RetrievedCheck extends Activity {

    private ImageView checkImage;
    private TextView senderName;
    private TextView recipientName;
    private TextView amount;
    private TextView bankBranch;
    private TextView checkDate;
    private Button cash;

    private FirebaseConnection connection;
    private Check info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieved);

        checkImage = findViewById(R.id.retrieved_image);
        senderName = findViewById(R.id.sender_name);
        recipientName = findViewById(R.id.recipient_name);
        amount = findViewById(R.id.check_amount);
        bankBranch = findViewById(R.id.check_branch);
        checkDate = findViewById(R.id.check_date);

        connection = new FirebaseConnection(this);
        connection.initConnection();
        cash.setOnClickListener(this.checkCash());

    }

    private View.OnClickListener checkCash() {
        connection.retrieveCheck("");
        return null;
    }
}
