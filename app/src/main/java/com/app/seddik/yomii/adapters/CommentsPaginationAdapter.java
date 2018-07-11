package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.models.CommentItems;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seddik on 09/07/2018.
 */

public class CommentsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<CommentItems> commentsResults;
    private Context context;

    private boolean isLoadingAdded = false;


    public CommentsPaginationAdapter(Context context) {
        this.context = context;
        commentsResults = new ArrayList<>();
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
        View v1 = inflater.inflate(R.layout.comments_items, parent, false);
        viewHolder = new CommentsPaginationAdapter.ViewHolder(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CommentItems result = commentsResults.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final CommentsPaginationAdapter.ViewHolder viewHolder = (CommentsPaginationAdapter.ViewHolder) holder;
                Glide.with(context)
                        .load(R.drawable.bgmoi2)
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewHolder.photo_profil);
                viewHolder.tv_name.setText(result.getFull_name());
                viewHolder.tv_comment.setText(result.getComment());


                break;

            case LOADING:
//                Do nothing
                break;
        }


    }

    @Override
    public int getItemCount() {
        return commentsResults == null ? 0 : commentsResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == commentsResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


       /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(CommentItems r) {
        commentsResults.add(r);
        notifyItemInserted(commentsResults.size() - 1);
    }

    public void addAll(List<CommentItems> mResults) {
        for (CommentItems result : mResults) {
            add(result);
        }
    }

    public void remove(CommentItems r) {
        int position = commentsResults.indexOf(r);
        if (position > -1) {
            commentsResults.remove(position);
            notifyItemRemoved(position);
        }
    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new CommentItems());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = commentsResults.size() - 1;
        CommentItems result = getItem(position);

        if (result != null) {
            commentsResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CommentItems getItem(int position) {
        return commentsResults.get(position);
    }


     /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo_profil;
        private TextView tv_name, tv_comment, tv_date, tv_delete, tv_publication;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            photo_profil = itemView.findViewById(R.id.photo_profil);
            tv_name = itemView.findViewById(R.id.name_profil);
            tv_comment = itemView.findViewById(R.id.comment);
            tv_date = itemView.findViewById(R.id.date);
            tv_delete = itemView.findViewById(R.id.delete);
            tv_publication = itemView.findViewById(R.id.publication);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
