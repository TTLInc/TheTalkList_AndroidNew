package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Popup_after_veesession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_after_veesession);


        final SharedPreferences preferences = getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);

//        Toast.makeText(this, "like "+getIntent().getStringExtra("fromCallActivity"), Toast.LENGTH_SHORT).show();

        if (getIntent().getStringExtra("fromCallActivity").equalsIgnoreCase("yes")) {

            Toast.makeText(getApplicationContext(), "class id: " + preferences.getInt("classId", 0), Toast.LENGTH_SHORT).show();

            Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.POST, "https://www.thetalklist.com/api/tutor_earn?class_id=" + preferences.getInt("classId", 0), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject responseObj = new JSONObject(response);

                        if (responseObj.getInt("status") == 0) {

                            JSONObject earn = responseObj.getJSONObject("earn_tutor");

                            ((TextView) findViewById(R.id.after_veesession_text)).setText("Your session with " + earn.getString("name") + " cost you " + String.format("%.02f", Float.parseFloat(earn.getString("earning"))) + " credits.\n" +
                                    "        Our community appreciates your business.");

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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







      /*      ((TextView)findViewById(R.id.after_veesession_text)).setText("Your session with "+preferences.getString("callSenderName","")+" cost you "+getIntent().getStringExtra("cost")+" credits.\n" +
                    "        Our community appreciates your business.");*/

         /*   ((TextView)findViewById(R.id.after_veesession_text)).setText("Your session with "+"XXX"+" cost you "+"0.05"+" credits.\n" +
                    "        Our community appreciates your business.");*/
        } else
            ((TextView) findViewById(R.id.after_veesession_text)).setText("Your session with " + getIntent().getStringExtra("name") + " cost you " + getIntent().getStringExtra("cost") + " credits.\n" +
                    "        Our community appreciates your business.");


        ((Button) findViewById(R.id.after_veesession_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getIntent().getStringExtra("fromCallActivity").equalsIgnoreCase("yes")) {

                    Intent i = new Intent(getApplicationContext(), StudentFeedBack.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else {
                    finish();onBackPressed();
                }
            }
        });
    }
}
