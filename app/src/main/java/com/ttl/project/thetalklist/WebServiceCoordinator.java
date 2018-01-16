package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceCoordinator {

    private static final String CHAT_SERVER_URL ="https://www.thetalklist.com/api/openTok_connect?" ;



    private static final String LOG_TAG = WebServiceCoordinator.class.getSimpleName();

    private final Context context;
    private final Listener delegate;

    public WebServiceCoordinator(Context context, Listener delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void fetchSessionConnectionData() {
        RequestQueue reqQueue = Volley.newRequestQueue(context);
        SharedPreferences preferences =context.getSharedPreferences("videoCallTutorDetails",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();

        Log.e("student id", String.valueOf(preferences.getInt("studentId",0)));
        Log.e("tutor id", String.valueOf(preferences.getInt("tutorId",0)));
        Log.e("class id", String.valueOf(preferences.getInt("classId",0)));

        JSONObject jsonObject=new JSONObject();
        final String SESSION_INFO_ENDPOINT = CHAT_SERVER_URL + "receiver_id="+preferences.getInt("studentId",0)+"&sender_id="+preferences.getInt("tutorId",0)+"&cid="+preferences.getInt("classId",0);



        Log.e("weqbServiceCoor URL",SESSION_INFO_ENDPOINT);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, SESSION_INFO_ENDPOINT,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                editor.clear();


                try {
                    if (response.getInt("status")==0) {
                        String apiKey = response.getString("apiKey");
                        String sessionId = response.getString("sessionId");
                        String token = response.getString("token");


                        Log.e("wsc response", response.toString());
                        Log.e("wsc session id", sessionId);

                        Log.e(LOG_TAG, apiKey);
                        Log.e(LOG_TAG, sessionId);
                        Log.e(LOG_TAG, token);

                        delegate.onSessionConnectionDataReady(apiKey, sessionId, token);
                    }else {
                        Toast.makeText(context, "Something went wrong..!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("wsc catch exception",e.toString());
                    delegate.onWebServiceCoordinatorError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wsc er exception",error.toString());
                delegate.onWebServiceCoordinatorError(error);
            }
        });
        reqQueue.add(jsonObjectRequest);
    }

    public  interface Listener {
        void onSessionConnectionDataReady(String apiKey, String sessionId, String token);
        void onWebServiceCoordinatorError(Exception error);

    }
}

