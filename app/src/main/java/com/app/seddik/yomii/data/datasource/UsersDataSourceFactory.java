package com.app.seddik.yomii.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.app.seddik.yomii.models.GalleryPhotosItems;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ahmed Abd-Elmeged on 2/13/2018.
 * <p>
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
public class UsersDataSourceFactory extends DataSource.Factory<Integer, GalleryPhotosItems> {

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<UsersDataSource> usersDataSourceLiveData = new MutableLiveData<>();

    public UsersDataSourceFactory(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public DataSource<Integer, GalleryPhotosItems> create() {
        UsersDataSource usersDataSource = new UsersDataSource(compositeDisposable);
        usersDataSourceLiveData.postValue(usersDataSource);
        return usersDataSource;
    }

    @NonNull
    public MutableLiveData<UsersDataSource> getUsersDataSourceLiveData() {
        return usersDataSourceLiveData;
    }

}
