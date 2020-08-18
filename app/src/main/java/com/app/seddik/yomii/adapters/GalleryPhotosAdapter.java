package com.app.seddik.yomii.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import java.util.Objects;

import io.reactivex.annotations.NonNull;

/**
 * Created by Seddik on 16/08/2018.
 */

public class GalleryPhotosAdapter extends PagedListAdapter<GalleryPhotosItems, RecyclerView.ViewHolder> {

    private static DiffUtil.ItemCallback<GalleryPhotosItems> DiffCallback = new DiffUtil.ItemCallback<GalleryPhotosItems>() {
        @Override
        public boolean areItemsTheSame(@NonNull GalleryPhotosItems oldItem, @NonNull GalleryPhotosItems newItem) {
            return oldItem.getPhoto_id() == newItem.getPhoto_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull GalleryPhotosItems oldItem, @NonNull GalleryPhotosItems newItem) {
            return Objects.equals(oldItem.getPhoto_id(), newItem.getPhoto_id())
                    && Objects.equals(oldItem.getPhoto_path(), newItem.getPhoto_path());
        }
    };
    private NetworkState networkState;
    private RetryCallback retryCallback;

    public GalleryPhotosAdapter(RetryCallback retryCallback) {
        super(DiffCallback);
        this.retryCallback = retryCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.gallery_photos_items:
                return GalleryPhotoViewHolder.create(parent);
            case R.layout.item_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.gallery_photos_items:
                ((GalleryPhotoViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.item_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        } else {
            return R.layout.gallery_photos_items;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newNetworkState the new network state
     */
    public void setNetworkState(NetworkState newNetworkState) {
        if (getCurrentList() != null) {
            if (getCurrentList().size() != 0) {
                NetworkState previousState = this.networkState;
                boolean hadExtraRow = hasExtraRow();
                this.networkState = newNetworkState;
                boolean hasExtraRow = hasExtraRow();
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount());
                    } else {
                        notifyItemInserted(super.getItemCount());
                    }
                } else if (hasExtraRow && previousState != newNetworkState) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }


}