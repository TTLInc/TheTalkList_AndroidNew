package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ttl.project.thetalklist.Adapter.VideoListAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Decorations.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;


public class VideoList extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FragmentManager fragmentManager;
    VideoListAdapter videoListAdapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    SearchView searchView;

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String videoSearchUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final FragmentStack fragmentStack = FragmentStack.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.videoList);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.videoListSwipeRefresh);
        final ImageView recordVid = (ImageView) view.findViewById(R.id.recordVid);
//        fragmentStack.add(new VideoList());
        linearLayout = (LinearLayout) view.findViewById(R.id.videoList_ProgressBar);
        recordVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoRecord videoRecord = new VideoRecord();
                fragmentStack.push(new VideoList());
                TabBackStack tabBackStack = TabBackStack.getInstance();
                tabBackStack.setTabPosition(1);
                SharedPreferences bio_videoPref = getContext().getSharedPreferences("bio_video", Context.MODE_PRIVATE);
                SharedPreferences.Editor bio_Editor = bio_videoPref.edit();
                bio_Editor.putBoolean("biography", false).apply();
                fragmentTransaction.replace(R.id.viewpager, videoRecord).commit();
            }
        });


        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch_yellow));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));


        queue1 = Volley.newRequestQueue(getContext());

        searchView = (SearchView) view.findViewById(R.id.videosearch_searchView);
        searchView.setQueryHint("1 video -1 topic -1 minute");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.threedotprogressbar);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String newQuery=newText.replace(" ","%20");
                final String URL = "https://www.thetalklist.com/api/videosearch?keyword="+newQuery;

                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("video list api URL",URL);
                        Log.e("video list api response","video search  "+ response.toString());

                        try {
                            jsonArray = response.getJSONArray("videos");
                            Log.e("video li response array", jsonArray.toString());
                            setRecycler(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        linearLayout.setVisibility(View.GONE);
                    }
                }
                );
                getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(getRequest);
                dialog.dismiss();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                searchView.clearFocus();


                String newQuery=query.replace(" ","%20");
                final String URL = "https://www.thetalklist.com/api/videosearch?keyword="+newQuery;
                dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.threedotprogressbar);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("video list api URL",URL);
                        Log.e("video list api response","video search  "+ response.toString());

                        try {
                            jsonArray = response.getJSONArray("videos");
                            Log.e("video li response array", jsonArray.toString());
                            setRecycler(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        linearLayout.setVisibility(View.GONE);
                    }
                }
                );
                getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(getApplicationContext()).add(getRequest);
                dialog.dismiss();
                return false;
            }

        });


        preferences = getApplicationContext().getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
        editor = preferences.edit();

       /* if (preferences.contains("jsonArray")) {

            try {
                new VideoPlayService().execute();


                jsonArray = new JSONArray(preferences.getString("jsonArray", ""));
                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, jsonArray);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.scrollToPosition(preferences.getInt("position", 0));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();


               */
       swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshItems();
                    }

                    void refreshItems() {
                        // Load items
                        // ...
                        new VideoPlayService().execute();
                        // Load complete
                       /* swipeRefreshLayout.setRefreshing(false);
                        // Update the adapter and notify data set changed
                        // ...
                        // Stop refresh animation
                        recyclerView.scrollToPosition(0);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(videoListAdapter);
                        videoListAdapter.notifyDataSetChanged();*/
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        /*
                linearLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {*/
            new VideoPlayService().execute();
            linearLayout.setVisibility(View.GONE);
//        }
        preferences1 = getContext().getSharedPreferences("videoPlaySelected", Context.MODE_PRIVATE);
        editor1 = preferences1.edit();
        return view;
    }

    SharedPreferences preferences1;
    SharedPreferences.Editor editor1;
    JSONArray jsonArray;
    RequestQueue queue1;

    public class VideoPlayService extends AsyncTask<Void, Void, Void> {
        Dialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new Dialog(getContext());
            dialog.setContentView(R.layout.threedotprogressbar);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String URL = "https://www.thetalklist.com/api/videolist?uid="+getContext().getSharedPreferences("loginStatus",Context.MODE_PRIVATE).getInt("id",0);

            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("video list api response", response.toString());

                    try {
                        jsonArray = response.getJSONArray("videos");
                        Log.e("video li response array", jsonArray.toString());

setRecycler(jsonArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    linearLayout.setVisibility(View.GONE);
                }
            }
            );
            getRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue1.add(getRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    public void setRecycler( JSONArray jsonArray){




                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                videoListAdapter = new VideoListAdapter(getContext(), fragmentManager, jsonArray);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.scrollToPosition(preferences.getInt("position", 0));

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(videoListAdapter);
                videoListAdapter.notifyDataSetChanged();


                /*swipeRefreshLayout.setRefreshing(false);
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
                });*/
                linearLayout.setVisibility(View.GONE);
    }

/*        @Override
        public void jumptotutor(View view) {
            startActivity(new Intent(videolist.this, available_tutor_expanded.class));
        }*/
    };


