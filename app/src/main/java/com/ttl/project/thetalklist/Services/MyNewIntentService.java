package com.ttl.project.thetalklist.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.ttl.project.thetalklist.R;
import com.ttl.project.thetalklist.SplashScreen;
import com.ttl.project.thetalklist.util.NotificationUtils;

/**
 * Created by Saubhagyam on 21/11/2017.
 */

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       /* Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Time to Available");
        builder.setContentText("Your scheduled tutoring window is now open and your TalkLight is on!");
        builder.setSmallIcon(R.mipmap.ttlg2);*/
        Intent notifyIntent = new Intent(this, SplashScreen.class);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        final int icon = R.mipmap.ttlg2;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker("Tutoring is On").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("Time to Available")
                        .setSound(Uri.parse(String.valueOf(Notification.DEFAULT_SOUND)))
                        .setStyle(inboxStyle)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ttlg2)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                        .setContentText("Your scheduled tutoring window is now open and your TalkLight is on!");
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

        String myText = "Your scheduled tutoring window is now open and your TalkLight is on!";
        android.app.Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                .bigText(myText).build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, notification);
    }
}