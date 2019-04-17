package com.projects.check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CapturedImage extends Activity {

    ImageView captured;
    ImageView dismiss;
    ImageView next;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured);

        user = (User) getIntent().getSerializableExtra("user");

        String filePath=getIntent().getStringExtra("path");
        File file = new File(filePath);
        Bitmap bit = BitmapFactory.decodeFile(file.getAbsolutePath());

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
            back.putExtra("user", user);
            startActivity(back);
        }
    };

    View.OnClickListener nextImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap map = ((BitmapDrawable)captured.getDrawable()).getBitmap();
            String filePath= tempFileImage(CapturedImage.this,map,"image");
            Intent intent = new Intent(CapturedImage.this, CheckInfo.class);
            intent.putExtra("path", filePath);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    };

    private String tempFileImage(Context context, Bitmap bitmap, String name) {
        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }

}
