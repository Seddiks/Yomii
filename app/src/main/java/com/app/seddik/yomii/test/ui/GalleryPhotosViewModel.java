package com.app.seddik.yomii.test.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.test.ui.data.datasource.GalleryPhotosDataSource;
import com.app.seddik.yomii.test.ui.data.datasource.GalleryPhotosDataSourceFactory;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Seddik on 16/08/2018.
 */

public class GalleryPhotosViewModel extends ViewModel {

    private static final int pageSize = 9;
    public LiveData<PagedList<GalleryPhotosItems>> photosList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private GalleryPhotosDataSourceFactory photosDataSourceFactory;

    public GalleryPhotosViewModel() {
        photosDataSourceFactory = new GalleryPhotosDataSourceFactory(compositeDisposable);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build();

        photosList = new LivePagedListBuilder<>(photosDataSourceFactory, config).build();


    }

    public void retry() {
        photosDataSourceFactory.getDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        photosDataSourceFactory.getDataSourceLiveData().getValue().invalidate();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(photosDataSourceFactory.getDataSourceLiveData(), GalleryPhotosDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(photosDataSourceFactory.getDataSourceLiveData(), GalleryPhotosDataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
