package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.NotificationItems;
import com.app.seddik.yomii.utils.NotificationUtils;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seddik on 08/08/2018.
 */

public class NotificationsPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int LIKE = 0;
    private static final int COMMENT = 1;
    private static final int FOLLOW = 2;
    String notificationText;
    String FullNotification;
    private String photo_profil_path;
    private String full_name;
    private int action_type;
    private boolean is_read;


    private ArrayList<NotificationItems> notificationResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private SessionManager session;

    public NotificationsPaginationAdapter(Context context) {
        this.context = context;
        notificationResults = new ArrayList<>();
        session = new SessionManager(context);
    }

    /*
Helpers
_________________________________________________________________________________________________
 */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
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
        View v1 = inflater.inflate(R.layout.notification_items, parent, false);
        viewHolder = new NotificationsPaginationAdapter.ViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final NotificationItems result = notificationResults.get(position);
        final int user_id = session.getUSER_ID();
        switch (getItemViewType(position)) {
            case ITEM:
                final NotificationsPaginationAdapter.ViewHolder viewHolder = (NotificationsPaginationAdapter.ViewHolder) holder;

                is_read = result.is_read();
                if (is_read) {
                    viewHolder.parent_layout.setBackgroundColor(Color.WHITE);

                } else {
                    viewHolder.parent_layout.setBackgroundColor(Color.parseColor("#E0ECF8"));

                }
                photo_profil_path = AppConfig.URL_UPLOAD_PHOTOS + result.getPhoto_profil_sender();
                Glide.with(context)
                        .load(photo_profil_path)
                        .apply(new RequestOptions().
                                placeholder(R.drawable.ic_person_circle_blue_a400_36dp).
                                error(R.drawable.ic_person_circle_blue_a400_36dp).
                                apply(RequestOptions.circleCropTransform()))
                        .into(viewHolder.photo_profil);
                viewHolder.date.setText(result.getCreated_at());

                full_name = result.getFull_name();
                action_type = result.getAction_type();

                switch (action_type) {

                    case LIKE:
                        notificationText = " has liked your photo";
                        FullNotification = "<b>" + full_name + "</b> " + notificationText;
                        viewHolder.notification.setText(fromHtml(FullNotification));
                        viewHolder.image_type_notification.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_white_18dp));
                        viewHolder.image_type_notification.setBackground(ContextCompat.getDrawable(context, R.drawable.red_circle));
                        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.parent_layout.setBackgroundColor(Color.WHITE);
                                new NotificationUtils().isReadNotification(result.getNotification_id());
                                Toast.makeText(context, "new like " + result.getNotification_id(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        break;

                    case COMMENT:
                        notificationText = " has commented your photo";
                        FullNotification = "<b>" + full_name + "</b> " + notificationText;
                        viewHolder.notification.setText(fromHtml(FullNotification));
                        viewHolder.image_type_notification.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_comment_white_18dp));
                        viewHolder.image_type_notification.setBackground(ContextCompat.getDrawable(context, R.drawable.green_light_circle));
                        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.parent_layout.setBackgroundColor(Color.WHITE);
                                new NotificationUtils().isReadNotification(result.getNotification_id());
                                Toast.makeText(context, "new comment " + result.getNotification_id(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        break;

                    case FOLLOW:
                        notificationText = " start following you";
                        FullNotification = "<b>" + full_name + "</b> " + notificationText;
                        viewHolder.notification.setText(fromHtml(FullNotification));
                        viewHolder.image_type_notification.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_add_white_18dp));
                        viewHolder.image_type_notification.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_circle));
                        viewHolder.parent_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewHolder.parent_layout.setBackgroundColor(Color.WHITE);
                                new NotificationUtils().isReadNotification(result.getNotification_id());
                                Toast.makeText(context, "new follower", Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;

                }

                viewHolder.more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(context, viewHolder.more);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.notification_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.hide:
                                        removeItem(position);
                                        new NotificationUtils().hideNotification(result.getNotification_id());
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();

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
        return notificationResults == null ? 0 : notificationResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == notificationResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(NotificationItems r) {
        notificationResults.add(r);
        notifyItemInserted(notificationResults.size() - 1);

    }


    public void addAll(List<NotificationItems> mResults) {
        for (NotificationItems result : mResults) {
            add(result);
        }
    }

    public void remove(NotificationItems r) {
        int position = notificationResults.indexOf(r);
        if (position > -1) {
            notificationResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        notificationResults.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notificationResults.size());

    }


    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NotificationItems());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = notificationResults.size() - 1;
        NotificationItems result = getItem(position);

        if (result != null) {
            notificationResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NotificationItems getItem(int position) {
        return notificationResults.get(position);
    }


     /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo_profil, image_type_notification, more;
        private TextView notification, date;
        private RelativeLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            photo_profil = itemView.findViewById(R.id.profile_image);
            image_type_notification = itemView.findViewById(R.id.type_notification);
            more = itemView.findViewById(R.id.more_image);
            notification = itemView.findViewById(R.id.notification);
            date = itemView.findViewById(R.id.date);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
