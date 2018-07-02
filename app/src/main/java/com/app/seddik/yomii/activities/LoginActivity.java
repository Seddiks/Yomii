package com.app.seddik.yomii.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;

import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.app.seddik.yomii.utils.SQLiteHandler;
import com.app.seddik.yomii.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    EditText etEmail,etMotDePasse;
    Button btnConnexion, btnConnexionFB;
    TextView tvMPOublie, tvInscription;
    static boolean et1 = false, et2 = false;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginactivity_main);
        getSupportActionBar().hide();

        TextView textView = (TextView) findViewById(R.id.title);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "ARABOLIC.TTF");
        textView.setTypeface(typeFace);
        etEmail = (EditText) findViewById(R.id.email_xml);
        etMotDePasse = (EditText) findViewById(R.id.motdepasse_xml);
        btnConnexion = (Button) findViewById(R.id.btnConnexion_xml);
        btnConnexionFB = (Button) findViewById(R.id.btnConnexionFB_xml);
        tvMPOublie = (TextView) findViewById(R.id.infosCnxOublie_xml);
        tvInscription = (TextView) findViewById(R.id.inscrivezVous_xml);
        activerBtnConnexion();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // take user to Registration
        tvInscription.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        }); // in case password forget
        tvMPOublie.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ForgetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        // Login button Click Event
        btnConnexion.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etMotDePasse.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin2(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin2(final String email, final String password) {
        pDialog.setMessage("Connexion en cours ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_LOGIN).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<UserItems> api =API.login(email,password);
        api.enqueue(new Callback<UserItems>() {
            @Override
            public void onResponse(Call<UserItems> call, retrofit2.Response<UserItems> response) {
                hideDialog();
                UserItems List = response.body();
                boolean success = List.getSuccess();
                int user_id = List.getUser_id();
                Log.e("USER ", "id: "+user_id);
                String message = List.getMessage();
                String token = List.getToken();
              //  String t = response.headers().get("token");
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_LONG)
                        .show();
                if (success){
                    session.createLoginSession(user_id,"Seddik",email,token);
                    // Launch Login activity
                    //Log.d("TAG", "token: "+t);
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();

                }



            }

            @Override
            public void onFailure(Call<UserItems> call, Throwable t) {
                hideDialog();
                Log.d("TAG","error: "+ t.toString());

                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_LONG)
                        .show();


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


    public void activerBtnConnexion(){
        btnConnexion.setEnabled(false);
        etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    et1 = true;
                    if (et2 == true){
                        btnConnexion.setEnabled(true);
                        btnConnexion.setBackgroundResource(R.drawable.round_corner_blue_five);
                        btnConnexion.setTextColor(Color.parseColor("#03A9F4"));


                    }else
                        btnConnexion.setBackgroundResource(R.drawable.round_corner_btn);

                }
                else {
                    et1 = false;
                    btnConnexion.setEnabled(false);
                    btnConnexion.setBackgroundResource(R.drawable.round_corner_btn);
                    btnConnexion.setTextColor(Color.parseColor("#9D9B9B"));
                }

            }
        });

        etMotDePasse.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                {
                    et2 = true;
                    if (et1 == true){
                        btnConnexion.setEnabled(true);
                        btnConnexion.setBackgroundResource(R.drawable.round_corner_blue_five);
                        btnConnexion.setTextColor(Color.parseColor("#03A9F4"));

                    }else
                        btnConnexion.setBackgroundResource(R.drawable.round_corner_btn);

                }
                else {
                    et2 = false;
                    btnConnexion.setEnabled(false);
                    btnConnexion.setBackgroundResource(R.drawable.round_corner_btn);
                    btnConnexion.setTextColor(Color.parseColor("#9D9B9B"));

                }

            }
        });



    }

}
