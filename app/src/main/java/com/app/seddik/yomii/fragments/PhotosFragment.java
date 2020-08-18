package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.test.ui.adapter.UserAdapter;
import com.app.seddik.yomii.adapters.GalleryPhotosPaginationAdapter;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.ResponseGalleryPhotosItems;
import com.app.seddik.yomii.test.ui.UsersViewModel;
import com.app.seddik.yomii.utils.PaginationGridScrollListener;
import com.app.seddik.yomii.utils.SessionManager;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private static final int PAGE_START = 1;
    public static int top = -1;
    GalleryPhotosPaginationAdapter adapterPagination;
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(AppConfig.URL_UPLOAD_PHOTOS).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;
    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;
    @BindView(R.id.retryLoadingButton)
    Button retryLoadingButton;
    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private SessionManager session;
    private int user_id;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private UsersViewModel usersViewModel;

    private UserAdapter userAdapter;


    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.testing, container, false);
        session = new SessionManager(getActivity());
        user_id = session.getUSER_ID();

        recyclerView = rootView.findViewById(R.id.recycleview);
        //NestedScrollView nestedScrollView = rootView.findViewById(R.id.nested);
        progressBar = rootView.findViewById(R.id.main_progress);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
        switch(adapterPagination.getItemViewType(position)){
        case ITEM:
        return 1;
        case LOADING:
        return 3;
        default:
        return -1;
        }
        }
        });


        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapterPagination = new GalleryPhotosPaginationAdapter(getActivity());
        recyclerView.setAdapter(adapterPagination);

        recyclerView.addOnScrollListener(new PaginationGridScrollListener(gridLayoutManager) {
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


        return rootView;
    }

    private void loadFirstPage() {

        Call<ResponseGalleryPhotosItems> api = API.getPhotosGallery(1, user_id, currentPage);
        api.enqueue(new Callback<ResponseGalleryPhotosItems>() {
            @Override
            public void onResponse(Call<ResponseGalleryPhotosItems> call, Response<ResponseGalleryPhotosItems> response) {
                // Got data. Send it to adapter
                ResponseGalleryPhotosItems results = response.body();
                boolean success = results.getSuccess();
                String message = results.getMessage();
                int numberItems = results.getNumber_pages();
                if (success) {
                    TOTAL_PAGES = numberItems;
//                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage <= TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    //       progressBar.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }
            }

            @Override
            public void onFailure(Call<ResponseGalleryPhotosItems> call, Throwable t) {
                t.printStackTrace();
                Log.e("PhotosFr", "err2" + t.toString());
                //      progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void loadNextPage() {

        Call<ResponseGalleryPhotosItems> api = API.getPhotosGallery(1, user_id, currentPage);
        api.enqueue(new Callback<ResponseGalleryPhotosItems>() {
            @Override
            public void onResponse(Call<ResponseGalleryPhotosItems> call, Response<ResponseGalleryPhotosItems> response) {
                adapterPagination.removeLoadingFooter();
                isLoading = false;

                ResponseGalleryPhotosItems results = response.body();
                boolean success = results.getSuccess();
                if (success) {
                    //    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    //  progressBar.setVisibility(View.GONE);
                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                }

            }

            @Override
            public void onFailure(Call<ResponseGalleryPhotosItems> call, Throwable t) {
                t.printStackTrace();
                //  progressBar.setVisibility(View.GONE);

                // TODO: 08/11/16 handle failure
            }
        });
    }


/**
 private void initAdapter() {
 GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
 gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
@Override public int getSpanSize(int position) {
switch(userAdapter.getItemViewType(position)){
case R.layout.item_user:
return 1;
case R.layout.item_network_state:
return 3;
default:
return -1;
}
}
});


 userAdapter = new UserAdapter(this);
 usersRecyclerView.setLayoutManager(gridLayoutManager);
 usersRecyclerView.setAdapter(userAdapter);
 usersViewModel.userList.observe(this, userAdapter::submitList);
 usersViewModel.getNetworkState().observe(this, userAdapter::setNetworkState);
 }


 private void initSwipeToRefresh() {
 usersViewModel.getRefreshState().observe(this, networkState -> {
 if (networkState != null) {
 if (userAdapter.getCurrentList() != null) {
 if (userAdapter.getCurrentList().size() > 0) {
 //  usersSwipeRefreshLayout.setRefreshing(networkState.getStatus() == NetworkState.LOADING.getStatus());
 } else {
 setInitialLoadingState(networkState);
 }
 } else {
 setInitialLoadingState(networkState);
 }
 }
 });
 //   usersSwipeRefreshLayout.setOnRefreshListener(() -> usersViewModel.refresh());
 }


 private void setInitialLoadingState(NetworkState networkState) {
 //error message
 errorMessageTextView.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
 if (networkState.getMessage() != null) {
 errorMessageTextView.setText(networkState.getMessage());
 }

 //loading and retry
 retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
 loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);

 //  usersSwipeRefreshLayout.setEnabled(networkState.getStatus() == Status.SUCCESS);
 }

 @OnClick(R.id.retryLoadingButton) void retryInitialLoading() {
 usersViewModel.retry();
 }

 @Override public void retry() {
 usersViewModel.retry();
 } **/


}
