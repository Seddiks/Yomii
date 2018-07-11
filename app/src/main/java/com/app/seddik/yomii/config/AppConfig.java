package com.app.seddik.yomii.config;

import com.app.seddik.yomii.utils.SessionManager;

/**
 * Created by Seddik on 06/11/2017.
 */

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://192.168.1.5:81/Yomii/auth/";
    // Server user register url
    public static String URL_REGISTER = "http://192.168.1.5:81/Yomii/auth/";
    // Server user forget password url
    public static String URL_FORGET_PASSWORD = "http://192.168.1.5:81/Yomii/auth/";
    //  public static String URL_REGISTER = "http://yomii.alwaysdata.net/android_login_api/register.php";
    //Upload photos to server
    public static String URL_UPLOAD_PHOTOS = "http://192.168.1.8:81/Yomii/profil/";
    //Home page
    public static String URL_UPLOAD_DATA_HOME = "http://192.168.1.8:81/Yomii/home/";
    //Notification counter
    public static int NOTIFICATION_COUNTER = 0;
    private SessionManager session;




}