package com.app.seddik.yomii.test.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.app.seddik.yomii.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed Abd-Elmeged on 2/20/2018.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.UserAvatar)
    ImageView userAvatar;

    private UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static UserViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    public void bindTo(GalleryPhotosItems galleryPhotosItems) {
        String path_photo = AppConfig.URL_UPLOAD_PHOTOS + galleryPhotosItems.getPhoto_path();

        GlideApp.with(itemView.getContext())
                .load(path_photo)
                .placeholder(R.mipmap.ic_launcher)
                .into(userAvatar);
    }

}
