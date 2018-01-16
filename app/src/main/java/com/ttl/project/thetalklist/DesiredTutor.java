package com.ttl.project.thetalklist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DesiredTutor extends Fragment {


    Spinner spnFirstLang, spnGen, spnCountry, spnState,spnSubject;
    EditText keyWord;

    Button DesiredTutor_saveButton;
    Dialog dialog;
    FragmentStack fragmentStack;
    SharedPreferences sharedPreferences;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    String stateString;
    ArrayAdapter stateAdapter;
    SharedPreferences pref1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.fragment_desired_tutor, container, false);
        fragmentStack = FragmentStack.getInstance();



        preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
        if (preference.contains("query")){
            FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.viewpager, new Available_tutor()).commit();
        }


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        queue = Volley.newRequestQueue(getContext());
        if (getActivity() != null) {

            spnFirstLang = (Spinner) convertView.findViewById(R.id.spnFirstlang);
            spnSubject = (Spinner) convertView.findViewById(R.id.spnSubject);
            spnCountry = (Spinner) convertView.findViewById(R.id.spnCountry);
            spnGen = (Spinner) convertView.findViewById(R.id.spnGen);
            spnState = (Spinner) convertView.findViewById(R.id.spnState);
            DesiredTutor_saveButton = (Button) convertView.findViewById(R.id.desiredTutor_save_button);
            keyWord= (EditText) convertView.findViewById(R.id.keyWord);


            pref1 = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE);



            keyWord.setText(pref1.getString("keyword",""));



            String gen[] = getResources().getStringArray(R.array.Gender);
            final ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, gen);
            spnGen.setAdapter(genderAdapter);
            spnGen.setSelection(genderAdapter.getPosition(pref1.getString("gender","")));



            String languages1[] = getResources().getStringArray(R.array.Language1);
            final ArrayAdapter langAdapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, languages1);
            spnFirstLang.setAdapter(langAdapter1);
            spnFirstLang.setSelection(langAdapter1.getPosition(pref1.getString("lang1","")));

            {
                RequestQueue queue1 = Volley.newRequestQueue(getContext());
                String URL = "https://www.thetalklist.com/api/states";
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ary = response.getJSONArray("states");
                            ArrayList<String> coun = new ArrayList<>();
                            coun.add("State");
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);
                                coun.add(data.getString("provice"));
                            }
                            if (getActivity() != null) {
                                stateAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                                spnState.setAdapter(stateAdapter);
                            }

                            spnState.setSelection(stateAdapter.getPosition(pref1.getString("state","")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
                queue1.add(getRequest);

            }



            {
                RequestQueue queue1 = Volley.newRequestQueue(getContext());
                String URL = "https://www.thetalklist.com/api/subject";
                JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray ary = response.getJSONArray("subjects");
                            ArrayList<String> coun = new ArrayList<>();
                            coun.add("Select Subject");
                            for (int i = 0; i < ary.length(); i++) {
                                JSONObject data = ary.getJSONObject(i);
                                coun.add(data.getString("subject"));
                            }
                            if (getActivity() != null) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                                spnSubject.setAdapter(arrayAdapter);

                                for (int i = 0; i < ary.length(); i++) {
                                    JSONObject data = ary.getJSONObject(i);

                                        spnSubject.setSelection(arrayAdapter.getPosition(pref1.getString("subject","")));

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
                queue1.add(getRequest);
            }

            SettingFlyout settingFlyout=new SettingFlyout();
           SharedPreferences pref = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            int roleId=pref.getInt("roleId", 0);
            if (roleId==1 | roleId==2 | roleId==3){
            }else {
                ((Switch) getActivity().findViewById(R.id.switch1)).setChecked(false);
             getActivity().findViewById(R.id.switch1).setClickable(false);
             getActivity().findViewById(R.id.switch1).setFocusable(false);
            }


            Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);

                View view1 = toolbar.getRootView();
                view1.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);

            spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spnCountry.getSelectedItem().toString().equalsIgnoreCase("USA")) {
                        spnState.setVisibility(View.VISIBLE);
                    } else spnState.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.threedotprogressbar);
            dialog.setCanceledOnTouchOutside(false);


            {
                sharedPreferences = getContext().getSharedPreferences("DesiredTutorCountries", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                if (sharedPreferences.contains("countries")) {
                    desiredapi= (desiredtutorapi) new desiredtutorapi().execute();
                    JSONArray ary= null;
                    try {
                        ary = new JSONArray(sharedPreferences.getString("countries",""));
                        ArrayList<String> coun = new ArrayList<>();
                        coun.add("Select Country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));
                        }
                        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
                        spnCountry.setAdapter(arrayAdapter);


                                spnCountry.setSelection(arrayAdapter.getPosition(pref1.getString("country","")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    dialog.show();
                    desiredapi= (desiredtutorapi) new desiredtutorapi().execute();
                }
            }


            DesiredTutor_saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (spnState.getVisibility()==View.VISIBLE){
                        stateString=spnState.getSelectedItem().toString();
                    }else stateString="";


                        SharedPreferences.Editor edi = pref1.edit();
                        edi.putString("subject", spnSubject.getSelectedItem().toString());
                        edi.putString("lang1", spnFirstLang.getSelectedItem().toString());
                        edi.putString("gender", spnGen.getSelectedItem().toString());
                        edi.putString("country", spnCountry.getSelectedItem().toString());

                        edi.putString("state", stateString);
                        edi.putString("keyword", keyWord.getText().toString()).apply();

                    preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
                    edi = preference.edit();
                    edi.clear().apply();




                        UserData data = UserData.getInstance();
                        data.setRoleId(0);

                        SharedPreferences preferences = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("status", 0).apply();


                        TTL ttl = (TTL) getActivity().getApplicationContext();
                        ttl.ExitBit = 1;

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();

                }
            });


        }
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coun);
        return convertView;
    }






    ArrayAdapter<String> arrayAdapter;
    RequestQueue queue;
    ArrayList<String> coun = new ArrayList<>();
    desiredtutorapi desiredapi;


    @Override
    public void onPause() {
        super.onPause();
        if (desiredapi!=null)
            desiredapi.cancel(true);
    }

    private class desiredtutorapi extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String URL = "https://www.thetalklist.com/api/countries";
            JsonObjectRequest getRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray ary = response.getJSONArray("countries");

                        coun.add("Select Country");
                        for (int i = 0; i < ary.length(); i++) {
                            JSONObject data = ary.getJSONObject(i);
                            coun.add(data.getString("country"));
                        }
                    if (dialog.isShowing())
                        dialog.dismiss();
                        editor.putString("countries",ary.toString()).apply();

                        spnCountry.setAdapter(arrayAdapter);

                        spnCountry.setSelection(arrayAdapter.getPosition(pref1.getString("country","")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (dialog.isShowing())
                    dialog.dismiss();
                }
            }
            );
            queue.add(getRequest);
            return null;
        }
    }




}

