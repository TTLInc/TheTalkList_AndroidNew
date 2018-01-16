package com.ttl.project.thetalklist.Services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 11/11/2017.
 */

public class MessageCountService {


    public void MessageCount(final Activity context, SharedPreferences loginPref){

//        Toast.makeText(getApplicationContext(), "Message count increase", Toast.LENGTH_SHORT).show();
        
        String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + loginPref.getInt("id", 0);
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("message count res ", response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("unread_count") > 0)
                        ((TextView) context.findViewById(R.id.bottombar_message_count)).setText(String.valueOf(object.getInt("unread_count")));
                    if (object.getInt("unread_count") == 0)
                        context.findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
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
