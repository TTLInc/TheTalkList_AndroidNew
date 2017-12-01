package com.ttl.project.thetalklist.Pagination;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.History_list_adapter;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Saubhagyam on 20/07/2017.
 */

public class Paginator {


    private Context c;
    private PullToLoadView pullToLoadView;
    RecyclerView rv;
    private History_list_adapter adapter;
    private boolean isLoading = false;
    private boolean hasLoadAll = false;
    private int nextPage;


    public Paginator(Context c, PullToLoadView pullToLoadView) {
        this.c = c;
        this.pullToLoadView = pullToLoadView;
        rv = pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(c));

        adapter = new History_list_adapter(c, new ArrayList<History_model>());
        rv.setAdapter(adapter);

        initializePagination();
    }


    public void initializePagination() {

        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                LoadData(nextPage);
            }

            @Override
            public void onRefresh() {
                adapter.clear();
                hasLoadAll=false;
                LoadData(1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadAll;
            }
        });

        pullToLoadView.initLoad();
    }


    private void LoadData(final int page) {

        isLoading=true;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(c);
            String URL="https://www.thetalklist.com/api/history?id=" + c.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id",0)+"&page_no="+page;
                Log.e("Grid service url", URL);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response history", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            SharedPreferences pref = c.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                            if (object.getInt("status") == 0) {
                                JSONArray array = object.getJSONArray("history");

                                Log.e("page IN ASYNC TASK ", String.valueOf(page));

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = (JSONObject) array.get(i);
                                    History_model History_model = new History_model();
                                    if (pref.getInt("id", 0) == o.getInt("sID")) {
                                        History_model.setName(o.getString("sFN") + " " + o.getString("sLN"));
                                        History_model.setTutorname(o.getString("tFN") + " " + o.getString("tLN"));
                                    } else {
                                        History_model.setName(o.getString("tFN") + " " + o.getString("tLN"));
                                        History_model.setTutorname(o.getString("sFN") + " " + o.getString("sLN"));
                                    }

                                    History_model.setSid(o.getInt("sID"));
                                    History_model.setTid(o.getInt("tID"));

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Date convertedDate = new Date();
                                    try {
                                        convertedDate = dateFormat.parse(o.getString("createAt"));
                                        History_model.setDate((convertedDate.getMonth() + 1) + "/" + convertedDate.getDate() + "/" + (convertedDate.getYear() + 1900));
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                  /*  if (o.getDouble("fee") == 0.00d) {
                                        History_model.setRate(Float.valueOf("0.00"));
                                    } else {*/

                                  if (!o.getString("fee").equals(""))
                                        History_model.setRate(String.format("%.02f",Float.parseFloat(o.getString("fee"))));
                                    else History_model.setRate("0.00");
//                                    }










                                    adapter.add(History_model);
//                                }

                                }
                                pullToLoadView.setComplete();
                                isLoading = false;
                                nextPage = page + 1;
                            }
                            else {
                                pullToLoadView.setComplete();
                                isLoading = false;
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
                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                requestQueue.add(stringRequest);
//            }
//        },3000);
    }
}
