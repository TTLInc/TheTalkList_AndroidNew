package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.VideoListAdapter;
import com.ttl.project.thetalklist.Decorations.DividerItemDecoration;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
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

import java.text.DecimalFormat;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 18/04/2017.
 */

public class Video_Play extends Fragment {
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView videoPlay_videoTitle, videoPlay_videoSeenCount, videoPlay_videodescription;
    ImageView videoPlay_fbBtn, videoPlay_msgBtn, videoPlay_VideoCallBtn, like;
    ShareButton videoplay_fbshare;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FragmentStack fragmentStack;

    String name;
    String desc;
    String source;
    int views;
    int id;
    MediaController mediaController;
    String jsonObj, jsonAry;

    JSONArray jsonArray;

    Float credit;
    String tutorName;


    //Exo player initialization

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayer";


    SimpleExoPlayer player;
    SimpleExoPlayerView playerView;

    ComponentListener componentListener;

    long PlayBackPosition;
    int CurrentWindow;
    boolean playWhenReady = true;
    JSONObject jsonObject;

    String imageUrl;
    int uid;

    //exo player over

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getContext().getSharedPreferences("videoPlaySelected", Context.MODE_PRIVATE);
        jsonObj = preferences.getString("response", "");
        jsonAry = preferences.getString("responseArray", "");


        Log.e("videoplay response", jsonObj);
        Log.e("videoplay response ary", jsonAry);

        try {

            jsonArray = new JSONArray(jsonAry);

            jsonObject = new JSONObject(jsonObj);
//            JSONObject jsonObject =  new JSONObject((jsonObj.substring(jsonObj.indexOf("{"), jsonObj.lastIndexOf("}")+1)));
            name = jsonObject.getString("name");
            desc = jsonObject.getString("desc");
            source = "https://www.thetalklist.com/uploads/video/" + jsonObject.getString("source");
            imageUrl = "https://www.thetalklist.com/uploads/image/" + jsonObject.getString("source") + ".jpg";
//            source = "https://www.thetalklist.com/uploads/video/" + source;
            Log.e("source", source);
            Log.e("play objrct", jsonObject.toString());
            views = jsonObject.getInt("likes");
            id = jsonObject.getInt("id");
            uid=jsonObject.getInt("uid");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    int isLiked = 0;
    ImageView expanded_fullscreen;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facebookSDKInitialize();
        view = inflater.inflate(R.layout.video_play_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.videoPlayList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.videoPLayListSwipeRefresh);
        videoPlay_videodescription = (TextView) view.findViewById(R.id.videoPlay_videoDescription);
        videoPlay_videoTitle = (TextView) view.findViewById(R.id.videoPlay_videoTitle);
        videoPlay_videoSeenCount = (TextView) view.findViewById(R.id.videoPlay_videoSeenCount);

        videoPlay_fbBtn = (ImageView) view.findViewById(R.id.videoPlay_fbBtn);
        videoplay_fbshare = (ShareButton) view.findViewById(R.id.videoplay_fbshare);
        videoPlay_msgBtn = (ImageView) view.findViewById(R.id.videoPlay_msgBtn);
        videoPlay_VideoCallBtn = (ImageView) view.findViewById(R.id.videoPlay_VideoCallBtn);
        mediaController = new MediaController(getContext());
        like = (ImageView) view.findViewById(R.id.videoPlay_like);
        expanded_fullscreen = (ImageView) view.findViewById(R.id.expanded_fullscreen);

        preferences = getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);


        try {
            if (jsonObject.getInt("isMyFavourite") == 1) {
                like.setImageDrawable(getResources().getDrawable(R.drawable.liked));
                isLiked = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        componentListener = new ComponentListener();
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.exo_player_view);


        InitializePLayer(source);
        expanded_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Exoplayer_fullscreen.class);
//                                        Toast.makeText(getContext(), player.getCurrentTrackGroups().get(0).toString(), Toast.LENGTH_SHORT).show();
                i.putExtra("fullscreen_video_url", source);
                i.putExtra("position", player.getCurrentPosition());
                startActivity(i);
            }
        });

        Log.e("source link", source);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(source,
                MediaStore.Images.Thumbnails.MINI_KIND);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLiked == 0) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.liked));

                    videoPlay_videoSeenCount.setText(String.valueOf(Integer.parseInt(videoPlay_videoSeenCount.getText().toString()) + 1));

//                    StringRequest sr= null;
                    try {
                        StringRequest sr = new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/video_like?uid=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0)
                                + "&vid=" + jsonObject.getInt("id"), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

//                                Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();


                                try {
                                    JSONObject o = new JSONObject(response);
                                    if (o.getInt("status") != 0) {
                                        Toast.makeText(getContext(), "Something went wrong.!", Toast.LENGTH_SHORT).show();
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
                        Request<String> requestQueue = Volley.newRequestQueue(getContext()).add(sr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    isLiked = 1;
                } else {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.like));

                    videoPlay_videoSeenCount.setText(String.valueOf(Integer.parseInt(videoPlay_videoSeenCount.getText().toString()) - 1));
                    try {
                        StringRequest sr = new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/video_like?uid=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0)
                                + "&vid=" + jsonObject.getInt("id"), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

//                                Toast.makeText(getContext(), "UnLiked", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getContext(), "Response "+response, Toast.LENGTH_SHORT).show();


                                try {
                                    JSONObject o = new JSONObject(response);
                                    if (o.getInt("status") != 0) {
                                        Toast.makeText(getContext(), "Something went wrong.!", Toast.LENGTH_SHORT).show();
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
                        Request<String> requestQueue = Volley.newRequestQueue(getContext()).add(sr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isLiked = 0;
                }

            }
        });


        return view;
    }

    ShareDialog shareDialog;

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getContext());
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());

        // this part is optional
        shareDialog.registerCallback(callbackManager, callback);

    }

    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.e(TAG, "Succesfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "Cancel occured");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.e(TAG, error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };


    private void InitializePLayer(String source) throws ParseException {
        Log.e("video url", source);
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

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(source),
                dataSourceFactory, extractorsFactory, null, null);

        //        MediaSource mediaSource = buildMediaSource(Uri.parse("https://www.youtube.com/watch?v=NVoEDjvuhNI"));
        player.prepare(mediaSource, true, false);


        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/video_views?uid=" +
                getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&vid=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("view count respo", response);
                Log.e("view count url", "https://www.thetalklist.com/api/video_views?uid=" +
                        getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&vid=" + id);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") != 0) {
                        Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));


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


    VideoListAdapter videoListAdapter;
    Bitmap image;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentStack = FragmentStack.getInstance();

        JSONArray ary = jsonArray;

        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        SpannableStringBuilder str = new SpannableStringBuilder("Description: ");
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), flag);

        final String sourceString = "<b>" + "Definition:- " + "</b> ";
        videoPlay_videoTitle.setText(name);
        videoPlay_videoSeenCount.setText(String.valueOf(views));
        videoPlay_videodescription.setText(str + desc);


        for (int i = 0; i < ary.length(); i++) {
            try {
                JSONObject jsonObject = ary.getJSONObject(i);
                if (jsonObject.getInt("id") == id) {
                    ary.remove(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("video play Jsonary", jsonArray.toString());
        Log.e("video play ary", ary.toString());

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, ary);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();


        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            void refreshItems() {
                // Load items
                // ...

                // Load complete
                swipeRefreshLayout.setRefreshing(false);
                // Update the adapter and notify data set changed
                // ...
                // Stop refresh animation
                recyclerView.scrollToPosition(0);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();
            }
        });


        videoPlay_fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentTitle(name)
                        .setImageUrl(Uri.parse(imageUrl))
                                        .setContentDescription(desc)
                                                        .setContentUrl(Uri.parse(source))
                                                                        .build();*/
//                        ShareDialog.show(getActivity(), content);


                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.thetalklist.com/en/search/videosearch/?v=" + id))
                        .setImageUrl(Uri.parse(source + ".jpg"))
                        .build();
                ShareDialog.show(getActivity(), content);

            }
        });

        SharedPreferences chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor chatPrefEditor = chatPref.edit();

        final SharedPreferences preferences = getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

//        try {

            String url = "https://www.thetalklist.com/api/tutor_info?tutor_id=" + uid;
            Log.e("url", url);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("tutor details", response);

                    try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 0) {

                        JSONArray ary = obj.getJSONArray("tutor");
                        final JSONObject o = ary.getJSONObject(0);

                        tutorName = o.getString("firstName");
                        credit = Float.parseFloat(String.valueOf(o.getDouble("hRate")));
                        final int id = o.getInt("id");


                        videoPlay_msgBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                    try {
                                if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) != uid) {
                                    MessageOneToOne messageList = new MessageOneToOne();
                                    chatPrefEditor.putString("firstName", tutorName);
                                    chatPrefEditor.putInt("receiverId", id).apply();
                                    TTL ttl = (TTL) getContext().getApplicationContext();
                                    ttl.ExitBit = 1;
                                    fragmentStack.push(new Video_Play());
                                    fragmentTransaction.replace(R.id.viewpager, messageList).commit();
                                } else
                                    Toast.makeText(getContext(), "You can't chat with yourself.", Toast.LENGTH_SHORT).show();
                                   /* } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                            }
                        });


                        if (o.getInt("readytotalk") == 0) {
                            videoPlay_VideoCallBtn.setImageDrawable(getResources().getDrawable(R.drawable.disabled_video));
                            videoPlay_VideoCallBtn.setClickable(false);
                        } else {
                            videoPlay_VideoCallBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    try {
                                    if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) != uid) {
                                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.talknow_confirmation_layout, null);
                                        final PopupWindow popupWindow = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                                        popupWindow.setFocusable(true);
                                        popupWindow.setOutsideTouchable(false);

                                        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.talknow_insufficient_layout, null);
                                        final PopupWindow popupWindow1 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


                                        TextView confirmation_tutorCredits = (TextView) view1.findViewById(R.id.confirmation_tutorCredits);
                                        TextView confirmation_tutorName = (TextView) view1.findViewById(R.id.confirmation_tutorName);

                                        confirmation_tutorName.setText(tutorName + " ");
                                        confirmation_tutorCredits.setText(new DecimalFormat("##.##").format(credit / 25));


                                        Button yesbtn = (Button) view1.findViewById(R.id.yesbtn);
                                        final Button nobtn = (Button) view1.findViewById(R.id.nobtn);

                                        yesbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                popupWindow.dismiss();
                                                if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) <= getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE).getFloat("hRate", 0.0f)) {

                                                    popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
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


                                                    try {
                                                        editor.putString("tutorName", o.getString("firstName"));
                                                        editor.putInt("flag", 1);
                                                        SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                                                        editor.putInt("studentId", pref.getInt("id", 0));
                                                        editor.putString("tutorName", o.getString("firstName"));
                                                        editor.putInt("tutorId", Integer.parseInt(o.getString("id")));
                                                        editor.putFloat("hRate", Float.parseFloat(o.getString("hRate")));
                                                        editor.putFloat("credit", pref.getFloat("money", 0.0f)).apply();
                                                        Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        i.putExtra("from", "availabletutor");
                                                        getContext().startActivity(i);
                                                        getActivity().onBackPressed();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }


                                            }
                                        });
                                        nobtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TTL ttl = (TTL) getApplicationContext();
                                                ttl.ExitBit = 2;
                                                popupWindow.dismiss();

                                            }
                                        });
                                    } else
                                        Toast.makeText(getContext(), "You can't chat with yourself.", Toast.LENGTH_SHORT).show();
                                  /*  } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            });


                        }
                    }
                    else {
                        videoPlay_VideoCallBtn.setImageDrawable(getResources().getDrawable(R.drawable.disabled_video));
                        videoPlay_VideoCallBtn.setClickable(false);
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

     /*   } catch (JSONException e) {
            e.printStackTrace();
        }
*/


      /*  videoPlay_VideoCallBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                try {
                    if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) != jsonObject.getInt("uid")) {
                        String url = "https://www.thetalklist.com/api/tutor_info?tutor_id=" + jsonObject.getInt("uid");
                        Log.e("url", url);
                        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("tutor details", response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getInt("status") == 0) {


                                        JSONArray ary = obj.getJSONArray("tutor");
                                        final JSONObject o = ary.getJSONObject(0);

                                        tutorName = o.getString("firstName") + " ";
                                        credit = Float.parseFloat(String.valueOf(o.getDouble("hRate")));


                                    } else {
                                        Toast.makeText(getContext(), "This user is not tutor. You can not call a student user.", Toast.LENGTH_SHORT).show();
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
                    } else
                        Toast.makeText(getContext(), "You can not call yourself.", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }


    @Override
    public void onPause() {
        super.onPause();
    }


}
