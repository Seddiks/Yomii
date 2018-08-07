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
import com.app.seddik.yomii.utils.CommentUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

/**
 * Created by Seddik on 09/07/2018.
 */

public class CommentsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<CommentItems> commentsResults;
    private Context context;
    private int position_photo;

    private boolean isLoadingAdded = false;
    private SessionManager session;

    public CommentsPaginationAdapter(Context context, int position_photo) {
        this.context = context;
        this.position_photo = position_photo;
        commentsResults = new ArrayList<>();
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
        View v1 = inflater.inflate(R.layout.comments_items, parent, false);
        viewHolder = new CommentsPaginationAdapter.ViewHolder(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final CommentItems result = commentsResults.get(position);
        final int user_id = session.getUSER_ID();
        switch (getItemViewType(position)) {
            case ITEM:
                CommentsPaginationAdapter.ViewHolder viewHolder = (CommentsPaginationAdapter.ViewHolder) holder;
                int Comment_ID = result.getComment_id();
                String path_photo_profil = URL_UPLOAD_PHOTOS + result.getPhoto_profil_path();
                Glide.with(context)
                        .load(path_photo_profil)
                        .apply(new RequestOptions().
                                placeholder(R.drawable.ic_person_circle_blue_a400_36dp).
                                error(R.drawable.ic_person_circle_blue_a400_36dp).
                                apply(RequestOptions.circleCropTransform()))
                        .into(viewHolder.photo_profil);

                viewHolder.tv_name.setText(result.getFull_name());
                viewHolder.tv_comment.setText(result.getComment());
                viewHolder.tv_date.setText(result.getCreated_at());

                if (Comment_ID >= 0) { // case load all comments from server
                    viewHolder.tv_date.setVisibility(View.VISIBLE);
                    if (user_id == result.getUser_id()) {
                        viewHolder.tv_delete.setVisibility(View.VISIBLE);

                    } else {
                        viewHolder.tv_delete.setVisibility(View.GONE);

                    }
                } else { // case insert new comment in server
                    new CommentUtils(viewHolder.tv_date, viewHolder.tv_delete, viewHolder.tv_publication, viewHolder.progressBar, viewHolder.tv_error)
                            .insertComment(context, user_id, result, position_photo, new CommentUtils.InsertCommentCallbacks() {
                                @Override
                                public void onInsertSuccess(int comment_id, int number_comments) {
                                    result.setComment_id(comment_id);

                                }

                                @Override
                                public void onInsertFailed(Throwable error) {

                                }
                            });


                }
                // Handle click in DELETE for delete comment
                viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new CommentUtils().deleteComment(context, user_id, result, position_photo, new CommentUtils.DeleteCommentCallbacks() {
                            @Override
                            public void onConfirm() {
                                removeItem(position);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

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

    public void addInTop(CommentItems r) {
        commentsResults.add(0, r);
        notifyItemInserted(0);
        notifyDataSetChanged();

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

    public void removeItem(int position) {
        commentsResults.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, commentsResults.size());

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
        private TextView tv_name, tv_comment, tv_date, tv_delete, tv_publication, tv_error;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            photo_profil = itemView.findViewById(R.id.photo_profil);
            tv_name = itemView.findViewById(R.id.name_profil);
            tv_comment = itemView.findViewById(R.id.comment);
            tv_date = itemView.findViewById(R.id.date);
            tv_delete = itemView.findViewById(R.id.delete);
            tv_publication = itemView.findViewById(R.id.publication);
            tv_error = itemView.findViewById(R.id.error);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
