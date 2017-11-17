package com.example.saubhagyam.thetalklist;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.saubhagyam.thetalklist.Adapter.AvailableTutorRecyclerAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.Decorations.DividerItemDecoration;
import com.example.saubhagyam.thetalklist.Services.LoginService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.WINDOW_SERVICE;
import static com.example.saubhagyam.thetalklist.R.id.keyWord;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Available_tutor extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Dialog dialog;
    JSONArray array;
    AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref, preference;
    SharedPreferences.Editor editor;
    JSONObject resultObj;
    int flag;
    Float credit;
    String tutorName;
    SearchView searchView;

    LinearLayout linearLayout;

    SharedPreferences prefDesired;
    SharedPreferences.Editor editor1;

    public Available_tutor() {
    }

    String desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender;

    public Available_tutor(String desire_subject, String desire_lang1, String desire_lang2, String desire_country, String desire_state, String desire_keyword, String desired_gender) {
        this.desire_subject = desire_subject;
        this.desire_lang1 = desire_lang1;
        this.desire_lang2 = desire_lang2;
        this.desire_country = desire_country;
        this.desire_state = desire_state;
        this.desire_keyword = desire_keyword;
        this.desired_gender = desired_gender;

        tutorSearch(desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
    }

    public Available_tutor(int flag, Float credit, String tutorName) {
        this.flag = flag;
        this.credit = credit;
        this.tutorName = tutorName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_available_tutor, container, false);


        SharedPreferences pref = getContext().getSharedPreferences("fromSignup", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorpref = pref.edit();

        if (pref.contains("fromSignup")) {
            TextView systemMessageStudent = (TextView) view.findViewById(R.id.systemMsgStudent);
            systemMessageStudent.setVisibility(View.VISIBLE);
            editorpref.clear().apply();
        }


        Explode explode = new Explode();
        getActivity().getWindow().setExitTransition(explode);


        Button available_tutor_filter = (Button) view.findViewById(R.id.available_tutor_filter);

        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));






        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.viewpager, new DesiredTutor()).commit();
            }
        });

        final TextView msg = (TextView) getActivity().findViewById(R.id.bottombar_message_count);
        final RelativeLayout bottombar_messageCount_layout = (RelativeLayout) getActivity().findViewById(R.id.bottombar_messageCount_layout);

        {
            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("message count res ", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("unread_count") > 0)
                            msg.setText(String.valueOf(object.getInt("unread_count")));
                        if (object.getInt("unread_count") == 0)
                            bottombar_messageCount_layout.setVisibility(View.GONE);
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
        if (flag == 1) {

            final View view1 = inflater.inflate(R.layout.talknow_confirmation_layout, null);
            final View view3 = inflater.inflate(R.layout.talknow_criticalcredit, null);
            final PopupWindow popupWindow = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(false);

            final View view2 = inflater.inflate(R.layout.talknow_insufficient_layout, null);
            final PopupWindow popupWindow1 = new PopupWindow(view2, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


            TextView confirmation_tutorCredits = (TextView) view1.findViewById(R.id.confirmation_tutorCredits);
            TextView confirmation_tutorName = (TextView) view1.findViewById(R.id.confirmation_tutorName);

            confirmation_tutorName.setText(tutorName);
            confirmation_tutorCredits.setText(new DecimalFormat("##.##").format(credit));


            Button yesbtn = (Button) view1.findViewById(R.id.yesbtn);
            final Button nobtn = (Button) view1.findViewById(R.id.nobtn);

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
                    if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) <= getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE).getFloat("hRate", 0.0f)) {

                        popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                        popupWindow1.setFocusable(true);
                        popupWindow1.setOutsideTouchable(false);


                        Button okbtn = (Button) view2.findViewById(R.id.okbtn);

                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow1.dismiss();
                                TTL ttl = (TTL) getApplicationContext();
                                ttl.ExitBit = 2;
                                startActivity(new Intent(getApplicationContext(), new StripePaymentActivity().getClass()));


                            }
                        });
                    } else {
                        final TTL ttl = new TTL();

                        if (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) >= credit * 10) {
                            ttl.setCallmin(-100);
                            Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("from", "availabletutor");
                            i.putExtra("min",-100);
                            getContext().startActivity(i);
                            getActivity().onBackPressed();
                        } else {
                            final PopupWindow popupWindow = new PopupWindow(view3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                            popupWindow.setFocusable(true);
                            popupWindow.setOutsideTouchable(false);


                            Button okButton = (Button) view3.findViewById(R.id.talknow_ok);
                            Button buyCredits = (Button) view3.findViewById(R.id.talknow_buycredits);
                            TextView tv = (TextView) view3.findViewById(R.id.talknow_text);

                            final int min = (int) (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) / credit);

                            if (min>1)
                            tv.setText("Your credits are low and this call will expire in " + min + " minutes.");

                            if (min==1)
                                tv.setText("Your credits are low and this call will expire in " + min + " minute.");


                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("from", "availabletutor");
                                    i.putExtra("min",min);
                                    getContext().startActivity(i);
                                    getActivity().onBackPressed();
                                    ttl.setCallmin(min);

                                }
                            });


                            buyCredits.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    fragmentManager.beginTransaction().replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();
                                    FragmentStack.getInstance().push(new Available_tutor());
                                }
                            });
                        }
                       /* Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("from", "availabletutor");
                        getContext().startActivity(i);
                        getActivity().onBackPressed();*/
                    }


                }
            });
            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTL ttl = (TTL) getApplicationContext();
                    ttl.ExitBit = 2;
                    popupWindow.dismiss();
                    getActivity().onBackPressed();
                }
            });


        }else
        {

            final SharedPreferences loginPref=getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
            LoginService loginService=new LoginService();
            loginService.login(loginPref.getString("email",""),loginPref.getString("pass",""),getContext());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if (loginPref.getFloat("money", 0.0f)< 3.0f) {
                        if (loginPref.getInt("roleId", 0)==0) {
//                Toast.makeText(getContext(), "less than 3 credits", Toast.LENGTH_SHORT).show();

//            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view3 = inflater.inflate(R.layout.talknow_criticalcredit, null);

                            final PopupWindow popupWindow7 = new PopupWindow(view3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        popupWindow7.showAtLocation(inflater.inflate(R.layout.fragment_available_tutor, container, false), Gravity.CENTER, 0, 0);
                                    }catch (WindowManager.BadTokenException e){
                                        Toast.makeText(getApplicationContext(),"Token null", Toast.LENGTH_SHORT).show();
                                    }
                                    return;

                                }
                            }, 3);


                            popupWindow7.setFocusable(true);
                            popupWindow7.setOutsideTouchable(false);


                            Button okButton = (Button) view3.findViewById(R.id.talknow_ok);
                            Button buyCredits = (Button) view3.findViewById(R.id.talknow_buycredits);
                            TextView tv = (TextView) view3.findViewById(R.id.talknow_text);

//                final int min = (int) (getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getFloat("money", 0.0f) / credit);


                            tv.setText("Your credits are low.\n" +
                                    "Need more?");

                            okButton.setText("Yes");

                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow7.dismiss();
                                    FragmentStack.getInstance().push(new Available_tutor());
                                    fragmentManager.beginTransaction().replace(R.id.viewpager, new Earn_Buy_tabLayout()).commit();


                                }
                            });

                            buyCredits.setText("No");

                            buyCredits.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow7.dismiss();

                                }
                            });
                        }
                    }
                }
            }, 3000);

        }

        linearLayout = (LinearLayout) view.findViewById(R.id.AvailableTutor_ProgressBar);
        available_tutor_filter = (Button) view.findViewById(R.id.available_tutor_filter);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.availableTutorList);


        prefDesired = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE);
        editor1 = prefDesired.edit();

        preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", Context.MODE_PRIVATE);
        edi = preference.edit();
        searchView = (SearchView) view.findViewById(R.id.tutorsearch_searchView);
        if (prefDesired.contains("subject")) {

            Log.e("desired pref ", "found");

            desire_subject = prefDesired.getString("subject", "");
            desire_lang1 = prefDesired.getString("lang1", "");
            desire_lang2 = prefDesired.getString("lang2", "");
            desired_gender = prefDesired.getString("gender", "");
            desire_country = prefDesired.getString("country", "");
            desire_state = prefDesired.getString("state", "");
            desire_keyword = prefDesired.getString("keyword", "");

            tutorSearch(desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
        } else if (preference.contains("query")) {

            new AvailableTutor(preference.getString("query", "")).execute();
            searchView.setQueryHint(preference.getString("query", ""));
        } else {
            searchView.setQueryHint("Ex. Statistics, USA");
            linearLayout.setVisibility(View.GONE);
            new AvailableTutor("").execute();
        }

//        }
        available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStack.getInstance().push(new Available_tutor());
                fragmentTransaction.replace(R.id.viewpager, new DesiredTutor()).commit();
                edi.clear().apply();
            }
        });


        final SharedPreferences.Editor edi = prefDesired.edit();


        searchView.setQuery(preference.getString("query", ""), true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                edi.putString("keyword", query).apply();
               /* editor.clear().apply();
                edi.putString("query", query).apply();*/
                linearLayout.setVisibility(View.VISIBLE);
                tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state", ""), query,
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender", ""));

                return false;
            }

        });


        ImageView closeButton = (ImageView) this.searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edi.putString("keyword", "").apply();
               /* editor.clear().apply();
                edi.putString("query", "").apply();*/
                tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state", ""), "",
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender", ""));
                searchView.setQuery("", true);
            }
        });


        return view;
    }


    String desireSubject;
    String desireLang1;
    String desireLang2;
    String desireGender;
    String desireCountry;
    String desireState;
    String desireKeyword;

    @Override
    public void onResume() {

        super.onResume();
        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favoritestar_settingflyout));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videosearch));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.new_tabuser_bottomlayout_yellow));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.dollar));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.message_icon_bottombar));

        availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext());

/*

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
*/

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            void refreshItems() {
                // Load items
                // ...
               /* tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("keyword", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender", ""));*/


               fragmentTransaction.replace(R.id.viewpager,new Available_tutor()).commit();

                // Load complete
                swipeRefreshLayout.setRefreshing(false);
                // Update the adapter and notify data set changed
                // ...
                // Stop refresh animation
                   /* availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(availableTutorRecyclerAdapter);
                    availableTutorRecyclerAdapter.notifyDataSetChanged();*/
            }
        });
    }

    public void tutorSearch(final String desire_subject, final String desire_lang1, final String desire_lang2, final String desire_country, final String desire_state, final String desire_keyword, final String desired_gender) {


        desireSubject = desire_subject.replaceAll(" ", "_");
        desireLang1 = desire_lang1.replaceAll(" ", "_");
        desireLang2 = desire_lang2.replaceAll(" ", "_");
        desireGender = desired_gender.replaceAll(" ", "_");
        desireCountry = desire_country.replaceAll(" ", "_");
        desireState = desire_state.replaceAll(" ", "_");
        desireKeyword = desire_keyword.replaceAll(" ", "_");


        if (desireSubject.equalsIgnoreCase("Select_Subject")) desireSubject = "";
        if (desireLang1.equalsIgnoreCase("Select_Second_Language")) desireLang1 = "";
        if (desireGender.equalsIgnoreCase("Select_Gender")) desireGender = "";
        if (desireCountry.equalsIgnoreCase("Select_country")) desireCountry = "";
        if (desireState.equalsIgnoreCase("state")) desireState = "";

        final String URL = "https://www.thetalklist.com/api/desired_tutor?subject=" + desireSubject + "&language1=" + desireLang1 +/*"&language2="+desireLang2+*/"&gender=" + desireGender + "&country=" + desireCountry + "&state=" + desireState + "&keyword=" + desireKeyword + "&id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        Log.e("desired tut search url", URL);
        RequestQueue queue11111 = Volley.newRequestQueue(getApplicationContext());

        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    resultObj = new JSONObject(response);
                    Log.e("tutor search desired", resultObj.toString());
                    if (resultObj.getInt("status") == 1) {
//                        new AvailableTutor("").execute();

                       /* SharedPreferences pref=getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();

                        editor.clear().apply();*/
                       /* tutorSearch( getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject",""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1",""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2",""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country",""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state",""), "",
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender",""));
*/
                        Toast.makeText(getContext(), "No Match Found", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);
                    } else {

                        array = resultObj.getJSONArray("tutors");
                        setRecyclar(array);
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


        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue11111.add(sr);
    }

    String URL;
    SharedPreferences.Editor edi;
    int isMyFavorite = 0;

    public class AvailableTutor extends AsyncTask<Void, Void, Void> {

        String keyword_search;


        AvailableTutor(String keyword_search) {
            this.keyword_search = keyword_search;

        }

        @Override
        protected Void doInBackground(Void... params) {

            String keyword = keyword_search.replace(" ", "");
            Log.e("keyword", keyword_search);
            String URL = "https://www.thetalklist.com/api/tutorsearch?keyword=" + keyword + "&id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
            Log.e("Available tutor url", URL);
            RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("tutor search", response);
                    try {

                        resultObj = new JSONObject(response);

                        if (resultObj.getInt("status") == 0) {
                            if (resultObj.getString("tutors").equals("No Results? Check spelling and limit your criteria.")) {
                                Toast.makeText(getContext(), "No Tutors found", Toast.LENGTH_SHORT).show();
                                new AvailableTutor("").execute();
                            } else {
                                array = resultObj.getJSONArray("tutors");
                                setRecyclar(array);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    linearLayout.setVisibility(View.GONE);
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(),
                                "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                    if (error.getClass().equals(ServerError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(),
                                "We are sorry for our Absence..! Wait for a While... We are setting up for you",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("keyword", keyword_search);
                    return headers;
                }
            };


            sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue1.add(sr);


            return null;
        }
    }

    public void setRecyclar(final JSONArray array) {
        swipeRefreshLayout.setRefreshing(true);
        Log.e("tutor search array", array.toString());
        if (array.equals("")) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.removeAllViews();
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(availableTutorRecyclerAdapter);
            availableTutorRecyclerAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            initSwipe();
            swipeRefreshLayout.setRefreshing(false);

            availableTutorRecyclerAdapter.notifyDataSetChanged();
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {


                    try {
                        JSONObject object = (JSONObject) array.get(position);
                        int tutorId = object.getInt("uid");
                        URL = "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + tutorId;

                        new Favorite(URL, position).execute();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public class Favorite extends AsyncTask<String, Integer, JSONObject> {
        JSONObject jsonObject;
        String URL;
        int pos;

        Favorite(String URL, int pos) {
            this.URL = URL;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Log.e("favorite res", response);
                    try {

                        jsonObject = new JSONObject(response);

                        if (jsonObject.getInt("status") == 0) {
                            if (jsonObject.getInt("fav") == 1) {
                                Toast.makeText(getContext(), "Added to favorites.", Toast.LENGTH_SHORT).show();
                                final Parcelable recyclerViewState;
                                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();


                                swipeLayout(pos, 1);

                               /* tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state", ""), "",
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender", ""));*/

                            } else {


                                // Restore state

                                Toast.makeText(getContext(), "Removed.", Toast.LENGTH_SHORT).show();
                                Parcelable recyclerViewState;
                                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();


                                swipeLayout(pos, 0);

                                /*tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("subject", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang1", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("lang2", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("country", ""),
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("state", ""), "",
                                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", Context.MODE_PRIVATE).getString("gender", ""));*/

                                // Restore state
                              /*  recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                availableTutorRecyclerAdapter.notifyDataSetChanged();
                                AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(pos, getContext(), array, fragmentManager, 0);
                                recyclerView.setAdapter(availableTutorRecyclerAdapter);*/
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
            queue.add(sr);
            return jsonObject;
        }


    }

    public void swipeLayout(final int pos, final int fav) {

        desire_subject = prefDesired.getString("subject", "");
        desire_lang1 = prefDesired.getString("lang1", "");
        desire_lang2 = prefDesired.getString("lang2", "");
        desired_gender = prefDesired.getString("gender", "");
        desire_country = prefDesired.getString("country", "");
        desire_state = prefDesired.getString("state", "");
        desire_keyword = prefDesired.getString("keyword", "");

        desireSubject = desire_subject.replaceAll(" ", "_");
        desireLang1 = desire_lang1.replaceAll(" ", "_");
        desireLang2 = desire_lang2.replaceAll(" ", "_");
        desireGender = desired_gender.replaceAll(" ", "_");
        desireCountry = desire_country.replaceAll(" ", "_");
        desireState = desire_state.replaceAll(" ", "_");
        desireKeyword = desire_keyword.replaceAll(" ", "_");


        if (desireSubject.equalsIgnoreCase("Select_Subject")) desireSubject = "";
        if (desireLang1.equalsIgnoreCase("Select_Second_Language")) desireLang1 = "";
        if (desireGender.equalsIgnoreCase("Select_Gender")) desireGender = "";
        if (desireCountry.equalsIgnoreCase("Select_country")) desireCountry = "";
        if (desireState.equalsIgnoreCase("state")) desireState = "";
        final String URL = "https://www.thetalklist.com/api/desired_tutor?subject=" + desireSubject + "&language1=" + desireLang1 +/*"&language2="+desireLang2+*/"&gender=" + desireGender + "&country=" + desireCountry + "&state=" + desireState + "&keyword=" + desireKeyword + "&id=" + getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        Log.e("desired tut search url", URL);
        RequestQueue queue11111 = Volley.newRequestQueue(getApplicationContext());
        final Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    resultObj = new JSONObject(response);
                    Log.e("tutor search desired", resultObj.toString());
                    if (resultObj.getInt("status") == 1) {


                        Toast.makeText(getContext(), "No Match Found", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.GONE);

                        swipeRefreshLayout.setRefreshing(true);
                        Log.e("tutor search array", array.toString());
                        if (array.equals("")) {
                            linearLayout.setVisibility(View.GONE);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                            recyclerView.removeAllViews();
                            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


                            availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
                            /*recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(availableTutorRecyclerAdapter);
                            availableTutorRecyclerAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            initSwipe();
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    refreshItems();
                                }

                                void refreshItems() {
                                    // Load items
                                    // ...

                                    // Load complete
                                    swipeRefreshLayout.setRefreshing(false);
                                    // Update the adapter and notify data set changed
                                    // ...
                                    // Stop refresh animation
                                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                    availableTutorRecyclerAdapter.notifyDataSetChanged();
                                    AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(pos, getContext(), array, fragmentManager, fav);
                                    recyclerView.setAdapter(availableTutorRecyclerAdapter);


                                }
                            });*/
                            availableTutorRecyclerAdapter.notifyDataSetChanged();
                            linearLayout.setVisibility(View.GONE);
                        }
                    } else {

                        array = resultObj.getJSONArray("tutors");
                        setRecyclar(array);
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


        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue11111.add(sr);
    }

}

