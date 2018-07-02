package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;

import com.app.seddik.yomii.models.TravelStoryItems;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class TravelStoryActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView photo, update;
    private TextView location, title, full_story;
    private int travel_story_id, user_id;
    private String mPathPhoto, mLocation, mTitle, mFull_story;
    private TravelStoryItems.Data travelStoryItems;
    Uri mUriPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_story);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Story");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        CollapsingToolbarLayout collapsingToolbar =  findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("  ");

        photo = findViewById(R.id.story_photo);
        update = findViewById(R.id.update);
        location = findViewById(R.id.location);
        title = findViewById(R.id.title);
        full_story = findViewById(R.id.full_story);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            travelStoryItems = new TravelStoryItems.Data();
            travelStoryItems = (TravelStoryItems.Data) getIntent().getExtras().getSerializable("mStory");
            travel_story_id = travelStoryItems.getTravel_story_id() ;
            mPathPhoto = URL_UPLOAD_PHOTOS+travelStoryItems.getPhoto_path();
            if (travelStoryItems.getPathPhoto_from_uri() != null){
                mUriPhoto = Uri.parse(travelStoryItems.getPathPhoto_from_uri());
            }
            mLocation = travelStoryItems.getLocation();
            mTitle = travelStoryItems.getTitle();
            mFull_story = travelStoryItems.getFull_story();

            if (mUriPhoto != null){
                Glide.with(getApplicationContext())
                        .load(mUriPhoto)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(photo);

            }else {
                Glide.with(getApplicationContext())
                        .load(mPathPhoto)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(photo);

            }
            location.setText(mLocation);
            title.setText(mTitle);
            full_story.setText(mFull_story);


        }
        update.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.update:
                Intent intent = new Intent(TravelStoryActivity.this, CreateTravelStoryActivity.class);
                intent.putExtra("mStory",travelStoryItems );
                TravelStoryActivity.this.startActivity(intent);
                finish();
                break;

        }

    }
}
