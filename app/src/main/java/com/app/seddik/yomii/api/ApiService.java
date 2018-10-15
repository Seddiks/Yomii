package com.app.seddik.yomii.api;

import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.app.seddik.yomii.models.GuideItems;
import com.app.seddik.yomii.models.ResponseGalleryPhotosItems;
import com.app.seddik.yomii.models.ResponseItems;
import com.app.seddik.yomii.models.ResponseNotificationItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.models.ResponsePostComments;
import com.app.seddik.yomii.models.TravelStoryItems;
import com.app.seddik.yomii.models.UserItems;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Seddik on 17/12/f.
 */

public interface ApiService {
    //Send token in request
    // @GET("getSth.php")
    //Call<UserItems> login(@Header("token")String t);


    @FormUrlEncoded
    @POST("treatment_photos.php")
    Single<List<GalleryPhotosItems>> getPhotosGallery(@Field("action") int act,
                                                      @Field("user_id") int user_id,
                                                      @Field("photo_id") int photo_id,
                                                      @Field("per_page") int perPage);



    //Login
    @FormUrlEncoded
    @POST("login.php")
    Call<UserItems> login(@Field("email") String msg,
                          @Field("password") String password,
                          @Field("token_firebase") String token_firebase);

    //Logout
    @FormUrlEncoded
    @POST("logout.php")
    Call<ResponseItems> logout(@Field("user_id") int user_id,
                               @Field("type_token_firebase") int type_token_firebase,
                               @Field("token_firebase") String token_firebase);

    //Registration
    @FormUrlEncoded
    @POST("register.php")
    Call<UserItems> registration(@Field("full_name") String fname,
                                 @Field("email") String mail,
                                 @Field("password") String password,
                                 @Field("passwordConfirm") String passwordConfirm,
                                 @Field("token_firebase") String token
    );

    //Case forget password
    @FormUrlEncoded
    @POST("forget_password.php")
    Call<List<ResponseItems>> sumbitEmail(@Field("email") String msg);

    //Upload photos
    @Multipart
    @POST("treatment_photos.php")
    Call<ResponseItems> uploadImages(@Part("action")  int act,
                                     @Part("user_id")  int id,
                                     @Part("location") RequestBody location,
                                     @Part("city") RequestBody city,
                                     @Part List<MultipartBody.Part> images,
                                     @Part("legende") RequestBody legende);

    //Get Paths photos published by user (PHOTOS Fragment)
    @FormUrlEncoded
    @POST("treatment_photos.php")
    Call<ResponseGalleryPhotosItems> getPhotosGallery(@Field("action") int act,
                                                      @Field("user_id") int user_id,
                                                      @Field("currentPage") int currentPage);


    //Get Paths Child photos published by user
    @FormUrlEncoded
    @POST("treatment_photos.php")
    Call<ResponsePhotoItems> getUserPathChildPhotosPublished(@Field("action") int act,
                                                             @Field("photo_id") int user_id);

    //Get profil user details
    @FormUrlEncoded
    @POST("profil_details.php")
    Call<UserItems> getUserProfilDetails(@Field("action") int act,
                                         @Field("user_id") int user_id);


    //Update profil user details
    @Multipart
    @POST("profil_details.php")
    Call<ResponseItems> updateUserProfilDetails(@Part("action") int act,
                                                @Part MultipartBody.Part profil_file,
                                                @Part MultipartBody.Part cover_file,
                                                @Part("user_id") int user_id,
                                                @Part("full_name") RequestBody name,
                                                @Part("username") RequestBody username,
                                                @Part("bio") RequestBody bio);

    //Create new Story
    @Multipart
    @POST("stories.php")
    Call<ResponseItems> sendStory(@Part("action") int act,
                                  @Part("travel_story_id") int travel_story_id,
                                  @Part("user_id") int user_id,
                                  @Part MultipartBody.Part photo,
                                  @Part("title") RequestBody name,
                                  @Part("location") RequestBody username,
                                  @Part("full_story") RequestBody story);

    //Get Stories
    @Multipart
    @POST("stories.php")
    Call<TravelStoryItems> getUserStories(@Part("action") int act,
                                          @Part("user_id") int user_id);

    //Create new Guide
    @Multipart
    @POST("guides.php")
    Call<ResponseItems> sendGuide(@Part("action") int act,
                                  @Part("guide_id") int guide_id,
                                  @Part("user_id") int user_id,
                                  @Part MultipartBody.Part photo,
                                  @Part("title_guide") RequestBody title_guide,
                                  @Part("location") RequestBody location,
                                  @Part("experience") RequestBody experience,
                                  @Part("history") RequestBody history,
                                  @Part("budget_advice") RequestBody budget_advice,
                                  @Part("best_time_to_visit") RequestBody best_time_to_visit,
                                  @Part("best_place_to_visit") RequestBody best_place_to_visit,
                                  @Part("restaurant_suggestions") RequestBody restaurant_suggestions,
                                  @Part("transportation_advice") RequestBody transportation_advice,
                                  @Part("language") RequestBody language,
                                  @Part("other_informations") RequestBody other_informations);

    //Get Guides
    @Multipart
    @POST("guides.php")
    Call<GuideItems> getUserGuides(@Part("action") int act,
                                   @Part("user_id") int user_id);

    //Get details photos published by users and users details in Home Fragment
    @GET("home_photos_published.php")
    Call<ResponsePhotoItems> getDetailsPhotos(@Query("action") int act,
                                              @Query("user_id") int user_id,
                                              @Query("currentPage") int currentPage);

    //Get comments per photo and publish in Comment Photos Activity
    @FormUrlEncoded
    @POST("comments.php")
    Call<ResponsePostComments> getCommentsPerPhoto(@Field("action") int act,
                                                   @Field("user_id") int user_id,
                                                   @Field("photo_id") int photo_id,
                                                   @Field("currentPage") int currentPage);

    //Insert comment and Get ID comment
    @FormUrlEncoded
    @POST("comments.php")
    Call<ResponsePostComments> insertComment(@Field("action") int act,
                                             @Field("user_id") int user_id,
                                             @Field("reciever_user_id") int reciever_user_id,
                                             @Field("photo_id") int photo_id,
                                             @Field("comment") String comment);

    //Delete comment
    @FormUrlEncoded
    @POST("comments.php")
    Call<ResponseItems> deleteComment(@Field("action") int act,
                                      @Field("user_id") int user_id,
                                      @Field("photo_id") int photo_id,
                                      @Field("comment_id") int comment_id);


    //Add Like
    @FormUrlEncoded
    @POST("likes.php")
    Call<ResponseItems> addLike(@Field("action") int act,
                                @Field("user_id") int user_id,
                                @Field("reciever_user_id") int reciever_user_id,
                                @Field("photo_id") int photo_id);

    //delete Like
    @FormUrlEncoded
    @POST("likes.php")
    Call<ResponseItems> deleteLike(@Field("action") int act,
                                   @Field("user_id") int user_id,
                                   @Field("photo_id") int photo_id);


    //Get Notification per user
    @FormUrlEncoded
    @POST("notifications.php")
    Call<ResponseNotificationItems> getNotifications(@Field("action") int act,
                                                     @Field("user_id") int user_id,
                                                     @Field("currentPage") int currentPage);


    //Hide or Read notification (update is_hide to 1 or update is_read to 1)
    @FormUrlEncoded
    @POST("notifications.php")
    Call<ResponseItems> actionNotification(@Field("action") int act,
                                           @Field("notification_id") int notification_id);

}
