package com.projects.check;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class CapturedImage extends Activity {

    ImageView captured;
    ImageView dismiss;
    ImageView next;
    public static byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured);
//        Bundle bundle = new Bundle();
//        bundle = getIntent().getBundleExtra("bArray");
//        bytes = bundle.getByteArray("array");

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
//            Drawable drawable=captured.getDrawable();
//            Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] b = baos.toByteArray();
            Intent intent=new Intent(CapturedImage.this,CheckInfo.class);
            CheckInfo.bytes = bytes;
            startActivity(intent);
        }
    };

    private byte[] decode(Bitmap image){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, out);
        return out.toByteArray();
    }

}
