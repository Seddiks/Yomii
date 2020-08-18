package com.app.seddik.yomii.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.ProfilPagerAdapter;
import com.app.seddik.yomii.adapters.TravelStoryAdapter;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.TravelStoryItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.utils.FileUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    static final int REQUEST_CODE_CHOOSE = 1001;
    private static int PhotoType = 0; //profil photo 1 or cover photo 2
    boolean success;
    String message;
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
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProfilPagerAdapter adapter;
    private LinearLayout layoutDetail;
    private LinearLayout layoutEdit;
    private RelativeLayout layout_done_exit;
    private TabLayout tabLayoutF;
    private ImageView cover,update_cover,update_photo_profile, update_profil, settings,done, exit;
    private CircleImageView profile_image;
    private TextView profile_name,bio, pubs, abonnes, abonnements, travel_text;
    private EditText full_name, username, bio_et;
    private RequestBody mFullName,mBio,mUserName;
    private MultipartBody.Part mImageProfilRequest, mImageCoverRequest;
    private Uri mCurrentProfilPhotoUri= null,mCurrentCoverPhotoUri = null;
    private TravelStoryAdapter adapterStory;
    private TravelStoryItems travelStoryItems;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SweetAlertDialog pDialog;

    private int[] tabIcons = {
            R.drawable.ic_home_grey_500_24dp,
            R.drawable.ic_whatshot_grey_500_24dp,
    };




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        id_user = user.getUser_id();
        // Progress dialog
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);

        profile_image =  rootView.findViewById(R.id.profile_image);
        cover =  rootView.findViewById(R.id.cover_photo);

        update_cover =  rootView.findViewById(R.id.update_cover_photo);
        update_photo_profile =  rootView.findViewById(R.id.update_profile_image);
        update_profil =  rootView.findViewById(R.id.update_profil);
        settings =  rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });
        exit =  rootView.findViewById(R.id.exit);
        done =  rootView.findViewById(R.id.done);

        profile_name =  rootView.findViewById(R.id.profile_name);
        bio =  rootView.findViewById(R.id.bio);
        pubs =  rootView.findViewById(R.id.pubs);
        abonnes =  rootView.findViewById(R.id.abonnes);
        abonnements =  rootView.findViewById(R.id.abonnements);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"LeagueSpartan-Bold.otf");
        Typeface type2 = Typeface.createFromAsset(getActivity().getAssets(),"Quicksand-Regular.otf");
        profile_name.setTypeface(type);
        pubs.setTypeface(type2);
        abonnes.setTypeface(type2);
        abonnements.setTypeface(type2);

        full_name =  rootView.findViewById(R.id.full_name);
        username =  rootView.findViewById(R.id.username);
        bio_et =  rootView.findViewById(R.id.bio_et);

        layoutDetail = rootView.findViewById(R.id.layout_detail);
        layoutEdit = rootView.findViewById(R.id.layout_edit);
        layout_done_exit = rootView.findViewById(R.id.layout_done_exit);
        travel_text = rootView.findViewById(R.id.travel_text);
        tabLayoutF = rootView.findViewById(R.id.tabs);


        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        viewPager = rootView.findViewById(R.id.viewpager);
        tabLayout =  rootView.findViewById(R.id.tabs);
        //   tabLayout.setupWithViewPager(viewPager);
        createTabLayout();

        update_profil.setOnClickListener(this);
        done.setOnClickListener(this);
        exit.setOnClickListener(this);
        update_photo_profile.setOnClickListener(this);
        update_cover.setOnClickListener(this);

        getUserProfilDetail("get");
        getUserStories();
        return rootView;
    }

    private void createTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        adapter = new ProfilPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

     //Get details profil
     private void getUserProfilDetail(final String typeCall) {
        Call<UserItems> api =API.getUserProfilDetails(0,id_user);
        api.enqueue(new Callback<UserItems>() {
            @Override
            public void onResponse(Call<UserItems> call, Response<UserItems> response) {
                UserItems userItems = response.body();
                success = userItems.getSuccess();
                message = userItems.getMessage();
                if (success){
                    //fill user profil details:
                    Glide.with(getActivity())
                            .load(URL_UPLOAD_PHOTOS+userItems.getPhoto_profil_path())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_person_circle_blue_a400_36dp).error(R.drawable.ic_person_circle_blue_a400_36dp))
                            .into(profile_image);
                    Glide.with(getActivity())
                            .load(URL_UPLOAD_PHOTOS+userItems.getPhoto_cover_path())
                            .apply(new RequestOptions().placeholder(R.drawable.bg_cover).error(R.drawable.bg_cover))
                            .into(cover);
                    profile_name.setText(userItems.getFull_name());
                    bio.setText(userItems.getBio());
                    //fill editText details
                    full_name.setText(userItems.getFull_name());
                    username.setText(userItems.getUsername());
                    bio_et.setText(userItems.getBio());
                    if (typeCall.equals("UpdateSession")) {
                        session.createLoginSession(userItems.getUser_id(), userItems.getFull_name(), userItems.getPhoto_profil_path(), userItems.getPhoto_cover_path(), userItems.getBio(), userItems.getEmail(), userItems.getToken());

                    } else {
                        // do nothing
                    }


                }else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<UserItems> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Error", Toast.LENGTH_LONG)
                        .show();

            }
        });

    }

    //Get Stories
    private void getUserStories(){
        Call<TravelStoryItems> api =API.getUserStories(1,id_user);
        api.enqueue(new Callback<TravelStoryItems>() {
            @Override
            public void onResponse(Call<TravelStoryItems> call, Response<TravelStoryItems> response) {
                travelStoryItems = response.body();
                success = travelStoryItems.isSuccess();
                message = travelStoryItems.getMessage();
                if (success){
                    adapterStory = new TravelStoryAdapter(getActivity(), travelStoryItems);
                    recyclerView.setAdapter(adapterStory);

                }else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<TravelStoryItems> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Error"+t.toString(), Toast.LENGTH_LONG)
                        .show();

            }
        });


    }

    //Update user profil details
    private void updateUserProfilDetails(){
        mUserName = RequestBody.create(MediaType.parse("multipart/form-data"), username.getText().toString());
        mFullName = RequestBody.create(MediaType.parse("multipart/form-data"), full_name.getText().toString());
        mBio = RequestBody.create(MediaType.parse("multipart/form-data"), bio_et.getText().toString());

        if (mCurrentProfilPhotoUri != null){
            mImageProfilRequest = prepareFilePart("profil_photo", mCurrentProfilPhotoUri);
        }
        if (mCurrentCoverPhotoUri != null) {
            mImageCoverRequest = prepareFilePart("cover_photo", mCurrentCoverPhotoUri);
        }

        Call<ResponseItems> api =API.updateUserProfilDetails(1,mImageProfilRequest,mImageCoverRequest,id_user,mFullName,mUserName,mBio);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems responseItems = response.body();
                success = responseItems.getSuccess();
                message = responseItems.getMessage();
                if (success){
                    getUserProfilDetail("UpdateSession");
                }else {
                    getUserProfilDetail("UpdateSession");
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                getUserProfilDetail("UpdateSession");
                Log.e("Update:", "error: " + t.toString());
                Toast.makeText(getActivity(),
                        "Error"+t.toString(), Toast.LENGTH_LONG)
                        .show();


            }
        });


    }

    private void logOutUser() {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Logout ...");
        pDialog.setCancelable(false);
        pDialog.show();

        String token = session.getTokenFirebase();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_AUTH).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponseItems> api = API.logout(id_user, 4, token);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                pDialog.dismiss();
                ResponseItems responseItems = response.body();
                boolean success = responseItems.getSuccess();
                String message = responseItems.getMessage();
                if (success) {
                    deleteTokenFirebaseInstance();
                } else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getActivity(),
                        "Error..", Toast.LENGTH_LONG)
                        .show();

            }
        });


    }

    private void deleteTokenFirebaseInstance() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                        getActivity().finishAffinity(); //Close all activities
                        session.logoutUser();
                        AppConfig.NOTIFICATION_COUNTER = 0;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //call your activity where you want to land after log out
            }
        }.execute();

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.update_profil:
                ((MainActivity)getActivity()).tabLayout.setVisibility(View.GONE);
                ((MainActivity)getActivity()).fab.setVisibility(View.GONE);
                update_profil.setVisibility(View.GONE);
                settings.setVisibility(View.GONE);
                profile_name.setVisibility(View.GONE);
                bio.setVisibility(View.GONE);
                layoutDetail.setVisibility(View.GONE);
                travel_text.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                tabLayoutF.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);

                layoutEdit.setVisibility(View.VISIBLE);
                update_photo_profile.setVisibility(View.VISIBLE);
                update_cover.setVisibility(View.VISIBLE);
                layout_done_exit.setVisibility(View.VISIBLE);

                break;
            case R.id.done:
                updateUserProfilDetails();

                layoutEdit.setVisibility(View.GONE);
                update_photo_profile.setVisibility(View.GONE);
                update_cover.setVisibility(View.GONE);
                layout_done_exit.setVisibility(View.GONE);

                ((MainActivity)getActivity()).tabLayout.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);
                update_profil.setVisibility(View.VISIBLE);
                settings.setVisibility(View.VISIBLE);
                profile_name.setVisibility(View.VISIBLE);
                bio.setVisibility(View.VISIBLE);
                layoutDetail.setVisibility(View.VISIBLE);
                travel_text.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                tabLayoutF.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);

                break;
            case R.id.exit:
                layoutEdit.setVisibility(View.GONE);
                update_photo_profile.setVisibility(View.GONE);
                update_cover.setVisibility(View.GONE);
                layout_done_exit.setVisibility(View.GONE);

                ((MainActivity)getActivity()).tabLayout.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);
                update_profil.setVisibility(View.VISIBLE);
                settings.setVisibility(View.VISIBLE);
                profile_name.setVisibility(View.VISIBLE);
                bio.setVisibility(View.VISIBLE);
                layoutDetail.setVisibility(View.VISIBLE);
                travel_text.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                tabLayoutF.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);

                break;

            case R.id.update_profile_image :
                PhotoType = 1;
                callMatisse();
                break;

            case R.id.update_cover_photo :
                PhotoType = 2;
                callMatisse();
                break;

        }

    }
    private void callMatisse(){
        Matisse.from(this)
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);
            if (PhotoType == 1){
                mCurrentProfilPhotoUri = mSelected.get(0);
                Glide.with(getActivity())
                        .load(mCurrentProfilPhotoUri)
                        .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(profile_image);

            }
            if (PhotoType == 2){
                mCurrentCoverPhotoUri = mSelected.get(0);
                Glide.with(getActivity())
                        .load(mCurrentCoverPhotoUri)
                        .apply(new RequestOptions().placeholder(R.drawable.bg_barca).error(R.drawable.bg_barca))
                        .into(cover);

            }


        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getActivity(), fileUri);
        //compress the image using Compressor lib
        // Log.d("size of image before compression --> " + file.getTotalSpace());
        try {
            compressedImageFile = new Compressor(getActivity()).compressToFile(file);

        }catch (IOException e) {
            e.printStackTrace();
        }
        //  PhotosToPublishActivity.d("size of image after compression --> " + compressedImageFile.getTotalSpace());
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getActivity().getContentResolver().getType(fileUri)),
                        compressedImageFile);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


}
