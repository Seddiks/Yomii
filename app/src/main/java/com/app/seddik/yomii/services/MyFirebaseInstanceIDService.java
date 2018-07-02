package com.app.seddik.yomii.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.app.seddik.yomii.utils.SessionManager;

/**
 * Created by Seddik on 17/06/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static String Tok = "";
    SessionManager session;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //we will save the token in sharedpreferences
     //   SessionManager.getInstance(getApplicationContext()).saveTokenFirebase(token);
        session = new SessionManager(this);
        session.saveTokenFirebase(token);
        Tok = token;

    }
}