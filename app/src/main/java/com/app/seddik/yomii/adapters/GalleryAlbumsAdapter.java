package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 23/12/2017.
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

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.DisplayPhotosPublishedActivity;
import com.app.seddik.yomii.models.GalleryAlbumsItems;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class GalleryAlbumsAdapter extends RecyclerView.Adapter<GalleryAlbumsAdapter.ViewHolder> {
    private ArrayList<GalleryAlbumsItems> galleryList;
    private Context context;
    ArrayList<GalleryAlbumsItems.Paths> ListPathsPhotos;


    public GalleryAlbumsAdapter(Context context, ArrayList<GalleryAlbumsItems> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public GalleryAlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_albums_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAlbumsAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String path = URL_UPLOAD_PHOTOS+galleryList.get(i).getData().get(0).getPhoto_path();

        Glide.with(context).load(path).into(viewHolder.img);

        viewHolder.title.setText(galleryList.get(i).getTitle());
        Typeface type = Typeface.createFromAsset(context.getAssets(),"LeagueSpartan-Bold.otf");
        viewHolder.title.setTypeface(type);


        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DisplayPhotosPublishedActivity.class);
                intent.putExtra("ALBMUM_Fragment_ListPaths",galleryList.get(i).getData());
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
        private TextView title;
        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.img);
            title = view.findViewById(R.id.title);
        }
    }

}