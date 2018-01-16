package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ttl.project.thetalklist.Available_Tutor_Expanded;
import com.ttl.project.thetalklist.FragmentStack;
import com.ttl.project.thetalklist.R;
import com.ttl.project.thetalklist.TTL;
import com.ttl.project.thetalklist.VideoList;
import com.ttl.project.thetalklist.Video_Play;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saubhagyam on 18/04/2017.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {

    private final Context context;
    final FragmentManager fragmentManager;
    JSONArray array;


    int withPos = 0;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public VideoListAdapter(Context context, FragmentManager fragmentManager, JSONArray array) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        withPos = 1;
        this.array = array;

    }


    @Override
    public VideoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_layout, parent, false);
        return new VideoListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Log.e("videoListAdapter array", array.toString());
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) array.get(position);
            final String name = jsonObject.getString("name");
            String firstName = jsonObject.getString("firstName");
            String lastNAme = jsonObject.getString("lastName");
            final String source = jsonObject.getString("source");
            final int views = jsonObject.getInt("views");
            final int uid=jsonObject.getInt("uid");


            holder.title.setText(name);
            holder.seen.setText(String.valueOf(views));
            holder.tutorName.setText(firstName + " " + lastNAme);
            final String videoPath = "https://www.thetalklist.com/uploads/video/" + source + ".jpg";

            Glide.with(context).load(videoPath).into(holder.videoThumb);
            final JSONObject finalJsonObject = jsonObject;
            holder.videoThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTL ttl = (TTL) context.getApplicationContext();
                    ttl.ExitBit = 1;

                    Video_Play video_play = new Video_Play();
                    /*bundle.putString("name", name);
                    bundle.putString("description", description);
                    bundle.putString("source", videoPath);
                    bundle.putInt("views", views);
                    bundle.putInt("id", id);*/
//                    tabBackStack.setTabPosition(1);

                    preferences = getApplicationContext().getSharedPreferences("videoListResponse", Context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putInt("position", position).apply();

                    SharedPreferences preferences = context.getSharedPreferences("videoPlaySelected", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("responseArray", array.toString());
                    editor.putString("response", finalJsonObject.toString()).apply();

                    FragmentStack fragmentStack = FragmentStack.getInstance();
                    fragmentStack.push(new VideoList());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.viewpager, video_play).commit();
                }
            });

            holder.tutorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String url = "https://www.thetalklist.com/api/tutor_info?tutor_id=" +uid;
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
                                    editor.putInt("tutorid", uid).apply();

                                    FragmentStack.getInstance().push(new VideoList());
                                    fragmentManager.beginTransaction().replace(R.id.viewpager,new Available_Tutor_Expanded()).commit();

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

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        final ImageView videoThumb;
        final LinearLayout videoListLayout;
        TextView title, tutorName, seen;


        public MyViewHolder(View itemView) {
            super(itemView);


            videoThumb = (ImageView) itemView.findViewById(R.id.videoThumb);
            videoListLayout = (LinearLayout) itemView.findViewById(R.id.videoListLayout);
            title = (TextView) itemView.findViewById(R.id.videoList_list_title);
            tutorName = (TextView) itemView.findViewById(R.id.videoList_list_tutorName);
            seen = (TextView) itemView.findViewById(R.id.videoList_list_seen);

        }
    }


}
