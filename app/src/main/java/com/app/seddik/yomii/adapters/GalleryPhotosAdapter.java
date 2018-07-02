package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 23/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.DisplayPhotosPublishedActivity;
import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class GalleryPhotosAdapter extends RecyclerView.Adapter<GalleryPhotosAdapter.ViewHolder> {
    private ArrayList<GalleryPhotosItems> galleryList;
    private Context context;
    private String path;
    private int parent;


    public GalleryPhotosAdapter(Context context, ArrayList<GalleryPhotosItems> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public GalleryPhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_photos_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryPhotosAdapter.ViewHolder viewHolder, final int i) {
        path = URL_UPLOAD_PHOTOS+galleryList.get(i).getPhoto_path();
        parent = galleryList.get(i).getParent();
        if (parent == -1){
            viewHolder.img_multiple.setVisibility(View.VISIBLE);
        }
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(path).into(viewHolder.img);

        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayPhotosPublishedActivity.class);
                intent.putExtra("PHOTOS_Fragment_Photo_id",galleryList.get(i).getPhoto_id());
                intent.putExtra("PHOTOS_Fragment_PathParent",galleryList.get(i).getPhoto_path());
                intent.putExtra("PHOTOS_Fragment_Parent",galleryList.get(i).getParent());
                context.startActivity(intent);


            }
        });
    }
//2560*1920
    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private ImageView img_multiple;
        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.img);
            img_multiple = view.findViewById(R.id.img_multiple);
        }
    }

}