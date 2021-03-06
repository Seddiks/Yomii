package com.app.seddik.yomii.test.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.test.ui.adapter.RetryCallback;
import com.app.seddik.yomii.test.ui.adapter.UserAdapter;
import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.test.ui.data.Status;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity2 extends AppCompatActivity implements RetryCallback {

    @BindView(R.id.usersSwipeRefreshLayout)
    SwipeRefreshLayout usersSwipeRefreshLayout;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;

    @BindView(R.id.errorMessageTextView)
    TextView errorMessageTextView;

    @BindView(R.id.retryLoadingButton)
    Button retryLoadingButton;

    @BindView(R.id.loadingProgressBar)
    ProgressBar loadingProgressBar;

    private UsersViewModel usersViewModel;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainm);
        ButterKnife.bind(this);
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        initAdapter();
        initSwipeToRefresh();
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (userAdapter.getItemViewType(position)) {
                    case R.layout.item_user:
                        return 1;
                    case R.layout.item_network_state:
                        return 3;
                    default:
                        return -1;
                }
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        userAdapter = new UserAdapter(this);
        usersRecyclerView.setLayoutManager(gridLayoutManager);
        usersRecyclerView.setAdapter(userAdapter);
        usersViewModel.userList.observe(this, userAdapter::submitList);
        usersViewModel.getNetworkState().observe(this, userAdapter::setNetworkState);
    }

    /**
     * Init swipe to refresh and enable pull to refresh only when there are items in the adapter
     */
    private void initSwipeToRefresh() {
        usersViewModel.getRefreshState().observe(this, networkState -> {
            if (networkState != null) {
                if (userAdapter.getCurrentList() != null) {
                    if (userAdapter.getCurrentList().size() > 0) {
                        usersSwipeRefreshLayout.setRefreshing(
                                networkState.getStatus() == NetworkState.LOADING.getStatus());
                    } else {
                        setInitialLoadingState(networkState);
                    }
                } else {
                    setInitialLoadingState(networkState);
                }
            }
        });
        usersSwipeRefreshLayout.setOnRefreshListener(() -> usersViewModel.refresh());
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

        usersSwipeRefreshLayout.setEnabled(networkState.getStatus() == Status.SUCCESS);
    }

    @OnClick(R.id.retryLoadingButton)
    void retryInitialLoading() {
        usersViewModel.retry();
    }

    @Override
    public void retry() {
        usersViewModel.retry();
    }

}
