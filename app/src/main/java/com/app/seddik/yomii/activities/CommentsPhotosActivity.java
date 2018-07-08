package com.app.seddik.yomii.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class CommentsPhotosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView photo_profil;
    private EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_photos);

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.main_progress);
        photo_profil = findViewById(R.id.photo_profil);
        comment = findViewById(R.id.comment);

        Glide.with(getApplicationContext())
                .load(R.drawable.bg_ny)
                .apply(RequestOptions.circleCropTransform())
                .into(photo_profil);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    comment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_green_400_36dp, 0);

                } else {
                    comment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_green_100_36dp, 0);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
