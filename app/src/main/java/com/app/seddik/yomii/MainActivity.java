package com.app.seddik.yomii;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.seddik.yomii.activities.CreateGuideActivity;
import com.app.seddik.yomii.activities.CreateTravelStoryActivity;
import com.app.seddik.yomii.activities.FiltersPhotoActivity;
import com.app.seddik.yomii.activities.PhotosToPublishActivity;
import com.app.seddik.yomii.adapters.MainPagerAdapter;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.app.seddik.yomii.R.id.tv_count;
import static com.app.seddik.yomii.utils.MyBitmapConfigs.getRealPathFromUri;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CODE_CHOOSE = 1001;
    static final int REQUEST_TAKE_PHOTO = 1;
    static int typeLinkPhoto;
    public FloatingActionButton fab,fab1,fab2,fab3, fab4, fab5;
    public TabLayout tabLayout;
    LinearLayout fabLayout1, fabLayout2, fabLayout3,fabLayout4,fabLayout5;
    View fabBGLayout;
    boolean isFABOpen=false;
    String mCurrentPhotoPath;
    Uri mCurrentPhotoUri;
    ArrayList<String> mListCurrentPhotoPath;
    ArrayList<Uri> mSelected;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private SessionManager session;
    private ViewPager viewPager;
    private MainPagerAdapter adapter;
    private int[] tabIcons = {
            R.drawable.ic_home_grey_500_24dp,
            R.drawable.ic_whatshot_grey_500_24dp,
            R.drawable.ic_subscribers_grey_500_24dp,
            R.drawable.ic_notifications_grey_500_24dp,
            R.drawable.ic_person_grey_500_24dp
    };
    private int icon_notification_black = R.drawable.ic_notifications_black_24dp;
    private int icon_notification_grey = R.drawable.ic_notifications_grey_500_24dp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Allowing Strict mode policy for Nougat support
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().hide();

        // Check if user is already logged in or not
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        viewPager = findViewById(R.id.viewpager);
        tabLayout =  findViewById(R.id.tabs);
        createTabLayout();


        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab5 = findViewById(R.id.fab5);

        fabLayout1 = findViewById(R.id.fabLayout1);
        fabLayout2 = findViewById(R.id.fabLayout2);
        fabLayout3 = findViewById(R.id.fabLayout3);
        fabLayout4 = findViewById(R.id.fabLayout4);
        fabLayout5 = findViewById(R.id.fabLayout5);
        fabBGLayout = findViewById(R.id.fabBGLayout);



        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);
        fabBGLayout.setOnClickListener(this);


    }

    private void createTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));
        tabLayout.addTab(tabLayout.newTab().setCustomView(prepareTabView()));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[4]));

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


        adapter = new MainPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3){
                    View view = tab.getCustomView();
                    ImageView icon =  view.findViewById(R.id.icon);
                    TextView counter =  view.findViewById(tv_count);
                    icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon_notification_black));
                    AppConfig.NOTIFICATION_COUNTER = 0;
                    counter.setVisibility(View.GONE);



                }else {
                    tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

                }
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3){
                    View view = tab.getCustomView();
                    ImageView icon =  view.findViewById(R.id.icon);
                    icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon_notification_grey));

                }else {
                    tab.getIcon().setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_IN);

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // After uploading photos go to Profil Tab!
        if (getIntent().getStringExtra("NumTab") != null){
            viewPager.setCurrentItem(0); //  tab number 0 "Home Fragment"

        }

        }

     View prepareTabView() {
        View view = getLayoutInflater().inflate(R.layout.custom_tab,null);
        TextView counter =  view.findViewById(tv_count);
        if (AppConfig.NOTIFICATION_COUNTER >0){
            counter.setVisibility(View.VISIBLE);
            counter.setText(""+AppConfig.NOTIFICATION_COUNTER);

        }

        return view;
    }





    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
               // animateFAB();
                break;
            case R.id.fab1:
                typeLinkPhoto = 1;
                 dispatchTakePictureIntent();

                break;
            case R.id.fab2:
                typeLinkPhoto = 2;
                Matisse.from(MainActivity.this)
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
            case R.id.fab3:
                typeLinkPhoto = 3;
                Matisse.from(MainActivity.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .capture(false)
                        .maxSelectable(9)
                        .theme(R.style.Matisse_Zhihu)
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);

                break;

            case R.id.fab4:
                Intent intent = new Intent(MainActivity.this,CreateTravelStoryActivity.class);
                startActivity(intent);
                break;

            case R.id.fab5:
                Intent intent2 = new Intent(MainActivity.this,CreateGuideActivity.class);
                startActivity(intent2);
                break;

            case R.id.fabBGLayout:

                closeFABMenu();
                break;

        }
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }





    //With this method available to create a file for the photo, you can now create and invoke the Intent like this:
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.app.seddik.yomii",
                        photoFile);
                Log.d("TAG Photo Uri", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                galleryAddPic();


            }
        }
    }
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && typeLinkPhoto ==1) {
           Intent intent = new Intent(MainActivity.this,
                   FiltersPhotoActivity.class);
           intent.putExtra("typeLinkPhoto", 1);
           intent.putExtra("PathPhoto", mCurrentPhotoPath);
           startActivity(intent);

       }
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && typeLinkPhoto == 2) {
            Intent intent = new Intent(MainActivity.this,
                    FiltersPhotoActivity.class);
            intent.putExtra("typeLinkPhoto", 2);
            mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);
            mCurrentPhotoUri = mSelected.get(0);
            mCurrentPhotoPath = getRealPathFromUri(mCurrentPhotoUri,getApplicationContext());
            intent.putExtra("PathPhoto", mCurrentPhotoPath);
            startActivity(intent);
        }
       if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && typeLinkPhoto == 3) {
           Intent intent = new Intent(MainActivity.this,
                   PhotosToPublishActivity.class);
           intent.putExtra("typeLinkPhoto", 3);
           mSelected = (ArrayList<Uri>) Matisse.obtainResult(data);
           intent.putExtra("UriPhotos", mSelected);
           startActivity(intent);
       }

    }
/**
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
           // Glide.with(getApplicationContext()).load("file:"+mCurrentPhotoPath)
             //                                  .into(imageView);
            Intent intent = new Intent(MainActivity.this,
                                       FiltersPhotoActivity.class);
            Log.e("TESST:  ",""+mCurrentPhotoPath);
            intent.putExtra("PathPhoto", mCurrentPhotoPath);
            startActivity(intent);

        }
    }
**/


    //Here's an example solution in a method that returns a unique file name for a new photo using a date-time stamp:
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/Yomis");
        storageDir.mkdirs();

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(this.mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabLayout4.setVisibility(View.VISIBLE);
        fabLayout5.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        fabLayout4.animate().translationY(-getResources().getDimension(R.dimen.standard_205));
        fabLayout5.animate().translationY(-getResources().getDimension(R.dimen.standard_250));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0);
        fabLayout4.animate().translationY(0);
        fabLayout5.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                    fabLayout4.setVisibility(View.GONE);
                    fabLayout5.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String TokenFirebase = instanceIdResult.getToken();
                Log.e("newTokenLogin Main", TokenFirebase);

            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.app.seddik.yomii.onMessageReceived");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onBackPressed() {
        if(isFABOpen){
            closeFABMenu();
        }else{
            super.onBackPressed();
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            int counter = extras.getInt("CounterNotification", 0);
            View view = tabLayout.getTabAt(3).getCustomView();
            TextView counterTV = view.findViewById(tv_count);
            if (counter > 0) {
                counterTV.setVisibility(View.VISIBLE);
                counterTV.setText("" + counter);

            }

        }
    }


}
