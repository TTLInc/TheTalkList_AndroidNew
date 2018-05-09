package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.ttl.project.thetalklist.Adapter.Biography_videoThumb_adapter;
import com.ttl.project.thetalklist.Services.LoginService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.ttl.project.thetalklist.R.array.sub;

public class Biography extends Fragment {

    ImageView rate_btn, biography_btn, video_btn, ratings_btn, biography_subject_btn, TutorImgBiography, videoPlay_VideoCallBtn;
    LinearLayout rate_11, biography_11, video_11, ratings_11, biography_subject_11, myBioLinearLayout, tutorSubLinearLayout, biography_review_layout,
            videoLinearLayout, rateLinearLayout, reviewLinearLayout;
    TextView biographyFirstName;
    Button biography_rate_edit;
    WebView biography_languages_webview;
    SharedPreferences preferences;


    LinearLayout review_root_biography;

    LinearLayout rate;

    View view;
    Biography.subjectHandler subHandler;
    Biography.VideoUrlHandler videoUrlHandler;


    int id;
    RequestQueue queue;
    RequestQueue queue1;
    RecyclerView biography_video_thum_recycle;
    int edit_bit;
    LinearLayout biography_biographyfrag_layout;
    RatingBar ratingBarbiography;

    Button biography_edit;
    TextView biography_personal, biography_educational, biography_professional, biography_rate_textview;
    EditText biography_personal_edit, biography_educational_edit, biography_professional_edit, biography_rate_edittext;


    //Exo player initialization

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayer";


    SimpleExoPlayer player;
    SimpleExoPlayerView playerView;

    ComponentListener componentListener;

    long PlayBackPosition;
    int CurrentWindow;
    boolean playWhenReady = true;

    //exo player over

    ImageView expanded_fullscreen;
    int uid;
    SharedPreferences loginpref;
    ImageView bioImage;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biography, container, false);

        preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        uid = preferences.getInt("id", 0);
        biography_btn = (ImageView) view.findViewById(R.id.biography_btn);
        rate_btn = (ImageView) view.findViewById(R.id.rate_btn);
        video_btn = (ImageView) view.findViewById(R.id.video_btn);
        ratings_btn = (ImageView) view.findViewById(R.id.ratings_btn);
        TutorImgBiography = (ImageView) view.findViewById(R.id.TutorImgBiography);
        videoPlay_VideoCallBtn = (ImageView) view.findViewById(R.id.videoPlay_VideoCallBtn);
        biography_subject_btn = (ImageView) view.findViewById(R.id.biography_subject_btn);
        biographyFirstName = (TextView) view.findViewById(R.id.biographyFirstName);
        biography_rate_edit = (Button) view.findViewById(R.id.biography_rate_edit);
        biography_rate_textview = (TextView) view.findViewById(R.id.biography_rate_textview);

        ratingBarbiography = (RatingBar) view.findViewById(R.id.ratingBarbiography);

        biography_video_thum_recycle = (RecyclerView) view.findViewById(R.id.biography_video_thum_recycle);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        componentListener = new ComponentListener();
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.exo_player_view);

        expanded_fullscreen = (ImageView) view.findViewById(R.id.expanded_fullscreen);


        biography_video_thum_recycle.setLayoutManager(layoutManager);

        new VideoUrlHandler().execute();


        loginpref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        bioImage = (ImageView) view.findViewById(R.id.bio_image);
        if (loginpref.getString("pic", "").equals("")) {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bioImage);
        } else {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + loginpref.getString("pic", ""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bioImage);
        }

        bioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        /*{
            String url = "http://www.thetalklist.com/api/biography_video?uid=" + preferences.getInt("id", 0);


            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("video response thumb", response);

                    try {
                        JSONObject resultObj = new JSONObject(response);

                        if (resultObj.getInt("status") == 0) {
                            JSONArray biography_video_ary = resultObj.getJSONArray("biography_video");
                            biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(getContext(), biography_video_ary, playerView));

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
            Volley.newRequestQueue(getContext()).add(sr);
        }*/
        setVideoIn();


        biography_professional = (TextView) view.findViewById(R.id.biography_professional);
        biography_personal = (TextView) view.findViewById(R.id.biography_personal);
        biography_educational = (TextView) view.findViewById(R.id.biography_educational);

        review_root_biography = (LinearLayout) view.findViewById(R.id.review_root_biography);
        biography_review_layout = (LinearLayout) view.findViewById(R.id.biography_review_layout);

        biography_professional_edit = (EditText) view.findViewById(R.id.biography_professional_edit);
        biography_personal_edit = (EditText) view.findViewById(R.id.biography_personal_edit);
        biography_educational_edit = (EditText) view.findViewById(R.id.biography_educational_edit);
        biography_rate_edittext = (EditText) view.findViewById(R.id.biography_rate_edittext);

        queue = Volley.newRequestQueue(getActivity());
        final LinearLayout biography_video = (LinearLayout) view.findViewById(R.id.biography_video);
        biography_biographyfrag_layout = (LinearLayout) view.findViewById(R.id.biography_biographyfrag_layout);
        rate = (LinearLayout) view.findViewById((R.id.rate));

        if (preferences.getInt("roleId", 0) == 0) {
            biography_review_layout.setVisibility(View.GONE);
        }
        if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {
            ((TextView) getActivity().findViewById(R.id.registration_line)).setText("Fill in bio to be seen by the world!");
            biography_review_layout.setVisibility(View.GONE);
        }
//            view.findViewById(R.id.biography_registration_finish).setVisibility(View.VISIBLE);
        if ((loginpref.getInt("roleId", 0) == 0 && getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.SettingFlyout")) || getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {
            view.findViewById(R.id.biography_registration_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/profile_complete?uid=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject resObj = new JSONObject(response);
                                JSONObject o = resObj.getJSONObject("result");
//                                JSONObject o = resAry.getJSONObject(0);

                                if (o.getInt("pic_upload") == 0 && o.getInt("vid_upload") == 0 && o.getInt("BioGraphy") == 0 && o.getInt("tutoring_subjects") == 0) {
                                    Toast.makeText(getContext(), "Please complete profile first", Toast.LENGTH_SHORT).show();
                                } else if (o.getInt("pic_upload") == 0) {
                                    Toast.makeText(getContext(), "Please add your photo.", Toast.LENGTH_SHORT).show();
//                                    TabBackStack.getInstance().setTabPosition(0);
//                                    getFragmentManager().beginTransaction().replace(R.id.registration_viewpager, new Tablayout_with_viewpager()).commit();
                                } else if (o.getInt("vid_upload") == 0) {
                                    biography_video.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Please upload video.", Toast.LENGTH_SHORT).show();
                                } else if (o.getInt("BioGraphy") == 0) {
                                    Toast.makeText(getContext(), "Please enter Biography.", Toast.LENGTH_SHORT).show();
                                } else if (o.getInt("tutoring_subjects") == 0) {
                                    Toast.makeText(getContext(), "Please enter Tutoring Subjects.", Toast.LENGTH_SHORT).show();
                                }  else if (o.getInt("pic_upload") == 1 && o.getInt("vid_upload") == 1 && o.getInt("BioGraphy") == 1 && o.getInt("tutoring_subjects")==1) {
                                    biography_biographyfrag_layout.setVisibility(View.VISIBLE);
                                    if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {
                                        getFragmentManager().beginTransaction().replace(R.id.registration_viewpager, new Availability_page_fragment()).commit();
                                    } else 
                                        getFragmentManager().beginTransaction().replace(R.id.viewpager, new Availability_page_fragment()).commit();
                                }


                            } catch (
                                    JSONException e)

                            {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }));

//                    getFragmentManager().beginTransaction().replace(R.id.registration_viewpager, new Availability_page_fragment()).commit();
                }
            });
        } else view.findViewById(R.id.biography_registration_finish).setVisibility(View.GONE);


        final FragmentStack fragmentStack = FragmentStack.getInstance();
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        videoPlay_VideoCallBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                VideoRecord videoRecord = new VideoRecord();

                TabBackStack.getInstance().setTabPosition(1);
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                fragmentStack.push(new Tablayout_with_viewpager());
                SharedPreferences bio_videoPref = getContext().getSharedPreferences("bio_video", Context.MODE_PRIVATE);
                SharedPreferences.Editor bio_Editor = bio_videoPref.edit();
                bio_Editor.putBoolean("biography", true).apply();
                if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {
                    fragmentTransaction.addToBackStack(getClass().toString());
                    fragmentTransaction.replace(R.id.registration_viewpager, videoRecord).commit();
                } else
                    fragmentTransaction.replace(R.id.viewpager, videoRecord).commit();
            }
        });

        biographyFirstName.setText(preferences.getString("usernm", ""));
        if (preferences.getFloat("hRate", 0.0f) != 0.0)

        {

            biography_rate_textview.setText(String.format("%.02f", preferences.getFloat("hRate", 0.00f) / 25.00f));
            biography_rate_edittext.setText(String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f));
        } else

        {
            biography_rate_textview.setText("0");
            biography_rate_edittext.setText("0");
        }


        if (preferences.getFloat("avgRate", 0.0f) != 0.0f)
            ratingBarbiography.setRating(preferences.getFloat("avgRate", 0.0f));


        biography_edit = (Button) view.findViewById(R.id.biography_edit);


        ratings_11 = (LinearLayout) view.findViewById(R.id.ratings_11);
        reviewLinearLayout = (LinearLayout) view.findViewById(R.id.reviewLinearLayout);
        final int height1 = review_root_biography.getHeight();
        reviewLinearLayout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (review_root_biography.getVisibility() == View.VISIBLE) {
                    review_root_biography.animate().translationY(0);
                    review_root_biography.setVisibility(View.GONE);
                    ratings_btn.setImageResource(R.drawable.side_aerrow);

                } else {
                    review_root_biography.animate().translationY(height1);
                    review_root_biography.setVisibility(View.VISIBLE);
                    ratings_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });

        {
            String URL = "http://www.thetalklist.com/api/reviews?uid=" + +getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
            Log.e("review url", URL);


            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("review response", response);
                        JSONObject res = new JSONObject(response);
                        if (res.getInt("status") == 0) {
                            JSONArray reviewAry = res.getJSONArray("review");
                            if (reviewAry.length() > 0) {
                                if (res.getInt("total_session") > 0)
                                    ((TextView) view.findViewById(R.id.biography_totalreview)).setText((String.valueOf(res.getInt("total_session"))));
                                else
                                    ((TextView) view.findViewById(R.id.biography_totalreview)).setText("0");
                                for (int i = 0; i < reviewAry.length(); i++) {

                                    JSONObject obj = (JSONObject) reviewAry.get(i);

                                    View convertView = LayoutInflater.from(getContext()).inflate(R.layout.available_tutor_expanded_ratings_feedback, null);

                                    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView9);
                                    Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + obj.getString("pic"))
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(imageView);
                                    TextView review_name = (TextView) convertView.findViewById(R.id.review_name);
                                    review_name.setText(obj.getString("firstName"));
                                    TextView review_rate = (TextView) convertView.findViewById(R.id.review_rate);
                                    review_rate.setText(obj.getString("msg"));

                                    RatingBar ratingBar1 = (RatingBar) convertView.findViewById(R.id.ratingBar1);

                                    ratingBar1.setRating(Float.parseFloat(obj.getString("clearReception")));

                                    String date = obj.getString("create_at");
                                    Date date_txt = null;
                                    String[] months = {"Jan", "Feb", "Mar", "April", "may", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

                                    if (date != null) {
                                        date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);
                                        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date_txt));
                                        int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date_txt));
                                        int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(date_txt));

                                        TextView biography_date_review = (TextView) convertView.findViewById(R.id.biography_date_review);

                                        biography_date_review.setText(String.valueOf(day) + "-" + months[month - 1] + "-" + String.valueOf(year));
                                    }

                                    review_root_biography.addView(convertView);

                                }
                            } else {
                                ((TextView) view.findViewById(R.id.biography_totalreview)).setText("00");
                            }
                        } else {
                            ((TextView) view.findViewById(R.id.biography_totalreview)).setText("0");
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

        biography_rate_edit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (edit_bit == 0) {
                    edit_bit = 1;
                    biography_rate_edit.setText("SAVE...");
                    biography_rate_textview.setVisibility(View.GONE);
                    biography_rate_edittext.setVisibility(View.VISIBLE);
                } else {
                    edit_bit = 0;
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    biography_rate_edit.setText("EDIT...");
                    biography_rate_textview.setText(biography_rate_edittext.getText().toString());
                    biography_rate_textview.setVisibility(View.VISIBLE);
                    biography_rate_edittext.setVisibility(View.GONE);

                    if (biography_rate_edittext.getText().toString().equals(null))
                        biography_rate_edittext.setText("00");

                    String Url = "https://www.thetalklist.com/api/minute_rate?uid=" + preferences.getInt("id", 0) + "&rate=" + biography_rate_edittext.getText().toString();
                    StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("rate response ", response);

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("status") == 0) {

                                            if (biography_rate_edittext.getText().toString().equals(""))
                                                biography_rate_textview.setText("0.00");
                                            else {
                                                biography_rate_textview.setText(String.format("%.2f", Double.parseDouble(biography_rate_edittext.getText().toString())));
                                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Something went wrong... Please try again..!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "rate api error", Toast.LENGTH_SHORT).show();

                                }
                            });

                    Volley.newRequestQueue(getContext()).add(strRequest);
                }
            }
        });


        biography_edit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (edit_bit == 0) {
                    biography_edit.setText("SAVE...");
                    edit_bit = 1;
                    biography_personal.setVisibility(View.GONE);
                    biography_educational.setVisibility(View.GONE);
                    biography_professional.setVisibility(View.GONE);


                    biography_personal_edit.setVisibility(View.VISIBLE);
                    biography_educational_edit.setVisibility(View.VISIBLE);
                    biography_professional_edit.setVisibility(View.VISIBLE);

                } else {
                    edit_bit = 0;
                    biography_edit.setText("EDIT...");
                    biography_personal.setVisibility(View.VISIBLE);
                    biography_educational.setVisibility(View.VISIBLE);
                    biography_professional.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                    biography_personal_edit.setVisibility(View.GONE);
                    biography_educational_edit.setVisibility(View.GONE);
                    biography_professional_edit.setVisibility(View.GONE);


                    Log.e("id", String.valueOf(preferences.getInt("id", 0)));
                    Log.e("academic", biography_educational_edit.getText().toString());
                    Log.e("professional", biography_professional_edit.getText().toString());
                    Log.e("personal", biography_personal_edit.getText().toString());

                    final String personal_txt = biography_personal_edit.getText().toString();

                    final String educational_txt = biography_educational_edit.getText().toString();
                    final String professional_txt = biography_professional_edit.getText().toString();


                    RequestQueue queue = Volley.newRequestQueue(getContext());


                    String URL = "https://www.thetalklist.com/api/edit_biogrpy?id=" + preferences.getInt("id", 0) + "&academic=" + biography_educational_edit.getText().toString().replace(" ", "%20").replace("\n", "%0A") +
                            "&professional=" + biography_professional_edit.getText().toString().replace(" ", "%20").replace("\n", "%0A") + "&personal=" + biography_personal_edit.getText().toString().replace(" ", "%20").replace("\n", "%0A");

                    Log.e("bio url", URL);
                    /*StringRequest strRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getContext(), "Biography "+response, Toast.LENGTH_SHORT).show();
                                    biography_personal.setText(biography_personal_edit.getText().toString());
                                    biography_educational.setText(biography_educational_edit.getText().toString());
                                    biography_professional.setText(biography_professional_edit.getText().toString());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", String.valueOf(preferences.getInt("id", 0)));
                            params.put("academic", educational_txt);
                            params.put("professional", professional_txt);
                            params.put("personal", personal_txt);
                            return params;
                        }
                    };*/


                    JSONObject param = new JSONObject();
                    try {

                        param.put("id", String.valueOf(preferences.getInt("id", 0)));
                        param.put("academic", educational_txt);
                        param.put("professional", professional_txt);
                        param.put("personal", personal_txt);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, param, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            Toast.makeText(getContext(), "Biography "+response, Toast.LENGTH_SHORT).show();

                            Log.e("subject response ", response.toString());
                            try {
                                if (response.getInt("status") == 0) {
                                    if (!response.getString("personal").equals("")) {
                                        biography_personal.setText(response.getString("personal"));
                                        biography_personal_edit.setText(response.getString("personal"));
                                    } else {
                                        biography_personal.setText(" I like to…");
                                        biography_personal_edit.setHint(" I like to…");
                                        biography_personal_edit.setText(" I like to…");
                                    }
                                    if (!response.getString("academic").equals("")) {
                                        biography_educational.setText(response.getString("academic"));
                                        biography_educational_edit.setText(response.getString("academic"));
                                    } else {
                                        biography_educational.setText("I have attended school at... ");
                                        biography_educational_edit.setHint("I have attended school at... ");
                                        biography_educational_edit.setText("I have attended school at... ");
                                    }


                                    if (!response.getString("professional").equals("")) {
                                        biography_professional.setText(response.getString("professional"));
                                        biography_professional_edit.setText(response.getString("professional"));
                                    } else {
                                        biography_professional.setText("I have worked at…");
                                        biography_professional_edit.setHint("I have worked at…");
                                        biography_professional_edit.setText("I have worked at…");
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
                    queue.add(jsonObjectRequest);
//                    queue.add(strRequest);

                }
            }
        });


        queue1 = Volley.newRequestQueue(

                getContext());


        if (preferences.getString("pic", "").

                equals(""))

        {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TutorImgBiography);
        } else

        {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + preferences.getString("pic", ""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(TutorImgBiography);
        }

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        final int entry = 10;

        biography_11 = (LinearLayout) view.findViewById(R.id.biography_11);
        myBioLinearLayout = (LinearLayout) view.findViewById(R.id.myBioLinearLayout);
        rate_11 = (LinearLayout) view.findViewById(R.id.rate_11);
        biography_11 = (LinearLayout) view.findViewById(R.id.biography_11);
        myBioLinearLayout = (LinearLayout) view.findViewById(R.id.myBioLinearLayout);
        rateLinearLayout = (LinearLayout) view.findViewById(R.id.rateLinearLayout);
        tutorSubLinearLayout = (LinearLayout) view.findViewById(R.id.tutorSubLinearLayout);
        videoLinearLayout = (LinearLayout) view.findViewById(R.id.videoLinearLayout);
        video_11 = (LinearLayout) view.findViewById(R.id.video_11);
        ratings_11 = (LinearLayout) view.findViewById(R.id.ratings_11);
        biography_subject_11 = (LinearLayout) view.findViewById(R.id.biography_subject_11);


//        biography_languages = (TextView) view.findViewById(R.id.biography_languages);

        biography_languages_webview = (WebView) view.findViewById(R.id.biography_languages_webview);
        biography_languages_webview.setHorizontalScrollbarOverlay(false);


        final int height = biography_biographyfrag_layout.getHeight();


        if (biography_biographyfrag_layout.getVisibility() == View.VISIBLE)

        {
            biography_btn.setImageResource(R.drawable.down_aerrow);

        }


        if (biography_biographyfrag_layout.getVisibility() == View.VISIBLE)

        {
            biography_btn.setImageResource(R.drawable.down_aerrow);

        }


          /*  @Override
            public void onClick (View v){


            if (biography_biographyfrag_layout.getVisibility() == View.VISIBLE) {
                biography_biographyfrag_layout.animate().translationY(0);
                biography_biographyfrag_layout.setVisibility(View.GONE);
                biography_btn.setImageResource(R.drawable.side_aerrow);*/


        myBioLinearLayout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (biography_biographyfrag_layout.getVisibility() == View.VISIBLE) {
                    biography_biographyfrag_layout.animate().translationY(0);
                    biography_biographyfrag_layout.setVisibility(View.GONE);
                    biography_btn.setImageResource(R.drawable.side_aerrow);

                } else {
                    biography_biographyfrag_layout.animate().translationY(height);
                    biography_biographyfrag_layout.setVisibility(View.VISIBLE);
                    biography_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });

        // Adding > button functionality in Tutoring Rate
        rateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rate.getVisibility() == View.VISIBLE) {
                    rate.animate().translationY(0);
                    rate.setVisibility(View.GONE);
                    rate_btn.setImageResource(R.drawable.side_aerrow);
                } else {
                    rate.animate().translationY(height);
                    rate.setVisibility(View.VISIBLE);
                    rate_btn.setImageResource(R.drawable.down_aerrow);
                }
            }
        });

        videoLinearLayout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                if (biography_video.getVisibility() == View.VISIBLE) {
                    biography_video.animate().translationY(0);
                    biography_video.setVisibility(View.GONE);
                    video_btn.setImageResource(R.drawable.side_aerrow);

                } else {
//                    biography_video.animate().translationY(height);
                    biography_video.setVisibility(View.VISIBLE);
                    video_btn.setImageResource(R.drawable.down_aerrow);
                }


            }
        });


        tutorSubLinearLayout.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                TabBackStack.getInstance().setTabPosition(1);
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                fragmentStack.push(new Tablayout_with_viewpager());
                FragmentTransaction t = fragmentManager.beginTransaction();
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());


                if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {

                    t.replace(R.id.registration_viewpager, new Biography_subject_Fragment()).commit();
                } else
                    t.replace(R.id.viewpager, new Biography_subject_Fragment()).commit();
            }
        });
        id =

                getContext().

                        getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);

        biography_subject_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                FragmentTransaction t = fragmentManager.beginTransaction();
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());
                if (getActivity().getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {

                    t.replace(R.id.registration_viewpager, new Biography_subject_Fragment()).commit();
                } else
                    t.replace(R.id.viewpager, new Biography_subject_Fragment()).commit();
            }
        });

               /* @Override
                public void onClick (View v){


                }
            });*/
        id =
                getContext().
                        getSharedPreferences("loginStatus", Context.MODE_PRIVATE).
                        getInt("userId", 0);
        return view;
    }


    //call api for video thumbnails ans set it to recycler view
    public void setVideoIn() {
        String url = "http://www.thetalklist.com/api/biography_video?uid=" + uid;


        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("video response thumb", response);

                try {
                    JSONObject resultObj = new JSONObject(response);

                    if (resultObj.getInt("status") == 0) {
                        JSONArray biography_video_ary = resultObj.getJSONArray("biography_video");
                        biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(getContext(), biography_video_ary, playerView, biography_video_thum_recycle));

                    }else if(resultObj.getInt("status") == 0 && resultObj.getString("message").equals("No Video Available.")){
                        biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(getContext(), new JSONArray(), playerView, biography_video_thum_recycle));
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(sr);
    }


    //call api to get details of biography
    private class subjectHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
            Log.e("subjects url", URL);

            final String htmlText = " %s ";
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("subjects tutor expanded", response);
                        if (jsonObject.getInt("status") == 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String nativeLang = obj.getString("tutoring_subjects");


                            if (!obj.getString("personal").equals("")) {
                                biography_personal.setText(obj.getString("personal"));
                                biography_personal_edit.setText(obj.getString("personal"));
                            } else {
                                biography_personal.setText(" I like to…");
                                biography_personal_edit.setHint(" I like to…");
                                biography_personal_edit.setText(" I like to…");
                            }
                            if (!obj.getString("academic").equals("")) {
                                biography_educational.setText(obj.getString("academic"));
                                biography_educational_edit.setText(obj.getString("academic"));
                            } else {
                                biography_educational.setText("I attend school at…");
                                biography_educational_edit.setHint("I attend school at…");
                                biography_educational_edit.setText("I attend school at…");
                            }


                            if (!obj.getString("professional").equals("")) {
                                biography_professional.setText(obj.getString("professional"));
                                biography_professional_edit.setText(obj.getString("professional"));
                            } else {
                                biography_professional.setText("I have worked at…");
                                biography_professional_edit.setHint("I have worked at…");
                                biography_professional_edit.setText("I have worked at…");
                            }

                            if (!nativeLang.equals("")) {
                                String sub = "";
                                JSONArray ar = new JSONArray(nativeLang);
                                for (int i = 0; i < ar.length(); i++) {
                                    if (sub.equals("")) {
                                        sub = ar.getString(i);
                                    } else {
                                        sub = sub + "," + ar.getString(i);
                                    }
                                }
//                                biography_languages.setText(sub);


                                biography_languages_webview.loadData(String.format(htmlText, "" +
                                        "<html><head><style type=\\\"text/css\\\">  " +
                                        "@font-face {  " +
                                        "font-family: MyFont;      " +
                                        "src: url(\\\"file:///android_asset/fonts/GothamBookRegular.ttf\\\")  }    " +
                                        "body { font-family: MyFont; color: #616A6B;  font-size: 12px;  text-align: justify;   }   " +
                                        "</style> " +
                                        "</head>\n" +
                                        "\t<body >"/*style=\"text-align:justify; font-size: 13px;\"*/ +
                                        "\t <font color='#616A6B'>" + sub + "</font>\n" +
                                        "\t </body>\n" +
                                        "</Html>"), "text/html", "utf-8");
//                            } else biography_languages.setText("");
                            } else
                                biography_languages_webview.loadData(String.format(htmlText, ""), "text/html", "utf-8");
//                            view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        biography_languages_webview.loadData(String.format(htmlText, "<html><head><style type=\\\"text/css\\\">  " +
                                "@font-face {  " +
                                "font-family: MyFont;      " +
                                "src: url(\\\"file:///android_asset/fonts/GothamBookRegular.ttf\\\")  }    " +
                                "body { font-family: MyFont; color: #616A6B; font-size: 12px;  text-align: justify;   }   " +
                                "</style> " +
                                "</head>\n" +
                                "\t<body >"/*style=\"text-align:justify; font-size: 13px;\"*//*>\n"*/ +
                                "\t <font color='#616A6B'>" + sub + "</font>\n" +
                                "\t </body>\n" +
                                "</Html>"), "text/html", "utf-8");

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            Volley.newRequestQueue(getContext()).add(sr);
            return null;
        }
    }

    // Method calls when fragment is in foreground
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ReleasePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + id;
        Log.e("subjects url", URL);


//        loginService();

        if (loginpref.getString("pic", "").equals("")) {
            Glide.with(getContext()).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bioImage);
        } else {
            Glide.with(getContext()).load("https://www.thetalklist.com/uploads/images/" + loginpref.getString("pic", ""))
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(bioImage);
        }

        final String htmlText = " %s ";
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("subjects tutor expanded", response);
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String nativeLang = obj.getString("tutoring_subjects");


                        if (!obj.getString("personal").equals("")) {
                            biography_personal.setText(obj.getString("personal"));
                            biography_personal_edit.setText(obj.getString("personal"));
                        } else {
                            biography_personal.setText(" I like to…");
                            biography_personal_edit.setHint(" I like to…");
                            biography_personal_edit.setText(" I like to…");
                        }
                        if (!obj.getString("academic").equals("")) {
                            biography_educational.setText(obj.getString("academic"));
                            biography_educational_edit.setText(obj.getString("academic"));
                        } else {
                            biography_educational.setText("I attend school at…");
                            biography_educational_edit.setHint("I attend school at…");
                            biography_educational_edit.setText("I attend school at…");
                        }


                        if (!obj.getString("professional").equals("")) {
                            biography_professional.setText(obj.getString("professional"));
                            biography_professional_edit.setText(obj.getString("professional"));
                        } else {
                            biography_professional.setText("I have worked at…");
                            biography_professional_edit.setHint("I have worked at…");
                            biography_professional_edit.setText("I have worked at…");
                        }


                        if (!nativeLang.equals("")) {
                            String sub = "";
                            JSONArray ar = new JSONArray(nativeLang);
                            for (int i = 0; i < ar.length(); i++) {
                                if (sub.equals("")) {
                                    sub = ar.getString(i);
                                } else {
                                    sub = sub + ", " + ar.getString(i);
                                }
                            }
                            biography_languages_webview.loadData(String.format(htmlText, "<html><head><style type=\\\"text/css\\\">  @font-face {  font-family: MyFont;      src: url(\\\"file:///android_asset/fonts/GothamBookRegular.ttf\\\")  }    body { font-family: MyFont; color: #616A6B; font-size: 12px;  text-align: justify;   }   </style> </head>\n" +
                                    "\t<body >"/*style=\"text-align:justify; font-size: 13px;\">\n" */ +
                                    "\t <font color='#616A6B'>" + sub + "</font>\n" +
                                    "\t </body>\n" +
                                    "</Html>"), "text/html", "utf-8");
//                            } else biography_languages.setText("");
                        } else
                            biography_languages_webview.loadData(String.format(htmlText, ""), "text/html", "utf-8");
//                            view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    biography_languages_webview.loadData(String.format(htmlText, "<html><head><style type=\\\"text/css\\\">  @font-face {  font-family: MyFont;      src: url(\\\"file:///android_asset/fonts/GothamBookRegular.ttf\\\")  }    body { font-family: MyFont; color: #616A6B;  font-size: 12px;  text-align: justify;   }   </style> </head>\n" +
                            "\t<body >"/*style=\"text-align:justify; font-size: 13px;\">\n"*/ +
                            "\t <font color='#616A6B'>" + sub + "</font>\n" +
                            "\t </body>\n" +
                            "</Html>"), "text/html", "utf-8");


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(sr);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (subHandler != null)
            subHandler.cancel(true);
        if (videoUrlHandler != null)
            videoUrlHandler.cancel(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ReleasePlayer();
    }


    //Initialize Exoplayer
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
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(link),
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource, true, false);
    }


    //Release Exoplayer
    private void ReleasePlayer() {
        if (player != null) {
            PlayBackPosition = player.getCurrentPosition();
            CurrentWindow = player.getCurrentWindowIndex();
            playWhenReady = false;
            player.removeListener(componentListener);
            player.setVideoListener(null);
            player.setVideoDebugListener(null);
            player.setAudioDebugListener(null);
            player.release();
            player = null;


        }
    }


    // Exoplayer's component Listener
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


    //call api to get video url
    private class VideoUrlHandler extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_video?tutor_id=" + preferences.getInt("userId", 0);


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

                                final SharedPreferences bio_vid_url = getContext().getSharedPreferences("biography_video", Context.MODE_PRIVATE);
                                SharedPreferences.Editor bio_edit = bio_vid_url.edit();

                                bio_edit.putString("videourl", link).apply();

                                InitializePLayer(link);
                                final String finalLink = link;
                                expanded_fullscreen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getContext(), Exoplayer_fullscreen.class);
//                                        Toast.makeText(getContext(), player.getCurrentTrackGroups().get(0).toString(), Toast.LENGTH_SHORT).show();
                                        i.putExtra("fullscreen_video_url", bio_vid_url.getString("videourl", ""));
                                        i.putExtra("position", playerView.getPlayer().getCurrentPosition());
                                        startActivity(i);
                                    }
                                });

                            } else {
                                ImageView exo_player_view_placeholder = (ImageView) view.findViewById(R.id.exo_player_view_placeholder);
                                playerView.setVisibility(View.GONE);
                                exo_player_view_placeholder.setVisibility(View.VISIBLE);

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
            Volley.newRequestQueue(getContext()).add(sr);

            return null;
        }
    }

    String userChoosenTask;
    final int CAMERA_REQUEST = 1323;
    final int GALLERY_REQUEST = 1342;
    final int CROP_REQUEST = 1352;


    //Method calls to get image from camera or gallary.
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    //If camera is selected
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    //if gallary is selected
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAMERA_REQUEST)
                onCaptureImageResult(data);
            else if (requestCode == CROP_REQUEST) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");


                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                roundedBitmapDrawable.setCornerRadius(80.0f);
                roundedBitmapDrawable.setAntiAlias(true);

                Glide.with(getContext()).load(roundedBitmapDrawable).into(bioImage);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                assert imageBitmap != null;
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                uploadImage(encodedImageString, imageBitmap, getContext(), pref.getInt("id", 0));

            }

        }
    }

    //captured image from camera's result
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
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
//        imageView1.setImageBitmap(thumbnail);
//        Bitmap bm = null;

       /* if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
//        imageView1.setImageBitmap(bm);

        Bitmap bb = getResizedBitmap(thumbnail, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();


        Intent ui = new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap", byteArray);
        startActivity(ui);

//        galleryIntent();
    }

    //Image upload method
    public void uploadImage(final String encodedImageString, final Bitmap bitmap, final Context context, final int id) {


        String uploadURL = "https://www.thetalklist.com/api/profile_pic"/*?uid=17430"&image="+encodedImageString*/;
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

                Toast.makeText(getContext(), "response" + s, Toast.LENGTH_SHORT).show();
                LoginService loginService = new LoginService();
                loginService.login(context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("email", ""), context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("pass", ""), context);

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

    //Gallary selected image result.
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        imageView1.setImageBitmap(bm);

        Bitmap bb = getResizedBitmap(bm, 500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bb.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();


/*
        Glide.with(getContext()).load(byteArray)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView1);*/
        Intent ui = new Intent(getApplicationContext(), Fragment_cropImage.class);
        ui.putExtra("bitmap", byteArray);
        startActivity(ui);

    }

    //to get bitmap resized as
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

    //Login api call
    public void loginService() {
        final SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
//                final String url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getInt("facebook_id", 0) + "&firstname=" + pref.getString("first_name", "") + "&lastname=" + pref.getString("last_name", "") + "&gender=" + pref.getString("gender", "") + "&birthdate=" + pref.getString("birthday", "");
        final SharedPreferences.Editor editor = pref.edit();

        if (loginpref.getString("LoginWay", "").equals("FacebookLogin")) {


            String url = "";
            if (pref.getInt("gender", 0) == 0)
                url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getString("facebook_id", "") + "&firstname=" + pref.getString("firstName", "") + "&lastname=" + pref.getString("lastName", "") + "&gender=female&birthdate=" + "";
            else
                url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getString("facebook_id", "") + "&firstname=" + pref.getString("firstName", "") + "&lastname=" + pref.getString("lastName", "") + "&gender=male&birthdate=" + "";
            //            final String url="https://www.thetalklist.com/api/fblogin?email="+email+"&facebook_id="+loginResult.getAccessToken().getUserId()+"&firstname="+first_name+"&lastname="+last_name+"&gender="+gender+"&birthdate="+"";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("status") == 0) {
                            JSONObject resObj = obj.getJSONObject("result");

                            final int roleId = resObj.getInt("roleId");
                            editor.putString("LoginWay", "FacebookLogin");
                            editor.putString("loginResponse", response);
                            editor.putString("email", resObj.getString("username"));
                            editor.putString("facebook_id", resObj.getString("facebook_id"));
                            editor.putInt("id", resObj.getInt("id"));
                            editor.putInt("gender", resObj.getInt("gender"));
                            editor.putInt("country", resObj.getInt("country"));
                            editor.putInt("province", resObj.getInt("province"));
                            editor.putString("cell", resObj.getString("cell"));
                            editor.putString("city", resObj.getString("city"));
                            editor.putFloat("hRate", Float.parseFloat(resObj.getString("hRate")));
                            if (resObj.getString("avgRate").equals(""))
                                editor.putFloat("avgRate", 0.0f);
                            else
                                editor.putFloat("avgRate", Float.parseFloat(resObj.getString("avgRate")));

                            if (resObj.getString("ttl_points").equals(""))
                                editor.putFloat("ttl_points", 0.0f);
                            else
                                editor.putFloat("ttl_points", Float.parseFloat(resObj.getString("ttl_points")));
                            editor.putString("nativeLanguage", resObj.getString("nativeLanguage"));
                            editor.putString("otherLanguage", resObj.getString("otherLanguage"));
                            editor.putInt("roleId", roleId);
                            editor.putInt("status", 0);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "Login Sucessfully..!", Toast.LENGTH_SHORT).show();
                            SettingFlyout settingFlyout = new SettingFlyout();
                            Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "Login Unsucessful..!", Toast.LENGTH_SHORT).show();
                    Log.e("fb login error", volleyError.toString());
                    editor.clear().apply();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(stringRequest);
        } else {

            String URL = "https://www.thetalklist.com/api/login?email=" + loginpref.getString("email", "") + "&password=" + loginpref.getString("pass", "");
            ;

            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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


                            String pic = resultObj.getString("pic");
                            if (!pic.equals("")) {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + pic)
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(bioImage);
                            } else {
                                Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                        .crossFade()
                                        .thumbnail(0.5f)
                                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(bioImage);
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
            Volley.newRequestQueue(getContext()).add(sr);
        }

    }
}



