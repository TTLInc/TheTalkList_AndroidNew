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

public class Request_Form extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView subjectText, levelText, timeText;
    private EditText subjectEdit, levelEdit, timeEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        sharedPreferences = getSharedPreferences( "Request_Form", Context.MODE_PRIVATE );

        Button submit = (Button)findViewById( R.id.submit_button );
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectText = (TextView)findViewById( R.id.SubjectTextview );
                levelText = ( TextView )findViewById( R.id.LevelTextView );
                timeText = ( TextView )findViewById( R.id.TimeTextview );

                subjectEdit = ( EditText )findViewById( R.id.subject );
                levelEdit = ( EditText )findViewById( R.id.level );
                timeEdit = ( EditText )findViewById( R.id.time );

                String subText = substring( subjectText.getText().toString());
                String levText = substring( levelText.getText().toString());
                String timText = substring( timeText.getText().toString());

                String subject = subjectEdit.getText().toString();
                String level = levelEdit.getText().toString();
                String time = timeEdit.getText().toString();

                String send =
                    subText + " " + subject + System.lineSeparator() + levText + " " + level +
                            System.lineSeparator() + timText + ": " + time;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString( "Request_Form", send );
                editor.commit();
                finish();
            }
        });
    }
    private String substring( String value ) {
        return value.substring( 0, value.length() - 3 );
    }
}
