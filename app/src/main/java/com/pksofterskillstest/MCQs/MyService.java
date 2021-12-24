package com.pksofterskillstest.MCQs;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class MyService extends FirebaseMessagingService {

    Context context;
    String tokenUpdated="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context= getApplicationContext();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            sendMessageNotification("Telenor Quiz", "New Question Added ", context);


        }



    }


    @SuppressLint("RemoteViewLayout")
    private void sendMessageNotification(String title, String messageBody,Context context) {


        Intent intent = new Intent(context, MainActivity.class);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setSmallIcon(R.drawable.ic_baseline_star_24)
                        .setAutoCancel(true)
                        .setTicker("custom")
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
       /* if (Helper.isAppRunning(context,"com.pksofter.eattogetheruser")){

            try {
                MediaPlayer mediaPlayer=new MediaPlayer();
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }else {
                    mediaPlayer=MediaPlayer.create(context,R.raw.notification);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {*/
        notificationManager.notify(0/* ID of notification */, notificationBuilder.build());
//    }
    }



}