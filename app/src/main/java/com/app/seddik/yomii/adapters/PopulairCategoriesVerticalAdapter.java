package com.app.seddik.yomii.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.seddik.yomii.R;

import java.util.ArrayList;

import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;

/**
 * Created by Seddik on 04/01/2018.
 */

public class PopulairCategoriesVerticalAdapter extends RecyclerView.Adapter<PopulairCategoriesVerticalAdapter.ViewHolder> {
    private ArrayList<PopulairCategoriesHorizontalItems> galleryList;
    private Context context;
    private final Integer image_ids[] = {
            R.drawable.bg_milan,
            R.drawable.bg_paris,
            R.drawable.bg_london,
            R.drawable.bg_moscow,
            R.drawable.bg_madrid,
            R.drawable.bg_munich,
            R.drawable.bg_barca,
            R.drawable.bg_ny,
    };
    private final String title[] = {
            "Paris",
            "Barçelone",
            "Montreal",
            "Madrid",
            "Roma",
            "New York",
            "Doha",
            "Alger",
    };



    public PopulairCategoriesVerticalAdapter(Context context, ArrayList<PopulairCategoriesHorizontalItems> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public PopulairCategoriesVerticalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.populair_categories_vertical_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopulairCategoriesVerticalAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(galleryList.get(i).getTitle());

        viewHolder.mRecycleviewH.setHasFixedSize(true);
        viewHolder.mRecycleviewH.setItemViewCacheSize(20);
        viewHolder.mRecycleviewH.setDrawingCacheEnabled(true);
        viewHolder.mRecycleviewH.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        viewHolder.mRecycleviewH.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.mRecycleviewH.setLayoutManager(layoutManager);
        ArrayList<PopulairCategoriesHorizontalItems> galleryPhotosItemses = prepareData();
        PopulairCategoriesHorizontalAdapter adapter = new PopulairCategoriesHorizontalAdapter(context, galleryPhotosItemses);
        viewHolder.mRecycleviewH.setAdapter(adapter);




    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private RecyclerView mRecycleviewH;
        public ViewHolder(View view) {
            super(view);

            mRecycleviewH = (RecyclerView) view.findViewById(R.id.recycleview);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    private ArrayList<PopulairCategoriesHorizontalItems> prepareData(){

        ArrayList<PopulairCategoriesHorizontalItems> theimage = new ArrayList<>();
        for(int i = 0; i< title.length; i++){
            PopulairCategoriesHorizontalItems galleryPhotosItems = new PopulairCategoriesHorizontalItems();
            galleryPhotosItems.setTitle(title[i]);
            galleryPhotosItems.setImage(image_ids[i]);
            theimage.add(galleryPhotosItems);
        }
        return theimage;
    }




}