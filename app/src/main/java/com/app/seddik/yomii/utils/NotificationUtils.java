package com.app.seddik.yomii.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.app.seddik.yomii.MainActivity;
import com.app.seddik.yomii.R;
import com.app.seddik.yomii.activities.CreateTravelStoryActivity;
import com.app.seddik.yomii.api.ApiService;
import com.app.seddik.yomii.config.AppConfig;
import com.app.seddik.yomii.models.NotificationVO;
import com.app.seddik.yomii.models.ResponseItems;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_DATA_HOME;
import static com.app.seddik.yomii.utils.MyBitmapConfigs.getBitmapFromURL;

/**
 * Created by Seddik on 20/06/2018.
 */

public class NotificationUtils {

    private static final int NOTIFICATION_ID = 200;
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "myChannel";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Retrofit retrofit = new Retrofit.Builder().
            baseUrl(URL_UPLOAD_DATA_HOME).
            addConverterFactory(GsonConverterFactory.create()).
            build();
    private ApiService API = retrofit.create(ApiService.class);
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", MainActivity.class);
        activityMap.put("SecondActivity", CreateTravelStoryActivity.class);
    }

    public NotificationUtils() {
    }

    /**
     * Displays notification based on parameters
     *
     * @param notificationVO
     * @param resultIntent
     */
    public void displayNotification(NotificationVO notificationVO, Intent resultIntent) {
        {
            String message = notificationVO.getMessage();
            String title = notificationVO.getTitle();
            String iconUrlBigPhoto = AppConfig.URL_UPLOAD_PHOTOS + notificationVO.getPath_big_photo();
            String iconUrlPhotoProfil = AppConfig.URL_UPLOAD_PHOTOS + notificationVO.getPath_photo_profil();
            String action = notificationVO.getAction();
            String destination = notificationVO.getActionDestination();
            Bitmap largeBitMap = null;
            Bitmap profilBitMap = null;

            if (iconUrlBigPhoto != null) {
                largeBitMap = getBitmapFromURL(iconUrlBigPhoto);
            }

            if (iconUrlPhotoProfil != null) {
                profilBitMap = getBitmapFromURL(iconUrlPhotoProfil);
                float multiplier = MyBitmapConfigs.getImageFactor(mContext.getResources());
                profilBitMap = Bitmap.createScaledBitmap(profilBitMap, (int) (profilBitMap.getWidth() * multiplier), (int) (profilBitMap.getHeight() * multiplier), false);

            }
            final int icon = R.drawable.ic_person_circle_blue_a400_36dp;

            PendingIntent resultPendingIntent;

            if (URL.equals(action)) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
                resultIntent = new Intent(mContext, activityMap.get(destination));

                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            } else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            }


            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    mContext, CHANNEL_ID);

            Notification notification;

            if (largeBitMap == null) {
                //When Inbox Style is applied, user can expand the notification
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(message);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(message)
                        .build();

            } else {
                //If Bitmap is created from URL, show big icon

                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(largeBitMap);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.drawable.ic_notifications_light_blue_200_18dp)
                        .setColor(Color.parseColor("#81D4FA"))
                        // .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setLargeIcon(profilBitMap)
                        .setContentText(message)
                        .setWhen(Calendar.getInstance().getTimeInMillis())
                        .setShowWhen(true)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }


    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideNotification(int notification_id) {
        Call<ResponseItems> api = API.actionNotification(1, notification_id);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems results = response.body();
                boolean success = results.getSuccess();
                if (success) {

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    public void isReadNotification(int notification_id) {
        Call<ResponseItems> api = API.actionNotification(2, notification_id);
        api.enqueue(new Callback<ResponseItems>() {
            @Override
            public void onResponse(Call<ResponseItems> call, Response<ResponseItems> response) {
                ResponseItems results = response.body();
                boolean success = results.getSuccess();
                if (success) {
                    Log.d("Utils", "Error1");

                } else {
                    Log.d("Utils", "Error2");

                }
            }

            @Override
            public void onFailure(Call<ResponseItems> call, Throwable t) {
                t.printStackTrace();
                Log.d("Utils", "Error3" + t.toString());

            }
        });

    }

}
