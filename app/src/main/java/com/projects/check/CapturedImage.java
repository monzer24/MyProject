package com.projects.check;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;

public class CapturedImage extends Activity {

    ImageView captured;
    ImageView dismiss;
    ImageView next;
    public static byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured);

        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        captured = findViewById(R.id.captured_image);
        captured.setImageBitmap(bit);

        dismiss = findViewById(R.id.x);
        next = findViewById(R.id.next);

        dismiss.setOnClickListener(this.dismissImage);
        next.setOnClickListener(this.nextImage);

    }

    View.OnClickListener dismissImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent back = new Intent(CapturedImage.this, MainActivity.class);
            startActivity(back);
        }
    };

    View.OnClickListener nextImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(CapturedImage.this,CheckInfo.class);
            CheckInfo.bytes = bytes;
            startActivity(intent);
        }
    };

}
