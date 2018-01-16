package com.ttl.project.thetalklist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

/**
 * Created by Saubhagyam on 03/06/2017.
 */

public class TTL_Score_Drawer_viewpager extends Fragment {


    RadioButton ttlscore_100,ttlscore_200,ttlscore_300;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    int one_bit,two_bit,three_bit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.ttl_score_drawer_fragment,container,false);

//        ttlscore_300= (RadioButton) view.findViewById(R.id.ttlscore_300);
//        ttlscore_200= (RadioButton) view.findViewById(R.id.ttlscore_200);
//        ttlscore_100= (RadioButton) view.findViewById(R.id.ttlscore_100);


       /* ttlscore_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (one_bit==0){
                    ttlscore_100.setChecked(true);
                    one_bit=1;
                }else {
                    ttlscore_100.setChecked(false);
                    one_bit=0;
                }
            }
        });

        ttlscore_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (two_bit==0){
                    ttlscore_200.setChecked(true);
                    two_bit=1;
                }else {
                    ttlscore_200.setChecked(false);
                    two_bit=0;
                }
            }
        });

        ttlscore_300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (three_bit==0){
                    ttlscore_300.setChecked(true);
                    three_bit=1;
                }else {
                    ttlscore_300.setChecked(false);
                    three_bit=0;
                }
            }
        });*/


        return view;
    }

}
