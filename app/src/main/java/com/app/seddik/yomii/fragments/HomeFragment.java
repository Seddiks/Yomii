package com.app.seddik.yomii.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.HomePaginationAdapter;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.utils.PaginationScrollListener;
import com.app.seddik.yomii.utils.SessionManager;

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

    private static final int PAGE_START = 1;
    public static int top = -1;
    HomePaginationAdapter adapterPagination;
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private SessionManager session;
    private int user_id;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int numberComments = intent.getIntExtra("NumberComments", 0);
            int position = intent.getIntExtra("Position", 0);
            adapterPagination.updateItem(position, numberComments);
        }
    };


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        session = new SessionManager(getActivity());
        user_id = session.getUSER_ID();

        recyclerView = rootView.findViewById(R.id.recycleview);
        progressBar = rootView.findViewById(R.id.main_progress);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
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

        loadFirstPage();
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("update_comment_number"));


        return rootView;
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }



    private void loadFirstPage() {

        Call<ResponsePhotoItems> api = API.getDetailsPhotos(0, user_id, currentPage);
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
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage <= TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }else {
                    progressBar.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }
            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void loadNextPage() {

        Call<ResponsePhotoItems> api = API.getDetailsPhotos(0, user_id, currentPage);
        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                ResponsePhotoItems results = response.body();
                boolean success = results.getSuccess();
                if (success) {
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    progressBar.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }

            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

                // TODO: 08/11/16 handle failure
            }
        });
    }


}
