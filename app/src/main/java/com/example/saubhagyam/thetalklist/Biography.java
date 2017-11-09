package com.example.saubhagyam.thetalklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.saubhagyam.thetalklist.Adapter.Biography_videoThumb_adapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Biography extends Fragment {

    ImageView biography_btn, video_btn, ratings_btn, biography_subject_btn, TutorImgBiography,videoPlay_VideoCallBtn;
    LinearLayout biography_11, video_11, ratings_11,  biography_subject_11, myBioLinearLayout;
    TextView biographyFirstName;
    Button biography_rate_edit;
    TextView biography_languages;
//    ProgressBar biography_languages_progress;
    SharedPreferences preferences;


    LinearLayout review_root_biography;


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

//    ImageView expanded_fullscreen;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biography, container, false);

        preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        biography_btn = (ImageView) view.findViewById(R.id.biography_btn);
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

//        expanded_fullscreen= (ImageView) view.findViewById(R.id.expanded_fullscreen);


        biography_video_thum_recycle.setLayoutManager(layoutManager);

        new VideoUrlHandler().execute();
        {
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
        }


        biography_professional = (TextView) view.findViewById(R.id.biography_professional);
        biography_personal = (TextView) view.findViewById(R.id.biography_personal);
        biography_educational = (TextView) view.findViewById(R.id.biography_educational);

        review_root_biography = (LinearLayout) view.findViewById(R.id.review_root_biography);

        biography_professional_edit = (EditText) view.findViewById(R.id.biography_professional_edit);
        biography_personal_edit = (EditText) view.findViewById(R.id.biography_personal_edit);
        biography_educational_edit = (EditText) view.findViewById(R.id.biography_educational_edit);
        biography_rate_edittext = (EditText) view.findViewById(R.id.biography_rate_edittext);
        queue = Volley.newRequestQueue(getActivity());

        final FragmentStack fragmentStack = FragmentStack.getInstance();
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        videoPlay_VideoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoRecord videoRecord = new VideoRecord();
                fragmentStack.push(new Tablayout_with_viewpager());
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                fragmentTransaction.replace(R.id.viewpager, videoRecord).commit();
            }
        });

        biographyFirstName.setText(preferences.getString("usernm", ""));
        if (preferences.getFloat("hRate", 0.0f) != 0.0) {

            biography_rate_textview.setText(String.format("%.02f",preferences.getFloat("hRate", 0.0f) / 25.0f));
            biography_rate_edittext.setText(String.valueOf(preferences.getFloat("hRate", 0.0f) / 25.0f));
        } else {
            biography_rate_textview.setText("0");
            biography_rate_edittext.setText("0");
        }


        if (preferences.getFloat("avgRate", 0.0f) != 0.0f)
            ratingBarbiography.setRating(preferences.getFloat("avgRate", 0.0f));


        biography_edit = (Button) view.findViewById(R.id.biography_edit);


        ratings_11 = (LinearLayout) view.findViewById(R.id.ratings_11);
        final int height1 = review_root_biography.getHeight();
        ratings_11.setOnClickListener(new View.OnClickListener() {
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
                                ((TextView) view.findViewById(R.id.biography_totalreview)).setText((String.valueOf(reviewAry.length())));

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
                            }else {
                                ((TextView) view.findViewById(R.id.biography_totalreview)).setText("00");
                            }
                        }
                        else {
                            ((TextView) view.findViewById(R.id.biography_totalreview)).setText("00");
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

        biography_rate_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_bit == 0) {
                    edit_bit = 1;
                    biography_rate_edit.setText("save...");
                    biography_rate_textview.setVisibility(View.GONE);
                    biography_rate_edittext.setVisibility(View.VISIBLE);
                } else {
                    edit_bit = 0;
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                                                biography_rate_textview.setText(String.format("%.02f", Float.parseFloat(biography_rate_edittext.getText().toString())));
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

        biography_biographyfrag_layout = (LinearLayout) view.findViewById(R.id.biography_biographyfrag_layout);

        biography_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_bit == 0) {
                    biography_edit.setText("Save...");
                    edit_bit = 1;
                    biography_personal.setVisibility(View.GONE);
                    biography_educational.setVisibility(View.GONE);
                    biography_professional.setVisibility(View.GONE);


                    biography_personal_edit.setVisibility(View.VISIBLE);
                    biography_educational_edit.setVisibility(View.VISIBLE);
                    biography_professional_edit.setVisibility(View.VISIBLE);

                } else  {
                    edit_bit = 0;
                    biography_edit.setText("Edit...");
                    biography_personal.setVisibility(View.VISIBLE);
                    biography_educational.setVisibility(View.VISIBLE);
                    biography_professional.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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



                    String URL = "https://www.thetalklist.com/api/edit_biogrpy?id="+preferences.getInt("id", 0)+"&academic="+biography_educational_edit.getText().toString().replace(" ","%20").replace("\n","%0A")+
                            "&professional="+biography_professional_edit.getText().toString().replace(" ","%20").replace("\n","%0A")+"&personal="+biography_personal_edit.getText().toString().replace(" ","%20").replace("\n","%0A");

                   Log.e("bio url",URL);
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


                    JSONObject param=new JSONObject();
                    try {

                        param.put("id", String.valueOf(preferences.getInt("id", 0)));
                        param.put("academic", educational_txt);
                        param.put("professional", professional_txt);
                        param.put("personal", personal_txt);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, param, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            Toast.makeText(getContext(), "Biography "+response, Toast.LENGTH_SHORT).show();

                            Log.e("subject response ",response.toString());
                            try {
                                if (response.getInt("status")==0){
                                    biography_personal.setText(response.getString("personal"));
                                    biography_educational.setText(response.getString("academic"));
                                    biography_professional.setText(response.getString("professional"));


                                    biography_personal_edit.setText(response.getString("personal"));
                                    biography_educational_edit.setText(response.getString("academic"));
                                    biography_professional_edit.setText(response.getString("professional"));
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
        video_11 = (LinearLayout) view.findViewById(R.id.video_11);
        ratings_11 = (LinearLayout) view.findViewById(R.id.ratings_11);
        biography_subject_11 = (LinearLayout) view.findViewById(R.id.biography_subject_11);


        biography_languages = (TextView) view.findViewById(R.id.biography_languages);




        final int height = biography_biographyfrag_layout.getHeight();

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

        final LinearLayout biography_video = (LinearLayout) view.findViewById(R.id.biography_video);
        video_11.setOnClickListener(new View.OnClickListener()

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


        biography_subject_11.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {

                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                FragmentTransaction t = fragmentManager.beginTransaction();
                FragmentStack.getInstance().push(new Tablayout_with_viewpager());
                t.replace(R.id.viewpager, new Biography_subject_Fragment()).commit();
            }
        });
        id = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
        return view;
    }


    private class subjectHandler extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
            Log.e("subjects url", URL);


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


                            biography_personal.setText(obj.getString("personal"));
                            biography_educational.setText(obj.getString("academic"));
                            biography_professional.setText(obj.getString("professional"));


                            biography_professional_edit.setText(obj.getString("professional"));
                            biography_personal_edit.setText(obj.getString("personal"));
                            biography_educational_edit.setText(obj.getString("academic"));


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
                                biography_languages.setText(sub);
                            } else biography_languages.setText("");
//                            view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        biography_languages.setText("");

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


                        biography_personal.setText(obj.getString("personal"));
                        biography_educational.setText(obj.getString("academic"));
                        biography_professional.setText(obj.getString("professional"));


                        biography_professional_edit.setText(obj.getString("professional"));
                        biography_personal_edit.setText(obj.getString("personal"));
                        biography_educational_edit.setText(obj.getString("academic"));


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
                            biography_languages.setText(sub);
                        } else biography_languages.setText("");
//                        view.findViewById(R.id.biography_languages_progress).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    biography_languages.setText("");

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



    private void InitializePLayer(String link) throws android.net.ParseException {
        if (player==null){
            TrackSelection.Factory factory=new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player= ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(factory),new DefaultLoadControl());
            player.addListener(componentListener);
            player.setAudioDebugListener(componentListener);
            player.setVideoDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(false);
            player.seekTo(CurrentWindow,PlayBackPosition);

        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"), BANDWIDTH_METER);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(link),
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource,true,false);
    }


    private void ReleasePlayer(){
        if (player != null){
            PlayBackPosition=player.getCurrentPosition();
            CurrentWindow=player.getCurrentWindowIndex();
            playWhenReady=false;
            player.removeListener(componentListener);
            player.setVideoListener(null);
            player.setVideoDebugListener(null);
            player.setAudioDebugListener(null);
            player.release();
            player=null;


        }
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
            switch (playbackState){
                case ExoPlayer.STATE_IDLE:
                    stateString="ExoPlayer.STATE_IDLE   -";
                    break;


                case ExoPlayer.STATE_BUFFERING:
                    stateString="ExoPlayer.STATE_BUFFERING   -";
                    break;


                case ExoPlayer.STATE_READY:
                    stateString="ExoPlayer.STATE_READY  -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString="ExoPlayer.STATE_ENDED  -";
                    break;

                default:
                    stateString="Unknown State  -";
                    break;
            }

            Log.e(TAG,"changed state to "+stateString+"PLay when ready "+playWhenReady);

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

                                final SharedPreferences bio_vid_url=getContext().getSharedPreferences("biography_video",Context.MODE_PRIVATE);
                                SharedPreferences.Editor bio_edit=bio_vid_url.edit();

                                bio_edit.putString("videourl",link).apply();

InitializePLayer(link);
                                final String finalLink = link;
                               /* expanded_fullscreen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i=new Intent(getContext(),Exoplayer_fullscreen.class);
//                                        Toast.makeText(getContext(), player.getCurrentTrackGroups().get(0).toString(), Toast.LENGTH_SHORT).show();
                                        i.putExtra("fullscreen_video_url", bio_vid_url.getString("videourl",""));
                                        i.putExtra("position",player.getCurrentPosition());
                                        startActivity(i);
                                    }
                                });*/

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
                }
            });
            Volley.newRequestQueue(getContext()).add(sr);
            return null;
        }
    }


}
