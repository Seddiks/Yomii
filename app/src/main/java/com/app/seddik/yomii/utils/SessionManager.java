package com.app.seddik.yomii.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.seddik.yomii.activities.LoginActivity;
import com.app.seddik.yomii.models.UserItems;

/**
 * Created by Seddik on 06/11/2017.
 */

public class SessionManager {
    // User ID (make variable public to access from outside)
    public static final String KEY_ID = "user_id";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_PHOTO_PROFIL = "photo_profil";

    public static final String KEY_PHOTO_COVER = "photo_cover";

    public static final String KEY_BIO = "bio";
    // Token (make variable public to access from outside)
    public static final String KEY_TOKEN = "token";
    // Token Firebase (make variable public to access from outside)
    public static final String KEY_TOKEN_FIREBASE = "token_firebase";
    // Sharedpref file name
    private static final String PREF_NAME = "YomiiPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static SessionManager mInstance;
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Create login session
     * */
    public void createLoginSession(int user_id, String name, String photo_profil, String photo_cover, String bio, String email, String token) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing id user in pref
        editor.putInt(KEY_ID,user_id);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing path photo profil in pref
        editor.putString(KEY_PHOTO_PROFIL, photo_profil);

        // Storing path photo profil in pref
        editor.putString(KEY_PHOTO_COVER, photo_cover);

        // Storing path photo profil in pref
        editor.putString(KEY_BIO, bio);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing token in pref
        editor.putString(KEY_TOKEN, token);

        // commit changes
        editor.commit();
    }


    /**
     * Create login session
     * */
    public void saveTokenFirebase(String token){
        // Storing token in pref
        editor.putString(KEY_TOKEN_FIREBASE, token);

        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public UserItems getUserDetails(){
        UserItems user = new UserItems();

        user.setUser_id(pref.getInt(KEY_ID, 0));
        user.setFull_name(pref.getString(KEY_NAME, null));
        user.setEmail(pref.getString(KEY_EMAIL, null));
        user.setPhoto_profil_path(pref.getString(KEY_PHOTO_PROFIL, null));
        user.setPhoto_cover_path(pref.getString(KEY_PHOTO_COVER, null));
        user.setBio(pref.getString(KEY_BIO, null));
        user.setToken(pref.getString(KEY_TOKEN, null));
        // return user
        return user;
    }

    /**
     * Get User ID
     * */
    public int getUSER_ID() {

        int id = pref.getInt(KEY_ID, 0);

        return id;
    }

    /**
     * Get Token Firebase
     * */
    public String getTokenFirebase(){

        String token = pref.getString(KEY_TOKEN_FIREBASE, null);

        return token;
    }




    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }
}