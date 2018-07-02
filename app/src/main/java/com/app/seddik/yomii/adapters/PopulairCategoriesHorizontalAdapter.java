package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 02/01/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.activities.DisplayPhotosPublishedActivity;
import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PopulairCategoriesHorizontalAdapter extends RecyclerView.Adapter<PopulairCategoriesHorizontalAdapter.ViewHolder> {
    private ArrayList<PopulairCategoriesHorizontalItems> galleryList;
    private Context context;


    public PopulairCategoriesHorizontalAdapter(Context context, ArrayList<PopulairCategoriesHorizontalItems> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public PopulairCategoriesHorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.populair_categories_horizontal_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopulairCategoriesHorizontalAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(galleryList.get(i).getImage()).into(viewHolder.img);
        viewHolder.title.setText(galleryList.get(i).getTitle());

        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked! "+i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DisplayPhotosPublishedActivity.class);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView title;
        public ViewHolder(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.imageView);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

}