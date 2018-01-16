package com.ttl.project.thetalklist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    View root;
    TextView textForget;
    EditText sendEmail;
    TextView senderEmail;
    Button resetButton;
    Typeface typeface;
    @Override
    protected void onStart() {
        super.onStart();
         typeface=Typeface.createFromAsset(getAssets(),"fonts/GothamBookRegular.ttf");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        resetButton = (Button) findViewById(R.id.forgetPassword_Button);
        resetButton.setTypeface(typeface);
        root=getLayoutInflater().inflate(R.layout.activity_forget_password,null);


        textForget = (TextView) findViewById(R.id.textView);
         sendEmail = (EditText) findViewById(R.id.sendEmail);
        sendEmail.setTypeface(typeface);
         senderEmail = (TextView) findViewById(R.id.textView2);
        senderEmail.setTypeface(typeface);


    }


    @Override
    protected void onResume() {
        super.onResume();
        senderEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });



        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=sendEmail.getText().toString();
                if (email.equals("")) {
                    sendEmail.setError("Required");
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    sendEmail.setError("Invalid Email Address..!");
                }
                else {

                    final ProgressDialog progressDialog=new ProgressDialog(ForgetPassword.this);
progressDialog.show();
                    String URL = "https://www.thetalklist.com/api/resetpassword?email=" + email;


                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


                    final StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(" Forgot pass response", response);


                            try {
                                progressDialog.dismiss();
                                JSONObject obj=new JSONObject(response);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),Login.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgetPassword.this, "Something went wrong... your request can not sent to the server", Toast.LENGTH_SHORT).show();

                        }
                    });
                    sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(sr);
                }








            }
        });
    }
}
