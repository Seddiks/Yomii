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

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
import com.app.seddik.yomii.utils.SQLiteHandler;
import com.app.seddik.yomii.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputCountry;
    private EditText inputCity;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        getSupportActionBar().hide();

        inputEmail = (EditText) findViewById(R.id.email_xml);
        inputPassword = (EditText) findViewById(R.id.password_xml);
        inputFirstName = (EditText) findViewById(R.id.fname_xml);
        inputLastName = (EditText) findViewById(R.id.lname_xml);
        inputCountry = (EditText) findViewById(R.id.country_xml);
        inputCity = (EditText) findViewById(R.id.city_xml);

        btnRegister = (Button) findViewById(R.id.btnRegister_xml);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        //session = new SessionManager(getApplicationContext());
        //session.checkLogin();

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String fname = inputFirstName.getText().toString().trim();
                String lname = inputLastName.getText().toString().trim();
                String country = inputCountry.getText().toString().trim();
                String city = inputCity.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty() && !fname.isEmpty() && !lname.isEmpty() && !country.isEmpty() && !city.isEmpty()) {
                    registerUser(email, password,fname,lname,country,city);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Required fields are missing!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

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
    private void registerUser(final String email, final String password,
                              final String fname, final String lname, final String country, final String city) {
        String token = session.getTokenFirebase();
        pDialog.setMessage("Registration ...");
        showDialog();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(AppConfig.URL_REGISTER).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<UserItems> api =API.registration(email,password,token,fname,lname,country,city);
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
                    session.createLoginSession(user_id,fname, email,token);
                    Intent intent = new Intent(RegistrationActivity.this,
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