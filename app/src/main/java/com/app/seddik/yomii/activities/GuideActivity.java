package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.models.GuideItems;
import com.bumptech.glide.Glide;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class GuideActivity extends AppCompatActivity {
    private FrameLayout frameExperience,frameHistory,frameBudget, frameBestTime, frameBestPlace,frameRestaurant,
            frameTransportation, frameLanguage, frameOtherInfo;
    private TextView title, location, experience, history, budget_advice, best_time_to_visit, best_place_to_visit,restaurant_suggestions,
            transportation_advice, language, other_informations;
    private ImageView photo, update;
    private GuideItems.Data guideItems;
    private String mPathPhoto;
    Uri mUriPhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Guide");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
        CollapsingToolbarLayout collapsingToolbar =  findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("  ");


        frameExperience = findViewById(R.id.frameExperience);
        frameHistory = findViewById(R.id.frameHistory);
        frameBudget = findViewById(R.id.frameBudget);
        frameBestTime = findViewById(R.id.frameBestTime);
        frameBestPlace = findViewById(R.id.frameBestPlace);
        frameRestaurant = findViewById(R.id.frameRestaurant);
        frameTransportation = findViewById(R.id.frameTransportation);
        frameLanguage = findViewById(R.id.frameLanguage);
        frameOtherInfo = findViewById(R.id.frameOtherInfo);

        title = findViewById(R.id.title);
        location = findViewById(R.id.location);
        experience = findViewById(R.id.experience);
        history = findViewById(R.id.history);
        budget_advice = findViewById(R.id.budget_advice);
        best_time_to_visit = findViewById(R.id.best_time_to_visit);
        best_place_to_visit = findViewById(R.id.best_place_to_visit);
        restaurant_suggestions = findViewById(R.id.restaurant_suggestions);
        transportation_advice = findViewById(R.id.transportation_advice);
        language = findViewById(R.id.language);
        other_informations = findViewById(R.id.other_informations);

        photo = findViewById(R.id.photo);
        update = findViewById(R.id.update);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            guideItems = new GuideItems.Data();
            guideItems = (GuideItems.Data) getIntent().getExtras().getSerializable("mGuide");
            mPathPhoto = URL_UPLOAD_PHOTOS+guideItems.getPhoto_path();
            if (guideItems.getPathPhoto_from_uri() != null){
                mUriPhoto = Uri.parse(guideItems.getPathPhoto_from_uri());
            }
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
            location.setText(guideItems.getLocation());
            title.setText(guideItems.getTitle_guide());

            if (!guideItems.getExperience().isEmpty()){
                experience.setText(guideItems.getExperience());
                frameExperience.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getHistory().isEmpty()){
                history.setText(guideItems.getHistory());
                frameHistory.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getBudget_advice().isEmpty()){
                budget_advice.setText(guideItems.getBudget_advice());
                frameBudget.setVisibility(View.VISIBLE);


            }
            if (!guideItems.getBest_time_to_visit().isEmpty()){
                best_time_to_visit.setText(guideItems.getBest_time_to_visit());
                frameBestTime.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getBest_place_to_visit().isEmpty()){
                best_place_to_visit.setText(guideItems.getBest_place_to_visit());
                frameBestPlace.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getRestaurant_suggestions().isEmpty()){
                restaurant_suggestions.setText(guideItems.getRestaurant_suggestions());
                frameRestaurant.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getTransportation_advice().isEmpty()){
                transportation_advice.setText(guideItems.getTransportation_advice());
                frameTransportation.setVisibility(View.VISIBLE);

            }
            if (!guideItems.getLanguage().isEmpty()){
                language.setText(guideItems.getLanguage());
                frameLanguage.setVisibility(View.VISIBLE);


            }
            if (!guideItems.getOther_informations().isEmpty()){
                other_informations.setText(guideItems.getOther_informations());
                frameOtherInfo.setVisibility(View.VISIBLE);


            }
    //Package Name: "com.app.seddik.yomii"
 // SHA1: "fb0cb10b3eb8272e10947b91da028def04b7e4b4"
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateGuideActivity.class);
                intent.putExtra("mGuide", guideItems);
                startActivity(intent);
                finish();

            }
        });



    }
}
