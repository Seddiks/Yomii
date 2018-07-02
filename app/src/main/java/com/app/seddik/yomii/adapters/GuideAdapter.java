package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.activities.GuideActivity;
import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;

import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.GuideItems;

/**
 * Created by Seddik on 10/06/2018.
 */

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {
    private GuideItems List;
    private Context context;
    private String path;


    public GuideAdapter(Context context, GuideItems List) {
        this.List = List;
        this.context = context;
    }

    @Override
    public GuideAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.guide_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GuideAdapter.ViewHolder viewHolder, final int i) {

        path = AppConfig.URL_UPLOAD_PHOTOS+List.getData().get(i).getPhoto_path();
        Log.e("MessagePath", "path: "+path);
        Glide.with(context).load(path).into(viewHolder.photo);
        viewHolder.location.setText(List.getData().get(i).getLocation());
        viewHolder.title.setText(List.getData().get(i).getTitle_guide());
        viewHolder.details.setText(List.getData().get(i).getExperience());

      //  Typeface type = Typeface.createFromAsset(context.getAssets(),"Quicksand-Regular.otf");
      //  viewHolder.name_city.setTypeface(type);
        viewHolder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuideActivity.class);
                intent.putExtra("mGuide", List.getData().get(i));
                context.startActivity(intent);


            }
        });


    }


    @Override
    public int getItemCount() {
        return List.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView itemCard;
        private ImageView photo, more ;
        private TextView location ,title ,details;
        public ViewHolder(View view) {
            super(view);

            itemCard =  view.findViewById(R.id.cardView);
            photo =  view.findViewById(R.id.photo);
            more =  view.findViewById(R.id.more);
            location =  view.findViewById(R.id.location);
            title =  view.findViewById(R.id.title);
            details =  view.findViewById(R.id.details);
        }
    }

}
