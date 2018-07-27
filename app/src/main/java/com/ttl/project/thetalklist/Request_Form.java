package com.ttl.project.thetalklist;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.util.Config;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Request_Form extends AppCompatActivity {
    private static final String TAG = "Request_Form";
    int sender_id, receiver_id;
    SharedPreferences chatPref;
    String sender_name;
    RequestQueue queue;
    private SharedPreferences sharedPreferences;
    private TextInputEditText subjectEdit, levelEdit, timeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        queue = Volley.newRequestQueue(getApplicationContext());

        sender_name = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("firstName", "");

        chatPref = getApplicationContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("Request_Form", Context.MODE_PRIVATE);
        receiver_id = chatPref.getInt("receiverId", 0);
        sender_id = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        subjectEdit = (TextInputEditText) findViewById(R.id.request_subject);
        levelEdit = (TextInputEditText) findViewById(R.id.request_level);
        timeEdit = (TextInputEditText) findViewById(R.id.request_time);


        try {
            subjectEdit.setText(sharedPreferences.getString("Subject Edit", null));
            levelEdit.setText(sharedPreferences.getString("Level Edit", null));
            timeEdit.setText(sharedPreferences.getString("Time Edit", null));
        } catch (Exception e) {
        }

        Button submit = (Button) findViewById(R.id.request_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = subjectEdit.getText().toString();
                String level = levelEdit.getText().toString();
                String time = timeEdit.getText().toString();

                int subjectCompleted, levelCompleted, timeCompleted;
                //subject
                if (subject.equals("")) {
                    Toast.makeText(getApplicationContext(), "Subject missing!",
                            Toast.LENGTH_LONG).show();
                    subjectCompleted = 0;
                } else subjectCompleted = 1;

                //level
                if (level.equals("")) {
                    Toast.makeText(getApplicationContext(), "Level missing!",
                            Toast.LENGTH_LONG).show();
                    levelCompleted = 0;
                } else levelCompleted = 1;

                //time
                if (time.equals("")) {
                    Toast.makeText(getApplicationContext(), "Desired time missing!",
                            Toast.LENGTH_LONG).show();
                    timeCompleted = 0;
                } else timeCompleted = 1;

                if (subjectCompleted == 1 && levelCompleted == 1 && timeCompleted == 1) {
                    String send =
                            "I need help with: " + subject + System.lineSeparator() +
                                    "My level is: " + level + System.lineSeparator() +
                                    "I need help at this time: " + time;

                    // put texts to shared preferences
                    Log.e(TAG, "Submit Form: " + send);
                    String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(send);
                    apicall(toServerUnicodeEncoded);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Request_Form", send);
                    editor.putString("Subject Edit", subject);
                    editor.putString("Level Edit", level);
                    editor.putString("Time Edit", time);
                    editor.putInt("Request Submitted", 1);
                    editor.apply();
                  /*  FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.viewpager, new MessageList()).commit();*/
                    Config.sumbitMsg = 1;
                    finish();
                }
            }
        });
    }

    private void apicall(String send) {

        String URL = "https://www.thetalklist.com/api/message?sender_id=" + sender_id + "&receiver_id=" + receiver_id + "&message=" + send/*.replace(" ", "%20")*/ + "&user_name=" + sender_name;
        Log.e("send Message list url", "send Message list url" + URL);


        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("message send response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 0) {

                        String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
                        Log.e("Message list url", URL);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(getContext(), "error t " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }
}
