package com.app.seddik.yomii.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.CommentsPaginationAdapter;
import com.app.seddik.yomii.models.CommentItems;
import com.app.seddik.yomii.models.ResponsePostComments;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.utils.PaginationScrollListener;
import com.app.seddik.yomii.utils.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;

public class CommentsPhotosActivity extends AppCompatActivity {

    private static final int PAGE_START = 1;
    CommentsPaginationAdapter adapterPagination;
    Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    ApiService API = retrofit.create(ApiService.class);
    private int user_id, photo_id;
    private SessionManager session;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView photo_profil;
    private EditText comment;
    private Intent mIntent;
    private int postion_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_photos);
        session = new SessionManager(getApplicationContext());
        user_id = session.getUSER_ID();

        photo_id = getIntent().getIntExtra("photo_id", -1);
        postion_photo = getIntent().getIntExtra("position_photo", 0);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Comments");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.main_progress);
        photo_profil = findViewById(R.id.photo_profil);
        comment = findViewById(R.id.comment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterPagination = new CommentsPaginationAdapter(this, postion_photo);
        adapterPagination.setHasStableIds(true);
        recyclerView.setAdapter(adapterPagination);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        loadFirstPage();

        Glide.with(getApplicationContext())
                .load(R.drawable.bgmoi2)
                .apply(RequestOptions.circleCropTransform())
                .into(photo_profil);


        showKeyboard();
        handleSendButton();


    }


    private void loadFirstPage() {

        Call<ResponsePostComments> api = API.getCommentsPerPhoto(0, user_id, photo_id, currentPage);
        api.enqueue(new Callback<ResponsePostComments>() {
            @Override
            public void onResponse(Call<ResponsePostComments> call, Response<ResponsePostComments> response) {
                // Got data. Send it to adapter
                ResponsePostComments results = response.body();
                boolean success = results.isSuccess();
                int numberItems = results.getNumber_pages();
                if (success) {
                    TOTAL_PAGES = numberItems;
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage < TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    Log.d("Home", "Error1");
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponsePostComments> call, Throwable t) {
                t.printStackTrace();
                Log.d("Home", "Error2" + t.toString());
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void loadNextPage() {
        Log.d("Comment", "loadNextPage: " + currentPage);
        Call<ResponsePostComments> api = API.getCommentsPerPhoto(0, user_id, photo_id, currentPage);

        api.enqueue(new Callback<ResponsePostComments>() {
            @Override
            public void onResponse(Call<ResponsePostComments> call, Response<ResponsePostComments> response) {

                adapterPagination.removeLoadingFooter();
                isLoading = false;

                ResponsePostComments results = response.body();
                boolean success = results.isSuccess();
                if (success) {
                    progressBar.setVisibility(View.GONE);
                    adapterPagination.addAll(results.getData());

                    if (currentPage != TOTAL_PAGES) adapterPagination.addLoadingFooter();
                    else isLastPage = true;

                } else {
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ResponsePostComments> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);

                // TODO: 08/11/16 handle failure
            }
        });
    }


    private void handleSendButton() {

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.toString().trim().length();
                if (len > 0) {

                    comment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_green_400_36dp, 0);
                    comment.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            //    final int DRAWABLE_LEFT = 0;
                            //     final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            //      final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (comment.getRight() - comment.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    comment.setText("");
                                    CommentItems item = new CommentItems();
                                    item.setComment_id(-1);
                                    item.setUser_id(user_id);
                                    item.setPhoto_id(photo_id);
                                    item.setFull_name("Fredj Moh");
                                    item.setComment(charSequence.toString());

                                    recyclerView.smoothScrollToPosition(0);
                                    adapterPagination.addInTop(item);
                                    adapterPagination.notifyDataSetChanged();
                                    //if (currentPage < TOTAL_PAGES) adapterPagination.addLoadingFooter();
                                    // else
                                    isLastPage = true;

                                    return true;
                                }
                            }
                            return false;
                        }
                    });


                } else {
                    comment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_green_100_36dp, 0);
                    comment.setOnTouchListener(null);


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void showKeyboard() {
        mIntent = getIntent();
        boolean isShowKeyboard = mIntent.getBooleanExtra("ShowKeyBoard", false);
        if (isShowKeyboard) {
            comment.post(new Runnable() {
                public void run() {
                    comment.requestFocusFromTouch();
                    InputMethodManager lManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(comment, 0);
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}
