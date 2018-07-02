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

import java.util.ArrayList;

import com.app.seddik.yomii.adapters.NotificationAdapter;
import com.app.seddik.yomii.models.NotificationItems;
import com.app.seddik.yomii.utils.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    SessionManager session;
    private final String text[] = {
            "Mohamed Salah a publié dans Annaba Tech Meetups.",
            "Zai Bet a ajouté une photo dans DZ DÉVELOPPEURS lundi.",
            "Akram Azoug a publié dans Les Petites Annonces Annaba",
            "Tarik Zakaria Benmerar a commenté une publication dans DZ DÉVELOPPEURS Tarik Zakaria Benmerar a commenté une publication dans DZ DÉVELOPPEURS.",
            "RENAULT R4 tl à vendre dans Les Petites Annonces Annaba.",
            "Kenza Louna a publié dans DZ DÉVELOPPEURS vendredi ",
            "Chaouki Haniche a également commenté la photo de ANNABA.",
            "Wâîl MH a ajouté une photo dans Les Petites Annonces Annaba.",
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



    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false) ;

        session = new SessionManager(getActivity());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        //GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(),1);
        //recyclerView.setLayoutManager(mGridLayoutManager);

        ArrayList<NotificationItems> notificationItems = prepareData();
        NotificationAdapter adapter = new NotificationAdapter(getActivity(), notificationItems);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    private ArrayList<NotificationItems> prepareData(){

        ArrayList<NotificationItems> item = new ArrayList<>();
        for(int i = 0; i< text.length; i++){
            NotificationItems notifItems = new NotificationItems();
            notifItems.setName_profile(text[i]);
            notifItems.setImage_profile(image_pro[i]);
            item.add(notifItems);
        }
        return item;
    }


}
