package com.checks.admin;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class CheckInfo extends Activity {

    private ImageView checkImage;
    private TextView id;
    private TextView senderName;
    private TextView recipientName;
    private TextView amount;
    private TextView date;
    private Button accept;
    private Button reject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        id = findViewById(R.id.id);
        senderName = findViewById(R.id.sender);
        recipientName = findViewById(R.id.recipient);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        checkImage = findViewById(R.id.check);

        Check check = (Check) getIntent().getSerializableExtra("check");

        id.setText(check.getId());
        senderName.setText(check.getSender().getFullName());
        recipientName.setText(check.getRecipient().getFullName());
        amount.setText(check.getAmount());
        date.setText(check.getDate());
        Picasso.get().load(Uri.parse(check.getPicture())).into(checkImage);

    }
}
