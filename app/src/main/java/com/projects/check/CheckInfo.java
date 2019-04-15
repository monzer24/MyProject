package com.projects.check;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckInfo extends Activity {
    String docID = "";

    ImageView capturedImage;
    EditText branch;
    EditText name;
    EditText jodAmount;
    EditText filsAmount;
    EditText date;
    DatePickerDialog picker;
    Button submit;
    private Check check;

    FirebaseConnection connect;
    
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
            check = new Check();
            check.setBankBranch(branch.getText().toString());
            check.setRecipientName(name.getText().toString());
            check.setAmount(Integer.valueOf(jodAmount.getText().toString()),
                    Integer.valueOf(jodAmount.getText().toString()));
            check.setCheckDate(date.getText().toString());

            Bitmap bit = ((BitmapDrawable)capturedImage.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();

           upload(bytes);

        }
    };

    private boolean upload(byte[] bytes) {
        connect.uploadImage(bytes);
        String url = FirebaseConnection.imageURL();
                System.out.println(url + " there");
        if(url != null){
            docID = addCheck(url);
            if(docID != null){
                AlertDialog d = new AlertDialog.Builder(this)
                        .setTitle("Failed")
                        .setMessage("Image has been uploaded successfully, but there is error with the check info. Please check out the info")
                        .setPositiveButton("OK", null)
                        .setIcon(R.drawable.common_google_signin_btn_icon_disabled)
                        .show();
                return true;
            }else{
               AlertDialog d = new AlertDialog.Builder(this)
                       .setTitle("Failed")
                       .setMessage("Image has been uploaded successfully, but there is error with the check info. Please check out the info")
                       .setPositiveButton("OK", null)
                       .setIcon(R.drawable.x)
                       .show();
                Toast.makeText(this, "Check Image uploaded successfully but not the check info, Please make sure ", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
//            Toast.makeText(this,  "Could not upload the Image, Please check out your connection", Toast.LENGTH_LONG).show();
            AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle("Failed")
                    .setMessage("Image has been uploaded successfully, but there is error with the check info. Please check out the info")
                    .setPositiveButton("OK", null)
                    .setIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .show();
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        String filePath=getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        capturedImage = findViewById(R.id.submited);
        capturedImage.setImageBitmap(bmp);

        branch = findViewById(R.id.branch);
        name = findViewById(R.id.name);
        jodAmount = findViewById(R.id.amount_jod);
        filsAmount= findViewById(R.id.amount_fils);
        date = findViewById(R.id.date);
        submit = findViewById(R.id.submit);

        connect = new FirebaseConnection();
        connect.initConnection();

        date.setOnClickListener(this.datePickerListener);
        submit.setOnClickListener(this.submitListener);

    }

    private String addCheck(String path){
        Map<String, Object> checkInfo = new HashMap<>();
        checkInfo.put("branch", check.getBankBranch());
        checkInfo.put("name", check.getRecipientName());
        checkInfo.put("amount", check.getAmount());
        checkInfo.put("date", check.getCheckDate());
        checkInfo.put("picture", path);

        return connect.addCheck(path, checkInfo);
    }

}
