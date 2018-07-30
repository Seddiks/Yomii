package com.app.seddik.yomii.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.GalleryPhotosAdapter;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import java.util.ArrayList;

public class ProfileAbonneActivity extends AppCompatActivity {
    private final Integer image_ids[] = {
            R.drawable.bg_milan,
            R.drawable.bg_paris,
            R.drawable.bg_london,
            R.drawable.bg_moscow,
            R.drawable.bg_madrid,
            R.drawable.bg_munich,
            R.drawable.bg_barca,
            R.drawable.bg_ny,
    };
    ImageView cover;
    TextView profile_name, pubs, abonnes, abonnements;
    Toolbar toolbar;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private Button btnFollow;
    private boolean clicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_abonne);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Fredj Seddik");

        CollapsingToolbarLayout collapsingToolbar =  findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        cover =  findViewById(R.id.cover_photo);
        profile_name =  findViewById(R.id.profile_name);
        pubs =  findViewById(R.id.pubs);
        abonnes = findViewById(R.id.abonnes);
        abonnements =  findViewById(R.id.abonnements);
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(),"LeagueSpartan-Bold.otf");
        Typeface type2 = Typeface.createFromAsset(getApplicationContext().getAssets(),"LinguisticsPro-Bold.otf");
        profile_name.setTypeface(type);
        //  pubs.setTypeface(type2);
        //  abonnes.setTypeface(type2);
        //  abonnements.setTypeface(type2);



        btnFollow =  findViewById(R.id.following);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked == false) {
                    btnFollow.setBackgroundResource(R.drawable.round_border_follwing);
                    btnFollow.setText("following");
                    btnFollow.setTextColor(Color.BLACK);
                    clicked = true;
                } else {
                    btnFollow.setBackgroundResource(R.drawable.round_border_follow);
                    btnFollow.setText("follow");
                    btnFollow.setTextColor(Color.WHITE);
                    clicked = false;

                }

            }
        });


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        mGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mGridLayoutManager);


        ArrayList<GalleryPhotosItems> galleryPhotosItemses = prepareData();
        GalleryPhotosAdapter adapter = new GalleryPhotosAdapter(getApplicationContext(), galleryPhotosItemses);
        recyclerView.setAdapter(adapter);


    }

    private ArrayList<GalleryPhotosItems> prepareData(){

        ArrayList<GalleryPhotosItems> theimage = new ArrayList<>();
        for(int i = 0; i< image_ids.length; i++){
            GalleryPhotosItems galleryPhotosItems = new GalleryPhotosItems();
            galleryPhotosItems.setImage_ID(image_ids[i]);
            theimage.add(galleryPhotosItems);
        }
        return theimage;
    }

}
