package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ttl.project.thetalklist.Available_Tutor_Expanded;
import com.ttl.project.thetalklist.Bean.MessageModel;
import com.ttl.project.thetalklist.CircleTransform;
import com.ttl.project.thetalklist.FragmentStack;
import com.ttl.project.thetalklist.MessageOneToOne;
import com.ttl.project.thetalklist.R;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Saubhagyam on 21/06/2017.
 */

public class MessageRecyclarAdapter extends RecyclerView.Adapter<MessageRecyclarAdapter.MyViewHolder> {

    final Context context;
    private List<MessageModel> messageModelList;
    String pic;

    String op;
    String min;
    String hr;

    String time;

    public MessageRecyclarAdapter(Context context, List<MessageModel> messageModelList, String pic) {
        this.context = context;
        this.messageModelList = messageModelList;
        this.pic = pic;
    }


    public MessageRecyclarAdapter(Context context, List<MessageModel> messageModelList, String pic, String op, String min, String hr) {
        this.context = context;
        this.messageModelList = messageModelList;
        this.pic = pic;
        this.op = op;
        this.min = min;
        this.hr = hr;
    }

    public void addMsg(MessageModel m) {
        messageModelList.add(0, m);
        Toast.makeText(context, "adapter refresh", Toast.LENGTH_SHORT).show();
        this.notifyDataSetChanged();
    }

    @Override
    public MessageRecyclarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_user_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageRecyclarAdapter.MyViewHolder holder, int position) {

        Log.e("message adapter list", messageModelList.toString());

        final MessageModel messageModel = messageModelList.get(position);


        if (messageModel.getSender_id() == context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0)) {
            SharedPreferences preferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            String picPath = preferences.getString("pic", "");

            Log.e("message pic path", "https://www.thetalklist.com/uploads/images/" + picPath);
            if (!picPath.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.userImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.userImg);
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            String user_msg = messageModel.getMsg_text();
            String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
            holder.user_msg.setText(fromServerUnicodeDecoded);
        } else {
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.userLayout.setVisibility(View.GONE);
            String user_msg = messageModel.getMsg_text();
            String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
            holder.sender_msg.setText(fromServerUnicodeDecoded);


            Log.e("tutor pic", pic);
            if (!pic.equals("")) {
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + pic)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.senderImg);
            } else
                Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.senderImg);

        }


        holder.senderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.thetalklist.com/api/tutor_info?tutor_id=" +messageModel.getSender_id();
                Log.e("url", url);
                StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("tutor details", response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("status") == 0) {

                                JSONArray ary = obj.getJSONArray("tutor");
                                final JSONObject o = ary.getJSONObject(0);





                                final SharedPreferences preferences = context.getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("tutorName", o.getString("firstName")+" "+o.getString("lastName"));
                                editor.putInt("tutorRoleId", o.getInt("roleId"));
                                editor.putString("tutorPic", o.getString("pic"));
                                editor.putString("hRate", o.getString("hRate"));
                                editor.putString("avgRate", o.getString("avgRate"));
                                editor.putInt("tutorid", messageModel.getSender_id()).apply();

                                FragmentStack.getInstance().push(new MessageOneToOne());
                                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.viewpager,new Available_Tutor_Expanded()).commit();

                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                }

                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(context).add(sr);
            }
        });
        String date = messageModelList.get(position).getTime();
        Date date_txt = null;
        String[] months = {"Jan", "Feb", "Mar", "April", "may", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        try {
            if (date != null) {
                date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);

                int hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(date_txt));
                int month = Integer.parseInt(new SimpleDateFormat("MM").format(date_txt));
                int day = Integer.parseInt(new SimpleDateFormat("dd").format(date_txt));

                String newStr = new SimpleDateFormat("HH:mm").format(date_txt); // 9:00
                String h = new SimpleDateFormat("HH").format(date_txt); // 9:00
                String m = new SimpleDateFormat("mm").format(date_txt); // 9:00

                hour = Integer.parseInt(h)/*+Integer.parseInt(hr)+8*/;
                int minf = Integer.parseInt(m)/*+Integer.parseInt(min)*/;

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                        Locale.getDefault());
                Date currentLocalTime = calendar.getTime();
                DateFormat xd = new SimpleDateFormat("Z");
                String localTime = xd.format(currentLocalTime);
                Log.e("time zone", localTime);
                localTime=localTime.replace("+","");
                int xh=localTime.charAt(0)+localTime.charAt(1);
                int xmin=localTime.charAt(2)+localTime.charAt(3);

                int yh=hour+xh+8;
                int ymin=minf+xmin;
                Log.e("min & hour",String.valueOf(yh)+String.valueOf(ymin));


                if (hour > 24) {
                    if (minf > 60) {
                        time = months[month - 1] + " " + String.valueOf(day + 1) + " " + (hour - 24 + 1) + ":" + (minf - 60);
                    } else {
                        time = months[month - 1] + " " + String.valueOf(day + 1) + " " + (hour - 24) + ":" + minf;
                    }
                    holder.sender_time.setText(time);
                    holder.receiver_time.setText(time);

                } else {
                    if (minf > 60) {
                        time = months[month - 1] + " " + String.valueOf(day) + " " + (hour + 1) + ":" + (minf - 60);
                    } else {
                        time = months[month - 1] + " " + String.valueOf(day) + " " + hour + ":" + minf;
                    }
                    String time =/*newStr+" "+*/months[month - 1] + " " + String.valueOf(day) + " " + hour + ":" + minf;
                    holder.sender_time.setText(time);
                    holder.receiver_time.setText(time);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


//notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final EmojiconTextView user_msg, sender_msg;
        TextView sender_time, receiver_time;
        final LinearLayout senderLayout, userLayout;
        final ImageView senderImg, userImg;

        public MyViewHolder(View itemView) {
            super(itemView);


            senderLayout = (LinearLayout) itemView.findViewById(R.id.chat_sender_layout);
            userLayout = (LinearLayout) itemView.findViewById(R.id.chat_user_layout);
            senderImg = (ImageView) itemView.findViewById(R.id.chat_sender_img);
            userImg = (ImageView) itemView.findViewById(R.id.chat_user_img);
            sender_msg = (EmojiconTextView) itemView.findViewById(R.id.chat_sender_text);
            user_msg = (EmojiconTextView) itemView.findViewById(R.id.chat_user_text);
            sender_time = (TextView) itemView.findViewById(R.id.sender_time);
            receiver_time = (TextView) itemView.findViewById(R.id.receiver_time);
        }
    }
}
