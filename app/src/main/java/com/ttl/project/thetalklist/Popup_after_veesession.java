package com.ttl.project.thetalklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Popup_after_veesession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_after_veesession);

        ((TextView)findViewById(R.id.after_veesession_text)).setText("Your session with "+getIntent().getStringExtra("name")+" cost you "+getIntent().getStringExtra("cost")+" credits.\n" +
                "        Our community appreciates your business.");


        ((Button)findViewById(R.id.after_veesession_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), StudentFeedBack.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
