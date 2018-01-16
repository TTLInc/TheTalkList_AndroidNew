package com.ttl.project.thetalklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 05/06/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (!isOnline(context)){

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

               Intent i=new Intent(getApplicationContext(),NoInternetConnection.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
            }

        }else {

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                SharedPreferences totalCostPref = getApplicationContext().getSharedPreferences("totalCostPref", MODE_PRIVATE);
                final SharedPreferences.Editor totaEditor = totalCostPref.edit();

                Log.e("total cost url", totalCostPref.getString("totalcost", ""));
                if (totalCostPref.contains("totalcost")) {
                    StringRequest sr = new StringRequest(Request.Method.POST, totalCostPref.getString("totalcost", ""), new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.e("total cost response", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("status") == 0) {
                                    totaEditor.clear().apply();
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
                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(getApplicationContext()).add(sr);

                    RequestQueue queue333 = Volley.newRequestQueue(getApplicationContext());


                    StringRequest sr1 = new StringRequest(Request.Method.POST, totalCostPref.getString("disconnect", ""), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Log.e("Call_activity_reject_ca", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue333.add(sr1);
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), SplashScreen.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getApplicationContext().startActivity(intent1);
                }

            }

        }
    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
