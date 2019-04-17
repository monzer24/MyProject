package com.projects.check.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.projects.check.model.Check;
import com.projects.check.database.FirebaseConnection;
import com.projects.check.R;
import com.projects.check.model.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckInfo extends Activity {

    TextView senderName;
    ImageView capturedImage;
    Spinner branch;
    EditText name;
    EditText jodAmount;
    EditText filsAmount;
    EditText date;
    DatePickerDialog picker;
    Button submit;

    private FirebaseConnection connect;
    private User user;
    
    private View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            picker = new DatePickerDialog(CheckInfo.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
                }
            }, year, month, day);
            picker.show();
        }
    };

    private View.OnClickListener submitListener = new View.OnClickListener() {
        // Upload the taken photo to storage firebase;
        @Override
        public void onClick(View v) {
            Check check = new Check();
            check.setBankBranch(branch.getSelectedItem().toString());
            check.setRecipientName(name.getText().toString());
            check.setAmount(jodAmount.getText().toString(), filsAmount.getText().toString());
            check.setCheckDate(date.getText().toString());

            Bitmap bit = ((BitmapDrawable)capturedImage.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();

           upload(bytes, check);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        user = (User) getIntent().getSerializableExtra("user");

        String filePath=getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        capturedImage = findViewById(R.id.submited);
        capturedImage.setImageBitmap(bmp);
        senderName = findViewById(R.id.sender);
        senderName.setText(user.getFullName());
        branch = findViewById(R.id.bank_branch);
        name = findViewById(R.id.name);
        jodAmount = findViewById(R.id.amount_jod);
        filsAmount= findViewById(R.id.amount_fils);
        date = findViewById(R.id.date);
        submit = findViewById(R.id.submit);

        connect = new FirebaseConnection(this);
        connect.initConnection();

        date.setOnClickListener(this.datePickerListener);
        submit.setOnClickListener(this.submitListener);

    }

    private void upload(byte[] bytes, Check info) {

        Map<String, Object> checkInfo = new HashMap<>();
        checkInfo.put("bankBranch", info.getBankBranch());
        checkInfo.put("recipientName", info.getRecipientName());
        checkInfo.put("amount", info.getAmount());
        checkInfo.put("date", info.getCheckDate());
        checkInfo.put("senderName", user.getFullName());

        connect.uploadImage(bytes, checkInfo, user);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent out = new Intent(this, ChoosingAction.class);
        out.putExtra("user", user);
        startActivity(out);
        finish();
    }
}
