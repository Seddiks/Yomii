package com.app.seddik.yomii.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.seddik.yomii.R;

import java.util.List;

import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.models.ResponseItems;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPasswordActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private EditText inputEmail;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        inputEmail = (EditText) findViewById(R.id.email_xml);
        btn = (Button) findViewById(R.id.btnSubmit_xml);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Register Button Click event
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();

                if (!email.isEmpty()) {
                    pDialog.setMessage("Sending email ...");
                    showDialog();
                    Retrofit retrofit = new Retrofit.Builder().
                            baseUrl(AppConfig.URL_FORGET_PASSWORD).
                            addConverterFactory(GsonConverterFactory.create()).
                            build();
                    ApiService API = retrofit.create(ApiService.class);
                    Call<List<ResponseItems>> api =API.sumbitEmail(email);
                    api.enqueue(new Callback<List<ResponseItems>>() {
                        @Override
                        public void onResponse(Call<List<ResponseItems>> call, Response<List<ResponseItems>> response) {
                            hideDialog();
                            List<ResponseItems> List = response.body();
                            boolean success = List.get(0).getSuccess();
                            String message = List.get(0).getMessage();
                            Toast.makeText(getApplicationContext(),
                                    message, Toast.LENGTH_LONG)
                                    .show();
                            if (success){
                                // Launch Login activity
                                Intent intent = new Intent(ForgetPasswordActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }



                        }

                        @Override
                        public void onFailure(Call<List<ResponseItems>> call, Throwable t) {
                            hideDialog();
                            Log.d("TAG","error: "+ t.toString());

                            Toast.makeText(getApplicationContext(),
                                    "Error", Toast.LENGTH_LONG)
                                    .show();


                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your email!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
