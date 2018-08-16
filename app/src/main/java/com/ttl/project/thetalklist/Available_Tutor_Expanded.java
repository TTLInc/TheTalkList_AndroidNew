package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.ttl.project.thetalklist.model.TutorInformationModel;
import com.ttl.project.thetalklist.retrofit.ApiClient;
import com.ttl.project.thetalklist.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.blogc.android.views.ExpandableTextView;
import retrofit2.Call;
import retrofit2.Callback;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 04/04/2017.
 */

public class Available_Tutor_Expanded extends Fragment {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayer";
    final FragmentStack fragmentStack = FragmentStack.getInstance();
    android.support.v7.widget.Toolbar toolbar;
    ImageButton msgBtn, videoBtn;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ImageView tutorImage;
    String firstName;
    LinearLayout review_root_biography, personalLinearLayout, eduLinearLayout, proLinearLayout,
            ratingLinearLayout;
    ExpandableTextView expandableTextView;
    ExpandableTextView expandableTextViewedu;
    ExpandableTextView expandableTextViewpro;
    ExpandableTextView TutorExpanded_review;
    Button buttonToggle;
    Button buttonToggleedu;
    Button buttonTogglepro;
    Button morelist;
    ImageView minus;
    View view1;
    WebView TutorExpanded_tutorin_languages_webview;
    int roleId, roleIdUser, getTutorId;
    String pic, hRate, avgRate;
    int tutorId;
    View convertView;
    TextView firstNameTV;
    TextView availableTutorListCPS;
    TextView rating_textview;
    //Exo player initialization
    RatingBar ratingBar;
    ImageView expanded_fullscreen;
    SimpleExoPlayer player;
    SimpleExoPlayerView playerView;


    ComponentListener componentListener;

    long PlayBackPosition;
    int CurrentWindow;
    boolean playWhenReady = true;
    SharedPreferences preferences1, preferences;
    String mTutorId;
    int mRedyTotalk;
    //exo player over
    int playing = 0;
    subjectHandler subHandler;
    VideoUrlHandler videoUrlHandler;
    ApiInterface apiService;
    String mReadyTotalk = "0";


    @Override
    public void onStart() {

        super.onStart();
        try {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                mTutorId = bundle.getString("Tutorid");
                mRedyTotalk = bundle.getInt("redyTotalk");
                Log.e(TAG, "mTutorId-->: " + mTutorId);

            }
        } catch (Exception e) {

        }
        getSharedPreferencesData();
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TutorInformationModel> modelCall = apiService.getInformation(String.valueOf(tutorId));
        modelCall.enqueue(new Callback<TutorInformationModel>() {
            @Override
            public void onResponse(Call<TutorInformationModel> call, retrofit2.Response<TutorInformationModel> response) {
                mReadyTotalk = response.body().getTutor().get(0).getReadytotalk();
                Log.e(TAG, "mReadyTotalk" + mReadyTotalk);
                if (response.body().getTutor().get(0).getReadytotalk().equals("0")) {
                    videoBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.disabled_video));
                } else {
                    videoBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.video));
                }
                Log.e(TAG, "onResponse: ");


            }

            @Override
            public void onFailure(Call<TutorInformationModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);

            }
        });
    }

    private void getSharedPreferencesData() {
        preferences1 = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

        roleIdUser = preferences1.getInt("roleId", 0);

        preferences = getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
        SharedPreferences preferences = getContext().getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
        firstName = preferences.getString("tutorName", "");
        roleId = preferences.getInt("tutorRoleId", 0);
        pic = preferences.getString("tutorPic", "");
        tutorId = preferences.getInt("tutorid", 0);
        hRate = preferences.getString("hRate", "");
        avgRate = preferences.getString("avgRate", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convertView = inflater.inflate(R.layout.available_tutor_expanded, container, false);
        preferences1 = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

        roleIdUser = preferences1.getInt("roleId", 0);
        //  getActivity.((ImageView) convertView.findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));

        preferences = getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
        SharedPreferences preferences = getContext().getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
        firstName = preferences.getString("tutorName", "");
        roleId = preferences.getInt("tutorRoleId", 0);
        pic = preferences.getString("tutorPic", "");
        tutorId = preferences.getInt("tutorid", 0);
        hRate = preferences.getString("hRate", "");
        avgRate = preferences.getString("avgRate", "");
        hideProgessbar();

        Log.e("pic tutor expanded", pic);

      /*  Bundle args = this.getArguments();
        String Flag = args.getString("Flag");
        Log.e(TAG, "ImageId " + Flag);*/
        componentListener = new ComponentListener();
        playerView = (SimpleExoPlayerView) convertView.findViewById(R.id.exo_player_view);


//        availableTutorListType = (TextView) convertView.findViewById(R.id.availableTutorListType);
//        TutorTypeImg = (ImageView) convertView.findViewById(R.id.TutorTypeImg);
       /* toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        View view = toolbar.getRootView();*/
//        view.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
//        view.findViewById(R.id.expandableToolbar).setVisibility(View.VISIBLE);
        firstNameTV = (TextView) convertView.findViewById(R.id.expandedToolbartutorName);
        review_root_biography = (LinearLayout) convertView.findViewById(R.id.review_root_expanded);

        expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_personal);
        expandableTextViewedu = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_educational);
        expandableTextViewpro = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_professional);
        // TutorExpanded_review = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_review);
//        final ExpandableTextView expandableTextViewpro = (ExpandableTextView) convertView.findViewById(R.id.TutorExpanded_professional);

        buttonToggle = (Button) convertView.findViewById(R.id.button_toggle);
        buttonToggleedu = (Button) convertView.findViewById(R.id.moreedu);
        buttonTogglepro = (Button) convertView.findViewById(R.id.moreprof);
        expanded_fullscreen = (ImageView) convertView.findViewById(R.id.expanded_fullscreen);

        personalLinearLayout = (LinearLayout) convertView.findViewById(R.id.personalLinearLayout);
        eduLinearLayout = (LinearLayout) convertView.findViewById(R.id.eduLinearLayout);
        proLinearLayout = (LinearLayout) convertView.findViewById(R.id.proLinearLayout);
        ratingLinearLayout = (LinearLayout) convertView.findViewById(R.id.ratingLinearLayout);
//        listView = (ListView) convertView.findViewById(R.id.ratingfeedbacklist);
//        videoView = (VideoView) convertView.findViewById(R.id.TutorExpanded_biography_videoView);
        availableTutorListCPS = (TextView) convertView.findViewById(R.id.availableTutorListCPS);


      /*  personalLinearLayout = (LinearLayout) convertView.findViewById(R.id.personalLinearLayout);
        eduLinearLayout = (LinearLayout) convertView.findViewById(R.id.eduLinearLayout);
        proLinearLayout = (LinearLayout) convertView.findViewById(R.id.proLinearLayout);
        ratingLinearLayout = (LinearLayout) convertView.findViewById(R.id.rateLinearLayout);*/


        msgBtn = (ImageButton) convertView.findViewById(R.id.imageButton3);
        videoBtn = (ImageButton) convertView.findViewById(R.id.imageButton6);
        tutorImage = (ImageView) convertView.findViewById(R.id.TutorImg);
        minus = (ImageView) convertView.findViewById(R.id.TutorExpanded_review_minus);
        morelist = (Button) convertView.findViewById(R.id.moreList);
//        controlLayout = (LinearLayout) convertView.findViewById(R.id.controlLayout);
        ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        //TutorExpanded_review_ratingBar1 = (RatingBar) convertView.findViewById(R.id.TutorExpanded_review_ratingBar1);
        rating_textview = (TextView) convertView.findViewById(R.id.rating_textview);
        if (avgRate.equalsIgnoreCase("")) {
            ratingBar.setRating(0f);
            //TutorExpanded_review_ratingBar1.setRating(0f);
            rating_textview.setText(0f + "");
        } else {
            Float rate = Float.parseFloat(avgRate);
//            Toast.makeText(getContext(), "rate "+rate, Toast.LENGTH_SHORT).show();
            ratingBar.setRating(rate);
            //TutorExpanded_review_ratingBar1.setRating(rate);
            String test = String.format("%.01f", rate);
            rating_textview.setText(test + "");
        }

//        TutorExpanded_tutorin_languages = (TextView) convertView.findViewById(R.id.TutorExpanded_tutorin_languages);
        TutorExpanded_tutorin_languages_webview = (WebView) convertView.findViewById(R.id.TutorExpanded_tutorin_languages_webview);
        TutorExpanded_tutorin_languages_webview.setHorizontalScrollbarOverlay(false);

      /*  proLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextViewpro.toggle();
                buttonTogglepro.setText(expandableTextViewpro.isExpanded() ? "more..." : "Less...");
            }
        });*/
     /*   try {
            if (mTutorId.equals("0")) {
                Log.e(TAG, "disable ");
                videoBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.disabled_video));
            } else {
                Log.e(TAG, "Enable");
                videoBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.video));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

      /*  expandableTextViewpro.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView expanded");
            }
            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d("Expand", "ExpandableTextView collapsed");
            }
        });
        expandableTextViewpro.setAnimationDuration(1000L);
//         set interpolators for both expanding and collapsing animations
        expandableTextViewpro.setInterpolator(new OvershootInterpolator());
// or set them separately
        expandableTextViewpro.setExpandInterpolator(new OvershootInterpolator());
        expandableTextViewpro.setCollapseInterpolator(new OvershootInterpolator());
*/

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        firstNameTV.setText(firstName);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        final String h_str = String.format("%.02f", Float.parseFloat(hRate) / 25);
        availableTutorListCPS.setText(h_str);
        if (!pic.equals("")) {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                    .crossFade()
                    .centerCrop()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tutorImage);
        } else {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .centerCrop()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(tutorImage);
        }

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor chatPrefEditor = chatPref.edit();
                chatPrefEditor.putString("firstName", firstName);
                chatPrefEditor.putInt("receiverId", tutorId).apply();

                fragmentStack.push(new Available_Tutor_Expanded());
                MessageFragment messageList = new MessageFragment();
                fragmentTransaction.replace(R.id.viewpager, messageList).commit();
            }
        });

        if (tutorId == 0) {
            Log.e(TAG, "onResume: " + mReadyTotalk);
        } else {
            videoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToolbar();

                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    View view1 = inflater.inflate(R.layout.talknow_confirmation_layout, null);
                    final PopupWindow popupWindow = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
                    popupWindow.showAtLocation(convertView, Gravity.CENTER, 0, 0);
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(false);

                    final View view2 = inflater.inflate(R.layout.talknow_insufficient_layout, null);
                    final PopupWindow popupWindow1 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


                    TextView confirmation_tutorCredits = (TextView) view1.findViewById(R.id.confirmation_tutorCredits);
                    TextView confirmation_tutorName = (TextView) view1.findViewById(R.id.confirmation_tutorName);

                    confirmation_tutorName.setText(firstName);
                    confirmation_tutorCredits.setText(h_str);


                    Button yesbtn = (Button) view1.findViewById(R.id.yesbtn);
                    final Button nobtn = (Button) view1.findViewById(R.id.nobtn);

                    yesbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString("tutorName", firstName);
                            editor.putInt("flag", 1);
                            SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                            editor.putInt("studentId", pref.getInt("id", 0));
                            editor.putString("tutorName", firstName);
                            editor.putInt("tutorId", tutorId);
                            editor.putFloat("hRate", Float.parseFloat(availableTutorListCPS.getText().toString()));
                            editor.putFloat("credit", pref.getFloat("money", 0.0f)).apply();

                            popupWindow.dismiss();
                            if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) <= getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE).getFloat("hRate", 0.0f)) {

                                popupWindow1.showAtLocation(convertView, Gravity.CENTER, 0, 0);
                                popupWindow1.setFocusable(true);
                                popupWindow1.setOutsideTouchable(false);


                                Button okbtn = (Button) view2.findViewById(R.id.okbtn);

                                okbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow1.dismiss();
                                        TTL ttl = (TTL) getApplicationContext();
                                        ttl.ExitBit = 2;
                                        startActivity(new Intent(getApplicationContext(), new StripePaymentActivity().getClass()));
                                    }
                                });
                            } else {
                                Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("from", "availabletutor");
                                getContext().startActivity(i);
                                getActivity().onBackPressed();
                            }


                        }
                    });
                    nobtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TTL ttl = (TTL) getApplicationContext();
                            ttl.ExitBit = 2;
                            popupWindow.dismiss();
                            getActivity().onBackPressed();
                        }
                    });


                }
            });
        }


        subHandler = (subjectHandler) new subjectHandler().execute();
        videoUrlHandler = (VideoUrlHandler) new VideoUrlHandler().execute();

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        morelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (morelist.getText().equals("MOREs...")) {

                    review_root_biography.setVisibility(View.VISIBLE);
                    morelist.setText("LESS...");
                } else {
                    review_root_biography.setVisibility(View.GONE);
                    morelist.setText("MOREs...");
                }


            }
        });

        {
            String URL = "http://www.thetalklist.com/api/reviews?uid=" + tutorId;
            Log.e("review url", URL);


            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("review response", response);
                        JSONObject res = new JSONObject(response);
                        if (res.getInt("status") == 0) {
                            JSONArray reviewAry = res.getJSONArray("review");

                            ((TextView) convertView.findViewById(R.id.TutorExpanded_review_count)).setText((String.valueOf(res.getInt("total_session"))));

                            for (int i = 0; i < reviewAry.length(); i++) {

                                JSONObject obj = (JSONObject) reviewAry.get(i);

                                view1 = LayoutInflater.from(getActivity()).inflate(R.layout.available_tutor_expanded_ratings_feedback, null);

                                ImageView imageView = (ImageView) view1.findViewById(R.id.imageView9);
                                Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + obj.getString("pic"))
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView);
                                TextView review_name = (TextView) view1.findViewById(R.id.review_name);
                                review_name.setText(obj.getString("firstName"));
                                TextView review_rate = (TextView) view1.findViewById(R.id.review_rate);
                                review_rate.setText(obj.getString("msg"));

                                RatingBar ratingBar1 = (RatingBar) view1.findViewById(R.id.ratingBar1);

                                ratingBar1.setRating(Float.parseFloat(obj.getString("clearReception")));

                                String date = obj.getString("create_at");
                                Date date_txt = null;
                                String[] months = {"Jan", "Feb", "Mar", "April", "may", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

                                if (date != null) {
                                    date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);
                                    int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date_txt));
                                    int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date_txt));
                                    int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(date_txt));

                                    TextView biography_date_review = (TextView) view1.findViewById(R.id.biography_date_review);

                                    biography_date_review.setText(String.valueOf(day) + "-" + months[month - 1] + "-" + String.valueOf(year));
                                }

                                review_root_biography.addView(view1);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getContext()).add(sr);
        }
        /*morelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (TutorExpanded_review.isExpanded()) {
                    TutorExpanded_review.collapse();
                    morelist.setText("MORE");
                } else {
                    TutorExpanded_review.expand();
                    morelist.setText("LESS");
                }
            }
        });*/
       /* TutorExpanded_review.setAnimationDuration(750L);
        // set interpolators for both expanding and collapsing animations
        TutorExpanded_review.setInterpolator(new OvershootInterpolator());
// or set them separately
        TutorExpanded_review.setExpandInterpolator(new OvershootInterpolator());
        TutorExpanded_review.setCollapseInterpolator(new OvershootInterpolator());
// toggle the ExpandableTextView
        morelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                morelist.setText(TutorExpanded_review.isExpanded() ? "MORE" : "LESS");
                TutorExpanded_review.toggle();
            }
        });
// but, you can also do the checks yourself
// listen for expand / collapse events
        TutorExpanded_review.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
            }
            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });*/


        expandableTextViewpro.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        expandableTextViewpro.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextViewpro.setExpandInterpolator(new OvershootInterpolator());
        expandableTextViewpro.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        buttonTogglepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                buttonTogglepro.setText(expandableTextViewpro.isExpanded() ? "MOREs..." : "LESS...");
                expandableTextViewpro.toggle();
            }
        });

// but, you can also do the checks yourself
        buttonTogglepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (expandableTextViewpro.isExpanded()) {
                    expandableTextViewpro.collapse();
                    buttonTogglepro.setText("MOREs...");
                } else {
                    expandableTextViewpro.expand();
                    buttonTogglepro.setText("LESS...");
                }
            }
        });

// listen for expand / collapse events
        expandableTextViewpro.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });
        expandableTextViewedu.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        expandableTextViewedu.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextViewedu.setExpandInterpolator(new OvershootInterpolator());
        expandableTextViewedu.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        buttonToggleedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                buttonToggleedu.setText(expandableTextViewedu.isExpanded() ? "MOREs..." : "LESS...");
                expandableTextViewedu.toggle();
            }
        });

// but, you can also do the checks yourself
        buttonToggleedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (expandableTextViewedu.isExpanded()) {
                    expandableTextViewedu.collapse();
                    buttonToggleedu.setText("MOREs...");
                } else {
                    expandableTextViewedu.expand();
                    buttonToggleedu.setText("LESS...");
                }
            }
        });

// listen for expand / collapse events
        expandableTextViewedu.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });
// set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        expandableTextView.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        expandableTextView.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableTextView.setExpandInterpolator(new OvershootInterpolator());
        expandableTextView.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                buttonToggle.setText(expandableTextView.isExpanded() ? "MOREs..." : "LESS...");
                expandableTextView.toggle();
            }
        });

// but, you can also do the checks yourself
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (expandableTextView.isExpanded()) {
                    expandableTextView.collapse();
                    buttonToggle.setText("MOREs...");
                } else {
                    expandableTextView.expand();
                    buttonToggle.setText("LESS...");
                }
            }
        });

// listen for expand / collapse events
        expandableTextView.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        super.onAttach(context);
    }

    //Initialize exoplayer
    private void InitializePLayer(String link) throws android.net.ParseException {

        if (player == null) {
            TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(factory), new DefaultLoadControl());
            player.addListener(componentListener);
            player.setAudioDebugListener(componentListener);
            player.setVideoDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(false);
            player.seekTo(CurrentWindow, PlayBackPosition);

        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"), BANDWIDTH_METER);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(/*"https://www.thetalklist.com/uploads/video/2015/05/26/757514f83fa063fa6d9732e54299de10.MOV"*/link),
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource, true, false);
    }

    //Release Exoplayer
    private void ReleasePlayer() {
        if (player != null) {
            PlayBackPosition = player.getCurrentPosition();
            CurrentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.setVideoListener(null);
            player.setVideoDebugListener(null);
            player.setAudioDebugListener(null);
            player.release();
            player = null;


        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        ReleasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        super.onDestroyView();
        toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void setToolbar() {
        View view = toolbar.getRootView();
        view.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();


        if (subHandler != null)
            subHandler.cancel(true);
        if (videoUrlHandler != null)
            videoUrlHandler.cancel(true);

    }

    private class ComponentListener implements ExoPlayer.EventListener, VideoRendererEventListener, AudioRendererEventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object o) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

        }

        @Override
        public void onLoadingChanged(boolean b) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE   -";
                    break;


                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING   -";
                    break;


                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY  -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED  -";
                    break;

                default:
                    stateString = "Unknown State  -";
                    break;
            }

            Log.e(TAG, "changed state to " + stateString + "PLay when ready " + playWhenReady);

        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onVideoDecoderInitialized(String s, long l, long l1) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int i, long l) {

        }

        @Override
        public void onVideoSizeChanged(int i, int i1, int i2, float v) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onAudioEnabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onAudioSessionId(int i) {

        }

        @Override
        public void onAudioDecoderInitialized(String s, long l, long l1) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioTrackUnderrun(int i, long l, long l1) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters decoderCounters) {

        }
    }

    public void hideProgessbar() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                convertView.findViewById(R.id.TutorExpanded_tutorin_languages_progress).setVisibility(View.GONE);
            }
        }, 7000);
    }

    private class subjectHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + tutorId;
            RequestQueue queue = Volley.newRequestQueue(getContext());

            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("subjects tutor expanded", response);
                        if (jsonObject.getInt("status") == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                            JSONObject obj = jsonArray.getJSONObject(0);

                            expandableTextView.setText(obj.getString("personal"));
                            expandableTextViewedu.setText(obj.getString("academic"));
                            expandableTextViewpro.setText(obj.getString("professional"));

                            final String htmlText = " %s ";


                            if (obj.getString("tutoring_subjects").equalsIgnoreCase("")) {
                                TutorExpanded_tutorin_languages_webview.setVisibility(View.VISIBLE);
                                TutorExpanded_tutorin_languages_webview.loadData(String.format(htmlText, ""), "text/html", "utf-8");
                                convertView.findViewById(R.id.TutorExpanded_tutorin_languages_progress).setVisibility(View.GONE);
                            } else {


                                String nativeLang = obj.getString("tutoring_subjects");
                                String sub = "";
                                JSONArray ar = new JSONArray(nativeLang);
                                for (int i = 0; i < ar.length(); i++) {
                                    if (sub.equals("")) {
                                        sub = ar.getString(i);
                                    } else {
                                        sub = sub + ", " + ar.getString(i);
                                    }
                                }
//                                TutorExpanded_tutorin_languages.setText(sub);
                                TutorExpanded_tutorin_languages_webview.setVisibility(View.VISIBLE);
                                TutorExpanded_tutorin_languages_webview.loadData(String.format(htmlText, "<html><head><style type=\\\"text/css\\\">  @font-face {  font-family: MyFont;      src: url(\\\"file:///android_asset/fonts/GothamBookRegular.ttf\\\")  }    body { font-family: MyFont; font-color:#616A6B;  font-size: 12px;  text-align: justify;   }   </style> </head>\n" +
                                        "\t<body >"/*style=\"text-align:justify; font-size: 13px;\"*/ +
                                        "\t <font color='#616A6B'>" + sub + "</font>\n" +
                                        "\t </body>\n" +
                                        "</Html>"), "text/html", "utf-8");
                                convertView.findViewById(R.id.TutorExpanded_tutorin_languages_progress).setVisibility(View.GONE);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(sr);
            return null;
        }
    }

    private class VideoUrlHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/tutoring_video?tutor_id=" + tutorId;
            RequestQueue queue1 = Volley.newRequestQueue(getContext());

            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("videourl tutor expanded", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("status") == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("video");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String link = obj.getString("vedio");
                            if (!link.equals("")) {
                                link = "https://www.thetalklist.com/uploads/video/" + link;


                                InitializePLayer(link);

                                final String finalLink = link;
                                expanded_fullscreen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getContext(), Exoplayer_fullscreen.class);
                                        i.putExtra("fullscreen_video_url", finalLink);
                                        i.putExtra("position", player.getCurrentPosition());
                                        startActivity(i);
                                    }
                                });


                            } else {
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "video not getting", Toast.LENGTH_SHORT).show();
                }
            });
            queue1.add(sr);
            return null;
        }
    }
}
