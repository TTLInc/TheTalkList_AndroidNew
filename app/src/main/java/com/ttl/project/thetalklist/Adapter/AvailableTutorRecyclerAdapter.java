package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ttl.project.thetalklist.Available_Tutor_Expanded;
import com.ttl.project.thetalklist.Available_tutor;
import com.ttl.project.thetalklist.CircleTransform;
import com.ttl.project.thetalklist.FragmentStack;
import com.ttl.project.thetalklist.MessageFragment;
import com.ttl.project.thetalklist.R;
import com.ttl.project.thetalklist.TTL;
import com.ttl.project.thetalklist.TabBackStack;
import com.ttl.project.thetalklist.model.FilterTutorsModel;
import com.ttl.project.thetalklist.model.SearchTutorsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 17/04/2017.
 */


//Used for available tutor recyclarview Adapter

public class AvailableTutorRecyclerAdapter extends RecyclerView.Adapter<AvailableTutorRecyclerAdapter.MyViewHolder> {

    private final Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentStack fragmentStack;
    int withPos = 0;
    int withfav = 0;
    int pos;
    JSONObject object;
    String FirstName;
    SharedPreferences pref1;
    SharedPreferences.Editor ed;
    SharedPreferences pref;
    String Flag = "1";
    TabBackStack tabBackStack;
    List<SearchTutorsModel.TutorsBean> body;

    public AvailableTutorRecyclerAdapter(Context context) {
        this.context = context;
    }

    public AvailableTutorRecyclerAdapter(Context context, List<SearchTutorsModel.TutorsBean> body, FragmentManager fragmentManager) {
        this.context = context;
        this.body = body;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_tutor_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
/*
   @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentStack = FragmentStack.getInstance();
        try {
            object = (JSONObject) array.get(position);
            if (!.getString("avgRate").equalsIgnoreCase(""))
                holder.ratingBar.setRating(Float.parseFloat(object.getString("avgRate")));
            else holder.ratingBar.setRating(0f);
            Log.e("available tutor obj", object.toString());
            Log.e("Tag", "onBindViewHolder: " + object.getInt("readytotalk"));
            if (object.getInt("readytotalk") == 0) {
                Flag = "0";
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.disabled_video));
            }else {
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.video));

            }

            final String picPath = object.getString("pic");
            final int tutorId = object.getInt("uid");
            if (withPos == 1) {
                if (position == pos) {

                    if (withfav == 1) {
                        holder.favoriteTag.setVisibility(View.VISIBLE);

                        pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();


                        new CountDownTimer(5000, 1000) { // 5000 = 5 sec

                            public void onTick(long millisUntilFinished) {
                                holder.TutorImg.setImageResource(R.drawable.favorite);
                            }

                            public void onFinish() {


                                if (!picPath.equals("")) {

                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + picPath)
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(holder.TutorImg);
                                } else
                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(holder.TutorImg);
                            }
                        }.start();
                    } else {
                        holder.favoriteTag.setVisibility(View.GONE);
                    }
                }
            }

            if (object.getInt("isMyFavourite") == 1) {
                holder.favoriteTag.setVisibility(View.GONE);

            } else holder.favoriteTag.setVisibility(View.GONE);
            FirstName = object.getString("firstName");


            holder.fn.setText(object.getString("firstName") + " " + object.getString("lastName"));
            holder.uid.setText(object.getString("uid"));
            holder.hpr.setText(object.getString("hRate"));


            if (!picPath.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.TutorImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.TutorImg);

            SharedPreferences chatPref = context.getSharedPreferences("chatPref", Context.MODE_PRIVATE);
            final SharedPreferences.Editor chatPrefEditor = chatPref.edit();


            holder.msgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFragment messageList = new MessageFragment();
                    chatPrefEditor.putString("firstName", FirstName);
                    chatPrefEditor.putInt("receiverId", tutorId).apply();
                    TTL ttl = (TTL) context.getApplicationContext();
                    ttl.ExitBit = 1;
                    fragmentStack.push(new Available_tutor());
                    tabBackStack.setTabPosition(0);
                    fragmentTransaction.replace(R.id.viewpager, messageList).commit();
                }
            });

            holder.msgButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFragment messageList = new MessageFragment();
                    chatPrefEditor.putString("firstName", FirstName);
                    chatPrefEditor.putInt("receiverId", tutorId).apply();
                    TTL ttl = (TTL) context.getApplicationContext();
                    ttl.ExitBit = 1;
                    fragmentStack.push(new Available_tutor());
                    tabBackStack.setTabPosition(0);
                    fragmentTransaction.replace(R.id.viewpager, messageList).commit();

                }
            });

            final SharedPreferences preferences = context.getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            final Float hPr = (Float.parseFloat(holder.hpr.getText().toString()) / 25);
            String h_str = String.format("%.02f", hPr);
            holder.CpM.setText(h_str);
            if (object.getInt("readytotalk") == 0) {
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.disabled_video));
               *//* holder.VideocallButton1.setClickable(false);
                holder.VideocallButton.setClickable(false);*//*
            } else {
                holder.VideocallButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TTL ttl = (TTL) context.getApplicationContext();
                        ttl.ExitBit = 1;
                        fragmentStack.push(new Available_tutor());


                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("flag", 1);
                        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        editor.putInt("studentId", pref.getInt("id", 0));
                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("tutorId", Integer.parseInt(holder.uid.getText().toString()));
                        editor.putFloat("hRate", Float.parseFloat(holder.CpM.getText().toString()));
                        editor.putFloat("credit", hPr).apply();
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor(preferences.getInt("flag", 0), preferences.getFloat("credit", 0), preferences.getString("tutorName", "")), "Available Tutor").commit();
                    }
                });


                holder.VideocallButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TTL ttl = (TTL) context.getApplicationContext();
                        ttl.ExitBit = 1;
                        fragmentStack.push(new Available_tutor());


                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("flag", 1);
                        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        editor.putInt("studentId", pref.getInt("id", 0));
                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("tutorId", Integer.parseInt(holder.uid.getText().toString()));
                        editor.putFloat("hRate", Float.parseFloat(holder.CpM.getText().toString()));
                        editor.putFloat("credit", hPr).apply();
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor(preferences.getInt("flag", 0), preferences.getFloat("credit", 0), preferences.getString("tutorName", "")), "Available Tutor").commit();

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabBackStack = TabBackStack.getInstance();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref1 = context.getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
                ed = pref1.edit();
                ed.putInt("position", position).apply();
                TTL ttl = (TTL) context.getApplicationContext();
                ttl.ExitBit = 1;
                int redyTtalk = 0;
                fragmentStack.push(new Available_tutor());
                Available_Tutor_Expanded available_tutoe_expanded = new Available_Tutor_Expanded();
                try {
                    redyTtalk = object.getInt("readytotalk");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = array.getJSONObject(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("Tutorid", jsonObject.getString("uid"));
                    bundle.putInt("redyTotalk", redyTtalk);
                    Log.e("TAG", "ImageId--> " + jsonObject.getString("uid"));
                    SharedPreferences preferences = context.getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("tutorName", jsonObject.getString("firstName"));
                    editor.putInt("tutorRoleId", jsonObject.getInt("roleId"));
                    editor.putString("tutorPic", jsonObject.getString("pic"));
                    editor.putString("hRate", jsonObject.getString("hRate"));
                    editor.putString("avgRate", jsonObject.getString("avgRate"));
                    editor.putInt("tutorid", jsonObject.getInt("uid")).apply();
                    available_tutoe_expanded.setArguments(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tabBackStack.setTabPosition(0);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, available_tutoe_expanded).commit();


            }
        });


    }*/

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentStack = FragmentStack.getInstance();
        try {

            if (!body.get(position).getAvgRate().equalsIgnoreCase(""))
                holder.ratingBar.setRating(Float.parseFloat(body.get(position).getAvgRate()));
            else holder.ratingBar.setRating(0f);
            //  Log.e("available tutor obj", object.toString());
            //   Log.e("Tag", "onBindViewHolder: " + object.getInt("readytotalk"));
            if (body.get(position).getReadytotalk().equals("0")) {
                Flag = "0";
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.disabled_video));
            } else {
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.video));

            }

            final String picPath = body.get(position).getPic();
            final String tutorId = body.get(position).getId();
            if (withPos == 1) {
                if (position == pos) {

                    if (withfav == 1) {
                        holder.favoriteTag.setVisibility(View.VISIBLE);

                        pref = getApplicationContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();


                        new CountDownTimer(5000, 1000) { // 5000 = 5 sec

                            public void onTick(long millisUntilFinished) {
                                holder.TutorImg.setImageResource(R.drawable.favorite);
                            }

                            public void onFinish() {


                                if (!picPath.equals("")) {

                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/uploads/images/" + picPath)
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(holder.TutorImg);
                                } else
                                    Glide.with(getApplicationContext()).load("https://www.thetalklist.com/images/header.jpg")
                                            .crossFade()
                                            .thumbnail(0.5f)
                                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(holder.TutorImg);
                            }
                        }.start();
                    } else {
                        holder.favoriteTag.setVisibility(View.GONE);
                    }
                }
            }

            if (body.get(position).getIsMyFavourite().equals("1")) {
                holder.favoriteTag.setVisibility(View.GONE);

            } else holder.favoriteTag.setVisibility(View.GONE);
            FirstName = body.get(position).getFirstName();


            holder.fn.setText(body.get(position).getFirstName() + " " + body.get(position).getLastName());
            holder.uid.setText(body.get(position).getId());
            holder.hpr.setText(body.get(position).getHRate());


            if (!picPath.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.TutorImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.TutorImg);

            SharedPreferences chatPref = context.getSharedPreferences("chatPref", Context.MODE_PRIVATE);
            final SharedPreferences.Editor chatPrefEditor = chatPref.edit();


            holder.msgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFragment messageList = new MessageFragment();
                    chatPrefEditor.putString("firstName", FirstName);
                    chatPrefEditor.putInt("receiverId", Integer.parseInt(tutorId)).apply();
                    TTL ttl = (TTL) context.getApplicationContext();
                    ttl.ExitBit = 1;
                    fragmentStack.push(new Available_tutor());
                    tabBackStack.setTabPosition(0);
                    fragmentTransaction.replace(R.id.viewpager, messageList).commit();
                }
            });

            holder.msgButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageFragment messageList = new MessageFragment();
                    chatPrefEditor.putString("firstName", FirstName);
                    chatPrefEditor.putInt("receiverId", Integer.parseInt(tutorId)).apply();
                    TTL ttl = (TTL) context.getApplicationContext();
                    ttl.ExitBit = 1;
                    fragmentStack.push(new Available_tutor());
                    tabBackStack.setTabPosition(0);
                    fragmentTransaction.replace(R.id.viewpager, messageList).commit();

                }
            });

            final SharedPreferences preferences = context.getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            final Float hPr = (Float.parseFloat(holder.hpr.getText().toString()) / 25);
            String h_str = String.format("%.02f", hPr);
            holder.CpM.setText(h_str);
            if (body.get(position).getReadytotalk().equals("0")) {
                holder.VideocallButton1.setImageDrawable(context.getResources().getDrawable(R.drawable.disabled_video));
                //* holder.VideocallButton1.setClickable(false);
                holder.VideocallButton.setClickable(false);//*
            } else {
                holder.VideocallButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TTL ttl = (TTL) context.getApplicationContext();
                        ttl.ExitBit = 1;
                        fragmentStack.push(new Available_tutor());


                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("flag", 1);
                        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        editor.putInt("studentId", pref.getInt("id", 0));
                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("tutorId", Integer.parseInt(holder.uid.getText().toString()));
                        editor.putFloat("hRate", Float.parseFloat(holder.CpM.getText().toString()));
                        editor.putFloat("credit", hPr).apply();
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor(preferences.getInt("flag", 0), preferences.getFloat("credit", 0), preferences.getString("tutorName", "")), "Available Tutor").commit();
                    }
                });


                holder.VideocallButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TTL ttl = (TTL) context.getApplicationContext();
                        ttl.ExitBit = 1;
                        fragmentStack.push(new Available_tutor());


                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("flag", 1);
                        pref = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        editor.putInt("studentId", pref.getInt("id", 0));
                        editor.putString("tutorName", holder.fn.getText().toString());
                        editor.putInt("tutorId", Integer.parseInt(holder.uid.getText().toString()));
                        editor.putFloat("hRate", Float.parseFloat(holder.CpM.getText().toString()));
                        editor.putFloat("credit", hPr).apply();
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor(preferences.getInt("flag", 0), preferences.getFloat("credit", 0), preferences.getString("tutorName", "")), "Available Tutor").commit();

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        tabBackStack = TabBackStack.getInstance();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref1 = context.getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
                ed = pref1.edit();
                ed.putInt("position", position).apply();
                TTL ttl = (TTL) context.getApplicationContext();
                ttl.ExitBit = 1;
                String redyTtalk = "0";
                fragmentStack.push(new Available_tutor());
                Available_Tutor_Expanded available_tutoe_expanded = new Available_Tutor_Expanded();
                try {
                    redyTtalk = body.get(position).getReadytotalk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //JSONObject jsonObject = array.getJSONObject(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("Tutorid", body.get(position).getId());
                    bundle.putInt("redyTotalk", Integer.parseInt(redyTtalk));
                    Log.e("TAG", "ImageId--> " + body.get(position).getId());
                    SharedPreferences preferences = context.getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("tutorName", body.get(position).getFirstName());
                    editor.putInt("tutorRoleId", Integer.parseInt(body.get(position).getRoleId()));
                    editor.putString("tutorPic", body.get(position).getPic());
                    editor.putString("hRate", body.get(position).getHRate());
                    editor.putString("avgRate", body.get(position).getAvgRate());
                    editor.putInt("tutorid", Integer.parseInt(body.get(position).getId())).apply();
                    available_tutoe_expanded.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tabBackStack.setTabPosition(0);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewpager, available_tutoe_expanded).commit();


            }
        });


    }


    @Override
    public int getItemCount() {
        return body == null ? 0 : body.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView fn;
        final TextView ln;
        final TextView CpM;
        final TextView uid;
        final TextView hpr;
        final ImageView TutorImg;
        final ImageView VideocallButton1;
        final ImageView msgButton1;
        final LinearLayout favoriteTag;
        final LinearLayout VideocallButton;
        final LinearLayout msgButton;
        final RatingBar ratingBar;
        final View view;


        public MyViewHolder(View itemView) {
            super(itemView);

            msgButton = (LinearLayout) itemView.findViewById(R.id.msgButton);
            VideocallButton = (LinearLayout) itemView.findViewById(R.id.videocallButton);
            msgButton1 = (ImageView) itemView.findViewById(R.id.msgButton1);
            VideocallButton1 = (ImageView) itemView.findViewById(R.id.videocallButton1);
            fn = (TextView) itemView.findViewById(R.id.availableTutorListFirstName);
            uid = (TextView) itemView.findViewById(R.id.availableTutor_uid);
            hpr = (TextView) itemView.findViewById(R.id.availableTutor_hpr);
            CpM = (TextView) itemView.findViewById(R.id.availableTutorListCPS);
            ln = (TextView) itemView.findViewById(R.id.availableTutorListLastName);
            TutorImg = (ImageView) itemView.findViewById(R.id.TutorImg);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            favoriteTag = (LinearLayout) itemView.findViewById(R.id.availableTutorListLayoutFavoriteTag);

            view = itemView.findViewById(R.id.available_tutor_layout_id);


        }


    }


}
