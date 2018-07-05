package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 23/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.FullScreenImageActivity;
import com.app.seddik.yomii.activities.ProfileAbonneActivity;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

public class DisplayPhotosPublishedAdapter extends RecyclerView.Adapter<DisplayPhotosPublishedAdapter.ViewHolder> {
    private ArrayList<DisplayPhotosPublishedItems> galleryList;
    private Context context;
    private final RequestManager glide;
    private boolean showFollowingButton;
    private boolean clicked = false;
    private String path_photo_published;
    private String path_photo_profil;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;




    public DisplayPhotosPublishedAdapter(RequestManager glide,Context context, ArrayList<DisplayPhotosPublishedItems> galleryList,boolean showFollowingButton) {
        this.galleryList = galleryList;
        this.context = context;
        this.showFollowingButton = showFollowingButton;
        this.glide = glide;
    }

    @Override
    public DisplayPhotosPublishedAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_photos_published_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DisplayPhotosPublishedAdapter.ViewHolder viewHolder, final int i) {

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        //Profile photo
        DisplayPhotosPublishedItems photo = galleryList.get(i);
        path_photo_profil= URL_UPLOAD_PHOTOS+photo.getPhoto_published();
        viewHolder.img_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);


        //Published photo
        path_photo_published = URL_UPLOAD_PHOTOS+galleryList.get(i).getPhoto_published();

        viewHolder.img_published.setScaleType(ImageView.ScaleType.FIT_CENTER);


        glide.asBitmap().load(path_photo_published)
              .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>(500, 500) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        viewHolder.img_published.setImageBitmap(resource);
                    }
                });



        if (!showFollowingButton){
            viewHolder.btnFollowing.setVisibility(View.GONE);
        }


        viewHolder.tv_name.setText(galleryList.get(i).getFull_name());
        viewHolder.tv_date.setText("28 Juin 2018");
        viewHolder.tv_likes.setText("1.3 K Likes");
        viewHolder.tv_comments.setText("94 comment");


        viewHolder.img_profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked! "+i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,  ProfileAbonneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        viewHolder.img_published.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,  FullScreenImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("PathPhoto",URL_UPLOAD_PHOTOS+galleryList.get(i).getPhoto_published());
                context.startActivity(intent);

            }
        });

        viewHolder.btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked == false){
                    viewHolder.btnFollowing.setBackgroundResource(R.drawable.ripple_after_blue_500);
                    viewHolder.btnFollowing.setText("Following");
                    viewHolder.btnFollowing.setTextColor(Color.WHITE);
                    clicked = true;
                }else {
                    viewHolder.btnFollowing.setBackgroundResource(R.drawable.ripple_blue_500);
                    viewHolder.btnFollowing.setText("Follow");
                    viewHolder.btnFollowing.setTextColor(Color.parseColor("#03A9F4"));
                    clicked = false;

                }

            }
        });

    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolder viewHolder = (ViewHolder) holder;
        glide.clear(viewHolder.img_published);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == galleryList.size() && isLoadingAdded) ? LOADING : ITEM;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_profile ,img_published;
        private TextView tv_name ,tv_date ,tv_likes,tv_comments;
        private Button btnFollowing;
        public ViewHolder(View view) {
            super(view);

            img_profile = (ImageView) view.findViewById(R.id.profile_image);
            img_published = (ImageView) view.findViewById(R.id.photosPublished);
            btnFollowing = (Button) view.findViewById(R.id.following);
            tv_name = (TextView) view.findViewById(R.id.profile_name);
            tv_date = (TextView) view.findViewById(R.id.date);
            tv_likes = (TextView) view.findViewById(R.id.numberReactions);
            tv_comments = (TextView) view.findViewById(R.id.numberComments);


        }
    }

}