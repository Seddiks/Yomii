package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

/**
 * Created by Seddik on 03/07/2018.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private ArrayList<DisplayPhotosPublishedItems> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
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
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DisplayPhotosPublishedItems result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                movieVH.tv_name.setText(result.getFull_name());
                movieVH.tv_date.setText("28 Juin 2018");
                movieVH.tv_likes.setText("1.3 K Likes");
                movieVH.tv_comments.setText("94 comment");

                String path_photo_published = URL_UPLOAD_PHOTOS+result.getPhoto_published();
                Glide.with(context).load(path_photo_published).into(movieVH.img_published);



                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
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
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<DisplayPhotosPublishedItems> moveResults) {
        for (DisplayPhotosPublishedItems result : moveResults) {
            add(result);
        }
    }

    public void remove(DisplayPhotosPublishedItems r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
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
        int position = movieResults.size() - 1;
        DisplayPhotosPublishedItems result = getItem(position);

        if (result != null) {
             movieResults.remove(position);
             notifyItemRemoved(position);
            }
    }

    public DisplayPhotosPublishedItems getItem(int position) {
        return movieResults.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        public ImageView img_profile ,img_published;
        private TextView tv_name ,tv_date ,tv_likes,tv_comments;
        private Button btnFollowing;

        public MovieVH(View itemView) {
            super(itemView);

            img_profile = (ImageView) itemView.findViewById(R.id.profile_image);
            img_published = (ImageView) itemView.findViewById(R.id.photosPublished);
            btnFollowing = (Button) itemView.findViewById(R.id.following);
            tv_name = (TextView) itemView.findViewById(R.id.profile_name);
            tv_date = (TextView) itemView.findViewById(R.id.date);
            tv_likes = (TextView) itemView.findViewById(R.id.numberReactions);
            tv_comments = (TextView) itemView.findViewById(R.id.numberComments);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}