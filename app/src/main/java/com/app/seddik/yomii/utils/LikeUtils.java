package com.app.seddik.yomii.utils;

import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.models.ResponseItems;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;

/**
 * Created by Seddik on 21/07/2018.
 */

public class LikeUtils {
    private Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    private ApiService API = retrofit.create(ApiService.class);


    public LikeUtils() {

    }

    public void Like(final int user_id, final int reciever_user_id, final int photo_id, final LikeCallbacks likeCallbacks) {
        Call<ResponseItems> api = API.addLike(0, user_id, reciever_user_id, photo_id);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems results = response.body();
                boolean success = results.getSuccess();
                int number_likes = results.getNumber_likes_per_post();
                if (success) {
                    likeCallbacks.onLikeSuccess(number_likes);


                } else {
                    likeCallbacks.onLikeSuccess(number_likes);

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                t.printStackTrace();
                likeCallbacks.onLikeFailed("Error");


            }
        });
    }

    public void UnLike(final int user_id, final int photo_id, final UnLikeCallbacks unLikeCallbacks) {
        Call<ResponseItems> api = API.deleteLike(1, user_id, photo_id);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems results = response.body();
                boolean success = results.getSuccess();
                int number_likes = results.getNumber_likes_per_post();
                if (success) {
                    unLikeCallbacks.onUnLikeSuccess(number_likes);


                } else {
                    unLikeCallbacks.onUnLikeSuccess(number_likes);

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                t.printStackTrace();
                unLikeCallbacks.onUnLikeFailed("Error");


            }
        });

    }


    public interface LikeCallbacks {
        void onLikeSuccess(int number_likes);


        void onLikeFailed(String error);
    }

    public interface UnLikeCallbacks {
        void onUnLikeSuccess(int number_likes);

        void onUnLikeFailed(String error);
    }
}
