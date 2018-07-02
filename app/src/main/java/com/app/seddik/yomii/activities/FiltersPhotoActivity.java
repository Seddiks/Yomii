package com.app.seddik.yomii.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.app.seddik.yomii.adapters.ListFiltersColorsAdapter;
import com.app.seddik.yomii.adapters.ListFiltersPhotoAdapter;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;
import com.app.seddik.yomii.utils.SessionManager;
import com.app.seddik.yomii.utils.filter.GPUImageFilterTools;

import static com.app.seddik.yomii.R.id.imageView;
import static com.app.seddik.yomii.utils.MyBitmapConfigs.decodeSampledBitmapFromPathFile;


public class FiltersPhotoActivity extends AppCompatActivity  {
    SessionManager session;
    Toolbar toolbar;
    Bundle extras;
    static String pathPhoto;
    static int typeLinkPhoto;

    static GPUImageView mGPUImageView;
    private static GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    private static GPUImageFilter mFilterColor;
    private static GPUImageFilter mFilterPhoto;

    private ImageView imageViewFilterPhoto;
    private ImageView imageViewFilterColors;
    private TextView txtViewNext;
    static android.support.v4.widget.NestedScrollView nestedScrollView;
    private static SeekBar seekBar ;
    private static int max = 100 ;
    private static int mPos  ;
    private static int mPosFilterPhoto  ;
    private static int mCurrentProgress  ;
    private static TextView valueFilter;
    private static TextView titleFilter;
    private static LinearLayout containerBtns;
    private static Button btnApply,btnCancel ;
    private static GPUImageFilterGroup groupAllFiltersTemp;
    private static GPUImageFilterGroup groupFilterCurrentColors;
    private static ArrayList<Integer> progressValues;
    private static int indexFilter ;

    static RecyclerView recyclerView;
    static ListFiltersPhotoAdapter adapterFilterPhoto;
    static ListFiltersColorsAdapter adapterFilterColors;
    static ArrayList<PopulairCategoriesHorizontalItems> filterPhotosItemses;
    static ArrayList<PopulairCategoriesHorizontalItems> markFilterItemses;
    static ArrayList<PopulairCategoriesHorizontalItems> filterColorsItemses;

    static public Bitmap mBitmap = null;
    static Bitmap thumbBitmap = null;
    static Typeface typeFace;
    static AnimationSet anim;
    static Animation animFadeIn;
    static Animation animZoomIn;
    static Animation animZoomOut;
    Uri uriBitmap;
    private static final String[] titleFilterPhoto = {
            "NORMAL",
            "1977",
            "VALENCIA",
            "AMARO",
            "BRANNAN",
            "EARLYBIRD",
            "HEFE",
            "HUDSON",
            "INKWELL",
            "LOMO",
            "LORDKELVIN",
            "NASHVILLE",
            "RISE",
            "SIERRA",
            "SUTRO",
            "TOASTER",
            "WALDEN",
            "XPROII",

    };
    private static final String[] titleFilterColors = {
            "Contrast",
            "Gamma",
            "GaussianBlur",
            "Brightness",
            "Sepia",
            "Saturation",
            "Exposure",
            "Vignette",
            "Tint",
            "Shadow",
            "Temperature",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filters_photo);
        // Check if user is already logged in or not
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        toolbar =  findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGPUImageView = findViewById(imageView);
        titleFilter = findViewById(R.id.titleFilter);
        txtViewNext = findViewById(R.id.txtViewNext);
        imageViewFilterPhoto = findViewById(R.id.filterPhoto);
        imageViewFilterColors = findViewById(R.id.filterColors);
        imageViewFilterColors.setImageResource(R.drawable.ic_filter_colors_off);
        nestedScrollView = findViewById(R.id.container_nestedScrollView);
        recyclerView = findViewById(R.id.recycleview);
        seekBar = findViewById(R.id.seekBar);
        btnApply = findViewById(R.id.apply_btn);
        btnCancel = findViewById(R.id.cancel_btn);
        containerBtns = findViewById(R.id.container_btns);
        containerBtns.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setMax(max);
        valueFilter =  findViewById(R.id.valueFilter);
        //get string path photo from bundle
        extras = getIntent().getExtras();
        typeLinkPhoto = extras.getInt("typeLinkPhoto");
        pathPhoto = extras.getString("PathPhoto");
        //display photo captured or selected in activity
        // mbitmap = decodeSampledBitmapFromPathFile(pathPhoto,1920,1920);
        Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(pathPhoto)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //    this.mbitmap = resource;
                            mGPUImageView.setImage(resource);


                        }
                    });

        groupAllFiltersTemp = new GPUImageFilterGroup();
        groupFilterCurrentColors = new GPUImageFilterGroup();
        // init progress values and groups
        initProgressValues();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        markFilterItemses = markFilterPhoto();
        filterPhotosItemses = prepareFilterPhoto();
        adapterFilterPhoto = new ListFiltersPhotoAdapter(getApplicationContext(), filterPhotosItemses,pathPhoto,markFilterItemses);
        recyclerView.setAdapter(adapterFilterPhoto);

        filterColorsItemses = prepareFilterColors();
        adapterFilterColors = new ListFiltersColorsAdapter(getApplicationContext(), filterColorsItemses);

        //Animation Text of Filter;
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);

        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        anim = new AnimationSet(false);//false means don't share interpolators
        typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"JosefinSans-Bold.ttf");
        titleFilter.setVisibility(View.INVISIBLE);

        hideStatutBar();
        controlApperenceFilters();
        nextActionPhotoReady();
    }

    private static void initProgressValues(){
        // Gausian, sepia,vignette,shadow
        progressValues = new ArrayList<Integer>();
        for (int i=0; i <12 ; i++){
            if ( i == 2 || i == 4 || i == 7 || i == 9){
                progressValues.add(0);

            }else {
                progressValues.add(50);
            }
        }

    }


    private void controlApperenceFilters(){
        imageViewFilterColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewFilterColors.setImageResource(R.drawable.ic_filter_colors_on);
                imageViewFilterPhoto.setImageResource(R.drawable.ic_filter_photo_off);

                seekBar.setVisibility(View.INVISIBLE);
                valueFilter.setVisibility(View.INVISIBLE);
                containerBtns.setVisibility(View.INVISIBLE);
                nestedScrollView.setVisibility(View.VISIBLE);
                filterColorsItemses = prepareFilterColors();
                adapterFilterColors = new ListFiltersColorsAdapter(getApplicationContext(), filterColorsItemses);
                recyclerView.setAdapter(adapterFilterColors);

            }
        });
        imageViewFilterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewFilterColors.setImageResource(R.drawable.ic_filter_colors_off);
                imageViewFilterPhoto.setImageResource(R.drawable.ic_filter_photo_on);

                seekBar.setVisibility(View.INVISIBLE);
                valueFilter.setVisibility(View.INVISIBLE);
                containerBtns.setVisibility(View.INVISIBLE);
                nestedScrollView.setVisibility(View.VISIBLE);
                adapterFilterPhoto.swapItems(mPosFilterPhoto);
                recyclerView.setAdapter(adapterFilterPhoto);
            }
        });

    }

    public  static void changeFilter(GPUImageFilter filter, int typeFilter,  int pos, Context context) {
        switch (typeFilter) {
            case 0: //Filter photo .. Valencia 1977 Rise ..
                switch (pos) { // Animation Title of Filter photo
                    case 1:
                        animateText(titleFilterPhoto[pos]);
                        break;
                    case 2:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 3:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 4:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 5:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 6:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 7:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 8:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 9:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 10:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 11:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 12:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 13:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 14:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 15:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 16:
                        animateText(titleFilterPhoto[pos]);

                        break;
                    case 17:
                        animateText(titleFilterPhoto[pos]);

                        break;

                }
                mPosFilterPhoto = pos;
                adapterFilterPhoto.swapItems(mPosFilterPhoto);

                mFilterPhoto = filter;
                if (pos == 0){
                    groupFilterCurrentColors.getFilters().clear();
                    groupAllFiltersTemp.getFilters().clear();
                    initProgressValues();
                    mGPUImageView.setFilter(mFilterPhoto);
                }else {
                    groupAllFiltersTemp.getFilters().clear();
                    groupAllFiltersTemp.addFilter(mFilterPhoto);
                    groupAllFiltersTemp.addFilter(groupFilterCurrentColors);
                    mGPUImageView.setFilter(groupAllFiltersTemp);

                }
                break;
            case 1: // Filter Color Contrast .. Sepia ..
                progressFilterColors(filter, pos, context);
                break;

        }
    }

    static void progressFilterColors(GPUImageFilter filter, final int pos, final Context context){
        nestedScrollView.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        valueFilter.setVisibility(View.VISIBLE);
        containerBtns.setVisibility(View.VISIBLE);
        mFilterColor = filter;
        mPos = pos;
        mCurrentProgress = progressValues.get(mPos);

        groupAllFiltersTemp.getFilters().clear();
        groupAllFiltersTemp.addFilter(mFilterPhoto);
        groupAllFiltersTemp.addFilter(groupFilterCurrentColors);
        indexFilter = getIndexFilter(mFilterColor,groupFilterCurrentColors);

        if (indexFilter < 0){
            groupAllFiltersTemp.addFilter(mFilterColor);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilterColor);
            if (mFilterAdjuster != null) {
                mFilterAdjuster.adjust(mCurrentProgress);
            }

            mGPUImageView.setFilter(groupAllFiltersTemp);
            mGPUImageView.requestRender();

        }
        else{
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(groupFilterCurrentColors.getFilters().get(indexFilter));
            if (mFilterAdjuster != null) {
                mFilterAdjuster.adjust(mCurrentProgress);
            }

            mGPUImageView.setFilter(groupAllFiltersTemp);
            mGPUImageView.requestRender();

        }
        seekBar.setProgress(mCurrentProgress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                valueFilter.setText("" + progress);
                valueFilter.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                valueFilter.setY(50);// just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar
                if (indexFilter < 0){
                    mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilterColor);
                    if (mFilterAdjuster != null) {
                        mFilterAdjuster.adjust(progress);
                    }

                    mGPUImageView.setFilter(groupAllFiltersTemp);
                    mGPUImageView.requestRender();


                }
                else{
                    mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(groupFilterCurrentColors.getFilters().get(indexFilter));
                    if (mFilterAdjuster != null) {
                        mFilterAdjuster.adjust(progress);
                    }

                    mGPUImageView.setFilter(groupAllFiltersTemp);
                    mGPUImageView.requestRender();

                }

                final int finalProgress = progress;
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (indexFilter < 0){
                            groupFilterCurrentColors.addFilter(mFilterColor);
                        }

                        progressValues.set(mPos, finalProgress);
                        containerBtns.setVisibility(View.INVISIBLE);
                        seekBar.setVisibility(View.INVISIBLE);
                        valueFilter.setVisibility(View.INVISIBLE);
                        nestedScrollView.setVisibility(View.VISIBLE);
                        nestedScrollView.setVisibility(View.VISIBLE);
                        filterColorsItemses = prepareFilterColors();
                        adapterFilterColors = new ListFiltersColorsAdapter(context, filterColorsItemses);
                        recyclerView.setAdapter(adapterFilterColors);

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupAllFiltersTemp.getFilters().clear();
                        groupAllFiltersTemp.addFilter(mFilterPhoto);
                        groupAllFiltersTemp.addFilter(groupFilterCurrentColors);
                        mGPUImageView.setFilter(groupAllFiltersTemp);
                        mGPUImageView.requestRender();
                        progressValues.set(mPos, mCurrentProgress);

                        containerBtns.setVisibility(View.INVISIBLE);
                        valueFilter.setVisibility(View.INVISIBLE);
                        seekBar.setVisibility(View.INVISIBLE);
                        nestedScrollView.setVisibility(View.VISIBLE);
                        nestedScrollView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapterFilterColors);

                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    public static int getIndexFilter(GPUImageFilter filter,GPUImageFilterGroup group){
        GPUImageFilter f;
        int pos= -1;
        for (int i=0; i<group.getFilters().size(); i++) {
            f = group.getFilters().get(i);
            if (f.getClass().getSimpleName().equals(filter.getClass().getSimpleName())){
                pos = i;
                return pos;
            }

        }
        return pos;
    }
    /**
    private ArrayList<PopulairCategoriesHorizontalItems> prepareFilterPhoto(){

        ArrayList<PopulairCategoriesHorizontalItems> theimage = new ArrayList<>();
        for(int i = 0; i< titleFilterPhoto.length; i++){
            PopulairCategoriesHorizontalItems galleryPhotosItems = new PopulairCategoriesHorizontalItems();
            switch(i) {
                case 0:
                    galleryPhotosItems.setFilter(new GPUImageFilter());
                    break;
                case 1:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_1977));

                    break;
                case 2:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_VALENCIA));
                    break;
                case 3:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_AMARO));
                    break;
                case 4:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_BRANNAN));
                    break;
                case 5:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_EARLYBIRD));
                    break;
                case 6:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_HEFE));
                    break;
                case 7:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_HUDSON));
                    break;
                case 8:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_INKWELL));
                    break;
                case 9:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_LOMO));
                    break;
                case 10:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_LORDKELVIN));
                    break;
                case 11:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_NASHVILLE));
                    break;
                case 12:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_RISE));
                    break;
                case 13:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_SIERRA));
                    break;
                case 14:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_SUTRO));
                    break;
                case 15:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_TOASTER));
                    break;
                case 16:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_WALDEN));
                    break;
                case 17:
                    galleryPhotosItems.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_XPROII));
                    break;
            }

            galleryPhotosItems.setTitle(titleFilterPhoto[i]);
            theimage.add(galleryPhotosItems);
        }
        return theimage;
    } **/
    private static ArrayList<PopulairCategoriesHorizontalItems> markFilterPhoto() {
        ArrayList<PopulairCategoriesHorizontalItems> theimage = new ArrayList<>();
        for(int i = 0; i< titleFilterPhoto.length; i++) {
            PopulairCategoriesHorizontalItems galleryPhotosItems = new PopulairCategoriesHorizontalItems();
            galleryPhotosItems.setMarkFilter(R.drawable.ic_current_filter_photo_off);

            theimage.add(galleryPhotosItems);

        }
        return theimage;
        }
    private ArrayList<PopulairCategoriesHorizontalItems> prepareFilterPhoto(){

        thumbBitmap = decodeSampledBitmapFromPathFile(pathPhoto,120,120);

        GPUImage gpuImage = new GPUImage(getApplicationContext());
        gpuImage.setImage(thumbBitmap);

        ArrayList<PopulairCategoriesHorizontalItems> theimage = new ArrayList<>();
        for(int i = 0; i< titleFilterPhoto.length; i++){
            PopulairCategoriesHorizontalItems galleryPhotosItems = new PopulairCategoriesHorizontalItems();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            switch(i) {
                case 0:
                    gpuImage.setFilter(new GPUImageFilter());
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 1:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_1977));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();

                    break;
                case 2:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_VALENCIA));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();

                    break;
                case 3:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_AMARO));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 4:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_BRANNAN));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 5:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_EARLYBIRD));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 6:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_HEFE));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 7:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_HUDSON));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 8:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_INKWELL));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 9:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_LOMO));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 10:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_LORDKELVIN));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 11:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_NASHVILLE));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 12:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_RISE));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 13:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_SIERRA));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 14:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_SUTRO));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 15:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_TOASTER));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 16:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_WALDEN));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                case 17:
                    gpuImage.setFilter(GPUImageFilterTools.createFilterForType(getApplicationContext(),
                            GPUImageFilterTools.FilterType.I_XPROII));
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
                    break;
                default:
                    gpuImage.setFilter(new GPUImageBilateralFilter());
                    thumbBitmap = gpuImage.getBitmapWithFilterApplied();
            }

            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            galleryPhotosItems.setTitle(titleFilterPhoto[i]);
            galleryPhotosItems.setStream(stream);
            theimage.add(galleryPhotosItems);
        }
        return theimage;
    }


    private static ArrayList<PopulairCategoriesHorizontalItems> prepareFilterColors(){

        ArrayList<PopulairCategoriesHorizontalItems> item = new ArrayList<>();
        for(int i = 0; i< titleFilterColors.length; i++){
            PopulairCategoriesHorizontalItems filterItems = new PopulairCategoriesHorizontalItems();
            filterItems.setTitle(titleFilterColors[i]);
            if ( i == 2 || i == 4 || i == 7 || i == 9){
                if (progressValues.get(i) == 0){
                    filterItems.setImage(R.drawable.ic_not_updated_filter_color);
                }else {
                    filterItems.setImage(R.drawable.ic_updated_filter_color);
                }
            }else {
                if (progressValues.get(i) == 50){
                    filterItems.setImage(R.drawable.ic_not_updated_filter_color);
                }else {
                    filterItems.setImage(R.drawable.ic_updated_filter_color);
                }            }

            item.add(filterItems);
        }
        return item;
    }

    private void nextActionPhotoReady(){
        txtViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBitmap = mGPUImageView.capture();
                    uriBitmap = getImageUri(getApplicationContext(),mBitmap);

                    Log.e("Main","uriiii: "+uriBitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(FiltersPhotoActivity.this,
                        PhotosToPublishActivity.class);
                intent.putExtra("UriPhoto", uriBitmap.toString());
                startActivity(intent);

            }
        });

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void animateText(String text){
        titleFilter.setTypeface(typeFace);
        titleFilter.setVisibility(View.VISIBLE);
        titleFilter.setText(text);
        anim = new AnimationSet(false);//false means don't share interpolators
        anim.addAnimation(animZoomOut);
        anim.addAnimation(animZoomIn);
        titleFilter.startAnimation(anim);
        titleFilter.setVisibility(View.INVISIBLE);

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


}
