package com.app.seddik.yomii.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.DisplayPhotosPublishedAdapter;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.app.seddik.yomii.models.GalleryAlbumsItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.networks.ApiService;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class DisplayPhotosPublishedActivity extends AppCompatActivity {
    DisplayPhotosPublishedAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<GalleryAlbumsItems.Paths> ListPathsPhotos;
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
    private  static ArrayList<ResponsePhotoItems.Paths> paths;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photos_published);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setCancelable(false);

        recyclerView =  findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(mGridLayoutManager);

        fillPhotosPublished();
    }


    private void fillPhotosPublished() {
        ArrayList<DisplayPhotosPublishedItems> item = new ArrayList<>();
        //Display Album items from ALBUMS FRAGMENT
        if (getIntent().getExtras().getSerializable("ALBMUM_Fragment_ListPaths") != null) {
            ListPathsPhotos = new ArrayList<>();
            ListPathsPhotos = (ArrayList<GalleryAlbumsItems.Paths>) getIntent().getExtras().getSerializable("ListPaths");
            for(int i = 0; i< ListPathsPhotos.size(); i++){
                DisplayPhotosPublishedItems photosItems = new DisplayPhotosPublishedItems();
                photosItems.setPhoto_published(ListPathsPhotos.get(i).getPhoto_path());
              //  photosItems.setPhoto_profil(R.drawable.bgmoi);
                item.add(photosItems);
            }
            adapter = new DisplayPhotosPublishedAdapter(Glide.with(this),getApplicationContext(), item,false);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);

        }
         // Display  Photos from PHOTOS FRAGMENT
        if (getIntent().getExtras().getString("PHOTOS_Fragment_PathParent") != null) {
            int photo_id = getIntent().getExtras().getInt("PHOTOS_Fragment_Photo_id");
            String path = getIntent().getExtras().getString("PHOTOS_Fragment_PathParent");
            int parent = getIntent().getExtras().getInt("PHOTOS_Fragment_Parent");
            if (parent == -1){
                // in case there are multiple photos in Photos Fragment
                getPathChildPhotosPublished(photo_id,path);


            }else {
                // in case there is one photo in Photos Fragment
                    DisplayPhotosPublishedItems photosItems = new DisplayPhotosPublishedItems();
                    photosItems.setPhoto_published(path);
                  //  photosItems.setPhoto_profil(R.drawable.bgmoi);
                    item.add(photosItems);
                    adapter = new DisplayPhotosPublishedAdapter(Glide.with(this),getApplicationContext(), item,false);
                    adapter.setHasStableIds(true);
                    recyclerView.setAdapter(adapter);


            }
        }


    }

    private void getPathChildPhotosPublished(int photo_id, final String path_parent){

        final ArrayList<DisplayPhotosPublishedItems> item = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL_UPLOAD_PHOTOS).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponsePhotoItems> api =API.getUserPathChildPhotosPublished(2,photo_id);
        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                ResponsePhotoItems List = response.body();
                boolean success = List.getSuccess();
                String message = List.getMessage();
                if (success){
                    paths = List.getData();
                    DisplayPhotosPublishedItems photosItems = new DisplayPhotosPublishedItems();
                    photosItems.setPhoto_published(path_parent);
                 //   photosItems.setPhoto_profil(R.drawable.bgmoi);
                    item.add(photosItems);

                    for(int i = 0; i< paths.size(); i++){
                     DisplayPhotosPublishedItems photosItems2 = new DisplayPhotosPublishedItems();
                        photosItems2.setPhoto_published(paths.get(i).getPhoto_path());
                      //  photosItems2.setPhoto_profil(R.drawable.bgmoi);
                     item.add(photosItems2);

                      adapter = new DisplayPhotosPublishedAdapter(Glide.with(DisplayPhotosPublishedActivity.this),getApplicationContext(), item,false);
                      adapter.setHasStableIds(true);
                      recyclerView.setAdapter(adapter);

                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_LONG)
                        .show();
                Log.e("Message ", "error: "+ t.toString());


            }
        });

    }



}


