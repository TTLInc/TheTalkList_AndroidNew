package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ttl.project.thetalklist.model.SearchFilterModel;
import com.ttl.project.thetalklist.model.SearchViewModel;
import com.ttl.project.thetalklist.retrofit.ApiClient;
import com.ttl.project.thetalklist.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewActivity extends AppCompatActivity/* implements View.OnClickListener */ {
    private static final String TAG = "SearchViewActivity";
    TextView[] myTextViews, myTextViews1, myTextViews2;

    ApiInterface mApiInterface;
    LinearLayout linearLayoutSubjectTextView, linearLayoutLocationTextView,
            linearLayoutPeopleTextView, linearLayoutSubjectImageView, linearLayoutLocationImageView,
            linearLayoutPeopleImageView;

    TextView rowTextView, rowTextView1, rowTextView2;

    ArrayList<String> arryListSubject, arryListLocation, arryListPeople, mSearchkeyword;
    TextView txtSubjectName, txtPeopleName, txtLocationName;
    String FLAG;
    ImageView imageSubject, imageLocation, imagePeople;
    Button btnCancel;
    Toolbar toolbar;
    String Name;
    TagsEditText mTagsEditText;
    String mStringDataSubject = "", mStringDataPeople = "", mStringDataLocation = "";
    int mSizeAfter;
    Handler handler;
    int size1;
    String valu = "0";
    int mmSize;
    int langths;
    int mainString;
    int acb;
    private ProgressDialog mProgressDialog;
    private String mSubject = "", mLocation = "", mPeople = "";
    private int mSubjectListSize, mLocationListSize, mPeopleListSize;
    private List<SearchFilterModel> mContactList;
    List<SearchViewModel.PeopleBean> respoBeans;
    ImageView mClearSearch;

    private String mSearchKeyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_serchview);
        initialization();
    }

    public void setmProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    private void initialization() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        linearLayoutSubjectTextView = (LinearLayout) findViewById(R.id.linearLayoutSubjectTextView);
        linearLayoutSubjectImageView = (LinearLayout) findViewById(R.id.linearLayoutSubjectImageView);
        //     linearLayoutlinear = (LinearLayout) findViewById(R.id.linearLayoutlinear1);
        linearLayoutLocationImageView = (LinearLayout) findViewById(R.id.linearLayoutLocationImageView);
        linearLayoutLocationTextView = (LinearLayout) findViewById(R.id.linearLayoutLocationTextView);
        linearLayoutPeopleImageView = (LinearLayout) findViewById(R.id.linearLayoutPeopleImageView);
        linearLayoutPeopleTextView = (LinearLayout) findViewById(R.id.linearLayoutPeopleTextView);
        //mSearchView = (SearchView) findViewById(R.id.tutorsearch_searchView);
        txtLocationName = (TextView) findViewById(R.id.locationName);
        txtSubjectName = (TextView) findViewById(R.id.subjectName);
        txtPeopleName = (TextView) findViewById(R.id.peopleName);
        mContactList = new ArrayList<>();
        mClearSearch = (ImageView) findViewById(R.id.imgeClear);


        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);

        mTagsEditText.requestFocus();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        FilterData();
        ClearData();

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                mainString = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                acb = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
                //  Log.e(TAG, "onTextChanged-->: " + acb + "--->" + mainString);
                if (acb < mainString) {

                    mStringDataSubject = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                    // InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //        imm.showSoftInput(mTagsEditText, InputMethodManager.SHOW_IMPLICIT);    Log.e(TAG, "run: " + mStringDataSubject);

                }
                if (String.valueOf(acb).equals("0")) {
                    txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 10);
            }
        };

        handler.postDelayed(r, 10);
        mTagsEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Toast.makeText(SearchViewActivity.this, mTagsEditText.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        mTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(final Editable editable) {


                Log.e(TAG, " mSubject" + mSubject + "-->" + mSubject.length());
                if (!mStringDataSubject.equals("") || !mStringDataLocation.equals("") || !mStringDataPeople.equals("")) {
                    try {

                        if (FLAG.equals("0")) {


                            String a1 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                            int b1 = a1.length();
                            Log.e(TAG, " mSubject-->" + a1 + "-->" + a1.length());
                            size1 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();

                            String abc1 = a1.substring(size1, b1).trim();

                            Log.e(TAG, "mSubjectstringLength:--> " + abc1.trim());
                            Log.e(TAG, "mSubjectonQueryTextChange-->: " + a1);
                            if (mProgressDialog != null) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                            if (!abc1.equals("")) {
                                setmProgressDialog();
                                ApiCallSearchView(abc1);
                            }


                        } else {
                            if (FLAG.equals("1")) {
                                if (mProgressDialog != null) {
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }
                                String a = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                int b = a.length();
                                Log.e(TAG, " LOCATION-->" + a + "-->" + a.length());
                                int size = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                String abc = a.substring(size, b).trim();
                                Log.e(TAG, "LOCATIONstringLength:--> " + abc.trim());
                                Log.e(TAG, "LOCATIONonQueryTextChange-->: " + a);
                                if (!abc.equals("")) {
                                    setmProgressDialog();
                                    ApiCallSearchView(abc);
                                }


                            } else {
                                if (FLAG.equals("2")) {
                                    if (mProgressDialog != null) {
                                        if (mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                        }
                                    }
                                    String a2 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                    int b2 = a2.length();
                                    Log.e(TAG, " PEOPLE-->" + a2 + "-->" + a2.length());
                                    int size2 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                    String abc2 = a2.substring(size2, b2).trim();
                                    Log.e(TAG, "PEOPLEstringLength:--> " + abc2.trim());
                                    Log.e(TAG, "PEOPLEonQueryTextChange-->: " + a2);

                                    if (!abc2.equals("")) {
                                        setmProgressDialog();
                                        ApiCallSearchView(abc2);
                                    }
                                    txtLocationName.setVisibility(View.GONE);
                                    txtSubjectName.setVisibility(View.GONE);
                                    txtPeopleName.setVisibility(View.GONE);
                                }
                            }

                        }


                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                        txtLocationName.setVisibility(View.GONE);
                        txtSubjectName.setVisibility(View.GONE);
                        txtPeopleName.setVisibility(View.GONE);
                    }

                } else {
                    if (!String.valueOf(editable).equals("")) {
                        setmProgressDialog();
                        ApiCallSearchView(String.valueOf(editable));
                        Log.e(TAG, "onTextChanged: ");
                    }

                }


                Log.e(TAG, "onQueryTextChange: " + editable);
                linearLayoutSubjectTextView.removeAllViews();
                linearLayoutSubjectImageView.removeAllViews();
                linearLayoutLocationTextView.removeAllViews();
                linearLayoutPeopleTextView.removeAllViews();
                linearLayoutLocationImageView.removeAllViews();
                linearLayoutPeopleImageView.removeAllViews();


            }
        });


        mTagsEditText.setTagsListener(new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> collection) {

                mmSize = collection.size();
                mSearchkeyword = new ArrayList<>();

                langths = collection.toString().length();
                String collectionString = collection.toString();
                Log.e(TAG, "onTagsChanged: " + collection + "Size-->" + collection.toString().length());
                String SearchKeyword = collectionString.substring(1, collectionString.length() - 1);
                Log.e(TAG, "final Text: " + SearchKeyword);
                SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("search_keyword", SearchKeyword);
                editor.clear();
                editor.apply();
            }

            @Override
            public void onEditingFinished() {
                Log.e(TAG, "onEditingFinished: ");
            }
        });
    }

    private void ClearData() {

        mClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagsEditText.setText("");
                mStringDataSubject = "";
                mStringDataPeople = "";
                mStringDataPeople = "";

                SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("search_keyword", "");
                editor.clear();
                editor.apply();
            }
        });
    }


    private void FilterData() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SearchViewActivity.this, SettingFlyout.class);
                startActivity(intent);
            }
        });
    }


    private void ApiCallSearchView(String mQury) {


        Call<SearchViewModel> viewModelCall = mApiInterface.getSearchItem(mQury);
        Log.e(TAG, "ApiCallSearchView--->: " + mQury);
        viewModelCall.enqueue(new Callback<SearchViewModel>() {
            @Override
            public void onResponse(Call<SearchViewModel> call, Response<SearchViewModel> response) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
                try {
                    mSubjectListSize = response.body().getSubject().size();
                    mLocationListSize = response.body().getLocation().size();
                    mPeopleListSize = response.body().getPeople().size();
                    respoBeans = response.body().getPeople();

                  /*  if (mSubjectListSize >= 10) {
                        mSubjectListSize = 10;
                    }
                    if (mLocationListSize >= 10) {
                        mLocationListSize = 10;
                    }
                    if (mPeopleListSize >= 10) {
                        mPeopleListSize = 10;
                    }*/
                    if (mSubjectListSize >= 0) {
                        txtSubjectName.setVisibility(View.VISIBLE);
                    }
                    if (mLocationListSize >= 0) {
                        txtLocationName.setVisibility(View.VISIBLE);
                    }
                    if (mPeopleListSize >= 0) {
                        txtPeopleName.setVisibility(View.VISIBLE);
                    }
                    arryListSubject = new ArrayList<>();
                    arryListLocation = new ArrayList<>();
                    arryListPeople = new ArrayList<>();
                    for (int i = 0; i < mSubjectListSize; i++) {
                        arryListSubject.add(response.body().getSubject().get(i).getSubject());
                    }
                    for (int i = 0; i < mLocationListSize; i++) {
                        arryListLocation.add(response.body().getLocation().get(i).getCountry());
                    }
                    for (int i = 0; i < mPeopleListSize; i++) {
                        arryListPeople.add(response.body().getPeople().get(i).getName());
                        Name = response.body().getPeople().get(i).getName();

                    }

                    setSubjectListView(mSubjectListSize, arryListSubject);
                    setLocationListView(mLocationListSize, arryListLocation);
                    setPeopleListView(mPeopleListSize, arryListPeople);
                    notifyAll();
                    //getContactList(mPeopleListSize, arryListPeople);
                    Log.e(TAG, "onResponse: " + mPeopleListSize + "==" + arryListPeople);
                } catch (Exception e) {
                    if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchViewModel> call, Throwable t) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    private void setPeopleListView(int mPeopleListSize, ArrayList<String> arryListPeople) {

        myTextViews2 = new TextView[mPeopleListSize];

        for (int i = 0; i < mPeopleListSize; i++) {

            rowTextView2 = new TextView(this);
            rowTextView2.setText(arryListPeople.get(i));

            myTextViews2[i] = rowTextView2;
            final int finalI = i;
            SearchFilterModel contactChip = new SearchFilterModel(arryListPeople.get(i));
            mContactList.add(contactChip);
            myTextViews2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FLAG = "2";
                    mPeople = myTextViews2[finalI].getText().toString();
                    String mFirstName = respoBeans.get(finalI).getFirstName();
                    Log.e(TAG, "onClick-=--=: " + mFirstName);
                    Log.e(TAG, "PEOPLE: " + mPeople);
                    mStringDataSubject = mStringDataSubject + mFirstName.replaceAll("\\s+", "");
                    mTagsEditText.setText(mFirstName);
                    txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);
                    Log.e(TAG, "FLAG VLUE" + FLAG);

                    mSizeAfter = mTagsEditText.getText().toString().trim().length();
                }

            });
            rowTextView2.setPadding(5, 52, 0, 10);
            rowTextView2.setTextColor(Color.parseColor("#000000"));
            imagePeople = new ImageView(this);
            imagePeople.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));
            imagePeople.setPadding(0, 50, 0, 10);
            imagePeople.setImageResource(R.drawable.people);
            linearLayoutPeopleTextView.addView(rowTextView2);
            linearLayoutPeopleImageView.addView(imagePeople);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setLocationListView(int mLocationListSize, ArrayList<String> arryListSubject) {
        myTextViews1 = new TextView[mLocationListSize];

        for (int i = 0; i < mLocationListSize; i++) {

            rowTextView1 = new TextView(this);
            rowTextView1.setText(arryListSubject.get(i));
            myTextViews1[i] = rowTextView1;
            final int finalI = i;
            myTextViews1[i].setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    FLAG = "1";
                    mLocation = myTextViews1[finalI].getText().toString();
                    Log.e(TAG, "LOCATION: " + mLocation);
                    mStringDataSubject = mStringDataSubject + mLocation.replaceAll("\\s+", "");
                    mTagsEditText.setText(mLocation);
                    txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
                }
            });
            rowTextView1.setPadding(5, 52, 0, 10);
            rowTextView1.setTextColor(Color.parseColor("#000000"));
            imageLocation = new ImageView(this);
            imageLocation.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

            imageLocation.setPadding(0, 50, 0, 10);
            imageLocation.setImageResource(R.drawable.location);

            linearLayoutLocationTextView.addView(rowTextView1);
            linearLayoutLocationImageView.addView(imageLocation);
        }

    }

    @SuppressLint("ResourceAsColor")
    private void setSubjectListView(int mSubjectListSize, ArrayList<String> arryList) {
        myTextViews = new TextView[mSubjectListSize];

        for (int i = 0; i < mSubjectListSize; i++) {
            rowTextView = new TextView(this);
            rowTextView.setText(arryList.get(i));
            rowTextView.setTextColor(Color.parseColor("#000000"));
            myTextViews[i] = rowTextView;
            final int finalI = i;
            myTextViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FLAG = "0";
                    valu = "0";
                    mSubject = myTextViews[finalI].getText().toString();
                    Log.e(TAG, "SUBJECT: " + mSubject);
                    mStringDataSubject = mStringDataSubject + mSubject.replaceAll("\\s+", "");
                    mTagsEditText.setText(mSubject);

                    txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().length();
                }
            });

            rowTextView.setPadding(5, 50, 0, 10);
            imageSubject = new ImageView(this);
            imageSubject.setLayoutParams(new android.view.ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

            imageSubject.setPadding(0, 50, 0, 10);
            imageSubject.setImageResource(R.drawable.notebook);

            linearLayoutSubjectTextView.addView(rowTextView);
            linearLayoutSubjectImageView.addView(imageSubject);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
