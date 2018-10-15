package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    static boolean et1 = false, et2 = false;
    EditText etEmail,etMotDePasse;
    Button btnConnexion, btnConnexionFB;
    TextView tvMPOublie, tvInscription;
    private SweetAlertDialog pDialog;
    private SweetAlertDialog sweetDialog;
    private SessionManager session;
    private String TokenFirebase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginactivity_main);
        getSupportActionBar().hide();


        TextView title = findViewById(R.id.title);
        TextView subTitle = findViewById(R.id.subTitle);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "ARABOLIC.TTF");
        title.setTypeface(typeFace);
        subTitle.setTypeface(typeFace);
        etEmail = (EditText) findViewById(R.id.email_xml);
        etMotDePasse = (EditText) findViewById(R.id.motdepasse_xml);
        btnConnexion = (Button) findViewById(R.id.btnConnexion_xml);
        btnConnexionFB = (Button) findViewById(R.id.btnConnexionFB_xml);
        tvMPOublie = (TextView) findViewById(R.id.infosCnxOublie_xml);
        tvInscription = (TextView) findViewById(R.id.inscrivezVous_xml);
        activerBtnConnexion();

        // Progress dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Warning")
                .setContentText("Your account has not been avtivated, please check your Email inbox")
                .setConfirmText("Ok");

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
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Wrong adress Email, please enter a valid adress Email", Toast.LENGTH_LONG).show();

                } else if (password.trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 character", Toast.LENGTH_LONG).show();

                } else if (TokenFirebase == null) {
                    Toast.makeText(getApplicationContext(), "Error, please try again", Toast.LENGTH_LONG).show();

                } else {
                    checkLogin(email, password);
                }
            }

        });


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Login ...");
        pDialog.setCancelable(false);
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_LOGIN).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<UserItems> api = API.login(email, password, TokenFirebase);
        api.enqueue(new Callback<UserItems>() {
            @Override
            public void onResponse(Call<UserItems> call, retrofit2.Response<UserItems> response) {
                hideDialog();
                UserItems List = response.body();
                boolean success = List.getSuccess();
                int user_id = List.getUser_id();
                String full_name = List.getFull_name();
                String photo_profil = List.getPhoto_profil_path();
                String photo_cover = List.getPhoto_cover_path();
                String bio = List.getBio();
                String message = List.getMessage();
                String token = List.getToken();
                int isConfirmed = List.getIsConfirmed();
              //  String t = response.headers().get("token");
                if (success){
                    if (isConfirmed == 1) {
                        session.createLoginSession(user_id, full_name, photo_profil, photo_cover, bio, email, token);
                        session.saveTokenFirebase(TokenFirebase);
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Warning")
                                .setContentText("Your account has not been avtivated, please check your Email inbox")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                    }
                                })
                                .show();


                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG)
                            .show();

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

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //  Get new Token Firebase
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                TokenFirebase = instanceIdResult.getToken();
                Log.e("newTokenLogin Login", TokenFirebase);

            }
        });

    }
}
