package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ttl.project.thetalklist.Adapter.AvailableTutorRecyclerAdapter;
import com.ttl.project.thetalklist.Services.LoginService;
import com.ttl.project.thetalklist.model.SearchTutorsModel;
import com.ttl.project.thetalklist.retrofit.ApiClient;
import com.ttl.project.thetalklist.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mabbas007.tagsedittext.TagsEditText;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Available_tutor extends Fragment {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "Available_tutor";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int mSetSelectetionPotion;
    Dialog dialog;
    JSONArray array;
    AvailableTutorRecyclerAdapter availableTutorRecyclerAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref, preference, GenderSearch, PriceSearch;
    SharedPreferences.Editor editor;
    JSONObject resultObj;
    int flag;
    Float credit;
    String tutorName;
    SearchView searchView;
    ApiInterface mApiInterface;
    LinearLayout linearLayout;
    List<SearchTutorsModel.TutorsBean> mResponse;
    SharedPreferences prefDesired;
    SharedPreferences.Editor editor1;
    String desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender;
    String desireSubject;
    String desireLang1;
    String desireLang2;
    String desireGender;
    String desireCountry;
    String desireState;
    String desireKeyword;
    String URL;
    ImageView mClearSearch;
    SharedPreferences.Editor edi;
    String mSearch_keyword;
    TagsEditText mTagsEditText;
    TextView txtNoResultFound, txtNoResultFound1;
    Spinner btnGender, btnPrice;
    String mSelectedGender, mSelectedPrice;
    int mTutors_id;
    int mPosition;
    Button btnRetry;
    String mSelectedId;
    Handler handler;
    ImageView imgSearchIcon;
    String[] plants = new String[]{
            "Black birch",
            "European weeping birch"
    };

    public Available_tutor() {
    }

    @SuppressLint("ValidFragment")
    public Available_tutor(String desire_subject, String desire_lang1, String desire_lang2, String desire_country, String desire_state, String desire_keyword, String desired_gender) {
        this.desire_subject = desire_subject;
        this.desire_lang1 = desire_lang1;
        this.desire_lang2 = desire_lang2;
        this.desire_country = desire_country;
        this.desire_state = desire_state;
        this.desire_keyword = desire_keyword;
        this.desired_gender = desired_gender;

        //tutorSearch(desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
    }

    @SuppressLint("ValidFragment")
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_available_tutor, container, false);

        txtNoResultFound = (TextView) view.findViewById(R.id.txtNoResultFound);
        txtNoResultFound1 = (TextView) view.findViewById(R.id.txtNoResultFound1);
        btnRetry = (Button) view.findViewById(R.id.btnRetry);
        btnGender = (Spinner) view.findViewById(R.id.btnGender);
        btnPrice = (Spinner) view.findViewById(R.id.btnPrice);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        imgSearchIcon = (ImageView) view.findViewById(R.id.imgSearchIcon);
        imgSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        GenderSearch = getContext().getSharedPreferences("GenderSearch", 0);
        PriceSearch = getContext().getSharedPreferences("PriceSearch", 0);
        try {
            if (PriceSearch.getString("Name", "").equals("Price")) {
                mSelectedPrice = "";
            }
            if (GenderSearch.getString("Gender", "").equals("gender")) {
                mSelectedGender = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (GenderSearch.getString("Gender", "").equals("0")) {
                btnGender.setSelection(0);
            } else {
                btnGender.setSelection(Integer.parseInt(GenderSearch.getString("Gender", "")));

            }


        } catch (Exception e) {
            Log.e(TAG, "onCreateView:-=---" + mPosition);
            e.printStackTrace();
        }
        try {
            Log.e(TAG, "onCreateView: selection Price" + PriceSearch.getString("Name", ""));
            Log.e(TAG, "onCreateView:-=---Price" + mPosition);
            if (PriceSearch.getString("Name", "").equals("Price")) {
                btnPrice.setSelection(0);

            } else {
                btnPrice.setSelection(Integer.parseInt(PriceSearch.getString("Name", "")));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences pref = getContext().getSharedPreferences("fromSignup", MODE_PRIVATE);


        SharedPreferences.Editor editorpref = pref.edit();
        mTutors_id = getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getInt("id", 0);
        if (pref.contains("fromSignup")) {
            TextView systemMessageStudent = (TextView) view.findViewById(R.id.systemMsgStudent);
            systemMessageStudent.setVisibility(View.VISIBLE);
            editorpref.clear().apply();
        }

        Explode explode = new Explode();
        getActivity().getWindow().setExitTransition(explode);

        SharedPreferences prefs = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        mSearch_keyword = prefs.getString("search_keyword", "").replaceAll(",\\s+", ",");
        mSelectedId = prefs.getString("selectedId", "");

        Log.e(TAG, "onCreateView=========>: " + mSearch_keyword);

        //  Button available_tutor_filter = (Button) view.findViewById(R.id.available_tutor_filter);

        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));
        ((TextView) getActivity().findViewById(R.id.txtTutors)).setTextColor(Color.parseColor("#3399CC"));
        ((TextView) getActivity().findViewById(R.id.txtVideos)).setTextColor(Color.parseColor("#666666"));
        ((TextView) getActivity().findViewById(R.id.txtMessages)).setTextColor(Color.parseColor("#666666"));
        ((TextView) getActivity().findViewById(R.id.txtPayments)).setTextColor(Color.parseColor("#666666"));
        ((TextView) getActivity().findViewById(R.id.txtFavorites)).setTextColor(Color.parseColor("#666666"));


        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
      /*  available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.viewpager, new DesiredTutor()).commit();
            }
        });*/

        final TextView msg = (TextView) getActivity().findViewById(R.id.bottombar_message_count);
        final RelativeLayout bottombar_messageCount_layout = (RelativeLayout) getActivity().findViewById(R.id.bottombar_messageCount_layout);

        {
            String URL = "https://www.thetalklist.com/api/count_messages?sender_id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
            Log.e(TAG, "onCreateView: " + "https://www.thetalklist.com/api/count_messages?sender_id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0));
            StringRequest sr = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("message count res ", response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("unread_count") > 0) {

                            msg.setText(String.valueOf(object.getInt("unread_count")));
                            Log.e(TAG, "Available_tutorsMSG==1");
                        } else {
                            msg.setVisibility(View.GONE);
                            Log.e(TAG, "Available_tutorsMSG==0 ");
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
            Volley.newRequestQueue(this.getActivity()).add(sr);
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
                    if (getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f) <= getContext().getSharedPreferences("videoCallTutorDetails", MODE_PRIVATE).getFloat("hRate", 0.0f)) {

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

                        if (getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f) >= credit * 10) {
                            ttl.setCallmin(-100);
                            Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("from", "availabletutor");
                            i.putExtra("min", -100);
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

                            final int min = (int) (getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getFloat("money", 0.0f) / credit);

                            if (min > 1)
                                tv.setText("Your credits are low and this call will expire in " + min + " minutes.");

                            if (min == 1)
                                tv.setText("Your credits are low and this call will expire in " + min + " minute.");


                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    Intent i = new Intent(getContext(), New_videocall_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("from", "availabletutor");
                                    i.putExtra("min", min);
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


        } else {

            final SharedPreferences loginPref = getContext().getSharedPreferences("loginStatus", MODE_PRIVATE);
            LoginService loginService = new LoginService();
            loginService.login(loginPref.getString("email", ""), loginPref.getString("pass", ""), getContext());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loginPref.getFloat("money", 0.0f) < 3.0f) {
                        if (loginPref.getInt("roleId", 0) == 0) {
//                Toast.makeText(getContext(), "less than 3 credits", Toast.LENGTH_SHORT).show();

//            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view3 = inflater.inflate(R.layout.talknow_criticalcredit, null);

                            final PopupWindow popupWindow7 = new PopupWindow(view3, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        popupWindow7.showAtLocation(inflater.inflate(R.layout.fragment_available_tutor, container, false), Gravity.CENTER, 0, 0);
                                    } catch (WindowManager.BadTokenException e) {
                                        Toast.makeText(getApplicationContext(), "Token null", Toast.LENGTH_SHORT).show();
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
        //available_tutor_filter = (Button) view.findViewById(R.id.available_tutor_filter);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.availableTutorList);


        prefDesired = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE);
        editor1 = prefDesired.edit();

        preference = getApplicationContext().getSharedPreferences("AvailableTutorPref", MODE_PRIVATE);
        edi = preference.edit();
        mTagsEditText = (TagsEditText) view.findViewById(R.id.tagsEditText);
        mClearSearch = (ImageView) view.findViewById(R.id.imgeClear);
        mClearSearch.setOnClickListener(null);
        mClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsEditText.setText("");
                SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("search_keyword", "");
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(), SettingFlyout.class);
                startActivity(intent);
            }
        });
        String[] animalsArray = mSearch_keyword.split(",");
        Log.e(TAG, "String " + animalsArray.length);
        Log.e(TAG, "String Keyword " + mSearch_keyword);
        for (int i = 0; i < animalsArray.length; i++) {
            String abc = animalsArray[i].trim();
            Log.e(TAG, "onCreateView---->>>>: " + abc);
            if (abc.length() > 5) {
                String charc = abc.substring(0, 5);
                mTagsEditText.setText(charc + "..");
            } else {
                mTagsEditText.setText(abc);
            }

        }
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if (mTagsEditText.getText().toString() != null && !mTagsEditText.getText().toString().isEmpty() && !mTagsEditText.getText().toString().equals("null")) {
                    mClearSearch.setVisibility(View.VISIBLE);
                } else {
                    mClearSearch.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
        mTagsEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(getActivity(), SearchViewActivity.class);
                startActivity(intent);*/
                // fragmentManager.beginTransaction().replace(R.id.viewpager, new SearchViewFragment()).commit();
            }
        });
        mTagsEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    FragmentStack fragmentStack = FragmentStack.getInstance();
                    fragmentStack.push(new Available_tutor());
                    fragmentManager.beginTransaction().replace(R.id.viewpager, new SearchViewFragment(), "fragment").addToBackStack("fragment").commit();
                    return true;
                }
                return false;
            }
        });
        imgSearchIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    FragmentStack fragmentStack = FragmentStack.getInstance();
                    fragmentStack.push(new Available_tutor());
                    fragmentManager.beginTransaction().replace(R.id.viewpager, new SearchViewFragment(), "fragment").addToBackStack("fragment").commit();
                    return true;
                }
                return false;
            }
        });

       /* mTagsEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        mTagsEditText.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(mTagsEditText, InputMethodManager.SHOW_FORCED);*/

        mTagsEditText.setTagsListener(new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> collection) {
                String collectionString = collection.toString();
                Log.e(TAG, "onTagsChanged: " + collection + "Size-->" + collection.toString().length());
                mSearch_keyword = collectionString.substring(1, collectionString.length() - 1);
                Log.e(TAG, "final Text: " + mSearch_keyword);
                SharedPreferences.Editor editor = getContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("search_keyword", mSearch_keyword);
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(), SettingFlyout.class);
                startActivity(intent);
            }

            @Override
            public void onEditingFinished() {

            }
        });
        //searchView = (SearchView) view.findViewById(R.id.tutorsearch_searchView);
      /*  searchView.setFocusable(false);
        searchView.setIconified(true);*/
        if (prefDesired.contains("subject")) {

            Log.e("desired pref ", "found");

            desire_subject = prefDesired.getString("subject", "");
            desire_lang1 = prefDesired.getString("lang1", "");
            desire_lang2 = prefDesired.getString("lang2", "");
            desired_gender = prefDesired.getString("gender", "");
            desire_country = prefDesired.getString("country", "");
            desire_state = prefDesired.getString("state", "");
            desire_keyword = prefDesired.getString("keyword", "");

            //tutorSearch(desire_subject, desire_lang1, desire_lang2, desire_country, desire_state, desire_keyword, desired_gender);
        } else if (preference.contains("query")) {
            if (getApplicationContext() != null) {
                new AvailableTutor(preference.getString("query", "")).execute();
            }
            //  searchView.setQueryHint(preference.getString("query", ""));
        } else {
            //    searchView.setQueryHint("Ex. Statistics, USA");
            if (getApplicationContext() != null) {
                linearLayout.setVisibility(View.GONE);
                new AvailableTutor("").execute();
            }

        }

//        }
       /* available_tutor_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStack.getInstance().push(new Available_tutor());
                fragmentTransaction.replace(R.id.viewpager, new DesiredTutor()).commit();
                edi.clear().apply();
            }
        });*/


        final SharedPreferences.Editor edi = prefDesired.edit();


       /* searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                view.setFocusable(false);
                Intent intent = new Intent(getActivity(), SearchViewActivity.class);
                startActivity(intent);
            }
        });*/
      /*  searchView.setQuery(preference.getString("query", ""), true);
        searchView.setIconifiedByDefault(false);*/
     /*   searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

             *//*   Intent intent = new Intent(getActivity(), SearchViewActivity.class);
                startActivity(intent);*//*
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                edi.putString("keyword", query).apply();
               *//* editor.clear().apply();
                edi.putString("query", query).apply();*//*
                linearLayout.setVisibility(View.VISIBLE);
                tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("subject", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang1", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang2", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("country", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("state", ""), query,
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("gender", ""));

                return false;
            }

        });
*/

        //    ImageView closeButton = (ImageView) this.searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

      /*  closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edi.putString("keyword", "").apply();
               *//* editor.clear().apply();
                edi.putString("query", "").apply();*//*
                tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("subject", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang1", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang2", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("country", ""),
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("state", ""), "",
                        getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("gender", ""));
                searchView.setQuery("", true);
            }
        });*/

        //APIFilterTutors();
        spinnerSearch();
        return view;
    }

    private void spinnerSearch() {
        btnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedGender = btnGender.getItemAtPosition(i).toString();
                Log.e(TAG, "onItemSelected: " + mSelectedGender);
                SharedPreferences.Editor editor = GenderSearch.edit();
                editor.putString("Gender", String.valueOf(i));
                editor.putString("Name", mSelectedGender.toLowerCase());
                editor.clear();
                editor.apply();


                Log.e(TAG, "TestingShre: " + GenderSearch.getString("Gender", "") + "--->" + GenderSearch.getString("Name", ""));

                if (mSelectedGender.equals("Gender")) {
                    mSelectedGender = "";
                    mSetSelectetionPotion = 0;
                    ((TextView) adapterView.getChildAt(0)).setTextColor(R.color.black);
                    btnGender.setBackgroundResource(R.drawable.buttonboarder);
                    Log.e(TAG, "onItemSelected:--> " + mSelectedGender + "===" + mSelectedPrice);
                }
                if (mSelectedGender.equals("Male")) {
                    mSelectedGender = "male";
                    mSetSelectetionPotion = 1;
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    btnGender.setBackgroundResource(R.drawable.spinner_boared);
                    Log.e(TAG, "onItemSelected:--> " + mSelectedGender + "===" + mSelectedPrice);

                }
                if (mSelectedGender.equals("Female")) {
                    mSelectedGender = "female";
                    mSetSelectetionPotion = 2;
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    btnGender.setBackgroundResource(R.drawable.spinner_boared);

                    Log.e(TAG, "onItemSelected:--> " + GenderSearch.getString("Name", "") + "---->" + mSelectedPrice);

                }
                if (GenderSearch.getString("Name", "").equals("gender")) {
                    if (mSelectedPrice.equals("")) {
                        Log.e(TAG, "Princeeeee1: " + mSelectedPrice);
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, mSelectedGender, mSelectedPrice);
                        Log.e(TAG, "Gender-->1" + mSelectedGender + "-->" + mSelectedPrice);

                    } else {
                        Log.e(TAG, "Princeeeee2: " + mSelectedPrice);
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, mSelectedGender, mSelectedPrice);
                        Log.e(TAG, "Gender-->2" + mSelectedGender + "-->" + mSelectedPrice);

                    }
                    Log.e(TAG, "Gender: " + mSelectedGender + "=" + mSelectedPrice);
                } else {
                    if (mSelectedPrice.equals("")) {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, GenderSearch.getString("Name", ""), "");
                        Log.e(TAG, "Gender-->3" + mSelectedGender + "-->" + mSelectedPrice);

                    } else {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, GenderSearch.getString("Name", ""), PriceSearch.getString("Name", ""));
                        Log.e(TAG, "Gender-->4" + mSelectedGender + "-->" + mSelectedPrice);
                    }
                    Log.e(TAG, "Select Gender: " + mSelectedGender + "=" + mSelectedPrice);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedPrice = btnPrice.getItemAtPosition(i).toString();
                Log.e(TAG, "onItemSelected: " + mSelectedPrice);
                SharedPreferences.Editor editor = PriceSearch.edit();
                editor.putString("Price", btnPrice.getItemAtPosition(i).toString());


                if (mSelectedPrice.equals("Price")) {

                    mSelectedPrice = "";

                    ((TextView) adapterView.getChildAt(0)).setTextColor(R.color.black);
                    btnPrice.setBackgroundResource(R.drawable.buttonboarder);
                    editor.putString("Name", "Price");
                    editor.clear();
                    editor.apply();
                    Log.e(TAG, "onItemSelected: price" + PriceSearch.getString("Name", "") + "--" + GenderSearch.getString("Name", ""));


                }
                if (mSelectedPrice.equals("$")) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setText("Price: $");
                    btnPrice.setBackgroundResource(R.drawable.spinner_boared);
                    mSelectedPrice = "1";
                    editor.putString("Name", "1");
                    editor.clear();
                    editor.apply();
                    Log.e(TAG, "onItemSelected: price" + PriceSearch.getString("Name", "") + "--" + GenderSearch.getString("Name", ""));

                }
                if (mSelectedPrice.equals("$$")) {
                    ((TextView) adapterView.getChildAt(0)).setText("Price: $$");
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    btnPrice.setBackgroundResource(R.drawable.spinner_boared);
                    mSelectedPrice = "2";
                    editor.putString("Name", "2");
                    editor.clear();
                    editor.apply();
                    Log.e(TAG, "onItemSelected: price" + PriceSearch.getString("Name", "") + "--" + GenderSearch.getString("Name", ""));

                }
                if (mSelectedPrice.equals("$$$")) {
                    ((TextView) adapterView.getChildAt(0)).setText("Price: $$$");
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    btnPrice.setBackgroundResource(R.drawable.spinner_boared);
                    mSelectedPrice = "3";
                    editor.putString("Name", "3");
                    editor.clear();
                    editor.apply();
                    Log.e(TAG, "onItemSelected: price" + PriceSearch.getString("Name", "") + "--" + GenderSearch.getString("Name", ""));

                }
                Log.e(TAG, "Name Selected " + PriceSearch.getString("Name", ""));
                Log.e(TAG, "Price Selected " + PriceSearch.getString("Price", ""));
                Log.e(TAG, "Gender SelectItem;;" + GenderSearch.getString("Name", ""));
                if (PriceSearch.getString("Name", "").equals("")) {
                    if (GenderSearch.getString("Name", "").equals("gender")) {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, "", "");
                        Log.e(TAG, "Apicall--1" + String.valueOf(mTutors_id) + "==" + mSearch_keyword + mSelectedGender + "==" + mSelectedPrice);
                    } else {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, mSelectedGender, "");
                        Log.e(TAG, "Apicall--2" + String.valueOf(mTutors_id) + "==" + mSearch_keyword + "==" + mSelectedGender + "==" + mSelectedPrice);

                    }

                    Log.e(TAG, "Price===>: " + mSelectedGender + "=" + mSelectedPrice);
                } else {
                    if (GenderSearch.getString("Name", "").equals("gender")) {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, "", mSelectedPrice);
                        Log.e(TAG, "Apicall--3" + String.valueOf(mTutors_id) + "==" + mSearch_keyword + "==" + "" + "==" + mSelectedPrice);

                    } else {
                        ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, GenderSearch.getString("Name", ""), mSelectedPrice);
                        Log.e(TAG, "Apicall--4" + String.valueOf(mTutors_id) + "==" + mSearch_keyword + "==" + GenderSearch.getString("Name", "") + "==" + mSelectedPrice);

                    }
                    Log.e(TAG, "Select Price: " + mSelectedGender + "=" + mSelectedPrice);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ApiCallGender(String s, String s1, String mSelectedGender, String mSelectedPrice) {
        Log.e(TAG, "ApiCallGender: " + s + "=" + s1 + "=" + mSelectedGender + "=" + mSelectedPrice);
        Call<SearchTutorsModel> modelCall = mApiInterface.searchTutorsGenderANDPrice(String.valueOf(mTutors_id), s1, mSelectedGender, mSelectedPrice);
        modelCall.enqueue(new Callback<SearchTutorsModel>() {
            @Override
            public void onResponse(Call<SearchTutorsModel> call, retrofit2.Response<SearchTutorsModel> response) {
                swipeRefreshLayout.setRefreshing(true);

                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.removeAllViews();
                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                int mSizeModel = response.body().getTutors() == null ? 0 : response.body().getTutors().size();

                Log.e(TAG, "onResponse---????: " + mSizeModel);
                if (mSizeModel == 0 || mSelectedId.equals("0")) {
                    linearLayout.setVisibility(View.GONE);
                    btnGender.setVisibility(View.GONE);
                    btnPrice.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    txtNoResultFound.setVisibility(View.VISIBLE);
                    txtNoResultFound1.setVisibility(View.VISIBLE);
                    btnRetry.setVisibility(View.VISIBLE);
                    btnRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            SharedPreferences.Editor editor = PriceSearch.edit();
                            editor.putString("Price", "0");
                            editor.putString("Name", "0");
                            editor.clear();
                            editor.apply();

                            SharedPreferences.Editor editor2 = GenderSearch.edit();
                            editor2.putString("Gender", "0");
                            editor2.putString("Name", "gender");
                            editor2.clear();
                            editor2.apply();

                            btnGender.setSelection(0);
                            btnPrice.setSelection(0);
                            btnGender.setVisibility(View.VISIBLE);
                            btnPrice.setVisibility(View.VISIBLE);
                            txtNoResultFound.setVisibility(View.GONE);
                            txtNoResultFound1.setVisibility(View.GONE);
                            btnRetry.setVisibility(View.GONE);
                            mTagsEditText.setText("");
                            SharedPreferences.Editor editor3 = getContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                            editor3.putString("search_keyword", "");
                            editor3.putString("selectedId", "1");
                            editor3.clear();
                            editor3.apply();
                            Intent intent = new Intent(getContext(), SettingFlyout.class);
                            startActivity(intent);
                          /* // Fragment frg = null;
                            // getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(Available_tutor.this);
                            ft.attach(Available_tutor.this);
                            ft.commit();*/
                        }
                    });


                    // txtNoResultFound.setText("No Results? Check spelling and limit your criteria.");
                } else {
                    btnGender.setVisibility(View.VISIBLE);
                    btnPrice.setVisibility(View.VISIBLE);
                    txtNoResultFound.setVisibility(View.GONE);
                    txtNoResultFound1.setVisibility(View.GONE);
                }
                availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), response.body().getTutors(), fragmentManager);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(availableTutorRecyclerAdapter);
                availableTutorRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                initSwipe(response);
                swipeRefreshLayout.setRefreshing(false);

                availableTutorRecyclerAdapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);

                // Log.e("MainActivity ", "Response------>" + result);
                Log.e("MainActivity ", "Response------>");

            }

            @Override
            public void onFailure(Call<SearchTutorsModel> call, Throwable t) {

            }
        });

    }


    private void APIFilterTutors() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<SearchTutorsModel> modelCall = mApiInterface.searchTutors(mSearch_keyword);
        modelCall.enqueue(new Callback<SearchTutorsModel>() {
            @Override
            public void onResponse(Call<SearchTutorsModel> call, retrofit2.Response<SearchTutorsModel> response) {

                swipeRefreshLayout.setRefreshing(true);


                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.removeAllViews();
                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                int mSizeModel = response.body().getTutors() == null ? 0 : response.body().getTutors().size();

                Log.e(TAG, "onResponse---????: " + mSizeModel);
                if (mSizeModel == 0) {
                    btnGender.setVisibility(View.GONE);
                    btnPrice.setVisibility(View.GONE);
                    txtNoResultFound.setVisibility(View.VISIBLE);
                    txtNoResultFound.setText("No Results? Check spelling and limit your criteria.");
                }
                availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), response.body().getTutors(), fragmentManager);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(availableTutorRecyclerAdapter);
                availableTutorRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                //initSwipe();
                swipeRefreshLayout.setRefreshing(false);

                availableTutorRecyclerAdapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);

                // Log.e("MainActivity ", "Response------>" + result);
                Log.e("MainActivity ", "Response------>");


            }

            @Override
            public void onFailure(Call<SearchTutorsModel> call, Throwable t) {
                Log.e(TAG, "onFailure---->>>: " + t);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       /* searchView.setFocusable(false);

        searchView.clearFocus();*/
    }

    @Override
    public void onResume() {

        super.onResume();

      /*  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchView.setFocusable(false);*/

        ((ImageView) getActivity().findViewById(R.id.imageView11)).setImageDrawable(getResources().getDrawable(R.drawable.favorites));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_videosearchImg)).setImageDrawable(getResources().getDrawable(R.drawable.videos));
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_payments_Img)).setImageDrawable(getResources().getDrawable(R.drawable.payments));
        ((ImageView) getActivity().findViewById(R.id.settingFlyout_bottomcontrol_MessageImg)).setImageDrawable(getResources().getDrawable(R.drawable.messages));

        availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext());


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }

            void refreshItems() {

                fragmentTransaction.replace(R.id.viewpager, new Available_tutor()).commit();

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

        final String URL = "https://www.thetalklist.com/api/desired_tutor?subject=" + desireSubject + "&language1=" + desireLang1 +/*"&language2="+desireLang2+*/"&gender=" + desireGender + "&country=" + desireCountry + "&state=" + desireState + "&keyword=" + desireKeyword + "&id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
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

                        SharedPreferences pref = getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.clear().apply();
                      /*  tutorSearch(getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("subject", ""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang1", ""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("lang2", ""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("country", ""),
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("state", ""), "",
                                getContext().getSharedPreferences("SearchTutorDesiredTutorPreferences", MODE_PRIVATE).getString("gender", ""));
                        Toast.makeText(getContext(), "No search results found.", Toast.LENGTH_SHORT).show();*/
                        linearLayout.setVisibility(View.GONE);
                    } else {

                        array = resultObj.getJSONArray("tutors");
                        // setRecyclar(array);
                        Log.e(TAG, "Array List Tutor " + array);
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

    //set data in recycler view
    public void setRecyclar(final JSONArray array) {
        swipeRefreshLayout.setRefreshing(true);
        Log.e("tutor search array", array.toString());
        if (array.equals("")) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.removeAllViews();
            final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            //       availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), array, fragmentManager);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            if (getApplicationContext() != null) {
                recyclerView.setAdapter(availableTutorRecyclerAdapter);
                availableTutorRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                //initSwipe();
                swipeRefreshLayout.setRefreshing(false);

                availableTutorRecyclerAdapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.GONE);
            }

        }
    }

    // Initialize the swipe functionality in recyclerview
    private void initSwipe(final retrofit2.Response<SearchTutorsModel> response) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                Log.e(TAG, "onSwiped--->: " + response.body().getTutors().get(position).getId());
                //   ApiCallGender(String.valueOf(mTutors_id), mSearch_keyword, GenderSearch.getString("Name", ""), mSelectedPrice);


                if (direction == ItemTouchHelper.RIGHT) {


                    try {
                      /*  JSONObject object = (JSONObject) array.get(position);
                        Log.e(TAG, "onSwiped: "+array.get(position) );
                        int tutorId = object.getInt("uid");*/
                        URL = "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + response.body().getTutors().get(position).getId();
                        Log.e(TAG, "onSwiped To Favrites: " + "https://www.thetalklist.com/api/favourite?student_id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0) + "&tutor_id=" + response.body().getTutors().get(position).getId());
                        new Favorite(URL, position).execute();


                    } catch (Exception e) {
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

    // call api after swipe to favorite the tutor
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
        final String URL = "https://www.thetalklist.com/api/desired_tutor?subject=" + desireSubject + "&language1=" + desireLang1 +/*"&language2="+desireLang2+*/"&gender=" + desireGender + "&country=" + desireCountry + "&state=" + desireState + "&keyword=" + desireKeyword + "&id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
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
                      /*  if (array.equals("")) {
                            linearLayout.setVisibility(View.GONE);
                        } else {*/
                        linearLayout.setVisibility(View.VISIBLE);
                        recyclerView.removeAllViews();
                        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


                        availableTutorRecyclerAdapter = new AvailableTutorRecyclerAdapter(getContext(), mResponse, fragmentManager);

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
                        //  }
                    } else {

                        array = resultObj.getJSONArray("tutors");
                        //setRecyclar(array);
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

    // To call api background thread
    public class AvailableTutor extends AsyncTask<Void, Void, Void> {

        String keyword_search;


        AvailableTutor(String keyword_search) {
            this.keyword_search = keyword_search;

        }

        @Override
        protected Void doInBackground(Void... params) {

            String keyword = keyword_search.replace(" ", "");
            Log.e("keyword", keyword_search);
            String URL = "https://www.thetalklist.com/api/desired_tutor?subject=&language1=&gender=&country=&state=&keyword=&id=" + getContext().getSharedPreferences("loginStatus", MODE_PRIVATE).getInt("id", 0);
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
                                if (getApplicationContext() != null) {
                                    new AvailableTutor("").execute();
                                }
                            } else {
                                array = resultObj.getJSONArray("tutors");
                                //setRecyclar(array);
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

    // Background thread to call api for favorite the data
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

}

