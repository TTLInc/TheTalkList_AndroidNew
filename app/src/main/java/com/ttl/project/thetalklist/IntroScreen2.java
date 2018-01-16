package com.ttl.project.thetalklist;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by saubhagyam on 2/23/2017.
 */

public class IntroScreen2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.introscreen2, container, false);
        TextView txt3,t4,t5;
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
        txt3=(TextView)view.findViewById(R.id.txts3);
        txt3.setTypeface(typeface);
        t4=(TextView)view.findViewById(R.id.txts4);
        t4.setTypeface(typeface);
        t5=(TextView)view.findViewById(R.id.txts5);
        t5.setTypeface(typeface);
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.set_left_in,R.anim.set_left_out);
        return view;
    }
}
