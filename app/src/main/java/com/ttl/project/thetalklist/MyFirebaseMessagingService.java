package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ttl.project.thetalklist.Config.Config;
import com.ttl.project.thetalklist.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.apache.http.impl.auth.SPNegoScheme;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saubhagyam on 10/04/2017.
 */

//Messaging service of firebase
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public String msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null) {
            Log.e("xyz                    ", "message null");
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());

//            Log.e("msg", msg);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    //Handle the data response from firebase
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());


        SharedPreferences NotificationPref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
        SharedPreferences LoginPref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);


        try {
            final JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            final String message = data.getString("message");
            String imageUrl = data.getString("image");
            NotificationManager mNotificationManager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "id_product";
                // The user-visible name of the channel.
                CharSequence name = "Product";
                // The user-visible description of the channel.
                String description = "Notifications regarding our products";
                @SuppressLint("WrongConstant")
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MAX);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(mChannel);
                Intent intent1 = new Intent(getApplicationContext(), SplashScreen.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                        .setSmallIcon(R.mipmap.ic_launcher) //your app icon
                        .setBadgeIconType(R.mipmap.ttlg2) //your app icon
                        .setChannelId("id_product")
                        .setContentTitle("hiiii")
                        .setAutoCancel(true).setContentIntent(pendingIntent)
                        .setNumber(1)
                        .setColor(255)
                        .setContentText("hiiiii")
                        .setWhen(System.currentTimeMillis());
                notificationManager.notify(1, notificationBuilder.build());
            }*/
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            String id2 = "id_product";
            // The user-visible name of the channel.

            // The user-visible description of the channel.
/*
            String description = "Notifications regarding our products";
            Intent intent1 = new Intent(getApplicationContext(), SplashScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                    .setSmallIcon(R.mipmap.ic_launcher) //your app icon
                    .setBadgeIconType(R.mipmap.ttlg2) //your app icon
                    .setChannelId(id2)
                    .setContentTitle("hiiii")
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setNumber(1)
                    .setColor(255)
                    .setContentText("hiiiii")
                    .setWhen(System.currentTimeMillis());
            mNotificationManager2.notify(1, notificationBuilder.build());
*/


            if (title.equalsIgnoreCase("msg")) {

                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                // Toast.makeText(getApplicationContext(), data.getString("uname")+" : "+message, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                final int icon = R.mipmap.ttlg2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    String id = "id_product";
                    // The user-visible name of the channel.
                    CharSequence name = "Product";
                    // The user-visible description of the channel.
                    String description = "Notifications regarding our products";
                    @SuppressLint("WrongConstant")
                    NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_MAX);
                    // Configure the notification channel.
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
                    // Sets the notification light color for notifications posted to this
                    // channel, if the device supports this feature.
                    mChannel.setLightColor(Color.RED);
                    notificationManager.createNotificationChannel(mChannel);
                   // Intent intent1 = new Intent(getApplicationContext(), SplashScreen.class);
                    Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.putExtra("message", "yes");
                    notificationIntent.putExtra("senderId", data.getInt("uid"));
                    notificationIntent.putExtra("firstName", data.getString("uname"));
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                            .setSmallIcon(R.mipmap.ic_launcher) //your app icon
                            .setBadgeIconType(R.mipmap.ttlg2) //your app icon
                            .setChannelId("id_product")
                            .setContentTitle("TheTalkList")
                            .setAutoCancel(true).setContentIntent(pendingIntent)
                            .setNumber(1)
                            .setColor(255)
                            .setContentText(data.getString("uname") + " says: " + message)
                            .setWhen(System.currentTimeMillis());
                    notificationManager.notify(1, notificationBuilder.build());
                }
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



                Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                notificationIntent.putExtra("message", "yes");
                notificationIntent.putExtra("senderId", data.getInt("uid"));
                notificationIntent.putExtra("firstName", data.getString("uname"));
                PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(icon)
                                .setTicker(title).setWhen(0)
                                .setAutoCancel(true)
                                .setContentTitle("TheTalkList")
                                .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                .setStyle(inboxStyle)
                                .setContentIntent(contentIntent)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ttlg2)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                .setContentText(data.getString("uname") + " says: " + message);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                android.app.Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                        .bigText(data.getString("uname") + " says: " + message).build();

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(100, notification);
                Intent iq = new Intent();
                iq.setAction("countrefresh");
                this.sendBroadcast(iq);


                Intent i = new Intent();
                i.setAction("appendChatScreenMsg");
                i.putExtra("sender_id", data.getInt("uid"));
                i.putExtra("message", data.getString("message"));
                i.putExtra("firstName", data.getString("uname"));
                this.sendBroadcast(i);


            } else if (title.equalsIgnoreCase("tutorFav")) {
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
                final int icon = R.mipmap.ttlg2;


                Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.putExtra("message", "yes");
                notificationIntent.putExtra("senderId", data.getInt("uid"));
                notificationIntent.putExtra("firstName", data.getString("uname"));
                PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker(title).setWhen(0)
                                .setAutoCancel(true)
                                .setContentTitle("TheTalkList")
                                .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                .setStyle(inboxStyle)
                                .setContentIntent(contentIntent)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ttlg2)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                .setContentText(data.getString("uname") + " has favorited you. Be the first to Send greetings.");
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                android.app.Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                        .bigText(data.getString("uname") + " has favorited you. Be the first to Send greetings.").build();

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(200, notification);
                Intent iq = new Intent();
                iq.setAction("countrefresh");
                this.sendBroadcast(iq);


                Intent i = new Intent();
                i.setAction("appendChatScreenMsg");
                i.putExtra("sender_id", data.getInt("uid"));
                i.putExtra("message", data.getString("message"));
                i.putExtra("firstName", data.getString("uname"));
                this.sendBroadcast(i);

            } else if (title.equalsIgnoreCase("criticalBal")) {
                Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);
                NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
                final int icon = R.mipmap.ttlg2;
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.putExtra("message", "no");


                PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker("Low Credited Balance..!").setWhen(0)
                                .setAutoCancel(true)
                                .setContentTitle("TheTalkList")
                                .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                .setStyle(inboxStyle)
                                .setContentIntent(contentIntent)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ttlg2)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                .setContentText(message);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

//                String myText="Your credited balance is less than 3 credits. \n Please refill it.";
                android.app.Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                        .bigText(message).build();

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(100, notification);
            }
            //                }
            else if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent ie = new Intent();
                ie.setAction("callEnd");
//                Intent intent = new Intent("close.Application");
                if (title.equalsIgnoreCase("rejectCall")) {


                    Log.e("rejecttttttttt", "rejecttttttttttttttttttttttttttttttttttttttt");

                    SharedPreferences p = getSharedPreferences("videocallrole", MODE_PRIVATE);

                    if (p.getString("videocallrole", "").equalsIgnoreCase("subscriber")) {
                        this.sendBroadcast(ie);
                    } else {


                        this.sendBroadcast(ie);
                    }
                    Log.e("title is rejectCall ", "in if condition");


                } else if (title.equalsIgnoreCase("call")) {
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    Intent i = new Intent(getApplicationContext(), CallActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                }
            } else if (title.equalsIgnoreCase("clear_login")) {
                SharedPreferences loginPref = getSharedPreferences("loginStatus", MODE_PRIVATE);
                final SharedPreferences.Editor editor = loginPref.edit();


                editor.clear().apply();


            }

            Log.e("yyyyyyyyyyyyyyyyyy", "messaging service");


            if (title.equalsIgnoreCase("call")) {

                boolean isBackground = data.getBoolean("is_background");
                String timestamp = data.getString("timestamp");
                JSONObject payload = data.getJSONObject("payload");
                int id = data.getInt("ID");
                int cid = data.getInt("cid");
                String name = data.getString("name");

                Log.e(TAG, "isBackground: " + isBackground);
                Log.e(TAG, "payload: " + payload.toString());
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);
                Log.e(TAG, "name: " + name);
                Log.e(TAG, "senderId ID: " + id);
                Log.e(TAG, "cid: " + cid);
                Log.e(TAG, "tutorId : " + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("classId", cid);
                editor.putString("callSenderName", name);
                editor.putString("image", imageUrl);
                editor.putInt("studentId", getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));
                editor.putInt("tutorId", id).apply();


                Log.e("title is call ", "in if condition");
                Intent i = new Intent();
                i.setClass(this, CallActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(i);
            } else if (title.equalsIgnoreCase("critical_credit")) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                final int icon = R.mipmap.ttlg2;


                Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notificationIntent.putExtra("message", "yes");
                notificationIntent.putExtra("senderId", data.getInt("uid"));
                notificationIntent.putExtra("firstName", data.getString("uname"));
                PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker(title).setWhen(0)
                                .setAutoCancel(true)
                                .setContentTitle("TheTalkList")
                                .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                .setStyle(inboxStyle)
                                .setContentIntent(contentIntent)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ttlg2)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                                .setContentText(data.getString("uname") + " says: " + message);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(100, mBuilder.build());
            }
           /* } else {
            }*/
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */

    /**
     * Showing notification with text and image
     */
//Generate notification from the data of the firebase
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }


}
