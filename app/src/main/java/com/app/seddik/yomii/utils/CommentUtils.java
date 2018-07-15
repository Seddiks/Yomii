package com.app.seddik.yomii.utils;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.models.ResponsePhotoComments;
import com.app.seddik.yomii.networks.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;

/**
 * Created by Seddik on 14/07/2018.
 */

public class CommentUtils {
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    private int comment_id;
    private TextView mDate;
    private TextView mDelete;
    private TextView mPublication;
    private ProgressBar mProgressBar;
    private TextView mError;

    public CommentUtils(TextView date, TextView delete, TextView publication, ProgressBar progressBar, TextView error) {
        mDate = date;
        mDelete = delete;
        mPublication = publication;
        mProgressBar = progressBar;
        mError = error;
    }

    public int insertComment(int user_id, int photo_id, String commentText, final InsertCommentCallbacks insertCommentCallbacks) {
        mProgressBar.setVisibility(View.VISIBLE);
        mPublication.setVisibility(View.VISIBLE);
        mDate.setVisibility(View.GONE);
        mDelete.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        Call<ResponsePhotoComments> api = API.insertComment(1, user_id, photo_id, commentText);
        api.enqueue(new Callback<ResponsePhotoComments>() {
            @Override
            public void onResponse(Call<ResponsePhotoComments> call, Response<ResponsePhotoComments> response) {
                ResponsePhotoComments results = response.body();
                boolean success = results.isSuccess();
                if (success) {
                    comment_id = results.getData().get(0).getComment_id();
                    if (insertCommentCallbacks != null)
                        insertCommentCallbacks.onInsertSuccess(comment_id);
                    mProgressBar.setVisibility(View.GONE);
                    mPublication.setVisibility(View.GONE);
                    mDate.setVisibility(View.VISIBLE);
                    mDelete.setVisibility(View.VISIBLE);


                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mPublication.setVisibility(View.GONE);
                    mError.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ResponsePhotoComments> call, Throwable t) {
                t.printStackTrace();
                if (insertCommentCallbacks != null) insertCommentCallbacks.onInsertFailed(t);

                mProgressBar.setVisibility(View.GONE);
                mPublication.setVisibility(View.GONE);
                mError.setVisibility(View.VISIBLE);

            }
        });
        return comment_id;
    }

    public interface InsertCommentCallbacks {
        void onInsertSuccess(int id);

        void onInsertFailed(Throwable error);
    }


}
