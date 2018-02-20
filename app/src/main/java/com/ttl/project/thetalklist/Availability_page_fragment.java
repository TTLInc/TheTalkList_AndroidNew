package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Saubhagyam on 01/08/2017.
 */

public class Availability_page_fragment extends Fragment {

    View view;
    TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday, startTime, endTime;
    Button saveBtn;
    LinearLayout sunday_root, monday_root, tuesday_root, wednesday_root, thursday_root, friday_root, saturday_root;

    int sun = 0, mon = 0, tue = 0, wed = 0, thu = 0, fri = 0, sat = 0;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.availability_page_layout, null);

        sunday = (TextView) view.findViewById(R.id.sunday_tv);
        monday = (TextView) view.findViewById(R.id.monday_tv);
        tuesday = (TextView) view.findViewById(R.id.tuesday_tv);
        wednesday = (TextView) view.findViewById(R.id.wednesday_tv);
        thursday = (TextView) view.findViewById(R.id.thursday_tv);
        friday = (TextView) view.findViewById(R.id.friday_tv);
        saturday = (TextView) view.findViewById(R.id.saturday_tv);
        startTime = (TextView) view.findViewById(R.id.startTime_tv);
        endTime = (TextView) view.findViewById(R.id.endTime_tv);


        sunday_root = (LinearLayout) view.findViewById(R.id.sunday_root);
        monday_root = (LinearLayout) view.findViewById(R.id.monday_root);
        tuesday_root = (LinearLayout) view.findViewById(R.id.tuesday_root);
        wednesday_root = (LinearLayout) view.findViewById(R.id.wednesday_root);
        thursday_root = (LinearLayout) view.findViewById(R.id.thursday_root);
        friday_root = (LinearLayout) view.findViewById(R.id.friday_root);
        saturday_root = (LinearLayout) view.findViewById(R.id.saturday_root);

        startTime.setText("5:00 PM");
        endTime.setText("10:00 PM");



        String url1 = "https://www.thetalklist.com/api/tutor_availability_info?uid=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);

        StringRequest sr = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getContext(), "Response "+response, Toast.LENGTH_SHORT).show();
                Log.e("response availablity ", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.getInt("status") == 0) {

                        JSONObject info = res.getJSONObject("info");
                        startTime.setText(info.getString("start_time"));
                        endTime.setText(info.getString("end_time"));

                        if (info.getInt("sunday") == 1) {
                            sunday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            sunday.setTextColor(getResources().getColor(android.R.color.white));
                            sun = 1;
                        }

                        if (Integer.parseInt(info.getString("monday")) == 1) {
                            monday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            monday.setTextColor(getResources().getColor(android.R.color.white));
                            mon = 1;
                        }
                        if (info.getInt("tuesday") == 1) {
                            tuesday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            tuesday.setTextColor(getResources().getColor(android.R.color.white));
                            tue = 1;
                        }
                        if (info.getInt("wednesday") == 1) {
                            wednesday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            wednesday.setTextColor(getResources().getColor(android.R.color.white));
                            wed = 1;
                        }
                        if (info.getInt("thursday") == 1) {
                            thursday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            thursday.setTextColor(getResources().getColor(android.R.color.white));
                            thu = 1;
                        }
                        if (info.getInt("friday") == 1) {
                            friday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            friday.setTextColor(getResources().getColor(android.R.color.white));
                            fri = 1;
                        }
                        if (info.getInt("saturday") == 1) {
                            saturday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                            saturday.setTextColor(getResources().getColor(android.R.color.white));
                            sat = 1;
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error " + error, Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(sr);


        startTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                final int hour = 0;
                int minute = 00;
                int h = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String format = showTime(hourOfDay, minute);

                                String min_txt = "";

                                if (hourOfDay > 12) {
                                    hourOfDay = hourOfDay - 12;
                                }


                                if (hourOfDay == 0)
                                    hourOfDay = 12;
                                if (minute == 0) {
                                    min_txt = "00";

                                    startTime.setText(hourOfDay + ":" + min_txt + " " + format);
                                } else
                                    startTime.setText(hourOfDay + ":" + String.format("%02d", minute) + " " + format);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();


            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = 0;
                int minute = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String format = showTime(hourOfDay, minute);

                                String min_txt = "";

                                if (hourOfDay > 12) {
                                    hourOfDay = hourOfDay - 12;
                                }


                                if (hourOfDay == 0)
                                    hourOfDay = 12;
                                if (minute == 0) {
                                    min_txt = "00";

                                    endTime.setText(hourOfDay + ":" + min_txt + " " + format);
                                } else
                                    endTime.setText(hourOfDay + ":" + String.format("%02d", minute) + " " + format);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });


        saveBtn = (Button) view.findViewById(R.id.Availablity_save_button);


        sunday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), "iusg8afgagf8sdagfdsabfasbfuaf", Toast.LENGTH_SHORT).show();

                if (sun == 0) {
                    sunday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    sunday.setTextColor(getResources().getColor(android.R.color.white));
                    sun = 1;
                } else {
                    sunday_root.setBackgroundResource(0);
                    sunday.setTextColor(Color.parseColor("#696969"));
                    sun = 0;
                }
            }
        });


        monday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (mon == 0) {
                    monday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    monday.setTextColor(getResources().getColor(android.R.color.white));
                    mon = 1;
                } else {
                    monday_root.setBackgroundResource(0);
                    monday.setTextColor(Color.parseColor("#696969"));
                    mon = 0;
                }
            }
        });

        tuesday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (tue == 0) {
                    tuesday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    tuesday.setTextColor(getResources().getColor(android.R.color.white));
                    tue = 1;
                } else {
                    tuesday_root.setBackgroundResource(0);
                    tuesday.setTextColor(Color.parseColor("#696969"));
                    tue = 0;
                }

            }
        });


        wednesday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (wed == 0) {
                    wednesday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    wednesday.setTextColor(getResources().getColor(android.R.color.white));
                    wed = 1;
                } else {
                    wednesday_root.setBackgroundResource(0);
                    wednesday.setTextColor(Color.parseColor("#696969"));
                    wed = 0;
                }
            }
        });

        thursday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (thu == 0) {
                    thursday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    thursday.setTextColor(getResources().getColor(android.R.color.white));
                    thu = 1;
                } else {
                    thursday_root.setBackgroundResource(0);
                    thursday.setTextColor(Color.parseColor("#696969"));
                    thu = 0;
                }
            }
        });

        friday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (fri == 0) {
                    friday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    friday.setTextColor(getResources().getColor(android.R.color.white));
                    fri = 1;
                } else {
                    friday_root.setBackgroundResource(0);
                    friday.setTextColor(Color.parseColor("#696969"));
                    fri = 0;
                }

            }
        });

        saturday_root.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (sat == 0) {
                    saturday_root.setBackground(getResources().getDrawable(R.drawable.roundcornered_orange));
                    saturday.setTextColor(getResources().getColor(android.R.color.white));
                    sat = 1;
                } else {
                    saturday_root.setBackgroundResource(0);
                    saturday.setTextColor(Color.parseColor("#696969"));
                    sat = 0;
                }
            }
        });


        if (getActivity().getClass().toString().equals("class com.ttl.project.thetalklist.Registration")){
            ((TextView)getActivity().findViewById(R.id.registration_line)).setText("Nice! The world sees your updated profile.  Set a general availability window or just toggle your Tutor light on whenever you want.");
            ((LinearLayout)view.findViewById(R.id.availibility_linearnotmyDetails)).setVisibility(View.GONE);

        }
        else {
            ((TextView)view.findViewById(R.id.availibility_registration_line)).setText("Nice! The world sees your updated profile.  Set a general availability window or just toggle your Tutor light on whenever you want.");
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.thetalklist.com/api/tutor_availability?" +
                        "uid=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) +
                        "&startTime=" + startTime.getText().toString().replace(" ", "%20") +
                        "&endTime=" + endTime.getText().toString().replace(" ", "%20") +
                        "&sunday=" + String.valueOf(sun) +
                        "&monday=" + String.valueOf(mon) +
                        "&tuesday=" + String.valueOf(tue) +
                        "&wednesday=" + String.valueOf(wed) +
                        "&thursday=" + String.valueOf(thu) +
                        "&friday=" + String.valueOf(fri) +
                        "&saturday=" + String.valueOf(sat);

                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getInt("status") == 0) {
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                if (getActivity().getClass().toString().equals("class com.ttl.project.thetalklist.Registration")) {

                                    Intent i=new Intent(getActivity(),SettingFlyout.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                }
                                else getFragmentManager().beginTransaction().replace(R.id.viewpager, new Available_tutor()).commit();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "error " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                Volley.newRequestQueue(getContext()).add(sr);

            }
        });


        return view;
    }




    //To show the time
    public String showTime(int hour, int min) {

        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return format;
    }


}
