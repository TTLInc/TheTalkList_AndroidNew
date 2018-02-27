package com.ttl.project.thetalklist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

//Sign up activity
public class SignUp extends Activity {

    Dialog dialog;
    SharedPreferences preferences;

    SessionManager session;

    Button signUp;
    EditText email;
    EditText password;
    EditText phone;
    EditText firstname;
    EditText lastname;
    TextView signin;
    TextView term_and_condition;
    CheckBox checkBox;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_sign_up);


    }


    @Override
    protected void onStart() {
        super.onStart();
        ButterKnife.bind(SignUp.this);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/GothamBookRegular.ttf");

        checkBox = (CheckBox) findViewById(R.id.termsNconCheck);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        preferences = getApplicationContext().getSharedPreferences("userDaTa", MODE_PRIVATE);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/GothamBookRegular.ttf");

        firstname = (EditText) findViewById(R.id.signupET1);
        firstname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        firstname.setTypeface(typeface);
        firstname.setVisibility(View.VISIBLE);
        lastname = (EditText) findViewById(R.id.signupET2);
        lastname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        lastname.setVisibility(View.VISIBLE);
        lastname.setTypeface(typeface);
        phone = (EditText) findViewById(R.id.signupET3);
        phone.setVisibility(View.VISIBLE);
        phone.setTypeface(typeface);
        email = (EditText) findViewById(R.id.signupET4);
        password = (EditText) findViewById(R.id.signupET5);
        password.setVisibility(View.VISIBLE);
        password.setTypeface(typeface);
        signin = (TextView) findViewById(R.id.signupTT2);
        term_and_condition = (TextView) findViewById(R.id.term_and_condition);
        signin.setTypeface(typeface);
        signUp = (Button) findViewById(R.id.signupButton);
    }

    @Override
    protected void onResume() {
        super.onResume();


        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("pass", password.getText().toString());
        editor.apply();
        editor.commit();

        session = new SessionManager(getApplicationContext());

        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth() / 5;
        signin.setWidth(width);

        signUp.setTypeface(typeface);


        term_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.thetalklist.com/en/article/terms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String first1 = firstname.getText().toString();
                final String last1 = lastname.getText().toString();
                final String emailadd1 = email.getText().toString();
                final String pass1 = password.getText().toString();


                if (first1.equals("")) {
                    firstname.setError("Required");
                } else if (last1.equals("")) {
                    lastname.setError("Required");
                } else if (phone.getText().toString().length() == 0) {
                    phone.setError("Required");
                } else if (emailadd1.equals("")) {
                    email.setError("Required");
                } else if (pass1.equals("")) {
                    password.setError("Required");
                } else if (pass1.length() < 6) {
                    password.setError("Length must be more than 6 characters.");
                } else if (!checkBox.isChecked()) {
                    checkBox.setError("Please check if you agree..!");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailadd1).matches()) {
                    email.setError("Invalid Email Address..!");
                } else if (phone.getText().toString().length() < 10) {
                    phone.setError("Please check your Phone number..!");
                } else {

//                    Toast.makeText(getApplicationContext(), "User Login status"+ session.isLoggedIn(), Toast.LENGTH_SHORT).show();


                    SharedPreferences pref = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref.edit();
                    editor1.putString("email", emailadd1);
                    editor1.putString("user", first1 + " " + last1);
                    editor1.putInt("status", 1);
                    editor1.putString("pass", pass1).apply();

                    session.createLoginSession(emailadd1, pass1);

                    dialog = new Dialog(SignUp.this);
                    dialog.setContentView(R.layout.threedotprogressbar);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


                    Log.e("phone numhber", phone.getText().toString());
                    String URL = "https://www.thetalklist.com/api/signup" +
                            "?firstName=" + first1.replace(" ", "%20") +
                            "&password=" + pass1 +
                            "&roleId=" + "0" +
                            "&lastName=" + last1.replace(" ", "%20") +
                            "&email=" + emailadd1 +
                            "&cell=" + phone.getText().toString()+
                            "&device_id="+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
                            +"&os=android";



                    Log.e("signup url", URL);

                    JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();

                            Log.e("signup response", response.toString());
                            try {
                                int a = response.getInt("status");
                                if (a == 0) {
                                    Intent i = new Intent(getApplicationContext(), IntroScreenSwipe.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(SignUp.this, response.getString("error"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                        }
                    }


                    );


                    queue.add(getRequest);
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        TextView cr = (TextView) findViewById(R.id.signupTL1);
        cr.setTypeface(typeface);
        TextView cb = (TextView) findViewById(R.id.signupTT1);
        cb.setTypeface(typeface);

    }
}
