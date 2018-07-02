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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.models.GuideItems;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
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

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;


public class CreateGuideActivity extends AppCompatActivity implements View.OnClickListener{
    private SessionManager session;
    private UserItems user ;
    private int id_user;

    static final int REQUEST_CODE_CHOOSE = 1001;
    private ArrayList<Uri> mSelected;
    private File compressedImageFile;
    private MultipartBody.Part mImageRequest;

    private EditText title_guide, location, experience, history, budget_advice, best_time_to_visit, best_place_to_visit, restaurant_suggestions, transportation_advice, language, other_informations;
    private ImageView photo, exit, done, add_photo;
    private RelativeLayout layout_addPhoto_exit_done;
    private String mTitle, mLocation, mExperience, mHistory, mBudgetAdvice, mBestTimeToVisit, mBestPlaceToVisit, mRestaurantSuggestions, mTransportationsAdvice, mLanguage, mOtherInformations;
    private Uri mPhotoUri;
    private GuideItems.Data guideItems;
    private int guide_id = -1;
    private String mPathPhoto;
    private boolean forUpdate;



    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_PHOTOS).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_guide);
        session = new SessionManager(this);
        user = session.getUserDetails();
        id_user = user.getUser_id();


        title_guide = findViewById(R.id.title_guide);
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
        exit = findViewById(R.id.exit);
        done = findViewById(R.id.done);
        add_photo = findViewById(R.id.add_photo);
        layout_addPhoto_exit_done = findViewById(R.id.layout_addPhoto_exit_done);

        // Updata guide item:
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            forUpdate = true;
            guideItems = new GuideItems.Data();
            guideItems = (GuideItems.Data) getIntent().getExtras().getSerializable("mGuide");
            guide_id = guideItems.getGuide_id() ;
            mPathPhoto = URL_UPLOAD_PHOTOS+guideItems.getPhoto_path();
            if (guideItems.getPathPhoto_from_uri() != null) mPhotoUri = Uri.parse(guideItems.getPathPhoto_from_uri());

            mTitle = guideItems.getTitle_guide();
            if (!mTitle.isEmpty()) title_guide.setText(mTitle);

            mLocation = guideItems.getLocation();
            if (!mLocation.isEmpty()) location.setText(mLocation);

            mExperience = guideItems.getExperience();
            if (!mExperience.isEmpty()) experience.setText(mExperience);

            mHistory = guideItems.getHistory();
            if (!mHistory.isEmpty()) history.setText(mHistory);

            mBudgetAdvice = guideItems.getBudget_advice();
            if (!mBudgetAdvice.isEmpty()) budget_advice.setText(mBudgetAdvice);

            mBestTimeToVisit = guideItems.getBest_time_to_visit();
            if (!mBestTimeToVisit.isEmpty()) best_time_to_visit.setText(mBestTimeToVisit);

            mBestPlaceToVisit = guideItems.getBest_place_to_visit();
            if (!mBestPlaceToVisit.isEmpty()) best_place_to_visit.setText(mBestPlaceToVisit);

            mRestaurantSuggestions = guideItems.getRestaurant_suggestions();
            if (!mRestaurantSuggestions.isEmpty()) restaurant_suggestions.setText(mRestaurantSuggestions);

            mTransportationsAdvice = guideItems.getTransportation_advice();
            if (!mTransportationsAdvice.isEmpty()) transportation_advice.setText(mTransportationsAdvice);

            mLanguage = guideItems.getLanguage();
            if (!mLanguage.isEmpty()) language.setText(mLanguage);

            mOtherInformations = guideItems.getOther_informations();
            if (!mOtherInformations.isEmpty()) other_informations.setText(mOtherInformations);

            if (mPhotoUri != null){
                Glide.with(getApplicationContext())
                        .load(mPhotoUri)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(photo);

            }else {
                Glide.with(getApplicationContext())
                        .load(mPathPhoto)
                        // .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(photo);

            }


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
                Matisse.from(CreateGuideActivity.this)
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
                mTitle = title_guide.getText().toString();
                mLocation = location.getText().toString();
                mExperience = experience.getText().toString();
                mHistory = history.getText().toString();
                mBudgetAdvice = budget_advice.getText().toString();
                mBestTimeToVisit = best_time_to_visit.getText().toString();
                mBestPlaceToVisit = best_place_to_visit.getText().toString();
                mRestaurantSuggestions = restaurant_suggestions.getText().toString();
                mTransportationsAdvice = transportation_advice.getText().toString();
                mLanguage = language.getText().toString();
                mOtherInformations = other_informations.getText().toString();
                if (mPhotoUri == null && mPathPhoto ==null ) {
                    Toast.makeText(getApplicationContext(),
                            "Please add photo", Toast.LENGTH_LONG)
                            .show();
                }else if (mTitle.isEmpty() || mLocation.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please enter Title & Location", Toast.LENGTH_LONG)
                            .show();
                } else {

                    sendGuide(mPhotoUri,mTitle, mLocation, mExperience, mHistory, mBudgetAdvice, mBestTimeToVisit,
                            mBestPlaceToVisit, mRestaurantSuggestions, mTransportationsAdvice, mLanguage, mOtherInformations);
                }

                break;

        }


    }

    private void sendGuide(final Uri mPhotoUri, final String mTitle, final String mLocation, final String mExperience, final String mHistory,
                           final String mBudgetAdvice, final String mBestTimeToVisit, final String mBestPlaceToVisit, final String mRestaurantSuggestions,
                           final String mTransportationsAdvice, final String mLanguage, final String mOtherInformations){
        if (mPhotoUri != null){
            mImageRequest = prepareFilePart("guide_photo", mPhotoUri);

        }
        RequestBody requestTitle = RequestBody.create(MediaType.parse("multipart/form-data"), mTitle);
        RequestBody requestLocation = RequestBody.create(MediaType.parse("multipart/form-data"), mLocation);
        RequestBody requestExperience = RequestBody.create(MediaType.parse("multipart/form-data"), mExperience);
        RequestBody requesHistory = RequestBody.create(MediaType.parse("multipart/form-data"), mHistory);
        RequestBody requestBudget = RequestBody.create(MediaType.parse("multipart/form-data"), mBudgetAdvice);
        RequestBody requestBestTime = RequestBody.create(MediaType.parse("multipart/form-data"), mBestTimeToVisit);
        RequestBody requestBestPlace = RequestBody.create(MediaType.parse("multipart/form-data"), mBestPlaceToVisit);
        RequestBody requestRestaurant = RequestBody.create(MediaType.parse("multipart/form-data"), mRestaurantSuggestions);
        RequestBody requestTransportation = RequestBody.create(MediaType.parse("multipart/form-data"), mTransportationsAdvice);
        RequestBody requestLanguage = RequestBody.create(MediaType.parse("multipart/form-data"), mLanguage);
        RequestBody requestInformations = RequestBody.create(MediaType.parse("multipart/form-data"), mOtherInformations);

        Call<ResponseItems> api =API.sendGuide(0,guide_id, id_user, mImageRequest, requestTitle, requestLocation,
                requestExperience,requesHistory,requestBudget,requestBestTime,requestBestPlace,requestRestaurant,
                requestTransportation,requestLanguage,requestInformations);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems responseItems = response.body();
                boolean success = responseItems.getSuccess();
                String message = responseItems.getMessage();
                if (success) {
                    if (forUpdate) {
                        guideItems.setLocation(mLocation);
                        guideItems.setTitle_guide(mTitle);
                        guideItems.setExperience(mExperience);
                        guideItems.setHistory(mHistory);
                        guideItems.setBudget_advice(mBudgetAdvice);
                        guideItems.setBest_time_to_visit(mBestTimeToVisit);
                        guideItems.setBest_place_to_visit(mBestPlaceToVisit);
                        guideItems.setRestaurant_suggestions(mRestaurantSuggestions);
                        guideItems.setTransportation_advice(mTransportationsAdvice);
                        guideItems.setLanguage(mLanguage);
                        guideItems.setOther_informations(mOtherInformations);
                        if (mPhotoUri != null){
                            guideItems.setPathPhoto_from_uri(mPhotoUri.toString());
                            Intent intent =  new Intent(CreateGuideActivity.this, GuideActivity.class);
                            intent.putExtra("mGuide",guideItems );
                            startActivity(intent);

                        } else {
                            Intent intent =  new Intent(CreateGuideActivity.this, GuideActivity.class);
                            intent.putExtra("mGuide",guideItems );
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
                    .into(photo);

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
