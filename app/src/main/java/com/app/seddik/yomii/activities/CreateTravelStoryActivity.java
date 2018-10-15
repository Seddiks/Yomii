package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.TravelStoryItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.utils.FileUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.R.id.photo;
import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class CreateTravelStoryActivity extends AppCompatActivity implements View.OnClickListener{
    static final int REQUEST_CODE_CHOOSE = 1001;
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_PHOTOS).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    private SessionManager session;
    private UserItems user ;
    private int id_user;
    private ArrayList<Uri> mSelected;
    private File compressedImageFile;
    private MultipartBody.Part mImageRequest;
    private ImageView exit,add_photo, done, story_photo;
    private EditText title, location, full_story;
    private String mTitle, mLocation, mFullStory;
    private Uri mPhotoUri;
    private TravelStoryItems.Data travelStoryItems;
    private int travel_story_id = -1;
    private String mPathPhoto;
    private boolean forUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel_story);
        session = new SessionManager(this);
        user = session.getUserDetails();
        id_user = user.getUser_id();



        exit = findViewById(R.id.exit);
        add_photo = findViewById(R.id.add_photo);
        done = findViewById(R.id.done);
        story_photo = findViewById(photo);
        title = findViewById(R.id.title_story);
        location = findViewById(R.id.location);
        full_story = findViewById(R.id.story);

        // Updata story item:
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            forUpdate = true;
            travelStoryItems = new TravelStoryItems.Data();
            travelStoryItems = (TravelStoryItems.Data) getIntent().getExtras().getSerializable("mStory");
            travel_story_id = travelStoryItems.getTravel_story_id() ;
            mPathPhoto = URL_UPLOAD_PHOTOS+travelStoryItems.getPhoto_path();
            if (travelStoryItems.getPathPhoto_from_uri() != null) mPhotoUri = Uri.parse(travelStoryItems.getPathPhoto_from_uri());
            String mLocation = travelStoryItems.getLocation();
            String mTitle = travelStoryItems.getTitle();
            String mFull_story = travelStoryItems.getFull_story();

            if (mPhotoUri != null){
                Glide.with(getApplicationContext())
                        .load(mPhotoUri)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(story_photo);

            }else {
                Glide.with(getApplicationContext())
                        .load(mPathPhoto)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(story_photo);

            }
            location.setText(mLocation);
            title.setText(mTitle);
            full_story.setText(mFull_story);


        }

        exit.setOnClickListener(this);
        add_photo.setOnClickListener(this);
        done.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.exit:
                finish();

                break;
            case R.id.add_photo:
                Matisse.from(CreateTravelStoryActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .capture(false)
                        .captureStrategy(new CaptureStrategy(true,"com.app.seddik.yomii"))
                        .maxSelectable(1)
                        .theme(R.style.Matisse_Zhihu)
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);


                break;
            case R.id.done:
                mTitle = title.getText().toString();
                mLocation = location.getText().toString();
                mFullStory = full_story.getText().toString();
                if (mPhotoUri == null && mPathPhoto ==null ) {
                    Toast.makeText(getApplicationContext(),
                            "Please add photo", Toast.LENGTH_LONG)
                            .show();
                }

                else if (mTitle.isEmpty() || mLocation.isEmpty() || mFullStory.isEmpty()){
                        Toast.makeText(getApplicationContext(),
                                "Please enter all details", Toast.LENGTH_LONG)
                                .show();
                    } else {

                            sendStory(mTitle, mLocation, mFullStory, mPhotoUri);

                    }

                break;

        }


    }

    private void sendStory(final String mTitle, final String mLocation, final String mFullStory, final Uri mPhotoUri){
        if (mPhotoUri != null){
            mImageRequest = prepareFilePart("story_photo", mPhotoUri);

        }
        RequestBody requestTitle = RequestBody.create(MediaType.parse("multipart/form-data"), mTitle);
        RequestBody requestLocation = RequestBody.create(MediaType.parse("multipart/form-data"), mLocation);
        RequestBody requestFullStory = RequestBody.create(MediaType.parse("multipart/form-data"), mFullStory);

        Call<ResponseItems> api =API.sendStory(0,travel_story_id, id_user, mImageRequest, requestTitle, requestLocation, requestFullStory);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems responseItems = response.body();
                boolean success = responseItems.getSuccess();
                String message = responseItems.getMessage();
                if (success) {
                    if (forUpdate) {
                        travelStoryItems.setLocation(mLocation);
                        travelStoryItems.setTitle(mTitle);
                        travelStoryItems.setFull_story(mFullStory);
                        if (mPhotoUri != null){
                            travelStoryItems.setPathPhoto_from_uri(mPhotoUri.toString());
                            Intent intent =  new Intent(CreateTravelStoryActivity.this, TravelStoryActivity.class);
                            intent.putExtra("mStory",travelStoryItems );
                            startActivity(intent);

                        } else {
                            Intent intent =  new Intent(CreateTravelStoryActivity.this, TravelStoryActivity.class);
                            intent.putExtra("mStory",travelStoryItems );
                            startActivity(intent);

                        }


                        finish();

                    }else {
                        finish();

                    }
                }else {
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_LONG)
                        .show();
                Log.e("Retour:", t.toString());

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);
            mPhotoUri = mSelected.get(0);
            Glide.with(getApplicationContext())
                    .load(mPhotoUri)
                    .apply(new RequestOptions().placeholder(R.drawable.hudson_background).error(R.drawable.hudson_background))
                    .into(story_photo);

        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getApplicationContext(), fileUri);
        //compress the image using Compressor lib
        // Log.d("size of image before compression --> " + file.getTotalSpace());
        try {
            compressedImageFile = new Compressor(getApplicationContext()).compressToFile(file);

        }catch (IOException e) {
            e.printStackTrace();
        }
        //  PhotosToPublishActivity.d("size of image after compression --> " + compressedImageFile.getTotalSpace());
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getApplicationContext().getContentResolver().getType(fileUri)),
                        compressedImageFile);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

}
