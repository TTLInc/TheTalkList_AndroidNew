package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ttl.project.thetalklist.R;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 20/09/2017.
 */

public class Biography_videoThumb_adapter extends RecyclerView.Adapter<Biography_videoThumb_adapter.MyViewHolder> {

    Context context;
    JSONArray biography_video_ary;


    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayer";


    SimpleExoPlayer player;
    SimpleExoPlayerView playerView;

    ComponentListener componentListener;

    long PlayBackPosition;
    int CurrentWindow;
    boolean playWhenReady = true;
    RecyclerView biography_video_thum_recycle;


    public Biography_videoThumb_adapter(Context context, JSONArray biography_video_ary, SimpleExoPlayerView playerView, RecyclerView biography_video_thum_recycle) {
        this.context = context;
        this.biography_video_ary = biography_video_ary;
        this.playerView = playerView;
        componentListener = new ComponentListener();
        this.biography_video_thum_recycle = biography_video_thum_recycle;
//        player=this.playerView.getPlayer();
    }

    public void clear() {
        biography_video_ary = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumblines_layout, parent, false);
        return new Biography_videoThumb_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            final JSONObject thumbObj = biography_video_ary.getJSONObject(position);
            Glide.with(context).load("https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source") + ".jpg").into(holder.thumb);

            holder.thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {
//                        String  link = "https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source");
//                        String  link = "http://52.8.60.79/uploads/video/" + thumbObj.getString("source");
                        String link = "https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source");
                        InitializePLayer(link);


                        final SharedPreferences bio_vid_url = context.getSharedPreferences("biography_video", Context.MODE_PRIVATE);
                        SharedPreferences.Editor bio_edit = bio_vid_url.edit();

                        bio_edit.putString("videourl", link).apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

//                Toast.makeText(getContext(), "less than 3 credits", Toast.LENGTH_SHORT).show();

//            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view3 = LayoutInflater.from(context).inflate(R.layout.talknow_criticalcredit, null);

                            final PopupWindow popupWindow7 = new PopupWindow(view3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        popupWindow7.showAtLocation(LayoutInflater.from(context).inflate(R.layout.fragment_available_tutor, null), Gravity.CENTER, 0, 0);
                                    } catch (WindowManager.BadTokenException e) {
                                        Toast.makeText(getApplicationContext(), "Token null", Toast.LENGTH_SHORT).show();
                                    }
                                    return;

                                }
                            }, 3);


                            popupWindow7.setFocusable(true);
                            popupWindow7.setOutsideTouchable(false);


                            Button okButton = (Button) view3.findViewById(R.id.talknow_ok);
                            Button buyCredits = (Button) view3.findViewById(R.id.talknow_buycredits);
                            TextView tv = (TextView) view3.findViewById(R.id.talknow_text);

//                final int min = (int) (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) / credit);


                            tv.setText("Confirm delete video?");

                            okButton.setText("Yes");

                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow7.dismiss();
                                    try {

                                        Log.e("delete video url", "https://www.thetalklist.com/api/delete_bio_video?video_id=" + thumbObj.getInt("id") + "&user_id=" + context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0));
                                        Volley.newRequestQueue(context).add(new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/delete_bio_video?video_id=" + thumbObj.getInt("id") + "&user_id=" + context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0), new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                Log.e("after video delete", response);
                                                try {
                                                    JSONObject resultObj = new JSONObject(response);

                                                    if (resultObj.getInt("status") == 0) {

                                                        if (resultObj.getString("message").equals("No Video Found For this User.")) {
                                                            JSONArray biography_video_ary = resultObj.getJSONArray("biography_video");

                                                            biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(context, null, playerView, biography_video_thum_recycle));

                                                        } else {

                                                            JSONArray biography_video_ary = resultObj.getJSONArray("biography_video");

                                                            biography_video_thum_recycle.setAdapter(new Biography_videoThumb_adapter(context, biography_video_ary, playerView, biography_video_thum_recycle));

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
                                        }));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                            buyCredits.setText("No");

                            buyCredits.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow7.dismiss();

                                }
                            });
                        }


                    }, 10);


                }
            });


        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return biography_video_ary.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb;
        ImageView delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            delete = (ImageView) itemView.findViewById(R.id.video_delete);


        }
    }

    private void InitializePLayer(String link) throws ParseException {
        if (player == null) {
            TrackSelection.Factory factory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), new DefaultTrackSelector(factory), new DefaultLoadControl());
            player.addListener(componentListener);
            player.setAudioDebugListener(componentListener);
            player.setVideoDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(false);
            player.seekTo(CurrentWindow, PlayBackPosition);

        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "yourApplicationName"), BANDWIDTH_METER);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(link), dataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource, true, false);
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
}
