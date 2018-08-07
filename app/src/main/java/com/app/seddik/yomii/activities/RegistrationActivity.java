package com.app.seddik.yomii.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.utils.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirm;
    private ImageButton btnRegister;
    private TextView headerText;
    private TextView backLogingText;
    private SweetAlertDialog pDialog;
    private SweetAlertDialog sweetDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        inputFullName = findViewById(R.id.fname_xml);
        inputEmail = findViewById(R.id.email_xml);
        inputPassword = findViewById(R.id.password_xml);
        inputPasswordConfirm = findViewById(R.id.password_confirm_xml);
        btnRegister = findViewById(R.id.btnRegister_xml);
        headerText = findViewById(R.id.header_text_xml);
        backLogingText = findViewById(R.id.backLogin_xml);

        Typeface type = Typeface.createFromAsset(getAssets(), "Walkway_Bold.ttf");
        headerText.setTypeface(type);


        // Progress dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        sweetDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Registration")
                .setContentText("You have created new account successfully, please check youe Email inbox for confirmation")
                .setConfirmText("Ok");

        // Session manager
        //session = new SessionManager(getApplicationContext());
        //session.checkLogin();

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String fname = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String passwordConfirm = inputPasswordConfirm.getText().toString().trim();


                if (fname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Required fields are missing", Toast.LENGTH_LONG).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Wrong adress Email, please enter a valid adress Email", Toast.LENGTH_LONG).show();

                } else if (!password.trim().equals(passwordConfirm.trim())) {
                    Toast.makeText(getApplicationContext(), "Passwords doesn't match", Toast.LENGTH_LONG).show();

                } else if (password.trim().length() < 8 || passwordConfirm.trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Passwords must be at least 8 character", Toast.LENGTH_LONG).show();
                } else {
                    registerUser(fname, email, password, passwordConfirm);

                }
            }
        });

        // Link to Login Screen
        backLogingText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String fname, final String email, final String password, final String passwordConfirm) {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Registration ...");
        pDialog.setCancelable(false);
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_REGISTER).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<UserItems> api = API.registration(fname, email, password, passwordConfirm, "");
        api.enqueue(new Callback<UserItems>() {
            @Override
            public void onResponse(Call<UserItems> call, retrofit2.Response<UserItems> response) {
                hideDialog();
                session = new SessionManager(getApplicationContext());
                UserItems List = response.body();
                boolean success = List.getSuccess();
                int user_id = List.getUser_id();
                String message = List.getMessage();
                String token = List.getToken();
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_LONG)
                        .show();
                if (success){
                    // Launch Main activity
                    //session.createLoginSession(user_id,fname, email,token);
                    sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(RegistrationActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    })
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
  /**  private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Inscription ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register ResponseItems: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = null;
                    try {
                        jObj = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        session.createLoginSession(name, email,"");

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegistrationActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "ERROR 1: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "ERROR 2:" + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

    }**/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}