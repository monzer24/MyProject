package com.projects.check;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Calendar;
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
    public static byte[] bytes;
    private Check check;
    
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
            Toast.makeText(CheckInfo.this, "Please wait, the data is being uploaded", Toast.LENGTH_LONG);
            check = new Check();
            check.setBankBranch(branch.getText().toString());
            check.setRecipientName(name.getText().toString());
            check.setAmount(Integer.valueOf(jodAmount.getText().toString()),
                    Integer.valueOf(jodAmount.getText().toString()));
            check.setCheckDate(date.getText().toString());

           uploadImage();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        capturedImage = findViewById(R.id.submited);
        capturedImage.setImageBitmap(bmp);

        branch = findViewById(R.id.branch);
        name = findViewById(R.id.name);
        jodAmount = findViewById(R.id.amount_jod);
        filsAmount= findViewById(R.id.amount_fils);
        date = findViewById(R.id.date);
        submit = findViewById(R.id.submit);

        date.setOnClickListener(this.datePickerListener);
        submit.setOnClickListener(this.submitListener);

    }
    
    
    private void uploadImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child("images/check.jpg");
        UploadTask uploadTask = ref.putBytes(bytes); // start uploading

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    addCheck(task.getResult().toString());
                } else {
                    Toast.makeText(CheckInfo.this, "Failed to upload the captured image, Please check out your internet connection", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void addCheck(String path){

        FirebaseFirestore store = FirebaseFirestore.getInstance();
        Map<String, Object> checkInfo = new HashMap<>();
        checkInfo.put("branch", check.getBankBranch());
        checkInfo.put("name", check.getRecipientName());
        checkInfo.put("amount", check.getAmount());
        checkInfo.put("date", check.getCheckDate());
        checkInfo.put("picture", path);

        System.out.println(checkInfo);
        store.collection("Checks").add(checkInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                docID = documentReference.getId();
                System.out.println(docID);
            }
        });
        
    }

}
