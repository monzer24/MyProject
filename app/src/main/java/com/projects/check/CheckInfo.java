package com.projects.check;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class CheckInfo extends Activity {

    ImageView capturedImage;
    public static byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        capturedImage = (ImageView) findViewById(R.id.submited);
        capturedImage.setImageBitmap(bmp);
    }
}
