package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.app.seddik.yomii.utils.FileUtils;
import com.app.seddik.yomii.utils.MyBitmapConfigs;
import com.app.seddik.yomii.utils.SessionManager;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.R.id.legende;
import static com.app.seddik.yomii.R.id.place;
import static com.app.seddik.yomii.activities.FiltersPhotoActivity.mBitmap;
import static com.app.seddik.yomii.utils.MyBitmapConfigs.getRealPathFromUri;

public class PhotosToPublishActivity extends AppCompatActivity {
    static int typeLinkPhoto;
    private static File file;
    SessionManager session;
    Toolbar toolbar;
    ImageView photo;
    TextView txtViewPublish;
    EditText legendeET;
    EditText placeET;
    Bundle extras;
    ArrayList<Uri> mListCurrentPhotoUri;
    ArrayList mCurrentPhotoUri;
    Boolean isMultiple = false;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    String mCity = "";
    private File compressedImageFile;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_to_publish);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = findViewById(R.id.photo);
        legendeET = findViewById(legende);
        placeET = findViewById(place);
        // Progress dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);

        // Get Uri's of photos selected
        mListCurrentPhotoUri = new ArrayList<>();
        mListCurrentPhotoUri = (ArrayList<Uri>) getIntent().getSerializableExtra("UriPhotos");
        if (mListCurrentPhotoUri != null) {
            isMultiple = true;
            mBitmap = MyBitmapConfigs.decodeSampledBitmapFromPathFile(getRealPathFromUri(mListCurrentPhotoUri.get(0), getApplicationContext()), 800, 800);
            photo.setImageBitmap(mBitmap);

        }
        // Get Uri of one photo filtred
        if (getIntent().getStringExtra("UriPhoto") != null) {
            mCurrentPhotoUri = new ArrayList<>();
            Uri mUri = Uri.parse(getIntent().getStringExtra("UriPhoto"));
            mCurrentPhotoUri.add(mUri);
        }


        txtViewPublish = findViewById(R.id.txtViewPublish);
        photo.setImageBitmap(mBitmap);
        txtViewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMultiple == true) {
                    uploadImages(mListCurrentPhotoUri);
                } else {
                    uploadImages(mCurrentPhotoUri);

                }

            }
        });

        placeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });

        hideStatutBar();
        //  publishPhotos();


    }



    private void uploadImages(List<Uri> paths) {
        session = new SessionManager(getApplicationContext());
        UserItems user;
        user = session.getUserDetails();
        int id_user = user.getUser_id();
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Publish ...");
        pDialog.setCancelable(false);
        pDialog.show();
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
        String mLocation = placeET.getText().toString();
        String mLegende = legendeET.getText().toString();

        RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), mLocation);
        RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), mCity);
        RequestBody legende = RequestBody.create(MediaType.parse("multipart/form-data"), mLegende);


        ApiService API = retrofit.create(ApiService.class);
        Call<ResponseItems> api = API.uploadImages(0, id_user, location, city, list, legende);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                hideDialog();
                ResponseItems responseItems = response.body();
                boolean success = responseItems.getSuccess();
                String message = responseItems.getMessage();
                if (success) {
                    Intent intent = new Intent(PhotosToPublishActivity.this, MainActivity.class);
                    intent.putExtra("NumTab", "4");
                    startActivity(intent);
                    finish();

                } else {
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

        } catch (IOException e) {
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

    public void hideStatutBar() {
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

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.

            AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    //.setCountry(countryCode)
                    .build();

            Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    //    .setFilter(autocompleteFilter)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("ActivityPHotos", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                placeET.setText(place.getAddress().toString());
                mCity = place.getName().toString();

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    //  mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    //  mPlaceAttribution.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("ActivityPHotos", "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
                Log.e("ActivityPHotos", "Error: Canceled!");

            }
        }
    }


}
