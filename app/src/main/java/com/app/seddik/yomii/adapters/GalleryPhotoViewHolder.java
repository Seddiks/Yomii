package com.app.seddik.yomii.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.bumptech.glide.Glide;

/**
 * Created by Seddik on 16/08/2018.
 */

public class GalleryPhotoViewHolder extends RecyclerView.ViewHolder {


    // @BindView(R.id.photo)
    ImageView photo;

    // @BindView(R.id.icon_gallery)
    ImageView icon_gallery;

    private GalleryPhotoViewHolder(View itemView) {
        super(itemView);
        // ButterKnife.bind(this, itemView);
        photo = itemView.findViewById(R.id.photo);
        icon_gallery = itemView.findViewById(R.id.icon_gallery);

    }

    public static GalleryPhotoViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.gallery_photos_items, parent, false);
        return new GalleryPhotoViewHolder(view);
    }

    public void bindTo(GalleryPhotosItems galleryPhotosItems) {
        String path_photo = AppConfig.URL_UPLOAD_PHOTOS + galleryPhotosItems.getPhoto_path();
        Glide.with(itemView.getContext())
                .load(path_photo)
                //      .placeholder(R.mipmap.ic_launcher)
                .into(photo);

    }

}
