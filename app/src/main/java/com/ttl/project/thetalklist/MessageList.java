package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.MessageListRecyclerAdapter;
import com.ttl.project.thetalklist.Bean.ChatroomModel;
import com.ttl.project.thetalklist.Services.MessageCountService;
import com.ttl.project.thetalklist.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

//Chatroom list class
public class MessageList extends Fragment {

    RequestQueue queue;
    List<ChatroomModel> chatroomModelList;
    RecyclerView recyclerView;

    TextView bottombar_message_count;
    RelativeLayout bottombar_messageCount_layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_message_list, container, false);

        ((ImageView) (getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg))).setImageDrawable(getResources().getDrawable(R.drawable.messages_activated));
        ((TextView) getActivity().findViewById(R.id.txtMessages)).setTextColor(Color.parseColor("#3399CC"));
        ((ImageView) (getActivity().findViewById(R.id.imageView13))).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
        ((TextView) getActivity().findViewById(R.id.txtTutors)).setTextColor(Color.parseColor("#666666"));
        int roleId = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("roleId", 0);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        View view1 = toolbar.getRootView();
        view1.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        bottombar_message_count = (TextView) getActivity().findViewById(R.id.bottombar_message_count);
        bottombar_messageCount_layout = (RelativeLayout) getActivity().findViewById(R.id.bottombar_messageCount_layout);


        MessageCountService messageCountService = new MessageCountService();
        messageCountService.MessageCount(getActivity(), getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE));

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String URL = "https://www.thetalklist.com/api/chatroom_list?sender_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        ;
        Log.e("chatroom list url", URL);
        queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("chatroom response", response);

                chatroomModelList = new ArrayList<>();
//
                try {
                    JSONObject chatroomObj = new JSONObject(response);
                    if (chatroomObj.getInt("status") == 0) {

                        JSONArray charoomArray = chatroomObj.getJSONArray("data");
//                    JSONArray timeArray=chatroomObj.getJSONArray("time");
                        if (charoomArray.length() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            dialog.dismiss();
                            view.findViewById(R.id.errormsg_Message).setVisibility(View.VISIBLE);
                        } else {


                            for (int i = 0; i < charoomArray.length(); i++) {
                                JSONObject obj = charoomArray.getJSONObject(i);
//                                JSONObject timeobj=timeArray.getJSONObject(i);
                                JSONObject nameAry = obj.getJSONObject("0");

                                JSONArray unreadAry = obj.getJSONArray("unread");
                                JSONObject unreadObj = unreadAry.getJSONObject(0);

                                JSONArray last_message_timeAry = obj.getJSONArray("last_message_time");
                                JSONObject last_message_timeObj = last_message_timeAry.getJSONObject(0);

                                ChatroomModel chatroomModel = new ChatroomModel();
                                chatroomModel.setSenderName(nameAry.getString("firstName"));
                                chatroomModel.setSenderPic(nameAry.getString("pic"));
                                if (!last_message_timeObj.getString("last_message_time").equals("null"))
                                    chatroomModel.setLastTime(last_message_timeObj.getString("last_message_time"));
                                else chatroomModel.setLastTime("");
                                chatroomModel.setUnread(unreadObj.getInt("unread"));
                                chatroomModel.setSenderId(nameAry.getInt("uid"));


                                chatroomModelList.add(0, chatroomModel);

                            }
                            Collections.reverse(chatroomModelList);

                            Log.e("chatroom list before ", chatroomModelList.toString());

                            FragmentManager fragmentManager = getFragmentManager();

                            final MessageListRecyclerAdapter messageListRecyclerAdapter = new MessageListRecyclerAdapter(getApplicationContext(), chatroomModelList, fragmentManager);
                            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(messageListRecyclerAdapter);
                            messageListRecyclerAdapter.notifyDataSetChanged();

                            SharedPreferences chatPref = getApplicationContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor chatPrefEditor = chatPref.edit();


                            dialog.dismiss();
                        }

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if(Config.msgCount>0){
            bottombar_message_count.setText(String.valueOf(Config.msgCount));

        }else {
            bottombar_message_count.setVisibility(View.GONE);
        }*/

        ((ImageView) (getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg))).setImageDrawable(getResources().getDrawable(R.drawable.messages_activated));
        ((TextView) getActivity().findViewById(R.id.txtMessages)).setTextColor(Color.parseColor("#3399CC"));
        ((ImageView) (getActivity().findViewById(R.id.imageView13))).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
        ((TextView) getActivity().findViewById(R.id.txtTutors)).setTextColor(Color.parseColor("#666666"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);*/
        super.onCreate(savedInstanceState);


    }
}
