package com.app.seddik.yomii.utils;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.seddik.yomii.models.CommentItems;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.ResponsePhotoComments;
import com.app.seddik.yomii.networks.ApiService;

import cn.pedant.SweetAlert.SweetAlertDialog;
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

    public CommentUtils() {

    }

    public void insertComment(CommentItems commentItems, final InsertCommentCallbacks insertCommentCallbacks) {
        mProgressBar.setVisibility(View.VISIBLE);
        mPublication.setVisibility(View.VISIBLE);
        mDate.setVisibility(View.GONE);
        mDelete.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);

        int user_id = commentItems.getUser_id();
        int photo_id = commentItems.getPhoto_id();
        String commentText = commentItems.getComment();
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
    }

    public void deleteComment(Context context, final CommentItems commentItems, final DeleteCommentCallbacks deleteCommentCallbacks) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Delete Comment")
                .setContentText("Do you want delete your comment?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        deleteCommentCallbacks.onConfirm();
                        deleteComment(commentItems);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        deleteCommentCallbacks.onCancel();
                    }
                })
                .show();


    }

    private void deleteComment(CommentItems commentItems) {
        int comment_id = commentItems.getComment_id();
        int user_id = commentItems.getUser_id();
        int photo_id = commentItems.getPhoto_id();
        Call<ResponseItems> api = API.deleteComment(2, user_id, photo_id, comment_id);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems results = response.body();
                boolean success = results.getSuccess();
                if (success) {


                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public interface InsertCommentCallbacks {
        void onInsertSuccess(int id);

        void onInsertFailed(Throwable error);
    }

    public interface DeleteCommentCallbacks {
        void onConfirm();

        void onCancel();
    }


}
