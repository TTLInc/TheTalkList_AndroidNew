package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pchmn.materialchips.ChipView;
import com.ttl.project.thetalklist.model.FilterTutorsModel;
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

import static com.ttl.project.thetalklist.R.color.black;

public class SearchViewActivity extends AppCompatActivity/* implements View.OnClickListener */ {
    private static final String TAG = "SearchViewActivity";
    TextView[] myTextViews, myTextViews1, myTextViews2;
    RecyclerView mRecyclerViewCountry;
    RecyclerView mRecyclerViewSubject;
    ApiInterface mApiInterface;
    LinearLayout linearLayoutSubjectTextView, linearLayoutLocationTextView,
            linearLayoutPeopleTextView, linearLayoutSubjectImageView, linearLayoutLocationImageView,
            linearLayoutPeopleImageView, linearLayoutlinear;
    //  SearchView mSearchView;
    TextView rowTextView, rowTextView1, rowTextView2;
    String mQury = "";
    ArrayList<String> arryListSubject, arryListLocation, arryListPeople;
    TextView txtSubjectName, txtPeopleName, txtLocationName;
    EditText etSearch;
    String FLAG;
    ImageView imageSubject, imageLocation, imagePeople;
    LinearLayout lLayour;
    Button btnCancel;
    Toolbar toolbar;
    ChipView chipsInput1;
    String Name;
    TagsEditText mTagsEditText;
    String mStringDataSubject = "", mStringDataPeople = "", mStringDataLocation = "";
    int mSizeAfter, mSizeOntext;
    int mCurrentSize;
    Handler handler;
    int size1;
    int Size;
    String Temp = "0";
    String valu = "0";
    int pluseVlue;
    int mmSize;
    int langths;
    int mainString;
    int acb;
    int mDiffrentValue;
    private ProgressDialog mProgressDialog;
    private RecyclerView.LayoutManager mLayoutManagerCountry;
    private RecyclerView.LayoutManager mLayoutManagerSubject;
    private RecyclerView.Adapter mAdapterCountry;
    private RecyclerView.Adapter mAdapterSubject;
    private String mSubject = "", mLocation = "", mPeople = "";
    private int mSubjectListSize, mLocationListSize, mPeopleListSize;
    private List<SearchFilterModel> mContactList;

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

        mTagsEditText = (TagsEditText) findViewById(R.id.tagsEditText);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        clearData();

     /*   mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.setQuery(mSubject, false);*/
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                mainString = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                acb = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
                Log.e(TAG, "onTextChanged-->: " + acb + "--->" + mainString);
                if (acb < mainString) {

                    mStringDataSubject = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                    Log.e(TAG, "run: " + mStringDataSubject);
                    //  mainString = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();

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


        mTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {


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

                            if (!abc1.equals("")) {
                                ApiCallSearchView(abc1);
                            }


                        } else {
                            if (FLAG.equals("1")) {
                                String a = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                int b = a.length();
                                Log.e(TAG, " LOCATION-->" + a + "-->" + a.length());
                                int size = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                String abc = a.substring(size, b).trim();
                                Log.e(TAG, "LOCATIONstringLength:--> " + abc.trim());
                                Log.e(TAG, "LOCATIONonQueryTextChange-->: " + a);
                                if (!abc.equals("")) {
                                    ApiCallSearchView(abc);
                                }


                            } else {
                                if (FLAG.equals("2")) {
                                    String a2 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                    int b2 = a2.length();
                                    Log.e(TAG, " PEOPLE-->" + a2 + "-->" + a2.length());
                                    int size2 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                    String abc2 = a2.substring(size2, b2).trim();
                                    Log.e(TAG, "PEOPLEstringLength:--> " + abc2.trim());
                                    Log.e(TAG, "PEOPLEonQueryTextChange-->: " + a2);

                                    if (!abc2.equals("")) {
                                        ApiCallSearchView(abc2);
                                    }
                                    txtLocationName.setVisibility(View.GONE);
                                    txtSubjectName.setVisibility(View.GONE);
                                    txtPeopleName.setVisibility(View.GONE);
                                }
                            }

                        }


                    } catch (Exception e) {

                        txtLocationName.setVisibility(View.GONE);
                        txtSubjectName.setVisibility(View.GONE);
                        txtPeopleName.setVisibility(View.GONE);
                    }

                } else {
                    ApiCallSearchView(String.valueOf(editable));
                    Log.e(TAG, "onTextChanged: ");
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
                langths = collection.toString().length();
                Log.e(TAG, "onTagsChanged: " + collection + "Size-->" + collection.toString().length());
            }

            @Override
            public void onEditingFinished() {
                Log.e(TAG, "onEditingFinished: ");
            }
        });
    }


    private void clearData() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTagsEditText.getText().clear();
                mStringDataSubject = "";
                mStringDataPeople = "";
                mStringDataLocation = "";
                mSubject = "";
                mLocation = "";
                mPeople = "";
                txtLocationName.setVisibility(View.GONE);
                txtSubjectName.setVisibility(View.GONE);
                txtPeopleName.setVisibility(View.GONE);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    private void APIFilterTutors() {

        Call<FilterTutorsModel> modelCall = mApiInterface.searchTutors("");
        modelCall.enqueue(new Callback<FilterTutorsModel>() {
            @Override
            public void onResponse(Call<FilterTutorsModel> call, Response<FilterTutorsModel> response) {
                Gson mGson = new Gson();
                String result = mGson.toJson(response);
                Log.e("MainActivity ", "Response------>" + result);
                Log.e("MainActivity ", "Response------>");
            }

            @Override
            public void onFailure(Call<FilterTutorsModel> call, Throwable t) {
                Log.e(TAG, "onFailure---->>>: " + t);
            }
        });


    }

    private void ApiCallSearchView(String mQury) {

        setmProgressDialog();
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

                    if (mSubjectListSize >= 10) {
                        mSubjectListSize = 10;
                    }
                    if (mLocationListSize >= 10) {
                        mLocationListSize = 10;
                    }
                    if (mPeopleListSize >= 10) {
                        mPeopleListSize = 10;
                    }
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
                    Log.e(TAG, "PEOPLE: " + mPeople);
                    mStringDataSubject = mStringDataSubject + mPeople.replaceAll("\\s+", "");
                    mTagsEditText.setText(mPeople);
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
