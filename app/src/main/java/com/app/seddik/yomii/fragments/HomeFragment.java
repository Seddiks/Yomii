package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.DisplayPhotosPublishedAdapter;
import com.app.seddik.yomii.adapters.HomePaginationAdapter;
import com.app.seddik.yomii.models.DisplayPhotosPublishedItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.networks.Client;
import com.app.seddik.yomii.utils.PaginationScrollListener;
import com.app.seddik.yomii.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;
import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final int PAGE_START = 1;

    HomePaginationAdapter adapterPagination;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private ApiService movieService;
    private SessionManager session;
    private Button btnFollow;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
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
        progressBar = rootView.findViewById(R.id.main_progress);


        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterPagination = new HomePaginationAdapter(getActivity());
        adapterPagination. setHasStableIds(true);
        recyclerView.setAdapter(adapterPagination);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        movieService = Client.getClient().create(ApiService.class);
        loadFirstPage();

     //   getDetailsPhotosPublishedByUsers();

        return rootView;
    }



    private ArrayList<DisplayPhotosPublishedItems> fillDetails(ArrayList<ResponsePhotoItems.Paths> data) {
        ArrayList<DisplayPhotosPublishedItems> photosItems = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            DisplayPhotosPublishedItems items = new DisplayPhotosPublishedItems();
            items.setPhoto_profil(data.get(i).getPhoto_profil_path());
            items.setPhoto_published(data.get(i).getPhoto_path());
            Log.e("HomeFragment", "link " + data.get(i).getPhoto_path());
            items.setFull_name(data.get(i).getFull_name());
            //  items.setDate(data.get(i).);
            photosItems.add(items);
        }
        return photosItems;
    }


    private void loadFirstPage() {
        Log.d("Home", "loadFirstPage: ");
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL_UPLOAD_DATA_HOME).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponsePhotoItems> api = API.getDetailsPhotos(0, currentPage);


        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                // Got data. Send it to adapter
                ResponsePhotoItems results = response.body();
                boolean success = results.getSuccess();
                String message = results.getMessage();
                int numberItems = results.getNumber_pages();
                if (success){
                    TOTAL_PAGES = numberItems;
                    ArrayList<DisplayPhotosPublishedItems> photosItems = new ArrayList<>();

                    progressBar.setVisibility(View.GONE);
                    photosItems = fillDetails(results.getData());
                    adapterPagination.addAll(photosItems);

                    if (currentPage <= TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL_UPLOAD_DATA_HOME).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponsePhotoItems> api = API.getDetailsPhotos(0, currentPage);

        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                ResponsePhotoItems results = response.body();
                ArrayList<DisplayPhotosPublishedItems> photosItems = new ArrayList<>();

                progressBar.setVisibility(View.GONE);
                photosItems = fillDetails(results.getData());
                adapterPagination.addAll(photosItems);

                if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });
    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */




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
        progressBar.setVisibility(View.GONE);
        adapterPagination.addAll(photosItems);

        //adapter = new DisplayPhotosPublishedAdapter(Glide.with(this),getActivity(), photosItems,true);
        adapterPagination.setHasStableIds(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPagination);

    }


    }
