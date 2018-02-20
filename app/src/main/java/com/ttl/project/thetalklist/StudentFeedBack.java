package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Services.LoginService;

//Student feedback page
public class StudentFeedBack extends AppCompatActivity {

    Button studentFeedback_submitButton;
    MultiAutoCompleteTextView student_feedback_msg;
    CheckBox report_inappropriate_behaviour;
    RatingBar student_feedback_ratingBar;

    int bit;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feed_back);

        student_feedback_ratingBar= (RatingBar) findViewById(R.id.student_feedback_ratingBar);
        student_feedback_msg= (MultiAutoCompleteTextView) findViewById(R.id.student_feedback_msg);
        report_inappropriate_behaviour= (CheckBox) findViewById(R.id.report_inappropriate_behaviour);

        SharedPreferences pref=getSharedPreferences("loginStatus",MODE_PRIVATE);

        LoginService  loginService=new LoginService();
        loginService.login(pref.getString("email",""),pref.getString("pass",""),getApplicationContext());




        studentFeedback_submitButton= (Button) findViewById(R.id.studentFeedback_submitButton);
        studentFeedback_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);


                if(report_inappropriate_behaviour.isSelected()==true){
                    bit=1;
                }
                else bit=0;
                
                if (student_feedback_ratingBar.getRating()!=0.0f) {

                    SharedPreferences Sessionpref=getSharedPreferences("sessionPref",MODE_PRIVATE);
                    final SharedPreferences.Editor editor=Sessionpref.edit();



                    final String URL = "https://www.thetalklist.com/api/student_feedback_form?cid=" + preferences.getInt("classId", 0) + "&sid=" +
                            preferences.getInt("studentId", 0) + "&tid=" + preferences.getInt("tutorId", 0) + "&user_given_rating=" +
                            student_feedback_ratingBar.getRating() + "&report_inappropriate=" + bit +
                            "&feedback_msg=" + student_feedback_msg.getText().toString().replace(" ", "%20")+"&session_id="+Sessionpref.getString("sessionId","");
                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            editor.clear().apply();
                            Log.e("feedback url", URL);
                            Log.e("feedback resp", response);
                            Intent i = new Intent(getApplicationContext(), new SettingFlyout().getClass());
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(getApplicationContext()).add(sr);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Rate the Tutor.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),new SettingFlyout().getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
