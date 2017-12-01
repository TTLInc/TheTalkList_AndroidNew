package com.ttl.project.thetalklist;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Registration extends AppCompatActivity {


    LinearLayout registration_viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registration_viewpager= (LinearLayout) findViewById(R.id.registration_viewpager);

        getSupportFragmentManager().beginTransaction().replace(R.id.registration_viewpager,new Tablayout_with_viewpager()).commit();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentStack fragments=FragmentStack.getInstance();

        if (fragments.size()>0)
        {
            Fragment fragment = fragments.pop();
            getSupportFragmentManager().beginTransaction().replace(R.id.registration_viewpager,fragment, fragment.getClass().toString()).commit();



        }
    }
}
