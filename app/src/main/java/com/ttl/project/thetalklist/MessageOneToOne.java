package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.MessageRecyclarAdapter;
import com.ttl.project.thetalklist.Bean.MessageModel;
import com.ttl.project.thetalklist.Decorations.DividerItemDecoration;
import com.ttl.project.thetalklist.Services.MessageCountService;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 18/04/2017.
 */

public class MessageOneToOne extends Fragment implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    View view;
    View msgDisplayLayoutview;
    EmojiconEditText message_editText_msg;
    EmojiconTextView user_msg, sender_msg;
    LinearLayout senderLayout, userLayout;
    ImageView message_sendBtn, message_searchBtn, senderImg, userImg/*, message_onetoone_backbtn*/;
    public RecyclerView recyclerView;
    public List<MessageModel> messageModelList;
    public MessageRecyclarAdapter messageRecyclarAdapter;
    LinearLayoutManager mLayoutManager;

    int receiver_id;
    int sender_id;
    String sender_name;
    RequestQueue queue, queue1;
    TextView chat_header;

    public MessageOneToOne() {
    }


    SharedPreferences chatPref,loginPref;

    BroadcastReceiver appendChatScreenMsgReceiver;

    @Override
    public boolean getUserVisibleHint() {


        RefreshFragment();

        return super.getUserVisibleHint();

    }

    public void RefreshFragment() {
        String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
        Log.e("Message list url", URL);

        messageModelList.clear();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("message response", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 0) {
                        dialog.dismiss();
                        JSONArray msgAry = jsonObject.getJSONArray("messages");
                        String pic = jsonObject.getString("tutor_pic");
                        chat_header.setText(jsonObject.getString("tutor_name"));
                        if (msgAry.length() == 0) {

                        } else {
                            for (int i = 0; i < msgAry.length(); i++) {
                                JSONObject msgObj = msgAry.getJSONObject(i);
                                MessageModel messageModel = new MessageModel();
                                messageModel.setMsg_id(msgObj.getInt("id"));
                                messageModel.setMsg_text(msgObj.getString("message"));
                                messageModel.setSender_id(msgObj.getInt("user_id"));
                                messageModel.setSender_name(msgObj.getString("user_name"));
                                messageModel.setTime(msgObj.getString("time"));
                                messageModelList.add(0, messageModel);
                            }
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            recyclerView.removeAllViews();
                            messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"));
                            Log.e("locale android ", new TTL().getUserCountry(getContext()));


                            /*try {
                                JSONArray timeZoneObj=new JSONArray(new TTL().json);

                                for (int i=0;i<timeZoneObj.length();i++){
                                    JSONObject obj=timeZoneObj.getJSONObject(i);
                                    if (obj.getString("IsoAlpha2").equalsIgnoreCase( new TTL().getUserCountry(getContext()).toUpperCase())){
                                        JSONArray winAry=obj.getJSONArray("WindowsTimeZones");
                                        JSONObject o=winAry.getJSONObject(0);
                                        String name=o.getString("Name");

                                        String userName = name.substring(name.lastIndexOf("(") + 1, name.lastIndexOf(")"));
                                        String time=userName.replace("UTC","");
                                        Log.e("time diff ",time);


                                        if (time.contains("+"))
                                            op="plus";
                                        else op="minus";
                                        time= (String) time.subSequence(1,time.length());
                                        String[] splitStr=time.split(":");

                                        Log.e("operator",op);
                                        Log.e("hour",splitStr[0]);
                                        Log.e("min",splitStr[1]);

                                        messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, pic,op,splitStr[1],splitStr[0]);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            recyclerView.setAdapter(messageRecyclarAdapter);
                            messageRecyclarAdapter.notifyDataSetChanged();
                        }
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
        Volley.newRequestQueue(getApplicationContext()).add(sr);
//        onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_one_to_one, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.messages);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        message_editText_msg = (EmojiconEditText) view.findViewById(R.id.message_editText_msg);
        message_sendBtn = (ImageView) view.findViewById(R.id.message_sendBtn);
        message_searchBtn = (ImageView) view.findViewById(R.id.message_searchBtn);
        chat_header = (TextView) view.findViewById(R.id.chat_header);

        messageModelList = new ArrayList<>();
        Collections.reverse(messageModelList);

        TTL ttl = new TTL();
        ttl.MessageBit = 0;

        chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        loginPref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);

        receiver_id = chatPref.getInt("receiverId", 0);
        sender_id = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        sender_name = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("firstName", "");

        queue = Volley.newRequestQueue(getContext());
        queue1 = Volley.newRequestQueue(getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);

        ImageView message_onetoone_attachment = (ImageView) view.findViewById(R.id.message_onetoone_attachment);

       /* message_onetoone_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.message_attachment_layout);
                dialog.show();

                LinearLayout message_attachment_images_google = (LinearLayout) dialog.findViewById(R.id.message_attachment_images_google);
                message_attachment_images_google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Images from google clicked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        message_sendBtn.setVisibility(View.GONE);
                        message_searchBtn.setVisibility(View.VISIBLE);

                        message_searchBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "message search button clicked", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


            }
        });*/

        msgDisplayLayoutview = inflater.inflate(R.layout.message_sender_user_layout, null);
        senderLayout = (LinearLayout) msgDisplayLayoutview.findViewById(R.id.chat_sender_layout);
        userLayout = (LinearLayout) msgDisplayLayoutview.findViewById(R.id.chat_user_layout);
        senderImg = (ImageView) msgDisplayLayoutview.findViewById(R.id.chat_sender_img);
        userImg = (ImageView) msgDisplayLayoutview.findViewById(R.id.chat_user_img);
        sender_msg = (EmojiconTextView) msgDisplayLayoutview.findViewById(R.id.chat_sender_text);
        user_msg = (EmojiconTextView) msgDisplayLayoutview.findViewById(R.id.chat_user_text);
//        message_onetoone_backbtn = (ImageView) view.findViewById(R.id.message_onetoone_backbtn);

        /*{
            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + loginPref.getInt("id", 0);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("message count res ", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("unread_count") > 0)
                            ((TextView) getActivity().findViewById(R.id.bottombar_message_count)).setText(String.valueOf(object.getInt("unread_count")));
                        if (object.getInt("unread_count") == 0)
                            getActivity().findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(sr);
        }*/
        MessageCountService messageCountService=new MessageCountService();
        messageCountService.MessageCount(getActivity(),loginPref);

        appendChatScreenMsgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                if (b != null) {


                    String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
                    Log.e("Message list url", URL);

                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("message response", response);

                            messageModelList.clear();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getInt("status") == 0) {
                                    JSONArray msgAry = jsonObject.getJSONArray("messages");
                                    chat_header.setText(jsonObject.getString("tutor_name"));
                                    String pic = jsonObject.getString("tutor_pic");
                                    if (msgAry.length() == 0) {
                                    } else {
                                        for (int i = 0; i < msgAry.length(); i++) {
                                            JSONObject msgObj = msgAry.getJSONObject(i);
                                            MessageModel messageModel = new MessageModel();
                                            messageModel.setMsg_id(msgObj.getInt("id"));
                                            messageModel.setMsg_text(msgObj.getString("message"));
                                            messageModel.setSender_id(msgObj.getInt("user_id"));
                                            messageModel.setSender_name(msgObj.getString("user_name"));
                                            messageModel.setTime(msgObj.getString("time"));
                                            messageModelList.add(0, messageModel);
                                        }

                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                                        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                                        messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"));
                                        Log.e("locale android ", new TTL().getUserCountry(getContext()));


                                       /* try {
                                            JSONArray timeZoneObj=new JSONArray(new TTL().json);

                                            for (int i=0;i<timeZoneObj.length();i++){
                                                JSONObject obj=timeZoneObj.getJSONObject(i);
                                                if (obj.getString("IsoAlpha2").equalsIgnoreCase( new TTL().getUserCountry(getContext()).toUpperCase())){
                                                    JSONArray winAry=obj.getJSONArray("WindowsTimeZones");
                                                    JSONObject o=winAry.getJSONObject(0);
                                                    String name=o.getString("Name");

                                                    String userName = name.substring(name.lastIndexOf("(") + 1, name.lastIndexOf(")"));
                                                    String time=userName.replace("UTC","");
                                                    Log.e("time diff ",time);


                                                    if (time.contains("+"))
                                                        op="plus";
                                                    else op="minus";
                                                    time= (String) time.subSequence(1,time.length());
                                                    String[] splitStr=time.split(":");

                                                    Log.e("operator",op);
                                                    Log.e("hour",splitStr[0]);
                                                    Log.e("min",splitStr[1]);

                                                    messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"),op,splitStr[1],splitStr[0]);
                                                }
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }*/
                                        recyclerView.setAdapter(messageRecyclarAdapter);
                                        messageRecyclarAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + loginPref.getInt("id", 0);
                            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.e("message count res ", response);

                                    try {
                                        JSONObject object = new JSONObject(response);
                                        if (object.getInt("unread_count") > 0)
                                            ((TextView) (getActivity().findViewById(R.id.bottombar_message_count))).setText(String.valueOf(object.getInt("unread_count")));
                                        if (object.getInt("unread_count") == 0)
                                            getActivity().findViewById(R.id.bottombar_messageCount_layout).setVisibility(View.GONE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            Volley.newRequestQueue(getApplicationContext()).add(sr);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Volley.newRequestQueue(getContext()).add(sr);

                }
            }
        };
        getActivity().registerReceiver(appendChatScreenMsgReceiver, new IntentFilter("appendChatScreenMsg"));


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(appendChatScreenMsgReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();



        final Dialog    dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.threedotprogressbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


//        RefreshFragment();
        String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
        Log.e("Message list url", URL);

        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("message response", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray msgAry = jsonObject.getJSONArray("messages");
                        String pic = jsonObject.getString("tutor_pic");
                        chat_header.setText(jsonObject.getString("tutor_name"));
                        if (msgAry.length() == 0) {
//                            dialog.dismiss();
                        } else {
                            for (int i = 0; i < msgAry.length(); i++) {
                                JSONObject msgObj = msgAry.getJSONObject(i);
                                MessageModel messageModel = new MessageModel();
                                messageModel.setMsg_id(msgObj.getInt("id"));
                                messageModel.setMsg_text(msgObj.getString("message"));
                                messageModel.setSender_id(msgObj.getInt("user_id"));
                                messageModel.setSender_name(msgObj.getString("user_name"));
                                messageModel.setTime(msgObj.getString("time"));
                                messageModelList.add(0, messageModel);
                            }
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"));
                            Log.e("locale android ", new TTL().getUserCountry(getContext()));


                           /* try {
                                JSONArray timeZoneObj=new JSONArray(new TTL().json);

                                for (int i=0;i<timeZoneObj.length();i++){
                                    JSONObject obj=timeZoneObj.getJSONObject(i);
                                    if (obj.getString("IsoAlpha2").equalsIgnoreCase( new TTL().getUserCountry(getContext()).toUpperCase())){
                                        JSONArray winAry=obj.getJSONArray("WindowsTimeZones");
                                        JSONObject o=winAry.getJSONObject(0);
                                        String name=o.getString("Name");

                                        String userName = name.substring(name.lastIndexOf("(") + 1, name.lastIndexOf(")"));
                                        String time=userName.replace("UTC","");
                                        Log.e("time diff ",time);


                                        if (time.contains("+"))
                                            op="plus";
                                        else op="minus";
                                        time= (String) time.subSequence(1,time.length());
                                        String[] splitStr=time.split(":");

                                        Log.e("operator",op);
                                        Log.e("hour",splitStr[0]);
                                        Log.e("min",splitStr[1]);

                                        messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"),op,splitStr[1],splitStr[0]);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            recyclerView.setAdapter(messageRecyclarAdapter);
                            messageRecyclarAdapter.notifyDataSetChanged();
//                            dialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Subject not getting", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);


        ImageView keyboard_open = (ImageView) view.findViewById(R.id.keyboard_open);
        keyboard_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_editText_msg.requestFocus();
                message_editText_msg.setFocusableInTouchMode(true);
                message_editText_msg.performClick();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(message_editText_msg, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        message_editText_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        message_sendBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String msgTxt = message_editText_msg.getText().toString();

                String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(msgTxt);
                message_editText_msg.setText("");


                if (!msgTxt.equals(""))
                    if (!msgTxt.equals(" "))
                        sendMessage(sender_id, receiver_id, toServerUnicodeEncoded, sender_name);

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

       /* message_onetoone_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getActivity().onBackPressed();
            }
        });*/

    }
    String op;

    @Override
    public void onPause() {
        super.onPause();
        messageModelList.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        messageModelList.clear();
    }

    public void sendMessage(final int sender_id, final int receiver_id, String msgTxt, String sender_name) {

        String URL = "https://www.thetalklist.com/api/message?sender_id=" + sender_id + "&receiver_id=" + receiver_id + "&message=" + msgTxt.replace(" ", "%20") + "&user_name=" + sender_name;
        Log.e("send Message list url", URL);

        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                Log.e("message send response", response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getInt("status")==0){
//                        RefreshFragment();

                        String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
                        Log.e("Message list url", URL);

                        messageModelList.clear();
                        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.e("message response", response);


                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("status") == 0) {
                                        JSONArray msgAry = jsonObject.getJSONArray("messages");
                                        String pic = jsonObject.getString("tutor_pic");
                                        chat_header.setText(jsonObject.getString("tutor_name"));
                                        if (msgAry.length() == 0) {

                                        } else {
                                            for (int i = 0; i < msgAry.length(); i++) {
                                                JSONObject msgObj = msgAry.getJSONObject(i);
                                                MessageModel messageModel = new MessageModel();
                                                messageModel.setMsg_id(msgObj.getInt("id"));
                                                messageModel.setMsg_text(msgObj.getString("message"));
                                                messageModel.setSender_id(msgObj.getInt("user_id"));
                                                messageModel.setSender_name(msgObj.getString("user_name"));
                                                messageModel.setTime(msgObj.getString("time"));
                                                messageModelList.add(0, messageModel);
                                            }
                                            messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, jsonObject.getString("tutor_pic"));
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
//                                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                                            recyclerView.removeAllViews();

                                            Log.e("locale android ", new TTL().getUserCountry(getContext()));


                                            /*try {
                                                JSONArray timeZoneObj=new JSONArray(new TTL().json);

                                                for (int i=0;i<timeZoneObj.length();i++){
                                                    JSONObject obj=timeZoneObj.getJSONObject(i);
                                                    if (obj.getString("IsoAlpha2").equalsIgnoreCase( new TTL().getUserCountry(getContext()).toUpperCase())){
                                                        JSONArray winAry=obj.getJSONArray("WindowsTimeZones");
                                                        JSONObject o=winAry.getJSONObject(0);
                                                        String name=o.getString("Name");

                                                        String userName = name.substring(name.lastIndexOf("(") + 1, name.lastIndexOf(")"));
                                                        String time=userName.replace("UTC","");
                                                        Log.e("time diff ",time);


                                                        if (time.contains("+"))
                                                            op="plus";
                                                        else op="minus";
                                                        time= (String) time.subSequence(1,time.length());
                                                        String[] splitStr=time.split(":");

                                                        Log.e("operator",op);
                                                        Log.e("hour",splitStr[0]);
                                                        Log.e("min",splitStr[1]);

                                                        messageRecyclarAdapter = new MessageRecyclarAdapter(getContext(), messageModelList, pic,op,splitStr[1],splitStr[0]);
                                                    }
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/

                                            recyclerView.setAdapter(messageRecyclarAdapter);
                                            messageRecyclarAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "error "+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(sr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error t "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(sr);

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {

    }
}
