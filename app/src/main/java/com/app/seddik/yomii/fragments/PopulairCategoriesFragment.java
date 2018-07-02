package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.PopulairCategoriesVerticalAdapter;
import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopulairCategoriesFragment extends Fragment {
    private final String title[] = {
            "CITIES",
            "NATURE",
            "SPORTS",
            "FOOD",
            "ANIMALS",
            "STYLES",
            "CULTURES",
            "SELFIES",
    };


    public PopulairCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_populair_categories, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<PopulairCategoriesHorizontalItems> galleryPhotosItemses = prepareData();
        PopulairCategoriesVerticalAdapter adapter = new PopulairCategoriesVerticalAdapter(getActivity(), galleryPhotosItemses);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<PopulairCategoriesHorizontalItems> prepareData(){

        ArrayList<PopulairCategoriesHorizontalItems> theimage = new ArrayList<>();
        for(int i = 0; i< title.length; i++){
            PopulairCategoriesHorizontalItems galleryPhotosItems = new PopulairCategoriesHorizontalItems();
            galleryPhotosItems.setTitle(title[i]);
            theimage.add(galleryPhotosItems);
        }
        return theimage;
    }


}
