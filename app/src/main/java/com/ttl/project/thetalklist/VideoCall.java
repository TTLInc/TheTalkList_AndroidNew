package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.opentok.android.Connection;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class VideoCall extends ActionBarActivity implements WebServiceCoordinator.Listener,
        Session.SessionListener, PublisherKit.PublisherListener, SubscriberKit.SubscriberListener, Session.SignalListener {

    private static final String LOG_TAG = VideoCall.class.getSimpleName();

    private String mApiKey;
    private String mSessionId;
    private String mToken;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    LinearLayout videocontrols1;
    AudioManager m_amAudioManager;

    int cutByUser;


    RequestQueue queue111, queue222;


    FrameLayout videoCallRootLayout;
    FrameLayout outgoingCallRootLayout;
    int layoutVisibilityBit;

    MediaPlayer mediaPlayer;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;

    ImageView  btn_cutcall;
    FrameLayout surfaceView;


    Intent i;

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    View videocontrols;
    ViewGroup parent;

    boolean bit = true;

    String CallerName, CallerPic;

    Timer t;
    int TimeCount;

    TextView callerName;
    ImageView callerImg;





    SharedPreferences preferences, pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_call);




    }
    ImageView callEnd;
    ImageView callMute;
    ImageView callChangeCamera;

    @SuppressLint("CutPasteId")
    @Override
    protected void onResume() {
        super.onResume();

        preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
        pref = getSharedPreferences("loginStatus", MODE_PRIVATE);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        outgoingCallRootLayout = (FrameLayout) findViewById(R.id.outgoingCallRootLayout);
        videoCallRootLayout = (FrameLayout) findViewById(R.id.videoCallRootLayout);
        videocontrols = findViewById(R.id.videocontrols1);
        parent = (ViewGroup) videocontrols.getParent();


        videoCallRootLayout.setVisibility(View.GONE);
        btn_cutcall = (ImageView)

                findViewById(R.id.outgoing_cutcall);

        callerName = (TextView) outgoingCallRootLayout.findViewById(R.id.callerName);
        callerImg = (ImageView) outgoingCallRootLayout.findViewById(R.id.callerImg);

        surfaceView = (FrameLayout)

                findViewById(R.id.outgoingCallSurfaceView);

        // initialize WebServiceCoordinator and kick off request for necessary data
        final WebServiceCoordinator mWebServiceCoordinator = new WebServiceCoordinator(VideoCall.this, VideoCall.this);
        i = getIntent();

        if (i.getStringExtra("from").

                equalsIgnoreCase("callActivity"))

        {
            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);


            mWebServiceCoordinator.fetchSessionConnectionData();
        } else

        {

            queue111 = Volley.newRequestQueue(getApplicationContext());

            final SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);


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


            SharedPreferences p = getSharedPreferences("videocallrole", MODE_PRIVATE);
            SharedPreferences.Editor ed = p.edit();
            ed.putString("videocallrole", "publisher").apply();



            mediaPlayer = MediaPlayer.create(this, R.raw.outgoing);
            m_amAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            m_amAudioManager.setSpeakerphoneOn(true);
            mediaPlayer.setLooping(true);
            if (m_amAudioManager.isWiredHeadsetOn()) {
                m_amAudioManager.setSpeakerphoneOn(false);
                m_amAudioManager.setWiredHeadsetOn(true);
            } else {
                m_amAudioManager.setWiredHeadsetOn(false);
                m_amAudioManager.setSpeakerphoneOn(true);
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.start();
            final TextView outgoungCallToast = (TextView) findViewById(R.id.outgoungCallToast);
            Timer T = new Timer();
            T.scheduleAtFixedRate(new TimerTask() {
                int count = 0;

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (count == 15) {
                                outgoungCallToast.setText(CallerName + " seems busy. We are still notify on behalf of you.");
                            }
                            if (count == 45) {
                                outgoungCallToast.setText("We think " + CallerName + " is not here to take your call. Still wait for a while.");
                            }
                            if (count == 70) {
                                outgoungCallToast.setBackgroundColor(Color.parseColor("#F44336"));
                                outgoungCallToast.setText("We think" + CallerName + " is not here to take your call. You should cut the call and try again later.");
                            }
                            if (count == 150) {
                                finish();
                            }
                            count++;
                        }
                    });
                }
            }, 1000, 1000);
        }

        btn_cutcall.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (mSession != null) {
                    mSession.disconnect();
                    onDisconnected(mSession);
                }
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();


                queue222 = Volley.newRequestQueue(getApplicationContext());
                SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE);
                SharedPreferences pref = getSharedPreferences("loginStatus", MODE_PRIVATE);


                String URL = "https://www.thetalklist.com/api/firebase_rejectcall?sender_id=" + pref.getInt("id", 0) + "&receiver_id=" + preferences.getInt("tutorId", 0) + "&cid=" + preferences.getInt("classId", 0);
                Log.e("firebase reject Call", URL);
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue222.add(sr);



            }
        });



        try

        {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//you can use open(int) to use different cameras
        } catch (
                Exception e)

        {
            Log.e("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null)

        {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            surfaceView.addView(mCameraView);//add the SurfaceView to the layout
        }


        //outgoing call button events done...................................



        callEnd = (ImageView) findViewById(R.id.callend);

        callMute = (ImageView) findViewById(R.id.callmute);

        callChangeCamera = (ImageView) findViewById(R.id.callchangecamera);

        videocontrols1 = (LinearLayout)

                findViewById(R.id.videocontrols1);

        mSubscriberViewContainer.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (layoutVisibilityBit == 1) {
                    videocontrols1.setVisibility(View.VISIBLE);
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
                cutByUser=1;
                onDisconnected(mSession);
                mSession.disconnect();


                if (cutByUser==0){

                }else {

                }
                String URL="https://www.thetalklist.com/api/veesession_disconnect?cid="+ preferences.getInt("classId", 0);
                connectionApiCall(URL);
                if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
                    if (t != null)

                        t.cancel();
//                    Toast.makeText(getApplicationContext(), "total time " + TimeCount, Toast.LENGTH_SHORT).show();



                    String URL2 = "https://www.thetalklist.com/api/total_cost?cid=" + preferences.getInt("classId", 0) + "&amount=" + getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE).getFloat("hRate", 0.0f) + "&time=" + TimeCount;


                    StringRequest sr = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.e("total cost response",response);

                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videocall_hangup_popup, null);
                            View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_video_call, null);

                            final PopupWindow popupWindow = new PopupWindow(view,340,470, true);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(getApplicationContext()).add(sr);
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
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPublisher != null) {
            mPublisherViewContainer.removeView(mPublisher.getView());
            mSession.unpublish(mPublisher);
            mPublisher.destroy();
            mPublisher = null;
        }
        mSession.disconnect();
    }




    public void LayoputVisibility() {


        if (layoutVisibilityBit == 0) {


            layoutVisibilityBit = 0;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeSession() {
        mSession = new Session(VideoCall.this, mApiKey, mSessionId);






        mSession.setSessionListener(this);
        mSession.connect(mToken);
        Log.e("initializeSesssion", "session initialization method");
        Log.e("initializeSesssion ssn", mSession.toString());


    }

    private void initializePublisher() {
        mPublisher = new Publisher(VideoCall.this);
        mPublisher.setPublisherListener(VideoCall.this);
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);

        Log.e("initializePublisher", "publisher initialization method");
        mSession.publish(mPublisher);

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
        }
        Log.e("initializeSesssion ssn", mPublisher.toString());

        mSession.setConnectionListener(new Session.ConnectionListener() {
            @Override
            public void onConnectionCreated(Session session, Connection connection) {

            }

            @Override
            public void onConnectionDestroyed(Session session, Connection connection) {


                View videocontrols = findViewById(R.id.videocontrols1);
                ViewGroup parent = (ViewGroup) videocontrols.getParent();
                parent.removeView(videocontrols);
                View videocontrols1 = (LinearLayout) getLayoutInflater().inflate(R.layout.videocontrol2, parent, false);
                parent.addView(videocontrols1);

                mPublisher=null;
                mSubscriber=null;

//                Toast.makeText(getApplicationContext(), "msession disconnect external listener ", Toast.LENGTH_SHORT).show();
                if (i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mSession != null)
                                mSession.disconnect();
//                            finish();
                            Intent i=new Intent(getApplicationContext(),SettingFlyout.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }, 5000);
                }
                else {
                    onDisconnected(mSession);
                    callEnd.performClick();
                }
            }
        });
    }

    private void logOpenTokError(OpentokError opentokError) {
        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    /* Web Service Coordinator delegate methods */

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
        Log.e("videocall act sessionId", sessionId);
        Log.e("videocall act token", token);
        Log.e("videocall act api key", apiKey);
        mApiKey = apiKey;
        mSessionId = sessionId;
        mToken = token;

        // IF any error regarding to connection and publishres occurs then retrive it again
        initializeSession();
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
    }


    @Override
    public void onConnected(Session session) {
        Log.e(LOG_TAG, "Session Connected");


        initializePublisher();


        Log.e("VideoCall", "publisher is published now");
//        }

    }

    @Override
    public void onDisconnected(Session session) {
        Log.e(LOG_TAG, "Session Disconnected");

        if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {
            Intent i = new Intent(getApplicationContext(), StudentFeedBack.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
        }


    }

    private static final int RC_VIDEO_APP_PERM = 124;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {


        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    int minute;

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if (!audioManager.isMicrophoneMute()) {
                audioManager.setMicrophoneMute(true);

            } else {
                audioManager.setMicrophoneMute(false);
            }

            outgoingCallRootLayout.setVisibility(View.GONE);
            videoCallRootLayout.setVisibility(View.VISIBLE);

            surfaceView.removeAllViews();
            mPublisherViewContainer.addView(mPublisher.getView());


            if (mediaPlayer != null)
                mediaPlayer.stop();
            mSubscriber = new Subscriber(this, stream);
            mSubscriber.setSubscriberListener(this);
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber);


            String Url = "https://www.thetalklist.com/api/veesession_connect?cid=" + preferences.getInt("classId", 0);
            connectionApiCall(Url);

            if (!i.getStringExtra("from").equalsIgnoreCase("callActivity")) {

                final Float money = getApplicationContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f);
                SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
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
                                TimeCount++;
                                if (money < 10) {
                                    if (TimeCount == minute * 60) {
                                        t.cancel();
                                        onDisconnected(mSession);
                                        SettingFlyout settingFlyout = new SettingFlyout();
                                        settingFlyout.setFragmentByVideoCall(new Earn_Buy_tabLayout());
                                        Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);

                                        if ((minute * 60 - TimeCount) == 60) {
                                            Toast.makeText(VideoCall.this, "Sorry, student is out of credits. Call will end in 60 secs.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                }, 000, 1000);

            }


        }
    }

    public void connectionApiCall(String URL) {
        RequestQueue queue333 = Volley.newRequestQueue(getApplicationContext());


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
    public void onStreamDropped(Session session, Stream stream) {
        Log.e(LOG_TAG, "Stream Dropped");
    }


    @Override

    public void onError(Session session, OpentokError opentokError) {
        Log.e("session ssn id", "session " + session);
        Log.e("VideoCall session Error", "Exception: " + opentokError.getMessage());
        logOpenTokError(opentokError);
    }

    /* Publisher Listener methods */

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.e(LOG_TAG, "Publisher Stream Created");
        Log.e(LOG_TAG, "Publisher Created Stream " + stream.toString());

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.e(LOG_TAG, "Publisher Stream Destroyed");

        onDisconnected(mSession);
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e("session ssn id", "publisher kit error.");
        logOpenTokError(opentokError);
    }


    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.e(LOG_TAG, "Subscriber Connected");
        mSubscriberViewContainer.addView(mSubscriber.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {

        Log.e(LOG_TAG, "Subscriber Disconnected");

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e("opentok error", "errrrrrrrrrrrrrrrrrrrr");
        logOpenTokError(opentokError);
    }

    // In the implementation of the Session.ReconnectionListener interface
    /*@Override
    public void onReconnecting(Session session) {
        // Display a user interface notification.

    }


    @Override
    public void changedCamera(Publisher mPublisher, int newCameraId) {
        //The publisher's camera changed.

        if (newCameraId == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
        }

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDisconnected(mSession);

    }

    @Override
    public void onSignalReceived(Session session, String s, String s1, Connection connection) {

    }



}
