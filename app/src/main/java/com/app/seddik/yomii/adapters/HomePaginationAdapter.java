package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.CommentsPhotosActivity;
import com.app.seddik.yomii.activities.FullScreenImageActivity;
import com.app.seddik.yomii.activities.ProfileAbonneActivity;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.app.seddik.yomii.utils.GlideImageLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

/**
 * Created by Seddik on 03/07/2018.
 */

public class HomePaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<DisplayPhotosPublishedItems> photoResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean clicked = false;


    public HomePaginationAdapter(Context context) {
        this.context = context;
        photoResults = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);

                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);

                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.display_photos_published_items, parent, false);
        viewHolder = new ViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final DisplayPhotosPublishedItems result = photoResults.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                viewHolder.tv_name.setText(result.getFull_name());
                viewHolder.tv_date.setText("28 Juin 2018");
                if (result.getNumber_likes() > 0)
                    viewHolder.tv_likes.setText(result.getNumber_likes() + " like");
                if (result.getNumber_comments() > 0)
                    viewHolder.tv_comments.setText(result.getNumber_comments() + " comment");

                String path_photo_published = URL_UPLOAD_PHOTOS+result.getPhoto_published();
                // Glide.with(context).load(path_photo_published).into(viewHolder.img_published);
                new GlideImageLoader(viewHolder.img_published,
                        viewHolder.progressBar).load(path_photo_published, requestOptions);


                viewHolder.img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProfileAbonneActivity.class);
                        context.startActivity(intent);

                    }
                });
                viewHolder.img_published.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullScreenImageActivity.class);
                        intent.putExtra("PathPhoto", URL_UPLOAD_PHOTOS + result.getPhoto_published());
                        context.startActivity(intent);

                    }
                });

                viewHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, CommentsPhotosActivity.class);
                        intent.putExtra("photo_id", result.getPhoto_id());
                        intent.putExtra("position_photo", position);
                        intent.putExtra("ShowKeyBoard", true);
                        context.startActivity(intent);

                    }
                });
                viewHolder.tv_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, CommentsPhotosActivity.class);
                        intent.putExtra("photo_id", result.getPhoto_id());
                        intent.putExtra("position_photo", position);
                        intent.putExtra("ShowKeyBoard", false);
                        context.startActivity(intent);

                    }
                });

                viewHolder.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clicked == false) {
                            viewHolder.btnFollowing.setBackgroundResource(R.drawable.ripple_after_blue_500);
                            viewHolder.btnFollowing.setText("Following");
                            viewHolder.btnFollowing.setTextColor(Color.WHITE);
                            clicked = true;
                        } else {
                            viewHolder.btnFollowing.setBackgroundResource(R.drawable.ripple_blue_500);
                            viewHolder.btnFollowing.setText("Follow");
                            viewHolder.btnFollowing.setTextColor(Color.parseColor("#03A9F4"));
                            clicked = false;

                        }

                    }
                });



                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return photoResults == null ? 0 : photoResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(DisplayPhotosPublishedItems r) {
        photoResults.add(r);
        notifyItemInserted(photoResults.size() - 1);
    }

    public void addAll(List<DisplayPhotosPublishedItems> mResults) {
        for (DisplayPhotosPublishedItems result : mResults) {
            add(result);
        }
    }

    public void remove(DisplayPhotosPublishedItems r) {
        int position = photoResults.indexOf(r);
        if (position > -1) {
            photoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateItem(int position, int number) {
        photoResults.get(position).setNumber_comments(number);
        notifyItemChanged(position);

    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
         add(new DisplayPhotosPublishedItems());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = photoResults.size() - 1;
        DisplayPhotosPublishedItems result = getItem(position);

        if (result != null) {
            photoResults.remove(position);
             notifyItemRemoved(position);
            }
    }

    public DisplayPhotosPublishedItems getItem(int position) {
        return photoResults.get(position);
    }




   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_profile, img_published, img_comment;
        private TextView tv_name ,tv_date ,tv_likes,tv_comments;
        private Button btnFollowing;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            img_profile = itemView.findViewById(R.id.profile_image);
            img_published = itemView.findViewById(R.id.photosPublished);
            img_comment = itemView.findViewById(R.id.comments);
            progressBar = itemView.findViewById(R.id.photo_progress);
            btnFollowing = itemView.findViewById(R.id.following);
            tv_name = itemView.findViewById(R.id.profile_name);
            tv_date = itemView.findViewById(R.id.date);
            tv_likes = itemView.findViewById(R.id.numberReactions);
            tv_comments = itemView.findViewById(R.id.numberComments);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}