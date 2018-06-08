package com.ttl.project.thetalklist;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ttl.project.thetalklist.model.MessageGetSet;

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
import java.util.ArrayList;

public class MessageFragment extends Fragment {

    private static final String TAG = "Message_fragment";
    public static ArrayList<MessageGetSet> arrayList;

    ListView lv;
    String emailid = "17600", Rid = "405", text, id, nm;
    ProgressDialog progressDialog;
    EditText send;
    String mSendImage;

    String Json_String;
    View view;
    int sender_id, receiver_id;
    SharedPreferences chatPref, loginPref;
    String mSenderId, mReceiverId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_one_to_one, container, false);

        chatPref = getContext().getSharedPreferences("chatPref", Context.MODE_PRIVATE);
        loginPref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        receiver_id = chatPref.getInt("receiverId", 0);
        sender_id = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        arrayList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.messages);
        MessageBack bg = new MessageBack();
        bg.execute(emailid, Rid);
        lv.invalidateViews();
        Log.e(TAG, "onCreateView: " + receiver_id + "---" + sender_id);
        mSenderId=String.valueOf(sender_id);
        mReceiverId=String.valueOf(receiver_id);
        return view;
    }

    public void senddata() {
        String s = send.getText().toString();

        if (!s.equals("")) {
            sendData data = new sendData();
            data.execute(emailid, s);

        } else {


        }
    }


    class sendData extends AsyncTask<String, Void, String> {
        String add_url;

        @Override
        protected void onPreExecute() {
            //add_url = p.path + "employee_msg_insert.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String email, message;
            email = args[0];
            message = args[1];
            try {
                URL url = new URL(add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("emailid", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();

                return null;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    class MessageBack extends AsyncTask<String, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Messages");
            progressDialog.setMessage("Loading..Please Wait..!!");
            progressDialog.show();
            json_url = "https://www.thetalklist.com/api/all_messages_new";
        }

        @Override
        protected String doInBackground(String... params) {
            String email, Rid;
            email = params[0];
            Rid = params[1];

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("sender_id", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("receiver_id", "UTF-8") + "=" + URLEncoder.encode(Rid, "UTF-8");
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

                Log.e(TAG, "onPostExecute: " + mSendImage);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    arrayList.add(new MessageGetSet(

                            jo.getString("message"),
                            jo.getString("time"),
                            jo.getString("user_name"),
                            jo.getString("user_id")

                    ));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

            MessageAdapter adapter = new MessageAdapter(getActivity(), R.layout.masseage_rowlist, arrayList);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
        }
    }

    public class MessageAdapter extends ArrayAdapter<MessageGetSet> {

        public static final String pref = "mypreference";
        ArrayList<MessageGetSet> messageGetSets;
        Context context;
        String emailid = "17600";
        int resources;

        public MessageAdapter(Context context, int resource, ArrayList<MessageGetSet> objects) {
            super(context, resource, objects);

            this.messageGetSets = objects;
            this.context = context;
            this.resources = resource;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
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


            TextView send = (TextView) row.findViewById(R.id.Send);


            TextView received = (TextView) row.findViewById(R.id.received);


            TextView recd = (TextView) row.findViewById(R.id.recdate);


            TextView senddate = (TextView) row.findViewById(R.id.date1);
            ImageView ReceivedImage = (ImageView) row.findViewById(R.id.chat_sender_img);
            ImageView SendImage = (ImageView) row.findViewById(R.id.chat_user_img);


            if (!mSenderId.equals(messageGetSet.getId())) {
                RelativeLayout linearLayout = (RelativeLayout) row.findViewById(R.id.linearLayout7);
                linearLayout.setVisibility(View.GONE);
                recd.setVisibility(View.GONE);
                String user_msg = messageGetSet.getText();
                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
                send.setText(fromServerUnicodeDecoded);
                senddate.setText(messageGetSet.getTime());
                Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + mSendImage)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(context))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(SendImage);
            } else {
                LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearLayout9);
                linearLayout.setVisibility(View.GONE);
                senddate.setVisibility(View.GONE);
                String user_msg = messageGetSet.getText();
                String fromServerUnicodeDecoded = StringEscapeUtils.unescapeJava(user_msg);
                received.setText(fromServerUnicodeDecoded);
                recd.setText(messageGetSet.getTime());
                SharedPreferences preferences = context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                String picPath = preferences.getString("pic", "");

                Log.e("message pic path", "https://www.thetalklist.com/uploads/images/" + picPath);
                if (!picPath.equals("")) {
                    Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ReceivedImage);
                } else
                    Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ReceivedImage);
            }

            return row;
        }


    }
}
