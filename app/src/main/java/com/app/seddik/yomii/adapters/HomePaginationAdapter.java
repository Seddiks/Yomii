package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.CommentsPhotosActivity;
import com.app.seddik.yomii.activities.FullScreenImageActivity;
import com.app.seddik.yomii.activities.ProfileAbonneActivity;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.app.seddik.yomii.utils.GlideImageLoader;
import com.app.seddik.yomii.utils.LikeUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import static com.app.seddik.yomii.R.id.profile_image;
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
    private SessionManager session;


    public HomePaginationAdapter(Context context) {
        this.context = context;
        photoResults = new ArrayList<>();
        session = new SessionManager(context);

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
        final int numberLikes = result.getNumber_likes();
        final int user_id = session.getUSER_ID();


        switch (getItemViewType(position)) {
            case ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                viewHolder.tv_name.setText(result.getFull_name());
                viewHolder.tv_date.setText(result.getCreated_at());
                viewHolder.btnLike.setChecked(result.isLike());

                if (!result.getLocation().isEmpty()) {
                    viewHolder.tv_location.setVisibility(View.VISIBLE);
                    viewHolder.tv_location.setText(result.getLocation());
                } else {
                    viewHolder.tv_location.setVisibility(View.GONE);
                }

                if (!result.getLegende().isEmpty()) {
                    viewHolder.tv_legende.setVisibility(View.VISIBLE);
                    viewHolder.tv_legende.setText(result.getLegende());
                    viewHolder.img_legende.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_legende.setVisibility(View.GONE);
                    viewHolder.img_legende.setVisibility(View.GONE);
                }

                Typeface typeJosefinSansBold = Typeface.createFromAsset(context.getAssets(), "JosefinSans-Bold.ttf");
                viewHolder.tv_location.setTypeface(typeJosefinSansBold);

                if (numberLikes > 0) {
                    viewHolder.tv_likes.setText(numberLikes + " like");
                } else {
                    viewHolder.tv_likes.setText(" like");

                }
                if (result.getNumber_comments() > 0) {
                    viewHolder.tv_comments.setText(result.getNumber_comments() + " comment");
                } else {
                    viewHolder.tv_comments.setText(" comment");

                }

                String path_photo_profil = URL_UPLOAD_PHOTOS + result.getPhoto_profil();
                Glide.with(context)
                        .load(path_photo_profil)
                        .apply(new RequestOptions().
                                placeholder(R.drawable.ic_person_circle_blue_a400_36dp).
                                error(R.drawable.ic_person_circle_blue_a400_36dp).
                                apply(RequestOptions.circleCropTransform()))
                        .into(viewHolder.img_profile);

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

                viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.btnLike.isChecked()) {
                            //Press Like
                            result.setLike(true);
                            int number = result.getNumber_likes() + 1;
                            result.setNumber_likes(number);
                            viewHolder.tv_likes.setText(result.getNumber_likes() + " like");
                            // Like request
                            new LikeUtils().Like(user_id, result.getUser_id(), result.getPhoto_id(), new LikeUtils.LikeCallbacks() {
                                @Override
                                public void onLikeSuccess(int number_likes) {
                                    result.setNumber_likes(number_likes);
                                    viewHolder.tv_likes.setText(result.getNumber_likes() + " like");

                                }


                                @Override
                                public void onLikeFailed(String error) {
                                    result.setLike(false);
                                    viewHolder.btnLike.setChecked(false);
                                    int number = result.getNumber_likes() - 1;
                                    result.setNumber_likes(number);
                                    if (result.getNumber_likes() > 0)
                                        viewHolder.tv_likes.setText(result.getNumber_likes() + " like");
                                    else viewHolder.tv_likes.setText(" like");

                                }
                            });


                        } else {
                            // Press Unlike
                            result.setLike(false);
                            int number = result.getNumber_likes() - 1;
                            result.setNumber_likes(number);
                            int numberLikes = result.getNumber_likes();
                            if (numberLikes > 0) {
                                viewHolder.tv_likes.setText(numberLikes + " like");
                            } else {
                                viewHolder.tv_likes.setText(" like");

                            }
                            // Unlike Request
                            new LikeUtils().UnLike(user_id, result.getPhoto_id(), new LikeUtils.UnLikeCallbacks() {
                                @Override
                                public void onUnLikeSuccess(int number_likes) {
                                    result.setNumber_likes(number_likes);
                                    int numberLikes = result.getNumber_likes();
                                    if (numberLikes > 0) {
                                        viewHolder.tv_likes.setText(numberLikes + " like");
                                    } else {
                                        viewHolder.tv_likes.setText(" like");

                                    }
                                }

                                @Override
                                public void onUnLikeFailed(String error) {
                                    result.setLike(true);
                                    viewHolder.btnLike.setChecked(true);
                                    int number = result.getNumber_likes() + 1;
                                    result.setNumber_likes(number);
                                    viewHolder.tv_likes.setText(result.getNumber_likes() + " like");

                                }
                            });

                        }

                    }
                });


                viewHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, CommentsPhotosActivity.class);
                        intent.putExtra("user_id", result.getUser_id());
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
                        intent.putExtra("user_id", result.getUser_id());
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
                            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                            viewHolder.btnFollowing.startAnimation(fadeIn);

                            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                                    // viewHolder.btnFollowing.startAnimation(fadeOut);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });

                            viewHolder.btnFollowing.setBackgroundResource(R.drawable.round_border_follwing);
                            viewHolder.btnFollowing.setText("Following");
                            viewHolder.btnFollowing.setTextColor(Color.parseColor("#607D8B"));
                            clicked = true;
                        } else {
                            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                            viewHolder.btnFollowing.startAnimation(fadeIn);

                            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                                    //    viewHolder.btnFollowing.startAnimation(fadeOut);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            viewHolder.btnFollowing.setBackgroundResource(R.drawable.round_border_follow);
                            viewHolder.btnFollowing.setText("Follow");
                            viewHolder.btnFollowing.setTextColor(Color.WHITE);
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
        public ImageView img_profile, img_published, img_comment, img_legende;
        private TextView tv_name, tv_date, tv_location, tv_likes, tv_comments, tv_legende;
        private CheckBox btnLike;
        private Button btnFollowing;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            img_profile = itemView.findViewById(profile_image);
            img_published = itemView.findViewById(R.id.photosPublished);
            img_comment = itemView.findViewById(R.id.comments);
            progressBar = itemView.findViewById(R.id.photo_progress);
            btnLike = itemView.findViewById(R.id.reaction);
            btnFollowing = itemView.findViewById(R.id.following);
            tv_name = itemView.findViewById(R.id.profile_name);
            tv_date = itemView.findViewById(R.id.date);
            tv_likes = itemView.findViewById(R.id.numberReactions);
            tv_comments = itemView.findViewById(R.id.numberComments);
            tv_location = itemView.findViewById(R.id.location);
            tv_legende = itemView.findViewById(R.id.legende);
            img_legende = itemView.findViewById(R.id.legende_img);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}