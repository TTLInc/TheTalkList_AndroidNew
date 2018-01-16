package com.ttl.project.thetalklist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by saubhagyam on 2/25/2017.
 */

public class PopUpWindowActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7;
    ImageView i1,i2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupscreen);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/GothamBookRegular.ttf");

        t1=(TextView)findViewById(R.id.customPopUpText1);
        t1.setTypeface(typeface);
        t2=(TextView)findViewById(R.id.customPopUpText2);
        t2.setTypeface(typeface);
        t3=(TextView)findViewById(R.id.customPopUpText3);
        t3.setTypeface(typeface);
        t4=(TextView)findViewById(R.id.customPopUpText4);
        t4.setTypeface(typeface);
        t5=(TextView)findViewById(R.id.textstudent);
        t5.setTypeface(typeface);
        t6=(TextView)findViewById(R.id.texttutor);
        t6.setTypeface(typeface);

        i1=(ImageView)findViewById(R.id.student);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PopUpWindowActivity.this,IntroScreenSwipe.class);
                startActivity(intent);
                finish();
            }
        });

        i2=(ImageView)findViewById(R.id.tutor);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PopUpWindowActivity.this,IntroScreenSwipe.class);
                startActivity(intent);
                finish();
            }
        });






    }
}
