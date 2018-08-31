package com.ttl.project.thetalklist;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.ttl.project.thetalklist.Services.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


//Videocall Activity

public class New_videocall_activity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        WebServiceCoordinator.Listener,
        Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener {

    private static final String LOG_TAG = New_videocall_activity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;
    private static final String TAG = "New_videocall_activity";

    AudioManager audioManager;
    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")

    int call_end_bit;
    TextView veesession_timer;
    BroadcastReceiver callEndReceiver;
    SharedPreferences talkNowOff;
    SharedPreferences.Editor editoroff;
    SharedPreferences preferences, pref;
    SharedPreferences.Editor editor;
    FrameLayout videoCallRootLayout;
    FrameLayout outgoingCallRootLayout;
    View videocontrols;
    View videocontrols2;
    //    ViewGroup parent;
    ImageView btn_cutcall;
    FrameLayout surfaceView;
    TextView callerName;
    ImageView callerImg;
    Intent i;
    RequestQueue queue111, queue222;
    String CallerName, CallerPic;
    ImageView callEnd;
    ImageView callMute;
    ImageView callChangeCamera;
    Timer t;
    TTL ttl;
    int TimeCount;
    int time;
    int layoutVisibilityBit;
    int minute;

    Handler handler;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreate");

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
       /* mp = MediaPlayer.create(this, R.raw.tring_tring);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);*//*
        mp = MediaPlayer.create(this, R.raw.incoming);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioManager m_amAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        m_amAudioManager.setMode(AudioManager.STREAM_MUSIC);
        m_amAudioManager.setSpeakerphoneOn(true);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.start();*/
        mp = MediaPlayer.create(this, R.raw.incoming);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //  AudioManager m_amAudioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(true);
        mp.setLooping(true);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        if (audioManager.isWiredHeadsetOn()) {
            Toast.makeText(this, "Headset plugged in", Toast.LENGTH_SHORT).show();
            audioManager.setMode(AudioManager.STREAM_MUSIC);
            audioManager.setSpeakerphoneOn(false);
            audioManager.setWiredHeadsetOn(true);
        } else {
            audioManager.setWiredHeadsetOn(false);
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.STREAM_MUSIC);
        }
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mp.start();


        // initialize view objects from your layout
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        veesession_timer = (TextView) findViewById(R.id.veesession_timer);

        requestPermissions();

        talkNowOff = getSharedPreferences("talknoeoff", MODE_PRIVATE);
        editoroff = talkNowOff.edit();
        editoroff.putString("inCall", "yes").apply();

        callEndReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Sorry but this tutor just went offline.  Try later or call another tutor.", Toast.LENGTH_LONG).show();
                finish();
            }
        };
        registerReceiver(callEndReceiver, new IntentFilter("callEnd"));

        audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        audioManager.setMicrophoneMute(false);
       /* msg_during_call= (ImageView) findViewById(R.id.msg_during_call);
        msg_during_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog fbDialogue = new Dialog(getApplicationContext(), android.R.style.Theme_Black_NoTitleBar);
//                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.message_one_to_one);
                fbDialogue.setCancelable(true);
                fbDialogue.show();
            }
        });*/
    }

    @Override
    protected void onPause() {

        Log.d(LOG_TAG, "onPause");
        audioManager.setMicrophoneMute(false);
        super.onPause();

//        callEnd.performClick();

        if (mSession != null) {
//            mSession.unpublish(mPublisher);
//            mSession.disconnect();
            mSession.onPause();
        }

        unregisterReceiver(callEndReceiver);
//        callEnd.performClick();

    }

    //Opentok connect api call
    public void connectionApiCall(String URL) {
        RequestQueue queue333 = Volley.newRequestQueue(getApplicationContext());
        mp.stop();

        Log.e("firebase reject Call", URL);
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("Call_activity_reject_ca", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue333.add(sr);
    }

    @Override
    protected void onResume() {

        Log.d(LOG_TAG, "onResume");

        super.onResume();

        if (mSession != null) {
            mSession.onResume();
        }


        ttl = new TTL();
        ttl.isCall = true;


        if (ttl.Callmin > 0) {

        }

       /* Intent notificationIntent = new Intent(getApplication(), New_videocall_activity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
//        notificationIntent.putExtra("message", "yes");
//        notificationIntent.putExtra("senderId", data.getInt("uid"));
//        notificationIntent.putExtra("firstName", data.getString("uname"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
        final int icon = R.mipmap.ttlg2;


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this).setSmallIcon(icon).setTicker("Ongoing Call").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("TheTalkList")
                        .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                        .setStyle(inboxStyle)
                        .setContentIntent(contentIntent)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ttlg2)
                        .setOngoing(true)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ttlg2))
                        .setContentText("call is running");
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

        android.app.Notification notification = new NotificationCompat.BigTextStyle(mBuilder)
                .bigText("call is running").build();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(200, notification);*/

        preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
        pref = getSharedPreferences("loginStatus", MODE_PRIVATE);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        outgoingCallRootLayout = (FrameLayout) findViewById(R.id.outgoingCallRootLayout);
        videoCallRootLayout = (FrameLayout) findViewById(R.id.videoCallRootLayout);
        videocontrols = findViewById(R.id.videocontrols1);
        videocontrols2 = findViewById(R.id.videocontrols2);
//        parent = (ViewGroup) videocontrols.getParent();


        videoCallRootLayout.setVisibility(View.GONE);
        btn_cutcall = (ImageView) findViewById(R.id.outgoing_cutcall);

        callerName = (TextView) outgoingCallRootLayout.findViewById(R.id.callerName);
        callerImg = (ImageView) outgoingCallRootLayout.findViewById(R.id.callerImg);

        surfaceView = (FrameLayout)

                findViewById(R.id.outgoingCallSurfaceView);


        final WebServiceCoordinator mWebServiceCoordinator = new WebServiceCoordinator(this, this);
        i = getIntent();

        time = i.getIntExtra("min", 0);
        if (i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
            mp.stop();
            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);


            mWebServiceCoordinator.fetchSessionConnectionData();
        } else

        {
            queue111 = Volley.newRequestQueue(getApplicationContext());

            final SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", MODE_PRIVATE);


            String URL = "https://www.thetalklist.com/api/firebase_call?sender_id=" + pref.getInt("id", 0) + "&receiver_id=" + preferences.getInt("tutorId", 0);
            Log.e("firebase Call url", URL);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        int status = jsonObject.getInt("status");

                        Log.e("video call api response", response);
                        if (status == 0) {


                            int cid = jsonObject.getInt("cid");

                            editor = preferences.edit();
                            editor.putInt("classId", cid).apply();

                            JSONObject object = jsonObject.getJSONObject("detail");
                            mWebServiceCoordinator.fetchSessionConnectionData();


                            CallerName = object.getString("firstName");
                            callerName.setText(object.getString("firstName"));
                            CallerPic = object.getString("pic");

                            if (!CallerPic.equals("")) {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + CallerPic)
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .placeholder(R.drawable.process)
                                        .error(R.drawable.black_person)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(callerImg);
                            } else {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(callerImg);
                            }


                        } else {
                            String error = jsonObject.getString("error");
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            finish();
                            onBackPressed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue111.add(sr);

        }
        btn_cutcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editoroff.clear().apply();
                mp.stop();
                if (mSession != null) {
                    mSession.disconnect();
                    onDisconnected(mSession);
                }
//                mSession.unpublish(mPublisher);
//                mSession.disconnect();

                queue222 = Volley.newRequestQueue(getApplicationContext());
                SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
                SharedPreferences pref = getSharedPreferences("loginStatus", MODE_PRIVATE);


                String URL = "https://www.thetalklist.com/api/firebase_rejectcall?sender_id=" + pref.getInt("id", 0) + "&receiver_id=" + preferences.getInt("tutorId", 0) + "&cid=" + preferences.getInt("classId", 0);
                Log.e("firebase reject Call", URL);
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue222.add(sr);


            }
        });

        callEnd = (ImageView) findViewById(R.id.callend);

        callMute = (ImageView) findViewById(R.id.callmute);

        callChangeCamera = (ImageView) findViewById(R.id.callchangecamera);


        mSubscriberViewContainer.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (layoutVisibilityBit == 1) {
                    videocontrols.setVisibility(View.VISIBLE);
                    LayoputVisibility();
                }
            }
        });


        requestPermissions();

        callChangeCamera.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                mPublisher.cycleCamera();
            }
        });

        callEnd.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                onDisconnected(mSession);


//                mSession.unpublish(mPublisher);
//                mSession.disconnect();
                ttl.isCall = false;

                String URL = "https://www.thetalklist.com/api/veesession_disconnect?cid=" + preferences.getInt("classId", 0);
                connectionApiCall(URL);
//                    if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
                if (t != null)

                    t.cancel();

                call_end_bit = 1;

                if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {

                    String URL2 = "https://www.thetalklist.com/api/total_cost?cid=" + preferences.getInt("classId", 0) + "&amount=" + getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE).getFloat("hRate", 0.0f) + "&time=" + TimeCount;

                    Log.e("total cost url", URL2);

                    StringRequest sr = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.e("total cost response", response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                Intent i = new Intent(getApplicationContext(), Popup_after_veesession.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("name", callerName.getText().toString());
                                i.putExtra("cost", String.format("%.02f", (Float.parseFloat(obj.getString("amount")))));
                                i.putExtra("fromCallActivity", "no");
//            i.putExtra("cost",)
                                startActivity(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                                mNotificationManager.cancel(200);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(getApplicationContext()).add(sr);
                } else {


                    Intent i = new Intent(getApplicationContext(), Popup_after_veesession.class);

                    float cost = (getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE).getFloat("hRate", 0.0f) / 60) * TimeCount;

                    i.putExtra("name", callerName.getText().toString());
                    i.putExtra("cost", cost * (1.33) - cost);
                    i.putExtra("fromCallActivity", "yes");
//                    i.putExtra("cost",String.format("%.02f",(Float.parseFloat(obj.getString("amount" )))));
//            i.putExtra("cost",)
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }


            }
        });

        ImageView callendMessage = (ImageView) findViewById(R.id.callendMessage);
        callendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        callMute.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (!audioManager.isMicrophoneMute()) {
                    audioManager.setMicrophoneMute(true);
                    callMute.setImageResource(R.drawable.mute);

                } else {
                    callMute.setImageResource(R.drawable.unmute);
                    audioManager.setMicrophoneMute(false);
                }
            }
        });
    }

    // checks the visibility of the bottom control view
    public void LayoputVisibility() {
        /*To make the layout invisible after 3 sec and when it touch the main layout it will again visible.*/


        if (layoutVisibilityBit == 0) {


            layoutVisibilityBit = 0;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        Log.d(LOG_TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setRationale(getString(R.string.rationale_ask_again))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel))
                    .setRequestCode(RC_SETTINGS_SCREEN_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {

        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // if there is no server URL set

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), RC_VIDEO_APP_PERM, perms);
        }
    }

    /* Web Service Coordinator delegate methods */

    //Initialize the opentok session
    private void initializeSession(String apiKey, String sessionId, String token) {

        mSession = new Session.Builder(this, apiKey, sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {

        Log.d(LOG_TAG, "ApiKey: " + apiKey + " SessionId: " + sessionId + " Token: " + token);

        initializeSession(apiKey, sessionId, token);
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {

        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
        finish();

    }

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());


        mPublisher = new Publisher(this);
        mPublisher.setPublisherListener(this);
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }
        Log.e("initializePublisher", "publisher initialization method");
        mSession.publish(mPublisher);

        mSession.setConnectionListener(new Session.ConnectionListener() {
            @Override
            public void onConnectionCreated(Session session, Connection connection) {

            }

            @Override
            public void onConnectionDestroyed(Session session, Connection connection) {


                View videocontrols = findViewById(R.id.videocontrols1);
                ViewGroup parent = (ViewGroup) videocontrols.getParent();
                parent.removeView(videocontrols);
                final View videocontrols1 = (LinearLayout) getLayoutInflater().inflate(R.layout.videocontrol2, parent, false);
                parent.addView(videocontrols1);
                mSession.unpublish(mPublisher);
//                mSession.disconnect();
                mPublisher = null;
                mSubscriber = null;

//                mNotificationManager.cancel(200);

                if (!i.getStringExtra("from").equalsIgnoreCase("callActivity") && call_end_bit == 0) {
                    callEnd.performClick();
                }


                ttl.isCall = false;
                LoginService loginService = new LoginService();
                loginService.login(pref.getString("email", ""), pref.getString("pass", ""), getApplicationContext());

                if (i.getStringExtra("from").equalsIgnoreCase("callActivity")) {

                    Intent i = new Intent(getApplicationContext(), Popup_after_veesession.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    float cost = (getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE).getFloat("hRate", 0.0f) / 60) * TimeCount;

                    float cost1 = (float) (cost * 100);
                    float finalCost = (float) (cost - (cost * 0.33));
                    //Toast.makeText(New_videocall_activity.this, "final cost "+finalCost, Toast.LENGTH_SHORT).show();
                    i.putExtra("name", callerName.getText().toString());
                    i.putExtra("cost", String.format("%.02f", finalCost / 100));
                    i.putExtra("fromCallActivity", "yes");
//                    i.putExtra("cost",String.format("%.02f",(Float.parseFloat(obj.getString("amount" )))));
//            i.putExtra("cost",)
                    startActivity(i);

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                    if (mSession != null) {
//                                mSession.disconnect();
                        onDisconnected(mSession);
                    }
                    videocontrols1.setVisibility(View.GONE);
                    videocontrols2.setVisibility(View.VISIBLE);

                    new CallActivity().finish();

                            /*Intent i = new Intent(getApplicationContext(), SettingFlyout.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);*/
//                        }
//                    }, 5000);
                } else {
                    onDisconnected(mSession);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDisconnected(Session session) {
        editoroff.clear().apply();
        mSession.unpublish(mPublisher);
        mSession.disconnect();
        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
        if (!i.getStringExtra("from").equalsIgnoreCase("callActivity") && TimeCount > 0) {


         /*   Intent i = new Intent(getApplicationContext(), StudentFeedBack.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);*/
        } else {

          /*  Intent i = new Intent(getApplicationContext(), Popup_after_veesession.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("name",callerName.getText().toString());
//            i.putExtra("cost",)
            startActivity(i);*/
        }
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber == null) {


            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);

            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);


            SharedPreferences Sessionpref = getSharedPreferences("sessionPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = Sessionpref.edit();

            editor.putString("sessionId", mSession.getSessionId()).apply();

            String Url = "https://www.thetalklist.com/api/veesession_connect?cid=" + preferences.getInt("classId", 0);
            connectionApiCall(Url);

//            if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {

            final Float money = getApplicationContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f);
            SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
            Float creditPerMinute = preferences.getFloat("credit", 0.0f);

            if (money < 10) {

                minute = (int) (money / creditPerMinute);

            }


            t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DecimalFormat formatter = new DecimalFormat("00");
                            int min = TimeCount / 60;
                            int sec = TimeCount % 60;

                            veesession_timer.setText(formatter.format(min) + ":" + formatter.format(sec));
                            TimeCount++;

                            if (time >= 0) {
                                if (time * 60 == TimeCount) {
                                    callEnd.performClick();
                                }
                            }
                                /*if (money < 10) {
                                    if (TimeCount == minute * 60) {
                                        t.cancel();
//                                        mSession.disconnect();
                                        onDisconnected(mSession);
                                      *//*  mPublisherViewContainer.removeAllViews();
                                        mSubscriberViewContainer.removeAllViews();*//*
                                        SettingFlyout settingFlyout = new SettingFlyout();
//                                        settingFlyout.setFragmentByVideoCall(new Earn_Buy_tabLayout());
                                        Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                        if ((minute * 60 - TimeCount) == 60) {
                                            Toast.makeText(New_videocall_activity.this, "Sorry, student is out of credits. Call will end in 60 secs.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }*/
                        }
                    });
                }
            }, 000, 1000);

//            }

            surfaceView.removeAllViews();
//            mPublisherViewContainer.removeAllViews();
//              mPublisherViewContainer.addView(mPublisher.getView());
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
//        mSession.unpublish(mPublisher);
//        mSession.disconnect();
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

        showOpenTokError(opentokError);
    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
//        mSession.unpublish(mPublisher);
//        mSession.disconnect();
        if (!i.getStringExtra("from").equals("callActivity")) {
//            callEnd.performClick();


            startActivity(new Intent(getApplicationContext(), SettingFlyout.class));
        }

//        Toast.makeText(getApplicationContext(), "call dropped", Toast.LENGTH_SHORT).show();
        SharedPreferences totalCostPref = getSharedPreferences("totalCostPref", MODE_PRIVATE);
        SharedPreferences.Editor totaEditor = totalCostPref.edit();

        totaEditor.putString("disconnect", "https://www.thetalklist.com/api/veesession_disconnect?cid=" + preferences.getInt("classId", 0));
        totaEditor.putString("totalcost", "https://www.thetalklist.com/api/total_cost?cid=" + preferences.getInt("classId", 0) + "&amount=" + getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE).getFloat("hRate", 0.0f) + "&time=" + TimeCount).apply();

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {

        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
//        mSession.unpublish(mPublisher);
//        mSession.disconnect();
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {

        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    //To see the opntok error
    private void showOpenTokError(OpentokError opentokError) {

//        Toast.makeText(this, opentokError.getErrorDomain().name() + ": " + opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finish();
    }


}