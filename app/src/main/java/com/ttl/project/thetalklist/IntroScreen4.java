package com.ttl.project.thetalklist;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by saubhagyam on 2/23/2017.
 */

public class IntroScreen4 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.introscreen4, container, false);
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
        TextView t9,t10;
        t9=(TextView)view.findViewById(R.id.txts9);
        t9.setTypeface(typeface);
        t10=(TextView)view.findViewById(R.id.txts10);
        t10.setTypeface(typeface);
        Button b=(Button)view.findViewById(R.id.Startbtn1);
        b.setTypeface(typeface);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getContext(),MyDetailsNotRegistered.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.set_left_in,R.anim.set_left_out);
                getActivity().finish();
            }
        });
        return view;
    }
}
