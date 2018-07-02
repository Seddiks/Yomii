package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 23/12/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.app.seddik.yomii.activities.DisplayPhotosPublishedActivity;

import com.app.seddik.yomii.models.NotificationItems;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<NotificationItems> notificationList;
    private Context context;


    public NotificationAdapter(Context context, ArrayList<NotificationItems> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.img_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(notificationList.get(i).getImage_profile()).into(viewHolder.img_profile);
        viewHolder.profile_name.setText(notificationList.get(i).getName_profile());
        viewHolder.date.setText("12 Jan 2017");


        viewHolder.img_profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked! "+i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DisplayPhotosPublishedActivity.class);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_profile;
        private TextView profile_name, date;
        public ViewHolder(View view) {
            super(view);

            img_profile = (ImageView) view.findViewById(R.id.profile_image);
            profile_name = (TextView) view.findViewById(R.id.profile_name);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

}