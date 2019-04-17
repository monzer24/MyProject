package com.projects.check;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class RetrieveCheck extends Activity {

    EditText searchText;
    ImageView search;
    private ImageView checkImage;
    private TextView senderName;
    private TextView recipientName;
    private TextView amount;
    private TextView bankBranch;
    private TextView checkDate;
    private Button cash;

    private User user;
    private FirebaseConnection connection;
    private Check info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrieved);

        user = (User) getIntent().getSerializableExtra("user");
        searchText = findViewById(R.id.checkid);
        search = findViewById(R.id.search);

        checkImage = findViewById(R.id.retrieved_image);
        senderName = findViewById(R.id.sender_name);
        recipientName = findViewById(R.id.recipient_name);
        amount = findViewById(R.id.check_amount);
        bankBranch = findViewById(R.id.check_branch);
        checkDate = findViewById(R.id.check_date);
        cash = findViewById(R.id.cash);
        View[] views = new View[]{
                checkImage, senderName, recipientName, amount, bankBranch, checkDate, cash
        };
        if(getIntent().hasExtra("check")){
            showViews(views);
            info = (Check) getIntent().getSerializableExtra("check");

            Picasso.get().load(Uri.parse(info.getCheckImage())).into(checkImage);
            senderName.setText(info.getSenderName());
            recipientName.setText(info.getRecipientName());
            amount.setText(info.getAmount());
            bankBranch.setText(info.getBankBranch());
            checkDate.setText(info.getCheckDate());
            cash.setEnabled(true);
        }

        connection = new FirebaseConnection(this);
        connection.initConnection();
        if(cash.isEnabled()){
            cash.setOnClickListener(this.checkCash());
        }
        search.setOnClickListener(this.searchForCheck());
    }

    private void showViews(View[] views) {
        for(View v : views){
            v.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener searchForCheck() {
        if(searchText.getText() == null){
            Toast.makeText(RetrieveCheck.this, "Please enter checkID to get the check", Toast.LENGTH_SHORT).show();
            return null;
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.retrieveCheck(searchText.getText().toString(), user);
            }
        };
    }

    private View.OnClickListener checkCash() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connection.cashCheck(info.getCheckId(), user);
            }
        };
    }
}
