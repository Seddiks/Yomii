package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.DisplayPhotosPublishedAdapter;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbonnesFragment extends Fragment {
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
    private final Integer image_pro[] = {
            R.drawable.bg_milan,
            R.drawable.bg_paris,
            R.drawable.bg_london,
            R.drawable.bg_moscow,
            R.drawable.bg_madrid,
            R.drawable.bg_munich,
            R.drawable.bg_barca,
            R.drawable.bg_ny,
    };




    public AbonnesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_abonnes, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        ArrayList<DisplayPhotosPublishedItems> PhotosItemses = prepareData();
        DisplayPhotosPublishedAdapter adapter = new DisplayPhotosPublishedAdapter(Glide.with(this),getActivity(), PhotosItemses,false);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);



        return rootView;
    }

    private ArrayList<DisplayPhotosPublishedItems> prepareData(){

        ArrayList<DisplayPhotosPublishedItems> item = new ArrayList<>();
        for(int i = 0; i< image_ids.length; i++){
            DisplayPhotosPublishedItems photosItems = new DisplayPhotosPublishedItems();
          //  photosItems.setImage_published(image_ids[i]);
          //  photosItems.setImage_profile(image_pro[i]);
            item.add(photosItems);
        }
        return item;
    }


}
