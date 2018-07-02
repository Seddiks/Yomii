package com.app.seddik.yomii.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getSupportActionBar().hide();

        PhotoView photoView = findViewById(R.id.photo_view);
        if (getIntent().getExtras().getString("PathPhoto") != null) {
            String path_photo = getIntent().getExtras().getString("PathPhoto");
            Glide.with(getApplicationContext()).load(path_photo).into(photoView);

        }




    }

}
