package com.ttl.project.thetalklist.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 12/05/2017.
 */

public class LoginService {
    String result;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    public String login(String email, String pass, final Context context) {

        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        editor = pref.edit();

if(pref.getString("LoginWay","").equals("FacebookLogin")){
    final SharedPreferences pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
//                final String url = "https://www.thetalklist.com/api/fblogin?email=" + pref.getString("email", "") + "&facebook_id=" + pref.getInt("facebook_id", 0) + "&firstname=" + pref.getString("first_name", "") + "&lastname=" + pref.getString("last_name", "") + "&gender=" + pref.getString("gender", "") + "&birthdate=" + pref.getString("birthday", "");
    final SharedPreferences.Editor editor = pref.edit();
//    final String url="https://www.thetalklist.com/api/fblogin?facebook_id="+pref.getString("facebook_id", "")+"&firstname="+pref.getString("first_name", "")+"&lastname="+pref.getString("last_name", "") ;


    String url="";
    if (pref.getInt("gender", 0)==0)
        url="https://www.thetalklist.com/api/fblogin?email="+pref.getString("email", "")+"&facebook_id="+pref.getString("facebook_id", "")+"&firstname="+pref.getString("firstName", "")+"&lastname="+pref.getString("lastName", "")+"&gender=female&birthdate="+"";
    else
        url="https://www.thetalklist.com/api/fblogin?email="+pref.getString("email", "")+"&facebook_id="+pref.getString("facebook_id", "")+"&firstname="+pref.getString("firstName", "")+"&lastname="+pref.getString("lastName", "")+"&gender=male&birthdate="+"";

    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {


            try {
                JSONObject obj=new JSONObject(response);
                if (obj.getInt("status")==0) {
                    JSONObject resObj=obj.getJSONObject("result");

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
                    editor.putFloat("frMoney", (float) resObj.getDouble("frMoney"));
                    editor.putInt("roleId",roleId);
                    editor.putInt("coupon_credits",resObj.getInt("coupon_credits"));
                    editor.putInt("status",0);
                    editor.apply();

               /*     Toast.makeText(getApplicationContext(), "Login Sucessfully..!", Toast.LENGTH_SHORT).show();
                    SettingFlyout settingFlyout = new SettingFlyout();
                    Intent i = new Intent(getApplicationContext(), settingFlyout.getClass());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(i);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getApplicationContext(), "Login Unsucessful..!", Toast.LENGTH_SHORT).show();
            editor.clear().apply();
        }
    });

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    queue.add(stringRequest);
}else {
    String URL = "https://www.thetalklist.com/api/login?email=" + email+ "&password=" + pass;

    queue = Volley.newRequestQueue(context);


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

                    String Err = (String) jsonObject.get("error");

                } else {


                    JSONObject resultObj = (JSONObject) jsonObject.get("result");
                    int roleId = resultObj.getInt("roleId");
                    String UserName = (String) resultObj.get("username");
                    int userId = resultObj.getInt("id");
                    String mail = resultObj.getString("email");

                    editor.putString("loginResponse", response);
                    editor.putString("user", UserName);
                    editor.putInt("roleId", roleId);
                    editor.putBoolean("logSta", true);
                    editor.putInt("userId", resultObj.getInt("id"));
                    editor.putString("credit_balance", resultObj.getString("credit_balance"));
                    editor.putString("usernm", resultObj.getString("usernm"));
                    editor.putString("pic", resultObj.getString("pic"));
                    editor.putString("firstName", resultObj.getString("firstName"));
                    editor.putString("lastName", resultObj.getString("lastName"));
                    editor.putString("city", resultObj.getString("city"));
                    editor.putString("nativeLanguage", resultObj.getString("nativeLanguage"));
                    editor.putString("otherLanguage", resultObj.getString("otherLanguage"));
                    editor.putInt("id", resultObj.getInt("id"));
                    editor.putInt("gender", resultObj.getInt("gender"));
                    editor.putString("cell", resultObj.getString("cell"));
                    editor.putFloat("hRate", Float.parseFloat(resultObj.getString("hRate")));
                    if (resultObj.getString("avgRate").equals(""))
                        editor.putFloat("avgRate", 0.0f);
                    else
                        editor.putFloat("avgRate", Float.parseFloat(resultObj.getString("avgRate")));

                    if (resultObj.getString("ttl_points").equals(""))
                        editor.putFloat("ttl_points", 0.0f);
                    else
                        editor.putFloat("ttl_points", Float.parseFloat(resultObj.getString("ttl_points")));
                    editor.putInt("country", resultObj.getInt("country"));
                    editor.putInt("coupon_credits", resultObj.getInt("coupon_credits"));
                    editor.putInt("province", resultObj.getInt("province"));
                    editor.putInt("status", 0);
                    editor.putFloat("money", Float.parseFloat(String.format("%.02f",Float.parseFloat(resultObj.getString("money")))));
                    editor.putFloat("frMoney", (float) resultObj.getDouble("frMoney"));
                    editor.putString("email", resultObj.getString("email"));
                    editor.apply();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (error.getClass().equals(TimeoutError.class)) {
                Toast.makeText(context,
                        "Oops. Timeout error!",
                        Toast.LENGTH_LONG).show();
            }
            if (error.getClass().equals(ServerError.class)) {
                Toast.makeText(context,
                        "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                        Toast.LENGTH_LONG).show();
            }


            Log.d("error", error.toString());
        }
    });
    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    queue.add(sr);
}



        return result;
    }

}
