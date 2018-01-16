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

public class IntroScreen1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.introscreen1, container, false);
        TextView txt1,txt2;
        Typeface typeface= Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
        txt1= (TextView) view.findViewById(R.id.txts1);
        txt2=(TextView)view.findViewById(R.id.txts2);
        txt1.setTypeface(typeface);
        txt2.setTypeface(typeface);

        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.set_left_in,R.anim.set_left_out);

        return view;
    }
}
