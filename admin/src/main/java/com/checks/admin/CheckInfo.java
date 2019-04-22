package com.checks.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    private Check check;
    private FirebaseConnection connection;
    private View.OnClickListener rejectAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Context context = CheckInfo.this;
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.check)
                    .setTitle("Login Failed, ")
                    .setMessage("Wrong Banck Account Number or Password")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            connection.reject(check);
                        }
                    })
                    .setNegativeButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String phone = check.getSender().getPhoneNumber();
                            ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("Phone Nubmber", phone);
                            clip.setPrimaryClip(data);
                            Toast.makeText(context, phone + "has been Copied to clip board", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(context, CashedChecks.class);
                            context.startActivity(in);
                            ((Activity)context).finish();
                        }
                    })
                    .show();
        }
    };
    private View.OnClickListener acceptAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            connection.accept(check);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        connection = new FirebaseConnection(this);
        connection.init();

        id = findViewById(R.id.id);
        senderName = findViewById(R.id.sender);
        recipientName = findViewById(R.id.recipient);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        checkImage = findViewById(R.id.check);

        check = (Check) getIntent().getSerializableExtra("check");

        id.setText(check.getId());
        senderName.setText(check.getSender().getFullName());
        recipientName.setText(check.getRecipient().getFullName());
        amount.setText(check.getAmount());
        date.setText(check.getDate());
        Picasso.get().load(Uri.parse(check.getPicture())).into(checkImage);

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        accept.setOnClickListener(this.acceptAction);
        reject.setOnClickListener(this.rejectAction);

    }
}
