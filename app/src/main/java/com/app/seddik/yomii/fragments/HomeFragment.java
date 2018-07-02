package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.DisplayPhotosPublishedAdapter;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SessionManager session;
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
    private Button btnFollow;
    private RecyclerView recyclerView;
    private DisplayPhotosPublishedAdapter adapter;
    private ArrayList<ResponsePhotoItems.Paths> data;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        session = new SessionManager(getActivity());

        recyclerView =  rootView.findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getDetailsPhotosPublishedByUsers();

        return rootView;
    }


    private void getDetailsPhotosPublishedByUsers(){

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL_UPLOAD_DATA_HOME).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponsePhotoItems> api =API.getDetailsPhotosPublishedByUsers(0);
        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                ResponsePhotoItems List = response.body();
                boolean success = List.getSuccess();
                String message = List.getMessage();
                if (success){
                    data = List.getData();
                    fillDetailsPhotosPublished(data);

                }else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();


                }

            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Error", Toast.LENGTH_LONG)
                        .show();


            }
        });

    }

    private void fillDetailsPhotosPublished(ArrayList<ResponsePhotoItems.Paths> data) {
        ArrayList<DisplayPhotosPublishedItems> photosItems = new ArrayList<>();

        for(int i=0 ; i <data.size(); i++){
            DisplayPhotosPublishedItems items = new DisplayPhotosPublishedItems();
            items.setPhoto_profil(data.get(i).getPhoto_profil_path());
            items.setPhoto_published(data.get(i).getPhoto_path());
            Log.e("HomeFragment", "link "+data.get(i).getPhoto_path());
            items.setFull_name(data.get(i).getFull_name());
          //  items.setDate(data.get(i).);
            photosItems.add(items);

        }

        adapter = new DisplayPhotosPublishedAdapter(Glide.with(this),getActivity(), photosItems,true);
        adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

    }




    }
