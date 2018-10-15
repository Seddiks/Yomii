package com.app.seddik.yomii.api;

import com.app.seddik.yomii.config.AppConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Seddik on 04/07/2018.
 */

public class Client {

    public static ApiService createAppService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.URL_UPLOAD_PHOTOS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    public static ApiService createAppService2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

}