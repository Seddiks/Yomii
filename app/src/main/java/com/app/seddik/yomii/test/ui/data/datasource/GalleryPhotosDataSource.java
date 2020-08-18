package com.app.seddik.yomii.test.ui.data.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.api.Client;
import com.app.seddik.yomii.test.ui.data.NetworkState;
import com.app.seddik.yomii.models.GalleryPhotosItems;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Seddik on 16/08/2018.
 */

public class GalleryPhotosDataSource extends ItemKeyedDataSource<Integer, GalleryPhotosItems> {

    public static final String TAG = "GalleryPhotosDataSource";

    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    GalleryPhotosDataSource(CompositeDisposable compositeDisposable) {
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


    /**
     * Load initial data.
     * <p>
     * This method is called first to initialize a PagedList with data. If it's possible to count
     * the items that can be loaded by the DataSource, it's recommended to pass the loaded data to
     * the callback via the three-parameter
     * {@link LoadInitialCallback#onResult(List, int, int)}. This enables PagedLists
     * presenting data from this source to display placeholders to represent unloaded items.
     * <p>
     * {@link LoadInitialParams#requestedInitialKey} and {@link LoadInitialParams#requestedLoadSize}
     * are hints, not requirements, so they may be altered or ignored. Note that ignoring the
     * {@code requestedInitialKey} can prevent subsequent PagedList/DataSource pairs from
     * initializing at the same location. If your data source never invalidates (for example,
     * loading from the network without the network ever signalling that old data must be reloaded),
     * it's fine to ignore the {@code initialLoadKey} and always start from the beginning of the
     * data set.
     *
     * @param params   Parameters for initial load, including initial key and requested size.
     * @param callback Callback that receives initial load data.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<GalleryPhotosItems> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        //get the initial users from the api
        compositeDisposable.add(apiService.getPhotosGallery(1, 5, -1, params.requestedLoadSize).subscribe(galleryPhotosItemses -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    initialLoad.postValue(NetworkState.LOADED);
                    callback.onResult(galleryPhotosItemses);
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

    /**
     * Load list data after the key specified in {@link LoadParams#key LoadParams.key}.
     * <p>
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     * <p>
     * Data may be passed synchronously during the loadAfter method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     * <p>
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call {@link #invalidate()} to invalidate the data source,
     * and prevent further loading.
     *
     * @param params   Parameters for the load, including the key to load after, and requested size.
     * @param callback Callback that receives loaded data.
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<GalleryPhotosItems> callback) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING);

        //get the users from the api after id
        compositeDisposable.add(apiService.getPhotosGallery(1, 5, params.key - 1, params.requestedLoadSize).subscribe(galleryPhotosItemses -> {
                    // clear retry since last request succeeded
                    setRetry(null);
                    networkState.postValue(NetworkState.LOADED);
                    callback.onResult(galleryPhotosItemses);
                },
                throwable -> {
                    // keep a Completable for future retry photos_user/5/1534161047_1534160953579.jpg
                    setRetry(() -> loadAfter(params, callback));
                    // publish the error
                    networkState.postValue(NetworkState.error(throwable.getMessage()));
                }));

    }

    /**
     * Load list data before the key specified in {@link LoadParams#key LoadParams.key}.
     * <p>
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     * <p>
     * <p class="note"><strong>Note:</strong> Data returned will be prepended just before the key
     * passed, so if you vary size, ensure that the last item is adjacent to the passed key.
     * <p>
     * Data may be passed synchronously during the loadBefore method, or deferred and called at a
     * later time. Further loads going up will be blocked until the callback is called.
     * <p>
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call {@link #invalidate()} to invalidate the data source,
     * and prevent further loading.
     *
     * @param params   Parameters for the load, including the key to load before, and requested size.
     * @param callback Callback that receives loaded data.
     */
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<GalleryPhotosItems> callback) {

    }

    /**
     * Return a key associated with the given item.
     * <p>
     * If your ItemKeyedDataSource is loading from a source that is sorted and loaded by a unique
     * integer ID, you would return {@code item.getID()} here. This key can then be passed to
     * {@link #loadBefore(LoadParams, LoadCallback)} or
     * {@link #loadAfter(LoadParams, LoadCallback)} to load additional items adjacent to the item
     * passed to this function.
     * <p>
     * If your key is more complex, such as when you're sorting by name, then resolving collisions
     * with integer ID, you'll need to return both. In such a case you would use a wrapper class,
     * such as {@code Pair<String, Integer>} or, in Kotlin,
     * {@code data class Key(val name: String, val id: Int)}
     *
     * @param item Item to get the key from.
     * @return Key associated with given item.
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
