package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

/**
 * Created by Seddik on 13/08/2018.
 */

public class GalleryPhotosPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<GalleryPhotosItems> photosResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private SessionManager session;
    private String path;
    private int parent;


    public GalleryPhotosPaginationAdapter(Context context) {
        this.context = context;
        photosResults = new ArrayList<>();
        session = new SessionManager(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        View v1 = inflater.inflate(R.layout.gallery_photos_items, parent, false);
        viewHolder = new GalleryPhotosPaginationAdapter.ViewHolder(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final GalleryPhotosItems result = photosResults.get(position);
        final int user_id = session.getUSER_ID();
        switch (getItemViewType(position)) {
            case ITEM:
                GalleryPhotosPaginationAdapter.ViewHolder viewHolder = (GalleryPhotosPaginationAdapter.ViewHolder) holder;
                parent = result.getParent();
                path = URL_UPLOAD_PHOTOS + result.getPhoto_path();

                if (parent == -1) viewHolder.img_multiple.setVisibility(View.VISIBLE);
                else viewHolder.img_multiple.setVisibility(View.GONE);

                viewHolder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(path).into(viewHolder.photo);


                break;

            case LOADING:
//                Do nothing
                break;
        }


    }

    @Override
    public int getItemCount() {
        return photosResults == null ? 0 : photosResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photosResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


       /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(GalleryPhotosItems r) {
        photosResults.add(r);
        notifyItemInserted(photosResults.size() - 1);
    }


    public void addAll(List<GalleryPhotosItems> mResults) {
        for (GalleryPhotosItems result : mResults) {
            add(result);
        }
    }

    public void remove(GalleryPhotosItems r) {
        int position = photosResults.indexOf(r);
        if (position > -1) {
            photosResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        photosResults.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, photosResults.size());

    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new GalleryPhotosItems());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = photosResults.size() - 1;
        GalleryPhotosItems result = getItem(position);

        if (result != null) {
            photosResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GalleryPhotosItems getItem(int position) {
        return photosResults.get(position);
    }


     /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView img_multiple;

        public ViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            img_multiple = itemView.findViewById(R.id.icon_gallery);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
