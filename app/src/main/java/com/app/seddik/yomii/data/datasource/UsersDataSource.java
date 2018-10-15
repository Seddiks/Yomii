package com.app.seddik.yomii.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.api.Client;
import com.app.seddik.yomii.data.NetworkState;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by Ahmed Abd-Elmeged on 2/13/2018.
 */
public class UsersDataSource extends ItemKeyedDataSource<Integer, GalleryPhotosItems> {

    private ApiService apiService;


    private CompositeDisposable compositeDisposable;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    UsersDataSource(CompositeDisposable compositeDisposable) {
        //this.githubService = GithubService.getService();
        this.apiService = Client.createAppService();
        this.compositeDisposable = compositeDisposable;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Timber.e(throwable.getMessage())));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<GalleryPhotosItems> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        //get the initial users from the api
        compositeDisposable.add(apiService.getPhotosGallery(1, 5, -1, params.requestedLoadSize).subscribe(galleryPhotosItems -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    initialLoad.postValue(NetworkState.LOADED);
                    callback.onResult(galleryPhotosItems);
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadInitial(params, callback));
                    NetworkState error = NetworkState.error(throwable.getMessage());
                    // publish the error
                    networkState.postValue(error);
                    initialLoad.postValue(error);
                }));
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<GalleryPhotosItems> callback) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING);

        //get the users from the api after id
        compositeDisposable.add(apiService.getPhotosGallery(1, 5, params.key - 1, params.requestedLoadSize).subscribe(galleryPhotosItems -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    callback.onResult(galleryPhotosItems);
                },
                throwable -> {
                    // keep a Completable for future retry
                    setRetry(() -> loadAfter(params, callback));
                    // publish the error
                    networkState.postValue(NetworkState.error(throwable.getMessage()));
                }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<GalleryPhotosItems> callback) {
        // ignored, since we only ever append to our initial load
    }

    /**
     * The id field is a unique identifier for users.
     */
    @NonNull
    @Override
    public Integer getKey(@NonNull GalleryPhotosItems item) {
        return item.getPhoto_id();
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

}
