package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


public class Notification extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_notification, container, false);

        final Switch message_switch= (Switch) view.findViewById(R.id.message_switch);
        SharedPreferences NotificationPref=getContext().getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=NotificationPref.edit();

        if (NotificationPref.getString("message", "").equals("true")) {
            message_switch.setChecked(true);
        }
        else {
            message_switch.setChecked(false);
        }


        message_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){


                    editor.putString("message","true").apply();
                }else editor.putString("message","false").apply();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
