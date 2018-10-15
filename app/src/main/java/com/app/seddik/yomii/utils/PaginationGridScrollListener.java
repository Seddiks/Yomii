package com.app.seddik.yomii.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Seddik on 02/07/2018.
 */

public abstract class PaginationGridScrollListener extends RecyclerView.OnScrollListener {

    GridLayoutManager gridlayoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    public PaginationGridScrollListener(GridLayoutManager layoutManager) {
        this.gridlayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        final int visibleThreshold = 2;
        int visibleItemCount = gridlayoutManager.getChildCount();
        int totalItemCount = gridlayoutManager.getItemCount();
        int firstVisibleItemPosition = gridlayoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }

    }


   /* @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int[] firstVisibleItemPositions = layoutManager.fi;
        if (!isLoading() && !isLastPage()) {
            if ((firstVisibleItemPositions[0] + visibleItemCount) >= totalItemCount
                    && firstVisibleItemPositions[0] >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }
    }*/

        /*@Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mGridLayoutManager.getChildCount();
            int totalItemCount = mGridLayoutManager.getItemCount();
            int[] firstVisibleItemPositions = mGridLayoutManager.findFirstVisibleItemPositions(null);
            if (!mIsLoading && !mIsLastPage) {
                if ((firstVisibleItemPositions[0] + visibleItemCount) >= totalItemCount
                        && firstVisibleItemPositions[0] >= 0
                        && totalItemCount >= Config.PAGE_SIZE) {
                    loadMorePosts();
                }
            }
        }
    });*/

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
