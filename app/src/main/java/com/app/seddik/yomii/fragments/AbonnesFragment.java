package com.app.seddik.yomii.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.GalleryPhotosAdapter;
import com.app.seddik.yomii.adapters.RetryCallback;
import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.test.ui.data.Status;
import com.app.seddik.yomii.test.ui.GalleryPhotosViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.seddik.yomii.R.id.usersSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */

public class AbonnesFragment extends Fragment implements RetryCallback {

    @BindView(R.id.test)
    Button btn;

    @BindView(usersSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;

    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;

    @BindView(R.id.retryLoadingButton)
    Button retryLoadingButton;

    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;
    View rootView;
    private GalleryPhotosViewModel viewModel;
    private GalleryPhotosAdapter Adapter;

    public AbonnesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photos, container, false);

        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(this).get(GalleryPhotosViewModel.class);
        initAdapter();
        initSwipeToRefresh();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      Intent intent = new Intent(getActivity(), TestingActivity.class);
                //      startActivity(intent);
            }
        });
        return rootView;
    }


    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (Adapter.getItemViewType(position)) {
                    case R.layout.gallery_photos_items:
                        return 1;
                    case R.layout.item_network_state:
                        return 3;
                    default:
                        return -1;
                }
            }
        });

        Adapter = new GalleryPhotosAdapter(this);
        usersRecyclerView.setLayoutManager(gridLayoutManager);
        usersRecyclerView.setAdapter(Adapter);

        viewModel.photosList.observe(this, Adapter::submitList);
        viewModel.getNetworkState().observe(this, Adapter::setNetworkState);
    }

    /**
     * Init swipe to refresh and enable pull to refresh only when there are items in the adapter
     */
    private void initSwipeToRefresh() {
        viewModel.getRefreshState().observe(this, networkState -> {
            if (networkState != null) {
                if (Adapter.getCurrentList() != null) {
                    if (Adapter.getCurrentList().size() > 0) {
                        mSwipeRefreshLayout.setRefreshing(networkState.getStatus() == NetworkState.LOADING.getStatus());
                    } else {
                        setInitialLoadingState(networkState);
                    }
                } else {
                    setInitialLoadingState(networkState);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> viewModel.refresh());

    }

    /**
     * Show the current network state for the first load when the user list
     * in the adapter is empty and disable swipe to scroll at the first loading
     *
     * @param networkState the new network state
     */
    private void setInitialLoadingState(NetworkState networkState) {
        //error message
        errorMessageTextView.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMessage() != null) {
            errorMessageTextView.setText(networkState.getMessage());
        }

        //loading and retry
        retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
        if (networkState.getStatus() == Status.SUCCESS) {
            mSwipeRefreshLayout.setEnabled(true);
        } else {
            mSwipeRefreshLayout.setEnabled(false);

        }
    }

    @OnClick(R.id.retryLoadingButton)
    void retryInitialLoading() {
        viewModel.retry();

    }

    @Override
    public void retry() {
        viewModel.retry();

    }
}
