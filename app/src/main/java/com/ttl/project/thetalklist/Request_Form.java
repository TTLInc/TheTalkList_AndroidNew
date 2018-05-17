package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Request_Form extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private EditText subjectEdit, levelEdit, timeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        sharedPreferences = getSharedPreferences( "Request_Form", Context.MODE_PRIVATE );

        subjectEdit = ( EditText )findViewById( R.id.subject );
        levelEdit = ( EditText )findViewById( R.id.level );
        timeEdit = ( EditText )findViewById( R.id.time2 );
        try {
            subjectEdit.setText( sharedPreferences.getString( "Subject Edit", null ));
            levelEdit.setText( sharedPreferences.getString( "Level Edit", null ));
            timeEdit.setText( sharedPreferences.getString( "Time Edit", null ));
        }
        catch( Exception e ) {
        }


        Button submit = (Button)findViewById( R.id.request_submit );
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
                }
                else
                    subjectCompleted = 1;

                //level
                if (level.equals("")) {
                    Toast.makeText(getApplicationContext(), "Level missing!",
                            Toast.LENGTH_LONG).show();
                    levelCompleted = 0;
                }
                else
                    levelCompleted = 1;

                //time
                if (time.equals("")) {
                    Toast.makeText(getApplicationContext(), "Desired time missing!",
                            Toast.LENGTH_LONG).show();
                    timeCompleted = 0;
                }
                else
                    timeCompleted = 1;

                if (subjectCompleted == 1 && levelCompleted == 1 && timeCompleted == 1) {
                    String send =
                            "I need help with: " + subject + System.lineSeparator() +
                                    "My level is: " + level + System.lineSeparator() +
                                    "I need help at this time: " + time;

                    // put texts to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Request_Form", send);
                    editor.putString("Subject Edit", subject);
                    editor.putString("Level Edit", level);
                    editor.putString("Time Edit", time);
                    editor.putInt("Request Submitted", 1);
                    editor.apply();
                    finish();
                }
            }
        });
    }
}
