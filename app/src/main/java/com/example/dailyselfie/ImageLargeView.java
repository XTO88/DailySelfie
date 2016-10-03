package com.example.dailyselfie;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;



public class ImageLargeView extends Activity {

    private ImageView mImageView;
    String photoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_image_layout);

        mImageView = (ImageView)findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();
        photoPath = extras.getString("path");

        if(photoPath!=null) {
            mImageView.setImageBitmap(BitmapFactory.decodeFile(photoPath));
        }
        else Toast.makeText(getApplicationContext(),"No path",Toast.LENGTH_SHORT).show();
    }

}