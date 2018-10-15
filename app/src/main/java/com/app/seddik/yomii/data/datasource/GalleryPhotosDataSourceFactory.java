package com.app.seddik.yomii.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.app.seddik.yomii.models.GalleryPhotosItems;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Seddik on 16/08/2018.
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */

public class GalleryPhotosDataSourceFactory extends DataSource.Factory<Integer, GalleryPhotosItems> {

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<GalleryPhotosDataSource> DataSourceMutableLiveData = new MutableLiveData<>();


    /**
     * Create a DataSource.
     * <p>
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     * <p>
     * {@link } for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * {@code LiveData<PagedList>} to observers.
     *
     * @return the new DataSource.
     */

    public GalleryPhotosDataSourceFactory(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public DataSource<Integer, GalleryPhotosItems> create() {
        GalleryPhotosDataSource galleryPhotosDataSource = new GalleryPhotosDataSource(compositeDisposable);
        DataSourceMutableLiveData.postValue(galleryPhotosDataSource);
        return galleryPhotosDataSource;
    }

    @NonNull
    public MutableLiveData<GalleryPhotosDataSource> getDataSourceLiveData() {
        return DataSourceMutableLiveData;
    }

}
