package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ttl.project.thetalklist.Config.Config;
import com.ttl.project.thetalklist.Services.LoginService;
import com.ttl.project.thetalklist.Services.MessageCountService;
import com.ttl.project.thetalklist.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.grantland.widget.AutofitHelper;

//Main Activity where all data fragment attached with
public class SettingFlyout extends AppCompatActivity {

    private static final String TAG = "SettingFlyout";
    final FragmentStack fragmentStack = FragmentStack.getInstance();
    final int CAMERA_REQUEST = 1323;
    final int GALLERY_REQUEST = 1342;
    final int CROP_REQUEST = 1352;
    public Switch talkNow;
    public NavigationView navigationView;
    public TextView TVuserName;
    public int manuallyTurnOn = -1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String email, pass;
    SharedPreferences preferences;
    DrawerLayout drawer;
    LinearLayout viewPager;
    int roleId;
    int status;
    Context context;
    List<Fragment> fragmentList;
    DrawerModel[] drawerItem;
    RatingBar ratingBar;
    DrawerItemCustomAdapter adapter;
    Toolbar toolbar;
    TextView credits, num_ttlScore;
    Typeface typeface;
    View v;
    ImageView TVusericon;
    View view1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LinearLayout return_to_call;
    boolean notification = false;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout settingFlyout_bottomcontrol_favorites, settingFlyout_bottomcontrol_videosearch, settingFlyout_bottomcontrol_Message,
            settingFlyout_bottomcontrol_payments, settingFlyout_bottomcontrol_tutorSearch, settingFlyout_bottomcontrol;
    TextView bottombar_message_count;
    TextView txtTutors, txtVideos, txtMessages, txtPayment, txtFavorits;
    Boolean MessageFrag;

    LinearLayout switch_layout;

    BroadcastReceiver countrefresh;
    int mSelectedItem;
    RequestQueue queue;
    Tracker t;
    RequestQueue queue111;
    String firebase_regId;
    int online = 1;
    FragmentTransaction ft;
    StringRequest sr;
    String Cls = " ";
    SharedPreferences prefAvailableTutor;
    SharedPreferences.Editor ed;
    SharedPreferences prefVideoList;
    SharedPreferences.Editor edvideo;
    TabBackStack tabBackStack;
    TTL ttl;
    int id;
    String userChoosenTask;
    String mFlag = "0";
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavigationDrawerItemTitles;
    private Handler mHandler;

    public SettingFlyout() {
    }

    public SettingFlyout(Boolean messageFrag) {
        MessageFrag = messageFrag;
        setFragmentByVideoCall(new VideoList());
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(countrefresh);

        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());



        TTL app = (TTL) getApplication();
        Tracker t = app.getGoogleAnalyticsTracker();
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.setScreenName("Main Setting Flyout");
        t.setPage("/SettingFlyOut");
        t.send(new HitBuilders.EventBuilder().setCategory("Main Screen").setAction("Landing Screen").setLabel("Landed").build());


        Intent i = this.getIntent();
        Uri uri = i.getData();


        if (uri != null && uri.getQueryParameter("utm_source") != null) {
            t.setCampaignParamsOnNextHit(uri);
        }

        setContentView(R.layout.activity_setting_flyout);

        preferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        v = getLayoutInflater().inflate(R.layout.tutor_actionbar_layout, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ttl = (TTL) getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (LinearLayout) findViewById(R.id.viewpager);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        credits = (TextView) toolbar.findViewById(R.id.num_credits);
        num_ttlScore = (TextView) toolbar.findViewById(R.id.num_ttlScore);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GothamBookRegular.ttf");
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        view1 = navigationView.getHeaderView(0);
        TVuserName = (TextView) view1.findViewById(R.id.personTextsettingflyout);
        TVusericon = (ImageView) view1.findViewById(R.id.imagesettingflyoutheader);
        fragmentManager = getSupportFragmentManager();
        com.ttl.project.thetalklist.util.Config.bottombar_message_count = (TextView) findViewById(R.id.bottombar_message_count);
        talkNow = (Switch) toolbar.findViewById(R.id.switch1);

        switch_layout = (LinearLayout) findViewById(R.id.switch_layout);

        settingFlyout_bottomcontrol_videosearch = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_videosearch);
        settingFlyout_bottomcontrol_Message = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_Message);
        settingFlyout_bottomcontrol_tutorSearch = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_tutorSearch);
        settingFlyout_bottomcontrol_payments = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_payments);
        settingFlyout_bottomcontrol_favorites = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol_favorites);
        settingFlyout_bottomcontrol = (LinearLayout) findViewById(R.id.settingFlyout_bottomcontrol);
        txtTutors = (TextView) findViewById(R.id.txtTutors);
        txtMessages = (TextView) findViewById(R.id.txtMessages);
        txtVideos = (TextView) findViewById(R.id.txtVideos);
        txtPayment = (TextView) findViewById(R.id.txtPayments);
        txtFavorits = (TextView) findViewById(R.id.txtFavorites);
        pref = getSharedPreferences("loginStatus", MODE_PRIVATE);
        fragmentTransaction = fragmentManager.beginTransaction();

        openProfileFragment();
        displayFirebaseRegId();


        ratingBar = (RatingBar) view1.findViewById(R.id.ratingBar);


        if (preferences.getFloat("avgRate", 0.0f) != 0.0f)
            ratingBar.setRating(preferences.getFloat("avgRate", 0.0f));

        if (ttl.isCall) {
            return_to_call.setVisibility(View.VISIBLE);
        }

        return_to_call = (LinearLayout) findViewById(R.id.return_to_call);
        return_to_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), New_videocall_activity.class);
                startActivity(i);
            }
        });

        manuallyTurnOn = 0;

        if (roleId == 0) {
            talkNow.setChecked(false);
            talkNow.setClickable(false);
            talkNow.setFocusable(false);
        }

        switch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getInt("roleId", 0) != 0) {
                    manuallyTurnOn = 1;
                    if (talkNow.isChecked()) {
                        talkNow.setChecked(false);
                    } else talkNow.setChecked(true);
                } else {
                }
            }
        });

        AutofitHelper.create(TVuserName);
        context = getApplicationContext();


        drawer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = drawer.getRootView().getHeight() - drawer.getHeight();

                if (heightDiff > 250) {
                    settingFlyout_bottomcontrol.setVisibility(View.GONE);
                    // Log.e("MyActivity", "keyboard opened");
                } else {
                    settingFlyout_bottomcontrol.setVisibility(View.VISIBLE);
                    //  Log.e("MyActivity", "keyboard closed");
                }
            }
        });


        MessageCountService messageCountService = new MessageCountService();
        messageCountService.MessageCount(this, pref);


        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("global");


        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(drawer);
                drawer.closeDrawers();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawer);

            }
        };

        queue111 = Volley.newRequestQueue(getApplicationContext());


        LoginService loginService = new LoginService();
        loginService.login(pref.getString("email", ""), pref.getString("pass", ""), getApplicationContext());


        if (pref.getFloat("ttl_points", 0.0f) > 0.0)
            num_ttlScore.setText(String.valueOf((int) pref.getFloat("ttl_points", 0.0f)));
        else num_ttlScore.setText("0");
        editor = pref.edit();
        email = pref.getString("email", "");
        pass = pref.getString("pass", "");

        mHandler = new Handler();


        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_item_text_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(drawer);
                drawer.closeDrawers();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawer);

            }
        };


        /*{
            if (pref.getInt("roleId", 0) != 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date x1 = new Date();
                final String dayOfTheWeek = sdf.format(x1);
                Log.e("today dayofweek", dayOfTheWeek);

                String url1 = "https://www.thetalklist.com/api/tutor_availability_info?uid=" + pref.getInt("id", 0);

                StringRequest sr = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                Toast.makeText(getContext(), "Response "+response, Toast.LENGTH_SHORT).show();
                        Log.e("response availablity ", response);
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getInt("status") == 0) {

                                JSONObject info = res.getJSONObject("info");

                                try {

                                    String string1 = info.getString("start_time");
                                    if (string1.contains("AM")) {
                                        string1 = string1.replace(" AM", "");
                                    } else if (string1.contains("PM")) {
                                        string1 = string1.replace(" PM", "PM");
                                        String hx = string1.substring(0, string1.indexOf(":") - 1);
                                        String remainder = string1.substring(string1.indexOf(":") + 1, string1.length());
                                        string1 = String.valueOf(Integer.parseInt(hx) + 12) + ":" + remainder;
                                    }

                                    String string2 = info.getString("end_time");
                                    if (string2.contains("AM")) {
                                        string2 = string2.replace(" AM", "");
                                    } else if (string2.contains("PM")) {
                                        string2 = string2.replace(" PM", "PM");
                                        String hx = string2.substring(0, string1.indexOf(":") - 1);
                                        String remainder = string2.substring(string2.indexOf(":") + 1, string2.length());
                                        string2 = String.valueOf(Integer.parseInt(hx) + 12) + ":" + remainder;
                                    }


                                    Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(time1);


                                    Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTime(time2);
                                    calendar2.add(Calendar.DATE, 1);

                                    String someRandomTime = "10:00";
                                    Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                                    Calendar calendar3 = Calendar.getInstance();
                                    calendar3.setTime(d);
                                    calendar3.add(Calendar.DATE, 1);

                                    Date x = calendar3.getTime();
                                    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                                        //checkes whether the current time is between 14:49:00 and 20:11:13.
//                                        Log.e("availability","yes");
                                        Log.e("day of week", dayOfTheWeek);


                                        if (info.getInt("sunday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Sunday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }

                                        }

                                        else if (Integer.parseInt(info.getString("monday")) == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Bonday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else if (info.getInt("tuesday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else if (info.getInt("wednesday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else if (info.getInt("thursday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Thursday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else if (info.getInt("friday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Friday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else if (info.getInt("saturday") == 1) {
                                            if (dayOfTheWeek.equalsIgnoreCase("Saturday")) {
                                                talkNow.setChecked(true);
                                                talkNow.setSelected(true);
                                                Log.e("availability", "yes");
                                            }
                                        }
                                        else {
                                            Log.e("availability", "No");
                                        }
                                    }

                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                Volley.newRequestQueue(getApplicationContext()).add(sr);
            }

        }*/

        /* setTalknow(Boolean.parseBoolean(*/

        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences talkNowOff = getSharedPreferences("talknoeoff", MODE_PRIVATE);
                // your code here...
                Log.e("inCall contains", String.valueOf(talkNowOff.contains("inCall")));
                if (talkNowOff.contains("inCall")) {
                    String Url = "https://www.thetalklist.com/api/tutor_online?uid=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&bit=" + 0;

                    Log.e("tutor online url", Url);

                    StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.e("tutor online res", response);

                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        if (obj.getInt("status") != 0) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                                }
                            });

                    Volley.newRequestQueue(getApplicationContext()).add(strRequest);
                } else {
                    TalkNow(pref, getApplicationContext());
                }
            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 10000);


        final FragmentStack fragmentStack = FragmentStack.getInstance();
        final TTL ttl = (TTL) getApplicationContext();


        settingFlyout_bottomcontrol_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefoultColorNavigation();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentStack.push(new Available_tutor());
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new FavoriteTutor()).commit();


                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites_activated));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
                txtTutors.setTextColor(Color.parseColor("#666666"));
                txtVideos.setTextColor(Color.parseColor("#666666"));
                txtMessages.setTextColor(Color.parseColor("#666666"));
                txtPayment.setTextColor(Color.parseColor("#666666"));
                txtFavorits.setTextColor(Color.parseColor("#3399CC"));
            }
        });


        settingFlyout_bottomcontrol_videosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefoultColorNavigation();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentStack.push(new Available_tutor());
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new VideoList()).commit();

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos_activated));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
                txtTutors.setTextColor(Color.parseColor("#666666"));
                txtVideos.setTextColor(Color.parseColor("#3399CC"));
                txtMessages.setTextColor(Color.parseColor("#666666"));
                txtPayment.setTextColor(Color.parseColor("#666666"));
                txtFavorits.setTextColor(Color.parseColor("#666666"));

            }
        });
        settingFlyout_bottomcontrol_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefoultColorNavigation();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentStack.push(new Available_tutor());
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new MessageList()).commit();

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages_activated));
                txtTutors.setTextColor(Color.parseColor("#666666"));
                txtVideos.setTextColor(Color.parseColor("#666666"));
                txtMessages.setTextColor(Color.parseColor("#3399CC"));
                txtPayment.setTextColor(Color.parseColor("#666666"));
                txtFavorits.setTextColor(Color.parseColor("#666666"));

            }
        });

        settingFlyout_bottomcontrol_tutorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefoultColorNavigation();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new Available_tutor()).commit();


                ttl.ExitBit = 1;

                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
                txtTutors.setTextColor(Color.parseColor("#3399CC"));
                txtVideos.setTextColor(Color.parseColor("#666666"));
                txtMessages.setTextColor(Color.parseColor("#666666"));
                txtPayment.setTextColor(Color.parseColor("#666666"));
                txtFavorits.setTextColor(Color.parseColor("#666666"));
            }
        });

        settingFlyout_bottomcontrol_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefoultColorNavigation();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentStack.push(new Available_tutor());
                final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();


                ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
                ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments_activated));
                ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
                txtTutors.setTextColor(Color.parseColor("#666666"));
                txtVideos.setTextColor(Color.parseColor("#666666"));
                txtMessages.setTextColor(Color.parseColor("#666666"));
                txtPayment.setTextColor(Color.parseColor("#3399CC"));
                txtFavorits.setTextColor(Color.parseColor("#666666"));
            }
        });


        DrawerModel[] drawerItem = new DrawerModel[8];
        drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
        drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
        drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
        drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
        drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
        drawerItem[5] = new DrawerModel(R.drawable.history, "History");
        drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
        drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.customdrawerlayout, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(mDrawerToggle);

        status = pref.getInt("status", 1);


        String username = preferences.getString("usernm", "");


        TVuserName.setText(username);
        String pic = preferences.getString("pic", "");
        if (!pic.equals("")) {
            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TVusericon);
        } else {
            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TVusericon);
        }


        TVusericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        if (status == 1) {

//            roleId = 1;
            Tablayout_with_viewpager withViewpager = new Tablayout_with_viewpager();
            fragmentTransaction.replace(R.id.viewpager, withViewpager);
            fragmentTransaction.commit();
        } else {
            roleId = preferences.getInt("roleId", 0);
            String ofMessage = getIntent().getStringExtra("message");
            String firstName = getIntent().getStringExtra("uname");
            int uid = getIntent().getIntExtra("senderId", 0);

            SharedPreferences pref123 = getSharedPreferences("roleChange", MODE_PRIVATE);
            SharedPreferences.Editor editor123 = pref123.edit();
            if (ofMessage != null) {
                Log.e("message", ofMessage);
                Log.e("uid", String.valueOf(uid));


                if (ofMessage.equals("no")) {
                    FragmentStack.getInstance().push(new Available_tutor());
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
                } else {


                    SharedPreferences chatPref = getSharedPreferences("chatPref", MODE_PRIVATE);
                    final SharedPreferences.Editor chatPrefEditor = chatPref.edit();

                    chatPrefEditor.putString("firstName", firstName);
                    chatPrefEditor.putInt("receiverId", uid).apply();

                    FragmentStack.getInstance().push(new Available_tutor());

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.viewpager, new MessageFragment()).commit();
                }
            } else if (pref123.getInt("roleId", 0) == 0) {
                editor123.putInt("roleId", -1).apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, new Available_tutor()).commit();

            } else {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                try {
                    if (extras != null) {
                        mFlag = extras.getString("flag");
                        Log.e(TAG, "FlagValue" + mFlag);
                        if (mFlag.equals("1")) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("RolId", 0);
                            int Rolid = pref.getInt("RolId", 0);
                            if (Rolid == 0) {
                                Tablayout_with_viewpager withViewpager = new Tablayout_with_viewpager();
                                fragmentTransaction.replace(R.id.viewpager, withViewpager);
                                fragmentTransaction.commit();
                            } else {
                                fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();

                            }
                        }
                    } else {
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();
                    }
                } catch (Exception e) {
                    fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();

                }

            }


        }


        prefAvailableTutor = getSharedPreferences("AvailableTutorPref", MODE_PRIVATE);
        ed = prefAvailableTutor.edit();

        prefVideoList = getSharedPreferences("videoListResponse", MODE_PRIVATE);
        edvideo = prefVideoList.edit();


        tabBackStack = TabBackStack.getInstance();

        if (preferences.getInt("roleId", 0) == 0) {
            talkNow.setChecked(false);
            talkNow.setClickable(false);
            talkNow.setFocusable(false);

        }


        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


    }

    private void openProfileFragment() {


    }

    public void DefoultColorNavigation() {
        drawerItem = new DrawerModel[8];
        drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
        drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
        drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
        drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
        drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
        drawerItem[5] = new DrawerModel(R.drawable.history, "History");
        drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
        drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");
        adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
        mDrawerList.setAdapter(adapter);
    }

    //check availibility api call
    public void TalkNow(SharedPreferences pref, final Context context) {


        if (pref.getInt("roleId", 0) != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date x1 = new Date();
            final String dayOfTheWeek = sdf.format(x1);
            Log.e("today dayofweek", dayOfTheWeek);

            String url1 = "https://www.thetalklist.com/api/tutor_availability_info?uid=" + pref.getInt("id", 0);

            StringRequest sr = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("response availablity ", response);
                    try {
                        JSONObject res = new JSONObject(response);
                        if (res.getInt("status") == 0) {

                            JSONObject info = res.getJSONObject("info");


                            String string1 = info.getString("start_time");
                            if (string1.contains("AM")) {
                                string1 = string1.replace(" AM", "");
                                String hx = string1.substring(0, string1.indexOf(":"));
                                String remainder = string1.substring(string1.indexOf(":") + 1, string1.length());
                                if (Integer.parseInt(hx) < 12)
                                    string1 = String.valueOf(Integer.parseInt(hx)) + ":" + remainder;
                                else if (Integer.parseInt(hx) == 12)
                                    string1 = String.valueOf(Integer.parseInt(hx) - 12) + ":" + remainder;
                                string1 = string1.replace("AM", "");
                            } else if (string1.contains("PM")) {
                                string1 = string1.replace(" PM", "PM");
                                Log.e("start time hours", string1);
                                String hx = string1.substring(0, string1.indexOf(":"));
                                String remainder = string1.substring(string1.indexOf(":") + 1, string1.length());
                                if (Integer.parseInt(hx) < 12)
                                    string1 = String.valueOf(Integer.parseInt(hx) + 12) + ":" + remainder;
                                else if (Integer.parseInt(hx) == 12)
                                    string1 = String.valueOf(Integer.parseInt(hx)) + ":" + remainder;
                                string1 = string1.replace("PM", "");
                            }


                            String string2 = info.getString("end_time");
                            if (string2.contains("AM")) {
                                string2 = string2.replace(" AM", "");
                                String hx = string2.substring(0, string2.indexOf(":"));
                                String remainder = string2.substring(string2.indexOf(":") + 1, string2.length());
                                if (Integer.parseInt(hx) < 12)
                                    string2 = String.valueOf(Integer.parseInt(hx)) + ":" + remainder;
                                else if (Integer.parseInt(hx) == 12)
                                    string2 = String.valueOf(Integer.parseInt(hx) - 12) + ":" + remainder;

                                string2 = string2.replace("AM", "");
                            } else if (string2.contains("PM")) {
                                string2 = string2.replace(" PM", "PM");
                                String hx = string2.substring(0, string2.indexOf(":"));
                                String remainder = string2.substring(string2.indexOf(":") + 1, string2.length());
                                if (Integer.parseInt(hx) < 12)
                                    string2 = String.valueOf(Integer.parseInt(hx) + 12) + ":" + remainder;
                                else if (Integer.parseInt(hx) == 12)
                                    string2 = String.valueOf(Integer.parseInt(hx)) + ":" + remainder;

                                string2 = string2.replace("PM", "");
                            }
                            int to = 0;
                            int from = 0;
                            Log.e("day of week", dayOfTheWeek);
                            Log.e("start time", string1);
                            Log.e("end time", string2);
                            try {
                                to = Integer.parseInt(string1.replace(":", ""));
                                from = Integer.parseInt(string2.replace(":", ""));
                            } catch (Exception e) {

                            }

                            Log.e("to", String.valueOf(to));
                            Log.e("from", String.valueOf(from));
                            Date date = new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
                            Log.e("cur", String.valueOf(t));
                            Log.e("Manually", String.valueOf(manuallyTurnOn));
                            Log.e("dayofweek", dayOfTheWeek.toLowerCase());


                            if (to <= t && t <= from) {
                                //checkes whether the current time is between 14:49:00 and 20:11:13.
                                Log.e("in if condition", "yes");


                                if (info.getInt("sunday") == 1 ||
                                        info.getInt("monday") == 1 ||
                                        info.getInt("tuesday") == 1 ||
                                        info.getInt("wednesday") == 1 ||
                                        info.getInt("thursday") == 1 ||
                                        info.getInt("friday") == 1 ||
                                        info.getInt("saturday") == 1
                                        ) {


                                    if (info.getInt(dayOfTheWeek.toLowerCase()) == 1) {
                                        talkNow.setChecked(true);
                                        Log.e("availability", "yes");
                                        if (to == t && !notification) {
                                            notification = true;
                                            Intent notificationIntent = new Intent(getApplication(), SettingFlyout.class);
                                            NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
                                            final int icon = R.mipmap.ttlg2;
                                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                                            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                            NotificationCompat.Builder mBuilder =
                                                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(icon).setTicker("Tutoring is On").setWhen(0)
                                                            .setAutoCancel(true)
                                                            .setContentTitle("Time to Available")
                                                            .setSound(Uri.parse(String.valueOf(android.app.Notification.DEFAULT_SOUND)))
                                                            .setStyle(inboxStyle)
                                                            .setContentIntent(contentIntent)
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
//                                        Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
                                    } else {
                                        Log.e("availability", "No");
                                        if (manuallyTurnOn != 1)
                                            talkNow.setChecked(false);
                                    }

                                    /*if (info.getInt("sunday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Sunday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }

                                    }
                                    if (info.getInt("monday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Monday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }
                                      if (info.getInt("tuesday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }
                                     if (info.getInt("wednesday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }
                                        if (info.getInt("thursday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Thursday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }
                                      if (info.getInt("friday") == 1) {

                                        if (dayOfTheWeek.equalsIgnoreCase("Friday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }
                                      if (info.getInt("saturday") == 1) {
                                        if (dayOfTheWeek.equalsIgnoreCase("Saturday")) {
                                            talkNow.setChecked(true);
                                            Log.e("availability", "yes");
                                            Notify(Integer.parseInt(string1.substring(0, string1.indexOf(":"))), Integer.parseInt(string1.substring(string1.indexOf(":") + 1, string1.length())));
//                                        notification = 1;
                                        }
                                    }*/
                                    /*else {
                                        Log.e("availability", "No");
                                        if (manuallyTurnOn != 1)
                                            talkNow.setChecked(false);
                                    }*/
                                } else {
                                    Log.e("availability", "No");
                                    if (manuallyTurnOn != 1)
                                        talkNow.setChecked(false);
                                }
                            } else {
                                if (manuallyTurnOn != 1)
                                    talkNow.setChecked(false);
                            }

                        } else {
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

            Volley.newRequestQueue(getApplicationContext()).add(sr);
        }
    }

    //Set the fragment afte video call
    public void setFragmentByVideoCall(Fragment fragmentByVideoCall) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.viewpager, fragmentByVideoCall).commit();
    }

    //Register firebase id in database
    private void displayFirebaseRegId() {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.sendRegistrationToServer(refreshedToken);


        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putString("firebase id", refreshedToken).apply();
        firebase_regId = refreshedToken;
        Log.e("firebase reg id 1111111", "Firebase reg id: " + refreshedToken + "--->Device Id<----" + android_id);
        String URL = "https://www.thetalklist.com/api/firebase_register?user_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&reg_id=" + refreshedToken + "&device_id=" + android_id;
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if (status == 0) {
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
        Volley.newRequestQueue(getApplicationContext()).add(sr);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(countrefresh);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(getApplicationContext()).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(getApplicationContext()).reportActivityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler1 = new Handler();
        Runnable r = new Runnable() {
            public void run() {

            }
        };
        handler1.postDelayed(r, 1000);
        /*FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        ft = fragmentManager.beginTransaction();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                loginService();
            }
        }, 5000);

        ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
        ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
        LoginService loginService = new LoginService();
        loginService.login(pref.getString("email", ""), pref.getString("pass", ""), getApplicationContext());
        queue = Volley.newRequestQueue(getApplicationContext());


        countrefresh = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
                Log.e(TAG, "onReceive: " + "https://www.thetalklist.com/api/count_messages?sender_id=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));
                StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /*if (com.ttl.project.thetalklist.util.Config.msgCount > 0) {
                            findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.VISIBLE);
                            bottombar_message_count.setText(String.valueOf(com.ttl.project.thetalklist.util.Config.msgCount));
                        }
                        if (com.ttl.project.thetalklist.util.Config.msgCount == 0) {
                            Log.e(TAG, "Gone ");
                            findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
                        }*/
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.e("MsgCountStatus ", response);

                            if (object.getInt("unread_count") > 0) {
                                com.ttl.project.thetalklist.util.Config.bottombar_message_count.setVisibility(View.VISIBLE);
                                com.ttl.project.thetalklist.util.Config.msgCount = object.getInt("unread_count");
                                com.ttl.project.thetalklist.util.Config.bottombar_message_count.setText(String.valueOf(object.getInt("unread_count")));
                                Log.e(TAG, "MsgCountDisplay " + String.valueOf(object.getInt("unread_count")));
                            } else {
                                com.ttl.project.thetalklist.util.Config.msgCount = 0;

                                Log.e(TAG, "Gone ");
                                findViewById(R.id.bottombar_message_count).setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse-0-0-0: ");
                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(sr);
            }
        };
        registerReceiver(countrefresh, new IntentFilter("countrefresh"));

        loginService();


        credits.setText(String.format("%.02f", pref.getFloat("money", 0.0f)));

        String mCredits = String.valueOf(pref.getFloat("money", 0.0f));
        Log.e("money", mCredits);
        try {
            BuyCredits buyCredits = new BuyCredits();
            Bundle bundle = new Bundle();
            bundle.putString("Credits", mCredits);
            buyCredits.setArguments(bundle);
            Log.e(TAG, "Credit-->SettingClass: " + mCredits);
        } catch (Exception e) {
            e.printStackTrace();
        }
        credits.setTypeface(typeface);
        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefultIcon();
                fragmentStack.push(new Available_tutor());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
            }
        });
        num_ttlScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDefultIcon();
                fragmentStack.push(new Available_tutor());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, new TTL_Score()).commit();

            }
        });
        talkNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    online = 1;
                } else {
                    online = 0;
                }
                String Url = "https://www.thetalklist.com/api/tutor_online?uid=" + getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&bit=" + online;

                Log.e("tutor online url", Url);

                StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.e("tutor online res", response);

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    if (obj.getInt("status") != 0) {
                                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                            }
                        });

                Volley.newRequestQueue(getApplicationContext()).add(strRequest);
            }

//            }
        });


    }

    private void getDefultIcon() {
        drawerItem = new DrawerModel[8];
        drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
        drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
        drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
        drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
        drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
        drawerItem[5] = new DrawerModel(R.drawable.history, "History");
        drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
        drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

        adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
        mDrawerList.setAdapter(adapter);
        ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
        ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
        txtTutors.setTextColor(Color.parseColor("#666666"));
        txtVideos.setTextColor(Color.parseColor("#666666"));
        txtMessages.setTextColor(Color.parseColor("#666666"));
        txtPayment.setTextColor(Color.parseColor("#666666"));
        txtFavorits.setTextColor(Color.parseColor("#666666"));
    }

    //Login service
    public void loginService() {

        String URL = "https://www.thetalklist.com/api/login?email=" + email + "&password=" + pass;


        sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);

                UserData data = UserData.getInstance();
                data.setLoginServResponse(response);


                try {

                    JSONObject jsonObject = new JSONObject(response);


                    Log.e("response", response);

                    int status = (int) jsonObject.get("status");
                    if (status == 1) {


                    } else {


                        JSONObject resultObj = (JSONObject) jsonObject.get("result");
                        int roleId = resultObj.getInt("roleId");
                        String UserName = (String) resultObj.get("username");
                        int userId = resultObj.getInt("id");
                        String mail = resultObj.getString("email");

                        editor.putString("LoginWay", "InternalLogin");
                        editor.putString("loginResponse", response);
                        editor.putString("user", UserName);
                        editor.putInt("roleId", roleId);
                        editor.putBoolean("logSta", true);
                        editor.putString("usernm", resultObj.getString("usernm"));
                        editor.putInt("userId", userId);
                        editor.putString("credit_balance", resultObj.getString("credit_balance"));
                        editor.putString("usernm", resultObj.getString("usernm"));
                        editor.putInt("id", resultObj.getInt("id"));
                        editor.putInt("country", resultObj.getInt("country"));
                        editor.putInt("province", resultObj.getInt("province"));
                        editor.putString("city", resultObj.getString("city"));
                        editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                        editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                        editor.putInt("status", 0);
                        editor.putString("email", mail);
                        editor.apply();

                        credits.setText(resultObj.getString("money"));
                        TVuserName.setText(resultObj.getString("usernm"));


                        if (resultObj.getInt("readytotalk") == 1) {
                            talkNow.setChecked(true);
                        }

                        if (!resultObj.getString("ttl_points").equals("")) {
                            int rewardPoints = resultObj.getInt("ttl_points")/*.substring(0, resultObj.getString("ttl_points").indexOf(".")*/;
                            if (rewardPoints < 0)
                                num_ttlScore.setText("0");
                            else
                                num_ttlScore.setText(String.valueOf(rewardPoints));
                        } else num_ttlScore.setText("0");
                        String pic = resultObj.getString("pic");
                        if (!pic.equals("")) {
                            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(TVusericon);
                        } else {
                            Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                    .crossFade()
                                    .thumbnail(0.5f)
                                    .bitmapTransform(new CircleTransform(getApplicationContext()))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(TVusericon);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.getClass().equals(TimeoutError.class)) {
                    // Show timeout error message
                    Toast.makeText(getApplicationContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
                if (error.getClass().equals(ServerError.class)) {
                    // Show timeout error message
                    Toast.makeText(getApplicationContext(),
                            "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                            Toast.LENGTH_LONG).show();
                }


                Log.d("error", error.toString());
            }
        });
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {


        ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
        ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
        ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));

        FragmentManager fragmentManager1 = getSupportFragmentManager();
        SharedPreferences chatPref = getSharedPreferences("chatPref", MODE_PRIVATE);
        SharedPreferences SearchTutorPref = getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE);
        final SharedPreferences.Editor chatPrefEditor = chatPref.edit();
        final SharedPreferences.Editor SearEditor = SearchTutorPref.edit();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (fragmentStack.size() > 0) {
                Fragment fragment = fragmentStack.pop();


                if (fragment != null) {
                    Cls = fragment.getClass().toString();
                    Log.e("class Name :- ", fragment.getClass().toString());

                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    fragmentManager.executePendingTransactions();

                    getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, fragment, fragment.getClass().toString()).commit();
                } else {
                    if (fragmentStack.size() > 0) {

                        if (fragmentStack.size() == 1) {
                            Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        fragmentStack.setSize(0);
                        tabBackStack.setTabPosition(0);
                        if (getSharedPreferences("loginStatus", MODE_PRIVATE).getString("LoginWay", "").equalsIgnoreCase("FacebookLogin")) {
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();

                            AccessToken.setCurrentAccessToken(null);
                        }
                        finish();
                        chatPrefEditor.clear().apply();
                        SearEditor.clear().apply();
                        ed.clear().apply();
                        edvideo.putInt("position", 0).apply();
                        moveTaskToBack(false);
                        new Login().finish();
                        ttl.ExitBit = 1;
                        new Login().onBackPressed();
                        new SplashScreen().onBackPressed();
                    }
                }
            } else {


                if (ttl.ExitBit > 0) {
                    if (ttl.ExitBit == 1)
                        Toast.makeText(getApplicationContext(), "Please press once to exit", Toast.LENGTH_SHORT).show();
                    ttl.ExitBit--;
                } else {
                    if (getSharedPreferences("loginStatus", MODE_PRIVATE).getString("LoginWay", "").equalsIgnoreCase("FacebookLogin")) {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager.getInstance().logOut();

                        AccessToken.setCurrentAccessToken(null);
                    }
                    tabBackStack.setTabPosition(0);
                    chatPrefEditor.clear().apply();
                    SearEditor.clear().apply();
                    ed.putInt("position", 0).apply();
                    edvideo.putInt("position", 0).apply();
                    moveTaskToBack(false);
                    new Login().finish();
                    ttl.ExitBit = 1;
                    new Login().onBackPressed();
                    new SplashScreen().onBackPressed();
                    finish();
                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    //Select the option for image uploading
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingFlyout.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //to get image from camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    //To select the image from gallary
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
            else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");


                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);
                TVusericon.setImageDrawable(roundedBitmapDrawable);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                uploadImage(encodedImageString, imageBitmap, getApplication(), pref.getInt("id", 0));
            }

        }
    }

    //Gallary image result
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TVusericon.setImageBitmap(bm);

        Bitmap bb = getResizedBitmap(bm, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();

        Intent ui = new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap", byteArray);
        startActivity(ui);
    }

    //Resize the bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //Captured image result
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bb = getResizedBitmap(thumbnail, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();


        Intent ui = new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap", byteArray);
        startActivity(ui);
    }

    //Image upload
    public void uploadImage(final String encodedImageString, final Bitmap bitmap, final Context context, final int id) {


        String uploadURL = "https://www.thetalklist.com/api/profile_pic";
        Log.e("image uploading url", uploadURL);
        Log.e("image uploading url", uploadURL);
        Log.e("encoded image string ", encodedImageString);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();


        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LoginService loginService = new LoginService();
                loginService.login(context.getSharedPreferences("loginStatus", MODE_PRIVATE).getString("email", ""), context.getSharedPreferences("loginStatus", MODE_PRIVATE).getString("pass", ""), context);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", encodedImageString);
                parameters.put("uid", String.valueOf(id));
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String ofMessage = intent.getStringExtra("message");
        String firstName = intent.getStringExtra("uname");
        int uid = intent.getIntExtra("senderId", 0);

        SharedPreferences pref = getSharedPreferences("roleChange", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

        if (intent.hasExtra("roll")) {
            talkNow.setChecked(false);
            talkNow.setClickable(false);
            talkNow.setFocusable(false);
        } else if (ofMessage != null) {
            Log.e("message", ofMessage);
            Log.e("uid", String.valueOf(uid));


            if (ofMessage.equals("no")) {
                FragmentStack.getInstance().push(new Available_tutor());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
            } else {


                SharedPreferences chatPref = getSharedPreferences("chatPref", MODE_PRIVATE);
                final SharedPreferences.Editor chatPrefEditor = chatPref.edit();

                chatPrefEditor.putString("firstName", firstName);
                chatPrefEditor.putInt("receiverId", uid).apply();

                FragmentStack.getInstance().push(new Available_tutor());

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.viewpager, new MessageFragment()).commit();
            }
        } else if (pref.getInt("roleId", 0) == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, new Available_tutor()).commit();
            editor.clear().apply();

        } else if (intent.hasExtra("back")) {
            if (intent.getBooleanExtra("back", true))
                onBackPressed();
        } else

            ft1.replace(R.id.viewpager, new Available_tutor()).commit();
    }

    //Drawer item click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        public void selectItem(int position) {
            SharedPreferences prefAvailableTutor = getSharedPreferences("AvailableTutorPref", MODE_PRIVATE);
            final SharedPreferences.Editor edavailabe = prefAvailableTutor.edit();

            ((ImageView) findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
            ((ImageView) findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
            ((ImageView) findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
            txtTutors.setTextColor(Color.parseColor("#666666"));
            txtVideos.setTextColor(Color.parseColor("#666666"));
            txtMessages.setTextColor(Color.parseColor("#666666"));
            txtPayment.setTextColor(Color.parseColor("#666666"));
            txtFavorits.setTextColor(Color.parseColor("#666666"));
            SharedPreferences prefVideoList = getSharedPreferences("videoListResponse", MODE_PRIVATE);
            final SharedPreferences.Editor edvideo = prefVideoList.edit();

            Fragment fragment = null;
            switch (position) {
                case 0:

                  /*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoginService loginService = new LoginService();
                            loginService.login(getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("email", ""), getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("pass", ""), getApplicationContext());

                        }
                    }, 200);*/
                    fragment = new Tablayout_with_viewpager(1);
                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile_activated, "Profile", true);
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;
                case 1:
                    fragment = new Availability_page_fragment();
                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability_activated, "Availability", true);
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;
                case 2:
                    fragment = new DesiredTutor();
                    SharedPreferences pref1 = getSharedPreferences("AvailableTutorPref", MODE_PRIVATE);
                    SharedPreferences.Editor edi = pref1.edit();
                    edi.clear().apply();
                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.desired_activated, "Desired Tutor", true);
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;
                case 3:
                    fragment = new Earn_Buy_tabLayout();
                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments_activated, "Payments", true);
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;

                case 4:
                    fragment = new TTL_Score();

                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards_activated, "Rewards", true);
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;
                case 5:
                    fragment = new History();

                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history_activated, "History", true);
                    drawerItem[6] = new DrawerModel(R.drawable.support, "Support");
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                  /*  Drawable mDrawable = context.getResources().getDrawable(R.drawable.history);
                    mDrawable.setColorFilter(new
                            PorterDuffColorFilter(0xabcebc, PorterDuff.Mode.MULTIPLY));*/
                    Log.e("tag", "selectItem: ");
                    break;
                case 6:
                    fragment = new Support();
                    drawerItem = new DrawerModel[8];
                    drawerItem[0] = new DrawerModel(R.drawable.profile, "Profile");
                    drawerItem[1] = new DrawerModel(R.drawable.availability, "Availability");
                    drawerItem[2] = new DrawerModel(R.drawable.disired, "Desired Tutor");
                    drawerItem[3] = new DrawerModel(R.drawable.payments, "Payments");
                    drawerItem[4] = new DrawerModel(R.drawable.rewards, "Rewards");
                    drawerItem[5] = new DrawerModel(R.drawable.history, "History");
                    drawerItem[6] = new DrawerModel(R.drawable.support_activated, "Support", true);
                    drawerItem[7] = new DrawerModel(R.drawable.signout, "Sign out");

                    adapter = new DrawerItemCustomAdapter(SettingFlyout.this, R.layout.customdrawerlayout, drawerItem);
                    mDrawerList.setAdapter(adapter);
                    break;


                case 7:

                    final Dialog dialog = new Dialog(SettingFlyout.this);
                    dialog.setContentView(R.layout.threedotprogressbar);
                    dialog.setCanceledOnTouchOutside(false);
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Your UI updates here
                            dialog.show();
                        }
                    });


                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                    String URL = "https://www.thetalklist.com/api/signout?uid=" + pref.getInt("id", 0);
                    StringRequest sr1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getInt("status") != 0) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                } else {


                                    pref = getSharedPreferences("loginStatus", MODE_PRIVATE);

                                    LoginManager.getInstance().logOut();
                                    SharedPreferences Desired_pref = getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE);
                                    SharedPreferences.Editor desiredEditor = Desired_pref.edit();
                                    desiredEditor.clear().apply();
                                    editor = pref.edit();
                                    editor.clear().apply();
                                    fragmentStack.clear();
                                    ttl.ExitBit = 1;
                                    SharedPreferences pref11 = getApplicationContext().getSharedPreferences("firstTime", MODE_PRIVATE);
                                    final SharedPreferences.Editor ed = pref11.edit();
                                    ed.clear().apply();

                                    edavailabe.clear().apply();
                                    edvideo.putInt("position", 0).apply();
                                    dialog.dismiss();

                                    try {
                                        FirebaseInstanceId.getInstance().deleteInstanceId();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "status " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(sr1);


                    break;


                default:
                    break;
            }
            if (fragment != null) {


                final Fragment finalFragment = fragment;
                Runnable mPendingRunnable = new Runnable() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void run() {
                        // update the main content by replacing fragments
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        fragmentTransaction.replace(R.id.viewpager, finalFragment);
                        TTL ttl = (TTL) getApplicationContext();
                        ttl.ExitBit = 1;

                        fragmentList = fragmentManager.getFragments();

                        if (fragmentStack.size() > 0) {
                            fragmentStack.pop();
                            fragmentStack.push(fragmentList.get(fragmentList.size() - 1));
                        }
                        fragmentStack.push(new Available_tutor());
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                };

                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                }

                drawer.closeDrawers();

                // refresh toolbar menu
                invalidateOptionsMenu();

                setTitle(mNavigationDrawerItemTitles[position]);

            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }
    }
}

