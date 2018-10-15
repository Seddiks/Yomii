package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.GuideAdapter;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.models.GuideItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {
    boolean success ;
    String message ;
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_PHOTOS).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    private SessionManager session;
    private UserItems user;
    private int id_user;
    private GuideAdapter adapter;
    private GuideItems guideItems;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;




    public GuideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_guide, container, false);

        session = new SessionManager(getActivity());
        user = session.getUserDetails();
        id_user = user.getUser_id();

        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getUserGuides();

        return rootView;
    }

    //Get Guides
    private void getUserGuides(){
        Call<GuideItems> api =API.getUserGuides(1,id_user);
        api.enqueue(new Callback<GuideItems>() {
            @Override
            public void onResponse(Call<GuideItems> call, Response<GuideItems> response) {
                guideItems = response.body();
                success = guideItems.isSuccess();
                message = guideItems.getMessage();
                if (success){
                    adapter = new GuideAdapter(getActivity(), guideItems);
                    recyclerView.setAdapter(adapter);
                    Log.e("Works; ","its work!!");


                }else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<GuideItems> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Error"+t.toString(), Toast.LENGTH_LONG)
                        .show();

            }
        });


    }


}
