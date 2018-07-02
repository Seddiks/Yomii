package com.app.seddik.yomii.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.utils.MyBitmapConfigs;
import com.app.seddik.yomii.utils.FileUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.activities.FiltersPhotoActivity.mBitmap;
import static com.app.seddik.yomii.utils.MyBitmapConfigs.getRealPathFromUri;

public class PhotosToPublishActivity extends AppCompatActivity {
    SessionManager session;
    Toolbar toolbar;
    ImageView photo;
    TextView txtViewPublish;
    EditText legende;
    PlacesAutocompleteTextView place;
    private static File file;
    Bundle extras;
    ArrayList<Uri> mListCurrentPhotoUri;
    ArrayList mCurrentPhotoUri;
    static int typeLinkPhoto;
    Boolean isMultiple = false;
    private File compressedImageFile;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_to_publish);
        toolbar =  findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = findViewById(R.id.photo);
        legende = findViewById(R.id.legende);
        place = findViewById(R.id.place);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Get Uri's of photos selected
        mListCurrentPhotoUri = new ArrayList<>();
        mListCurrentPhotoUri = (ArrayList<Uri>) getIntent().getSerializableExtra("UriPhotos");
        if (mListCurrentPhotoUri != null) {
            isMultiple = true;
            mBitmap = MyBitmapConfigs.decodeSampledBitmapFromPathFile(getRealPathFromUri(mListCurrentPhotoUri.get(0),getApplicationContext()),800,800);
            photo.setImageBitmap(mBitmap);

        }
        // Get Uri of one photo filtred
        if (getIntent().getStringExtra("UriPhoto") != null){
            mCurrentPhotoUri = new ArrayList<>();
            Uri mUri = Uri.parse(getIntent().getStringExtra("UriPhoto"));
            mCurrentPhotoUri.add(mUri);
        }


        // Google Key place: AIzaSyBYSIbedXNm5m20v5v_Slmdwff8b-uqmW0
        place.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {

                    }
                }
        );

        txtViewPublish = findViewById(R.id.txtViewPublish);
        photo.setImageBitmap(mBitmap);
        txtViewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMultiple == true){
                    uploadImages(mListCurrentPhotoUri);
                } else {
                    uploadImages(mCurrentPhotoUri);

                }

            }
        });

        hideStatutBar();
      //  publishPhotos();
    }
/**
    private void publishPhotos(Bitmap bitmap){
                String FileName = System.currentTimeMillis() + ".jpg";
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                file = new File(path, "Yomii" + "/" + FileName);
                file.getParentFile().mkdirs();
                new SingleMediaScanner(getApplicationContext(), file);

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                 uploadPhotos(file);

    }



    private void uploadPhotos(File f){
        session = new SessionManager(getApplicationContext());
        UserItems user ;
        user = session.getUserDetails();
        int id_user = user.getUser_id();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_UPLOAD_PHOTOS).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        RequestBody country = RequestBody.create(MediaType.parse("multipart/form-data"), "Algeria");
        RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), "Annaba");
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));

        ApiService API = retrofit.create(ApiService.class);
        Call<ResponseItems> api =API.uploadPhotos(id_user,
                country,
                city,
                filePart);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {

                Log.e(" code 200",response.toString());
                ResponseItems List = response.body();
                boolean success = List.getSuccess();
                if (success) {
                    Log.e("Response",List.getMessage());
                }else {
                    Log.e("Response Error",List.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                Log.e(" Response Error2",t.toString());
            }
        });
    } **/

    private void uploadImages(List<Uri> paths) {
        session = new SessionManager(getApplicationContext());
        UserItems user ;
        user = session.getUserDetails();
        int id_user = user.getUser_id();
        pDialog.setMessage("Téléchargement en cours ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_UPLOAD_PHOTOS).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        List<MultipartBody.Part> list = new ArrayList<>();
        int i = 0;
        for (Uri uri : paths) {
            String fileName = FileUtils.getFile(this, uri).getName();
            //very important files[]
            MultipartBody.Part imageRequest = prepareFilePart("file[]", uri);
            list.add(imageRequest);
        }
        RequestBody country = RequestBody.create(MediaType.parse("multipart/form-data"), "Algeria");
        RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), "Annaba");


        ApiService API = retrofit.create(ApiService.class);
        Call<ResponseItems> api =API.uploadImages(0,id_user,
                country,
                city,
                list);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                hideDialog();
                ResponseItems responseItems = response.body();
                boolean success = responseItems.getSuccess();
                String message = responseItems.getMessage();
                if (success){
                    Intent intent = new Intent(PhotosToPublishActivity.this,MainActivity.class);
                    intent.putExtra("NumTab", "0");
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable throwable) {
                Log.e("main", "on error is called and the error is  ----> " + throwable.getMessage());
                hideDialog();

            }
        });


    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);
        //compress the image using Compressor lib
       // Log.d("size of image before compression --> " + file.getTotalSpace());
        try {
             compressedImageFile = new Compressor(this).compressToFile(file);

        }catch (IOException e) {
            e.printStackTrace();
        }
      //  PhotosToPublishActivity.d("size of image after compression --> " + compressedImageFile.getTotalSpace());
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        compressedImageFile);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public void hideStatutBar(){
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
