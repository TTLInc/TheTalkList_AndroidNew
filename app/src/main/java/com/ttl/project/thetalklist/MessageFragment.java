package com.ttl.project.thetalklist;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.ttl.project.thetalklist.model.MessageGetSet;
import com.ttl.project.thetalklist.util.Config;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MessageFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Message_fragment";
    public static ArrayList<MessageGetSet> arrayList;
    ImageView message_sendBtn, message_searchBtn, request;
    ListView lv;
    String text, id;
    ProgressDialog progressDialog;
    EditText send;
    String mSendImage;
    RequestQueue queue;
    String refresh = "1";
    String Json_String;
    View view;
    int sender_id, receiver_id;
    SharedPreferences chatPref, loginPref;
    String mSenderId, mReceiverId, sender_name;
    TextView chat_header;
    String mMSGCount;
    Button preset_how_are_you, preset_when_available, preset_tutor_now, preset_call_me, preset_what_subject, preset_busy;
    EmojiconEditText message_editText_msg;
    BroadcastReceiver appendChatScreenMsgReceiver;
    MessageAdapter adapter;
    int count;
    String toServerUnicodeEncoded;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_one_to_one, container, false);
        Log.e(TAG, "MessageFragmentonCreateView: ");
        if (getActivity() != null) {
            initilation();
            CreateBoarcastMsg();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onPause: " + Config.sumbitMsg);
        if (Config.sumbitMsg == 1) {
            CallAllMessageList();
            Log.e(TAG, "Runnnninggg " + Config.sumbitMsg);
            Config.sumbitMsg = 0;

        }


        ((ImageView) (getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg))).setImageDrawable(getResources().getDrawable(R.drawable.messages_activated));
        ((TextView) getActivity().findViewById(R.id.txtMessages)).setTextColor(Color.parseColor("#3399CC"));
        ((ImageView) (getActivity().findViewById(R.id.imageView13))).setImageDrawable(getResources().getDrawable(R.drawable.tutors));
        ((TextView) getActivity().findViewById(R.id.txtTutors)).setTextColor(Color.parseColor("#666666"));

        lv.invalidateViews();
    }

    private void CreateBoarcastMsg() {
        appendChatScreenMsgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e(TAG, "onReceive:----->" + refresh);
                Bundle b = intent.getExtras();
                if (b != null) {
                    refresh = "0";
                    if (getActivity() != null) {
                        CallAllMessageList();
                    }
                    String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + loginPref.getInt("id", 0);
                    Log.e(TAG, "loginId " + loginPref.getInt("id", 0));
                    StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("message count res ", response);

                            try {
                                JSONObject object = new JSONObject(response);
                                Log.e(TAG, "CountDisplayFirst " + object.getInt("unread_count"));
                                Config.msgCount = object.getInt("unread_count");
                                if (getActivity() != null) {
                                    if (object.getInt("unread_count") > 0) {
                                        Log.e(TAG, "MassageFragmentCount==1>>>> ");
                                        Config.bottombar_message_count.setText(String.valueOf(object.getInt("unread_count")));
                                    } else {
                                        Log.e(TAG, "MassageFragmentCount==0>>>> ");
                                        Config.bottombar_message_count.setVisibility(View.GONE);

                                    }
                                }

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
            }
        };
        getActivity().registerReceiver(appendChatScreenMsgReceiver, new IntentFilter("appendChatScreenMsg"));

    }


    private void initilation() {
        queue = Volley.newRequestQueue(getContext());
        sender_name = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("firstName", "");
        chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        loginPref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        receiver_id = chatPref.getInt("receiverId", 0);
        sender_id = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.messages);
        message_editText_msg = (EmojiconEditText) view.findViewById(R.id.message_editText_msg);
        message_sendBtn = (ImageView) view.findViewById(R.id.message_sendBtn);
        message_sendBtn.setOnClickListener(this);
        chat_header = (TextView) view.findViewById(R.id.chat_header);
        Log.e(TAG, "onCreateView: " + receiver_id + "---" + sender_id);
        mSenderId = String.valueOf(sender_id);
        mReceiverId = String.valueOf(receiver_id);
        CallAllMessageList();
        preset_how_are_you = (Button) view.findViewById(R.id.how_are_you);
        preset_when_available = (Button) view.findViewById(R.id.when_available);
        preset_tutor_now = (Button) view.findViewById(R.id.tutor_right_now);
        preset_what_subject = (Button) view.findViewById(R.id.what_subject);
        preset_call_me = (Button) view.findViewById(R.id.call_me);
        preset_busy = (Button) view.findViewById(R.id.busy_right_now);
        request = (ImageView) view.findViewById(R.id.request);
        ((ImageView) (getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg))).setImageDrawable(getResources().getDrawable(R.drawable.messages_activated));
        ((TextView) getActivity().findViewById(R.id.txtMessages)).setTextColor(Color.parseColor("#3399CC"));

        onRequestClicked(request);
        staticMsgClick();
    }

    private void onRequestClicked(ImageView request) {
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Request_Form.class);
                startActivity(intent);

            }
        });
    }


    private void CallAllMessageList() {

        MessageBack bg = new MessageBack();
        bg.execute(mSenderId, mReceiverId);

    }

    private void staticMsgClick() {
        onClickedOnPreset(preset_how_are_you, preset_how_are_you.getText().toString());
        onClickedOnPreset(preset_when_available, preset_when_available.getText().toString());
        onClickedOnPreset(preset_tutor_now, preset_tutor_now.getText().toString());
        onClickedOnPreset(preset_what_subject, preset_what_subject.getText().toString());
        onClickedOnPreset(preset_call_me, preset_call_me.getText().toString());
        onClickedOnPreset(preset_busy, preset_busy.getText().toString());

    }

    public void onClickedOnPreset(Button preset, final String text) {
        preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textWOspaces = text.substring(2, text.length() - 1);
                message_editText_msg.setText(message_editText_msg.getText().toString() + textWOspaces);
                message_editText_msg.setSelection(message_editText_msg.getText().length());
                message_editText_msg.requestFocus();
                message_editText_msg.setFocusableInTouchMode(true);
                message_editText_msg.performClick();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(message_editText_msg, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_sendBtn:
                adapter.notifyDataSetChanged();
                lv.invalidateViews();
                refresh = "0";

                sendTextMessage();
                break;
        }
    }


    private void sendTextMessage() {
        String msgTxt = message_editText_msg.getText().toString();

        toServerUnicodeEncoded = StringEscapeUtils.escapeJava(msgTxt);
        message_editText_msg.setText("");

        if (!msgTxt.equals(""))
            if (!msgTxt.equals(" "))
                sendMessage(sender_id, receiver_id, toServerUnicodeEncoded, sender_name);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendMessage(final int sender_id, final int receiver_id, String msgTxt, String sender_name) {

        String URL = "https://www.thetalklist.com/api/message?sender_id=" + sender_id + "&receiver_id=" + receiver_id + "&message=" + msgTxt.replace(" ", "%20") + "&user_name=" + sender_name;
        Log.e("send Message list url", "send Message list url" + URL);


        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("message send response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 0) {

                        String URL = "https://www.thetalklist.com/api/all_messages?sender_id=" + sender_id + "&receiver_id=" + receiver_id;
                        Log.e("Message list url", URL);

                        CallAllMessageList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error t " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    class MessageBack extends AsyncTask<String, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "https://www.thetalklist.com/api/all_messages_new";
        }

        @Override
        protected String doInBackground(String... params) {
            String receiverid, senderid;
            senderid = params[0];
            receiverid = params[1];

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("sender_id", "UTF-8") + "=" + URLEncoder.encode(senderid, "UTF-8") + "&" +
                        URLEncoder.encode("receiver_id", "UTF-8") + "=" + URLEncoder.encode(receiverid, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((Json_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Json_String + "\n");
                }
                Log.e(TAG, "doInBackground: " + senderid + "--" + receiverid);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("messages");
                mSendImage = jsonObject.getString("tutor_pic");
                chat_header.setText(jsonObject.getString("tutor_name"));
                try {
                    Config.msgCount= Integer.parseInt(jsonObject.getString("unread"));

                }catch (Exception e){
                    e.printStackTrace();
                }
  //              Log.e(TAG, " mMSGCount-->"+mMSGCount );
//                Log.e(TAG, "onPostExecute: " + result);
                if (refresh.equals("0")) {

                    JSONObject jo = jsonArray.getJSONObject(jsonArray.length() - 1);
                    arrayList.add(new MessageGetSet(

                            jo.getString("message"),
                            jo.getString("time"),
                            jo.getString("user_name"),
                            jo.getString("user_id")

                    ));


                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        arrayList.add(new MessageGetSet(

                                jo.getString("message"),
                                jo.getString("time"),
                                jo.getString("user_name"),
                                jo.getString("user_id")

                        ));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {
                if (Config.msgCount > 0) {
                    Log.e(TAG, "CountDisplayLast " + Config.msgCount);

                    Config.bottombar_message_count.setText(String.valueOf(Config.msgCount));
                } else {
                    Log.e(TAG, "MassageFragmentCount==0>>>>LST ");
                    Config.bottombar_message_count.setVisibility(View.GONE);

                }
                adapter = new MessageAdapter(getActivity(), R.layout.masseage_rowlist, arrayList);
                lv.setAdapter(adapter);
            }
        }
    }

    public class MessageAdapter extends ArrayAdapter<MessageGetSet> {

        public static final String pref = "mypreference";
        ArrayList<MessageGetSet> messageGetSets;
        Context context;
        int resources;
        TextView send, received, recd, senddate;
        ImageView ReceivedImage, SendImage;

        private MessageAdapter(Context context, int resource, ArrayList<MessageGetSet> objects) {
            super(context, resource, objects);
            this.messageGetSets = objects;
            this.context = context;
            this.resources = resource;
        }

        @Override
        public int getItemViewType(int position) {
            Log.e(TAG, "getItemViewType: " + position);
            return position;
        }

        @Override
        public int getViewTypeCount() {

            if (getCount() == 0) {
                return 1;
            } else {
                return getCount();
            }

        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.masseage_rowlist, parent, false);

            }

            row = convertView;


            MessageGetSet messageGetSet = getItem(position);

            send = (TextView) row.findViewById(R.id.Send);
            received = (TextView) row.findViewById(R.id.received);
            recd = (TextView) row.findViewById(R.id.recdate);
            senddate = (TextView) row.findViewById(R.id.date1);
            ReceivedImage = (ImageView) row.findViewById(R.id.imageReceiver);
            SendImage = (ImageView) row.findViewById(R.id.imageSender);
            ReceivedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openVideo();
                }
            });

            if (!mSenderId.equals(messageGetSet.getId())) {
                String user_msg;
                RelativeLayout linearLayout = (RelativeLayout) row.findViewById(R.id.linearLayout7);
                linearLayout.setVisibility(View.GONE);
                recd.setVisibility(View.GONE);
                user_msg = messageGetSet.getText();
                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
                send.setText(fromServerUnicodeDecoded);
                setDate(messageGetSet.getTime());
                String imagePAth = "https://www.thetalklist.com/uploads/images/" + mSendImage;
                Log.e(TAG, "ImagePth: " + imagePAth);
                Glide.with(context).load(imagePAth)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ReceivedImage);
            } else {
                LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearLayout9);
                linearLayout.setVisibility(View.GONE);
                senddate.setVisibility(View.GONE);
                String user_msg = messageGetSet.getText();
                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
                received.setText(fromServerUnicodeDecoded);
                setDate(messageGetSet.getTime());
                SharedPreferences preferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                String picPath = preferences.getString("pic", "");

                Log.e("message pic path", "https://www.thetalklist.com/uploads/images/" + picPath);
                if (!picPath.equals("")) {
                    Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(SendImage);
                } else
                    Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(SendImage);
            }

            return row;
        }

        private void setDate(String time) {


            Date date_txt = null;
            String[] months = {"Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
            try {
                if (time != null) {
                    date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(time);

                    int hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(date_txt));
                    int month = Integer.parseInt(new SimpleDateFormat("MM").format(date_txt));
                    int day = Integer.parseInt(new SimpleDateFormat("dd").format(date_txt));
                    int Year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date_txt));

                    String h = new SimpleDateFormat("K").format(date_txt); // 9:00
                    String m = new SimpleDateFormat("mm").format(date_txt); // 9:00
                    String a = new SimpleDateFormat("a").format(date_txt); // 9:00


                    senddate.setText(String.valueOf(day) + " " + months[month - 1] + " " + Year + " " + h + ":" + m + " " + a);
                    recd.setText(String.valueOf(day) + " " + months[month - 1] + " " + Year + " " + h + ":" + m + " " + a);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        private void openVideo() {

            String url = "https://www.thetalklist.com/api/tutor_info?tutor_id=" + receiver_id;
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

                            Available_Tutor_Expanded availableTutorExpanded = new Available_Tutor_Expanded();
                            final SharedPreferences preferences = context.getSharedPreferences("availableTutoeExpPref", Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tutorName", o.getString("firstName") + " " + o.getString("lastName"));
                            editor.putInt("tutorRoleId", o.getInt("roleId"));
                            editor.putString("tutorPic", o.getString("pic"));
                            editor.putString("hRate", o.getString("hRate"));
                            editor.putString("avgRate", o.getString("avgRate"));
                            editor.putInt("tutorid", receiver_id).apply();
                            Log.e("TAG", "Tutor_Role_id " + receiver_id);
                            FragmentStack.getInstance().push(new MessageFragment());

                            Bundle bundle = new Bundle();
                            int mToturId = receiver_id;
                            bundle.putString("Tutorid", String.valueOf(mToturId));
                            Log.e("Tag", "OnClikkokkkk: " + mToturId);
                            availableTutorExpanded.setArguments(bundle);
                            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, availableTutorExpanded).commit();

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

    }
}


