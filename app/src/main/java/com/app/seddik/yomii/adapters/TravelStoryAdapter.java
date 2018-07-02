package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 02/01/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.activities.CreateTravelStoryActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.TravelStoryActivity;
import com.bumptech.glide.Glide;

import com.app.seddik.yomii.config.AppConfig;
import de.hdodenhof.circleimageview.CircleImageView;
import com.app.seddik.yomii.models.TravelStoryItems;


public class TravelStoryAdapter extends RecyclerView.Adapter<TravelStoryAdapter.ViewHolder> {
    private TravelStoryItems galleryList;
    private Context context;
    private String path;


    public TravelStoryAdapter(Context context, TravelStoryItems galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public TravelStoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_story_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TravelStoryAdapter.ViewHolder viewHolder, final int i) {

        //viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (i == 0){
            viewHolder.ic_add.setVisibility(View.VISIBLE);
            path = "";
            Glide.with(context).load(path).into(viewHolder.img);
            viewHolder.name_city.setText("");

        }else {
            viewHolder.ic_add.setVisibility(View.INVISIBLE);
            path = AppConfig.URL_UPLOAD_PHOTOS+galleryList.getData().get(i-1).getPhoto_path();
            Glide.with(context).load(path).into(viewHolder.img);
            viewHolder.name_city.setText(galleryList.getData().get(i-1).getTitle());
            Typeface type = Typeface.createFromAsset(context.getAssets(),"Quicksand-Regular.otf");
            viewHolder.name_city.setTypeface(type);

        }


        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i){
                    case 0:
                        Intent intent = new Intent(context, CreateTravelStoryActivity.class);
                        context.startActivity(intent);
                        break;
                    default:
                        Intent intent2 = new Intent(context, TravelStoryActivity.class);
                        intent2.putExtra("mStory", galleryList.getData().get(i-1));
                        context.startActivity(intent2);
                        break;

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.getData().size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView img;
        private ImageView ic_add;
        private TextView name_city;
        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.story_image);
            ic_add = view.findViewById(R.id.ic_add);
            name_city =  view.findViewById(R.id.name_city);
        }
    }

}