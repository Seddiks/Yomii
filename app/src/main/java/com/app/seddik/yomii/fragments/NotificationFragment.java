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
import android.widget.ProgressBar;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.NotificationsPaginationAdapter;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.models.ResponseNotificationItems;
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
public class NotificationFragment extends Fragment {

    private static final int PAGE_START = 1;
    RecyclerView recyclerView;
    NotificationsPaginationAdapter adapterPagination;
    private Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    private ApiService API = retrofit.create(ApiService.class);
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private SessionManager session;
    private int user_id;
    private ProgressBar progressBar;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false) ;

        session = new SessionManager(getActivity());
        user_id = session.getUSER_ID();
        recyclerView = rootView.findViewById(R.id.recycleview);
        progressBar = rootView.findViewById(R.id.main_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterPagination = new NotificationsPaginationAdapter(getActivity());
        adapterPagination.setHasStableIds(true);
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
        loadFirstPage();

        return rootView;
    }

    private void loadFirstPage() {

        Call<ResponseNotificationItems> api = API.getNotifications(0, user_id, currentPage);
        api.enqueue(new Callback<ResponseNotificationItems>() {
            @Override
            public void onResponse(Call<ResponseNotificationItems> call, Response<ResponseNotificationItems> response) {
                // Got data. Send it to adapter
                ResponseNotificationItems results = response.body();
                boolean success = results.isSuccess();
                int numberItems = results.getNumber_pages();
                if (success) {
                    TOTAL_PAGES = numberItems;
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage < TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    Log.d("Home", "Error1");
                    progressBar.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }
            }

            @Override
            public void onFailure(Call<ResponseNotificationItems> call, Throwable t) {
                t.printStackTrace();
                Log.d("Home", "Error2" + t.toString());
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void loadNextPage() {

        Call<ResponseNotificationItems> api = API.getNotifications(0, user_id, currentPage);
        api.enqueue(new Callback<ResponseNotificationItems>() {
            @Override
            public void onResponse(Call<ResponseNotificationItems> call, Response<ResponseNotificationItems> response) {

                adapterPagination.removeLoadingFooter();
                isLoading = false;

                ResponseNotificationItems results = response.body();
                boolean success = results.isSuccess();
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
            public void onFailure(Call<ResponseNotificationItems> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });
    }


}
