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

public class IntroScreen3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.introscreen3, container, false);
        TextView t6,t7,t8;
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"fonts/GothamBookRegular.ttf");
        t6=(TextView)view.findViewById(R.id.txts6);
        t6.setTypeface(typeface);
        t7=(TextView)view.findViewById(R.id.txts7);
        t7.setTypeface(typeface);
        t8=(TextView)view.findViewById(R.id.txts8);
        t8.setTypeface(typeface);
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.set_left_in,R.anim.set_left_out);
        return view;
    }
}
