package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.Biography_subject_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Saubhagyam on 21/08/2017.
 */

public class Biography_subject_Fragment extends Fragment {

    ListView biography_subject_recyclerview;
    Button biography_biography_save_button;

    JSONArray languageAry;
    JSONArray generalAry;
    public ArrayList<String> subList;



    JSONArray mJSONArray;
    SharedPreferences sharedPreferences;
    StringRequest sr, sr1;
    RequestQueue queue1, queue2;


    int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.biography_subject_layout, null);

        queue1 = Volley.newRequestQueue(getContext());
        queue2 = Volley.newRequestQueue(getContext());

        sharedPreferences = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("userId", 0);

        biography_subject_recyclerview = (ListView) v.findViewById(R.id.biography_subject_languages_recyclerview);
        biography_biography_save_button = (Button) v.findViewById(R.id.biography_biography_save_button);
        biography_biography_save_button.setVisibility(View.GONE);
//        subJectArrayRefresh();

        subList = new ArrayList<String>();


        String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + id;
        sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("subjects tutor expanded", response);
                    if (jsonObject.getInt("status") == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String nativeLang = obj.getString("tutoring_subjects");
                        mJSONArray = new JSONArray(nativeLang);


                        Log.e("mJSONArray", mJSONArray.toString());

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
        queue1.add(sr);


        String Url = "https://www.thetalklist.com/api/tutoring_subject_list";
        sr1 = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("subject response ", response);
//                        Toast.makeText(getContext(), "subject response "+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);


                            languageAry = obj.getJSONArray("language");
                            generalAry = obj.getJSONArray("general");

                            for (int i = 0; i <= generalAry.length(); i++) {

                                if (i == 0) {
                                    String s = "{\"id\":\"#\",\"category\":\"General\",\"subject\":\"General Subjects\"}";
                                    JSONObject o = new JSONObject(s);
                                    languageAry.put(o);
                                } else {
                                    languageAry.put(generalAry.get(i - 1));
                                }
                            }

                            String s = "{\"id\":\"#\",\"category\":\"Language\",\"subject\":\"Languages\"}";
                            JSONObject o = new JSONObject(s);
                            languageAry.put(0, o);

                            String URL = "https://www.thetalklist.com/api/tutoring_subject?tutor_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("userId", 0);
                            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.e("subjects tutor expanded", response);
                                        if (jsonObject.getInt("status") == 0) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
                                            JSONObject obj = jsonArray.getJSONObject(0);
                                            String nativeLang = obj.getString("tutoring_subjects");
                                            if (nativeLang.equals("")){
                                                nativeLang="[]";
                                            }
                                            JSONArray selectedAry = new JSONArray(nativeLang);


                                            biography_biography_save_button.setVisibility(View.VISIBLE);
                                            final Biography_subject_adapter subjectAdapter = new Biography_subject_adapter(getContext(), languageAry, selectedAry, getFragmentManager(),biography_biography_save_button,getActivity());


                                            biography_subject_recyclerview.setAdapter(subjectAdapter);
                                            subjectAdapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        String Url = "https://www.thetalklist.com/api/save_tutoring_subject?id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&subject=" + "[]";
                                        StringRequest strRequest = new StringRequest(Request.Method.POST, Url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.e("subject save response ", response);
                                                        try {
                                                            JSONObject obj = new JSONObject(response);


                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                        Volley.newRequestQueue(getContext()).add(strRequest);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            Volley.newRequestQueue(getContext()).add(sr);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

                    }
                });

        queue2.add(sr1);



        return v;
    }

}
