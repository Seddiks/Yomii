package com.app.seddik.yomii.test.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.test.ui.data.datasource.UsersDataSource;
import com.app.seddik.yomii.test.ui.data.datasource.UsersDataSourceFactory;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ahmed Abd-Elmeged on 2/13/2018.
 */
public class UsersViewModel extends ViewModel {

    private static final int pageSize = 9;
    LiveData<PagedList<GalleryPhotosItems>> userList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UsersDataSourceFactory usersDataSourceFactory;

    public UsersViewModel() {
        usersDataSourceFactory = new UsersDataSourceFactory(compositeDisposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setPrefetchDistance(pageSize * 1)
                .setEnablePlaceholders(true)
                .build();

        userList = new LivePagedListBuilder<>(usersDataSourceFactory, config).build();
    }

    public void retry() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        usersDataSourceFactory.getUsersDataSourceLiveData().getValue().invalidate();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(usersDataSourceFactory.getUsersDataSourceLiveData(), UsersDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(usersDataSourceFactory.getUsersDataSourceLiveData(), UsersDataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
