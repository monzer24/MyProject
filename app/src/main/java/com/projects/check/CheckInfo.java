package com.projects.check;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class CheckInfo extends Activity {

    ImageView capturedImage;
    EditText branch;
    EditText name;
    EditText jodAmount;
    EditText filsAmount;
    EditText date;
    DatePickerDialog picker;
    public static byte[] bytes;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        capturedImage = (ImageView) findViewById(R.id.submited);
        capturedImage.setImageBitmap(bmp);

        branch = findViewById(R.id.branch);
        name = findViewById(R.id.name);
        jodAmount = findViewById(R.id.amount_jod);
        filsAmount= findViewById(R.id.amount_fils);
        date = findViewById(R.id.date);

        date.setOnClickListener(this.datePickerListener);

    }




}
