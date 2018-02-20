package com.ttl.project.thetalklist.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Biography_subject_Fragment;
import com.ttl.project.thetalklist.R;
import com.ttl.project.thetalklist.Tablayout_with_viewpager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saubhagyam on 21/08/2017.
 */

// Used for Biography_subject_Fragment adapter
public class Biography_subject_adapter extends BaseAdapter {

    private final Context context;
    JSONArray array;

    JSONArray selectedAry;
    android.support.v4.app.FragmentManager fragmentManager;
    Button button;
    Activity activity;

    public Biography_subject_adapter(Context context, JSONArray array, JSONArray selectedAry, android.support.v4.app.FragmentManager fragmentManager,Button button, Activity activity) {
        this.context = context;
        this.array = array;
        notifyDataSetChanged();
        this.selectedAry = selectedAry;
        this.fragmentManager = fragmentManager;
        this.button=button;
        this.activity=activity;
    }


    public void setSelectedAry(JSONArray selectedAry) {
        this.selectedAry = selectedAry;
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(R.layout.biography_subject_recycler_child_layout, null);

        try {
            JSONObject obj = (JSONObject) array.get(position);

            Log.e("selected ary", selectedAry.toString());


            final TextView name = (TextView) v.findViewById(R.id.biography_subject_textview);
            final CheckBox check = (CheckBox) v.findViewById(R.id.biography_subject_checkbox);
            final LinearLayout biography_subject_linear = (LinearLayout) v.findViewById(R.id.biography_subject_linear);
            if (obj.getString("subject").equals("General Subjects") || obj.getString("subject").equals("Languages")) {
                name.setText(obj.getString("subject"));
                check.setVisibility(View.GONE);
                name.setTextSize(20);
            } else {
                name.setText(obj.getString("subject"));
            }



            for (int j = 0; j < selectedAry.length(); j++) {
                if (obj.getString("subject").equalsIgnoreCase(selectedAry.getString(j))) {
                    check.setChecked(true);
                }
            }

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (check.isChecked()) {

                        selectedAry.put(name.getText().toString());
                        setSelectedAry(selectedAry);
                    } else {
                        for (int i = 0; i < selectedAry.length(); i++) {
                            try {
                                if (name.getText().toString().equalsIgnoreCase(selectedAry.getString(i))) {
                                    selectedAry.remove(i);
                                    setSelectedAry(selectedAry);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            biography_subject_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check.isChecked()){
                        check.setChecked(false);
                    }else check.setChecked(true);
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Url = "https://www.thetalklist.com/api/save_tutoring_subject?id=" + context.getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&subject=" + selectedAry.toString().replace(" ","%20");
                    StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("subject response ", response);
                                    try {
                                        JSONObject obj = new JSONObject(response);


                                        notifyDataSetChanged();
                                                        Toast.makeText(context, "Saved ", Toast.LENGTH_SHORT).show();
                                                        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                                        if (activity.getClass().toString().equalsIgnoreCase("class com.ttl.project.thetalklist.Registration")) {

                                            fragmentTransaction.replace(R.id.registration_viewpager, new Tablayout_with_viewpager()).commit();
                                        }else
                                        fragmentTransaction.replace(R.id.viewpager, new Tablayout_with_viewpager()).commit();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                                }
                            });

                    Volley.newRequestQueue(context).add(strRequest);
                }
            });




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}