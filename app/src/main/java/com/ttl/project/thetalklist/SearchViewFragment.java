package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttl.project.thetalklist.model.AllSearchDataModel;
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

import static android.content.Context.MODE_PRIVATE;

public class SearchViewFragment extends Fragment {
    private static final String TAG = "SearchViewFragment";
    TextView[] myTextViews, myTextViews1, myTextViews2;

    ApiInterface mApiInterface;
    LinearLayout linearLayoutSubjectTextView, linearLayoutLocationTextView,
            linearLayoutPeopleTextView, linearLayoutSubjectImageView, linearLayoutLocationImageView,
            linearLayoutPeopleImageView, viewLayout;
    RelativeLayout viewLayout1;
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
    List<SearchViewModel.PeopleBean> respoBeans;
    ImageView mClearSearch;
    View view;
    LinearLayout txtPlaceholderSubject, txtPlaceholderLocation, txtPlaceholderPeople;
    String SearchKeyword;
    int mSizeAllSubjectList, mSizeAllPeopleList, mSizeAllLocationList;
    int countLocation, countPeople, countSubject;
    List<AllSearchDataModel.PeopleBean> peopleResponse;
    LinearLayout layout2;
    String mEditableText = "";
    String removeSpace;
    String mUnselectedKeyword = "";
    boolean userPressedKey = false;
    String mSpaceSepretString = "";
    String removeSpace1;
    private ProgressDialog mProgressDialog;
    private String mSubject = "", mLocation = "", mPeople = "";
    private int mSubjectListSize, mLocationListSize, mPeopleListSize;
    private List<SearchFilterModel> mContactList;
    private String mSearchKeyWord;
    private ArrayList<String> mAllSubjectList, mAllLocationList, mAllPeopleList, mAllPeopleFirstName;
    private ArrayList<String> mSelectedPeople, mSelectedSubject, mSelectedLocation, mSelectedPeopleFirstName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_serchview, container, false);
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
        initialization();
        apicallGetAllData();
        return view;
    }

    @Override
    public void onResume() {
        ((ImageView) getActivity().findViewById(R.id.imageView13)).setImageDrawable(getResources().getDrawable(R.drawable.tutors_activated));
        super.onResume();
    }

    private void apicallGetAllData() {
        setmProgressDialog();
        Call<AllSearchDataModel> modelCall = mApiInterface.getAllSearchData();
        modelCall.enqueue(new Callback<AllSearchDataModel>() {
            @Override
            public void onResponse(Call<AllSearchDataModel> call, final Response<AllSearchDataModel> response) {

                mAllSubjectList = new ArrayList<>();
                mAllLocationList = new ArrayList<>();
                mAllPeopleList = new ArrayList<>();
                mAllPeopleFirstName = new ArrayList<>();
                mSelectedPeople = new ArrayList<>();
                mSelectedLocation = new ArrayList<>();
                mSelectedSubject = new ArrayList<>();
                mSelectedPeopleFirstName = new ArrayList<>();
                peopleResponse = response.body().getPeople();
                for (int i = 0; i < response.body().getSubject().size(); i++) {
                    mAllSubjectList.add(response.body().getSubject().get(i).getSubject());
                }

                for (int i = 0; i < response.body().getLocation().size(); i++) {
                    mAllLocationList.add(response.body().getLocation().get(i).getCountry());
                }

                for (int i = 0; i < response.body().getPeople().size(); i++) {
                    mAllPeopleList.add(response.body().getPeople().get(i).getName().trim());
                    mAllPeopleFirstName.add(response.body().getPeople().get(i).getFirstName().trim());
                }


                mProgressDialog.dismiss();
               /* InputFilter filter = new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (Character.isWhitespace(source.charAt(i))) {
                                return " ";
                            }
                        }
                        return null;
                    }

                };
                mTagsEditText.setFilters(new InputFilter[] { filter });*/


                mTagsEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.e(TAG, "onTextChanged: " + charSequence + "././." + i + "./././." + i1 + "././." + i2);
                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {
                     
                            if (String.valueOf(editable).contains(",")) {

                                if (!mEditableText.equals("null") && !mEditableText.isEmpty() && !mEditableText.equals("")) {
                                    Log.e(TAG, "Spacesss" + mStringDataSubject.length());

                                    String temp = String.valueOf(editable).replace(",", "");
                                    removeSpace1 = temp.replaceAll(" ", "");
                                    String mTagString = mTagsEditText.getText().toString().replaceAll(" ", "");
                                    Log.e(TAG, "afterTextChanged: ./././." + mTagString);
                                    String SubKeyword = mTagString.substring(mStringDataSubject.length(), removeSpace1.length());
                                    mTagsEditText.setText(SubKeyword);
                                    mStringDataSubject = mStringDataSubject + removeSpace1;
                                    mEditableText = removeSpace1;
                                    FLAG = "4";
                                } else {
                                    if (!mStringDataSubject.equals("")) {

                                        String temp = String.valueOf(editable).replace(",", "");
                                        removeSpace1 = temp.replaceAll(" ", "");
                                        String mTagString = mTagsEditText.getText().toString().replaceAll(" ", "");
                                        Log.e(TAG, "afterTextChanged: ./././." + mTagString);
                                        String SubKeyword = mTagString.substring(mStringDataSubject.length(), removeSpace1.length());
                                        mTagsEditText.setText(SubKeyword);
                                        mStringDataSubject = mStringDataSubject + removeSpace1;
                                        mEditableText = removeSpace1;
                                        FLAG = "4";
                                    } else {
                                        mEditableText = String.valueOf(editable);
                                        Log.e(TAG, "Spacesss" + mEditableText);
                                        String temp = String.valueOf(editable).replace(",", "");
                                        mTagsEditText.setText(temp);
                                        mStringDataSubject = mStringDataSubject + temp;
                                        FLAG = "4";
                                    }

                                }
                            }

                        String temp1 = null;
                        try {

                            if (mSpaceSepretString.equals(editable.toString())) {
                                temp1 = editable.toString().replaceAll(" ", "");

                            } else {
                                if (editable.toString().contains(",")) {

                                    Log.e(TAG, "Spacesss" + mStringDataSubject.length());

                                    String temp = String.valueOf(editable).replace(",", "");
                                    removeSpace1 = temp.replaceAll(" ", "");
                                    String mTagString = mTagsEditText.getText().toString().replaceAll(" ", "");
                                    Log.e(TAG, "afterTextChanged: ./././." + mTagString);
                                    String SubKeyword = mTagString.substring(mStringDataSubject.length(), removeSpace1.length());
                                    mTagsEditText.setText(SubKeyword);
                                    mStringDataSubject = mStringDataSubject + removeSpace1;
                                    mEditableText = removeSpace1;
                                    FLAG = "4";


                                } else {
                                    temp1 = editable.toString().substring(editable.length() - 1, editable.length());
                                }
                            }

                            if (temp1.equals(" ") && !temp1.contains(",")) {
                                String temp = String.valueOf(editable).replace(" ", "");
                                mSpaceSepretString = editable.toString().replaceAll(" ", "");

                                Log.e(TAG, "text dfff" + temp.length());
                                mStringDataSubject = mStringDataSubject + mSpaceSepretString;
                                FLAG = "5";
                                if (mSpaceSepretString.equals(editable.toString().trim().replaceAll(" ", ""))) {
                                    FLAG = "5";
                                } else {
                                    mTagsEditText.setText(mSpaceSepretString);

                                }


                            }
                        } catch (Exception e) {


                        }


                        if (!mStringDataSubject.equals("") || !mStringDataLocation.equals("") || !mStringDataPeople.equals("")) {

                            try {
                                if (FLAG.equals("4")) {
                                    String a1 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                    int b1 = a1.length();

                                    size1 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();

                                    String abc1 = a1.substring(size1, b1).trim();

                                    if (!abc1.equals("")) {
                                        if (mSelectedPeopleFirstName.size() != 0) {
                                            mSelectedPeopleFirstName.clear();
                                        }
                                        filterText(abc1, response.body());
                                    }
                                }
                                if (FLAG.equals("5")) {
                                    String a1 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                    int b1 = a1.length();

                                    size1 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();

                                    String abc1 = a1.substring(size1, b1).trim();

                                    if (!abc1.equals("")) {
                                        if (mSelectedPeopleFirstName.size() != 0) {
                                            mSelectedPeopleFirstName.clear();
                                        }
                                        filterText(abc1, response.body());
                                    }
                                }


                                if (FLAG.equals("0")) {


                                    String a1 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                    int b1 = a1.length();

                                    size1 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();

                                    String abc1 = a1.substring(size1, b1).trim();

                                    if (!abc1.equals("")) {
                                        if (mSelectedPeopleFirstName.size() != 0) {
                                            mSelectedPeopleFirstName.clear();
                                        }
                                        filterText(abc1, response.body());
                                    }


                                } else {
                                    if (FLAG.equals("1")) {

                                        String a = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                        int b = a.length();

                                        int size4 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                        String abc = a.substring(size4, b).trim();

                                        if (!abc.equals("")) {
                                            if (mSelectedPeopleFirstName.size() != 0) {
                                                mSelectedPeopleFirstName.clear();
                                            }
                                            filterText(abc, response.body());

                                        }


                                    } else {
                                        if (FLAG.equals("2")) {

                                            String a2 = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                                            int b2 = a2.length();

                                            int size2 = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                                            String abc2 = a2.substring(size2, b2).trim();

                                            if (!abc2.equals("")) {
                                                if (mSelectedPeopleFirstName.size() != 0) {
                                                    mSelectedPeopleFirstName.clear();
                                                }
                                                filterText(abc2, response.body());

                                            }
                                        }
                                    }

                                }


                            } catch (Exception e) {

                            }

                        } else {
                            if (!String.valueOf(editable).equals("")) {
                                if (mSelectedPeopleFirstName.size() != 0) {
                                    mSelectedPeopleFirstName.clear();
                                }
                                filterText(String.valueOf(editable), response.body());
                                Log.e(TAG, "onTextChanged: ");
                            }

                        }

                    }


                });

            }

            @Override
            public void onFailure(Call<AllSearchDataModel> call, Throwable t) {

            }
        });
    }


    private void filterText(String editable, AllSearchDataModel body) {
        int size = editable.length();
        for (int i = 0; i < body.getSubject().size(); i++) {
            if (size != 0) {
                if (mAllSubjectList.get(i).length() >= size) {
                    if (mAllSubjectList.get(i).substring(0, size).equalsIgnoreCase(editable)) {
                        Log.e(TAG, "Subject result" + mAllSubjectList.get(i));
                        mSelectedSubject.add(mAllSubjectList.get(i));

                    }
                }
            }
        }
        for (int i = 0; i < body.getLocation().size(); i++) {
            if (mAllLocationList.get(i).length() >= size) {
                if (size != 0) {
                    if (mAllLocationList.get(i).substring(0, size).equalsIgnoreCase(editable)) {
                        Log.e(TAG, "Location result" + mAllLocationList.get(i));
                        mSelectedLocation.add(mAllLocationList.get(i));

                    }
                }
            }
        }
        try {

            for (int i = 0; i < body.getPeople().size(); i++) {
                if (mAllPeopleList.get(i).length() >= size) {
                    if (size != 0) {
                        if (mAllPeopleList.get(i).substring(0, size).equalsIgnoreCase(editable)) {
                            Log.e(TAG, "People result-->" + mAllPeopleList.get(i));
                            Log.e(TAG, "People result name" + mAllPeopleFirstName.get(i));
                            mSelectedPeople.add(mAllPeopleList.get(i));


                        }
                    }
                }
            }
            for (int i = 0; i < body.getPeople().size(); i++) {
                if (mAllPeopleFirstName.get(i).length() >= size) {
                    if (size != 0) {
                        if (mAllPeopleFirstName.get(i).substring(0, size).equalsIgnoreCase(editable)) {
                            Log.e(TAG, "People result-->" + mAllPeopleFirstName.get(i));
                            mSelectedPeopleFirstName.add(mAllPeopleFirstName.get(i));

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        countPeople = mSelectedPeople.size();
        countSubject = mSelectedSubject.size();
        countLocation = mSelectedLocation.size();

        if (mSelectedSubject.size() >= 10) {
            countSubject = 10;
        }
        if (mSelectedPeople.size() >= 10) {
            countPeople = 10;
        }
        if (mSelectedLocation.size() >= 10) {
            countLocation = 10;
            Log.e(TAG, "Count Location" + countLocation);
        }
        setSubject(mSelectedSubject, countSubject);
        setLocation(mSelectedLocation, countLocation);
        setPeople(mSelectedPeople, mSelectedPeopleFirstName, countPeople);
        for (int i = 0; i < mSelectedPeople.size(); i++) {
            Log.e(TAG, "mSelectPersion list " + mSelectedPeople.get(i));
            Log.e(TAG, "mSelectPersion list  name " + mSelectedPeopleFirstName.get(i));
        }
        for (int i = 0; i < mSelectedSubject.size(); i++) {
            Log.e(TAG, "mSelectSubject list " + mSelectedSubject.get(i));
        }
        for (int i = 0; i < mSelectedLocation.size(); i++) {
            Log.e(TAG, "mSelectLocation list " + mSelectedLocation.get(i));

        }
    }

    @SuppressLint("ResourceAsColor")
    private void setPeople(ArrayList<String> mSelectedPeople, final ArrayList<String> mSelectedPeopleName, int count) {
        myTextViews2 = new TextView[count];
        int countLoop = 0;


        for (int i = 0; i < count; i++) {
            Log.e(TAG, "setPeople: full name--> " + mSelectedPeople);
            Log.e(TAG, "setPeople:name--> " + mSelectedPeopleName);
            LinearLayout layoutText, layoutImage;
            layoutText = new LinearLayout(getActivity());
            layoutImage = new LinearLayout(getActivity());
            rowTextView2 = new TextView(getActivity());
            rowTextView2.setText(mSelectedPeople.get(i));

            myTextViews2[i] = rowTextView2;
            final int finalI = i;
            myTextViews2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FLAG = "2";
                    mPeople = myTextViews2[finalI].getText().toString();
                    String mFirstName = mSelectedPeopleName.get(finalI);
                    Log.e(TAG, "onClick-=--=: " + mFirstName);
                    Log.e(TAG, "PEOPLE: " + mPeople);
                    mStringDataSubject = mStringDataSubject + mFirstName.replaceAll("\\s+", "");
                    mEditableText = mStringDataSubject;

                    Log.e(TAG, "mStringDataSubject----> " + mStringDataSubject);
                    mTagsEditText.setText(mFirstName);
                    Log.e(TAG, "FLAG VLUE" + FLAG);

                    mSizeAfter = mTagsEditText.getText().toString().trim().length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);

                }

            });

            rowTextView2.setPadding(5, 27, 0, 25);
            rowTextView2.setTextColor(Color.parseColor("#000000"));
            imagePeople = new ImageView(getActivity());
            imagePeople.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));
            imagePeople.setPadding(0, 25, 0, 25);
            imagePeople.setImageResource(R.drawable.people);

            layoutText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutText.setBackgroundColor(R.color.black);

            layoutImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutImage.setBackgroundColor(R.color.black);


            linearLayoutPeopleTextView.addView(rowTextView2);
            linearLayoutPeopleImageView.addView(imagePeople);
            if (count != (countLoop + 1)) {
                Log.e(TAG, "setPeople:count " + count);
                Log.e(TAG, "countLoop" + countLoop);
                for (int j = 0; j < 1; j++) {
                    linearLayoutPeopleTextView.addView(layoutText);
                    linearLayoutPeopleImageView.addView(layoutImage);
                    countLoop = countLoop + 1;
                }
            }

        }

        mSelectedPeople.clear();

    }

    @SuppressLint("ResourceAsColor")
    private void setLocation(ArrayList<String> mSelectedLocation, int count) {
        myTextViews1 = new TextView[count];
        int countLoop = 0;
        for (int i = 0; i < count; i++) {

            rowTextView1 = new TextView(getActivity());
            LinearLayout layoutText = new LinearLayout(getActivity());
            LinearLayout layoutImage = new LinearLayout(getActivity());
            rowTextView1.setText(mSelectedLocation.get(i));
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
                    mEditableText = mStringDataSubject;

                    mTagsEditText.setText(mLocation);
                   /* txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                }
            });
            rowTextView1.setPadding(5, 27, 0, 25);
            rowTextView1.setTextColor(Color.parseColor("#000000"));
            imageLocation = new ImageView(getActivity());
            imageLocation.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

            imageLocation.setPadding(0, 25, 0, 25);
            imageLocation.setImageResource(R.drawable.location);


            layoutText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutText.setBackgroundColor(R.color.black);
            layoutImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutImage.setBackgroundColor(R.color.black);

            linearLayoutLocationTextView.addView(rowTextView1);
            linearLayoutLocationImageView.addView(imageLocation);
            Log.e(TAG, "setLocation: count" + count);


            if (count != (countLoop + 1)) {
                Log.e(TAG, "setPeople:count " + count);
                Log.e(TAG, "countLoop" + countLoop);
                for (int j = 0; j < 1; j++) {
                    linearLayoutLocationTextView.addView(layoutText);
                    linearLayoutLocationImageView.addView(layoutImage);
                    countLoop = countLoop + 1;
                }
            }

        }
        mSelectedLocation.clear();
    }

    @SuppressLint("ResourceAsColor")
    private void setSubject(ArrayList<String> mSelectedSubject, int count) {
        myTextViews = new TextView[count];
        int countLoop = 0;
        for (int i = 0; i < count; i++) {
            LinearLayout layoutText = new LinearLayout(getActivity());
            LinearLayout layoutImage = new LinearLayout(getActivity());

            rowTextView = new TextView(getActivity());
            rowTextView.setText(mSelectedSubject.get(i));
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
                    mStringDataSubject = mEditableText + mStringDataSubject + mSubject.replaceAll("\\s+", "");
                    mEditableText = mStringDataSubject;
                    mTagsEditText.setText(mSubject);

                 /*   txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                }
            });

            rowTextView.setPadding(5, 27, 0, 25);
            imageSubject = new ImageView(getActivity());
            imageSubject.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

            imageSubject.setPadding(0, 25, 0, 25);
            imageSubject.setImageResource(R.drawable.notebook);

            layoutText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutText.setBackgroundColor(R.color.black);
            layoutImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layoutImage.setBackgroundColor(R.color.black);

            linearLayoutSubjectTextView.addView(rowTextView);
            linearLayoutSubjectImageView.addView(imageSubject);
            Log.e(TAG, "setSubject: Count" + count);


            if (count != (countLoop + 1)) {
                Log.e(TAG, "setPeople:count " + count);
                Log.e(TAG, "countLoop" + countLoop);
                for (int j = 0; j < 1; j++) {
                    linearLayoutSubjectTextView.addView(layoutText);
                    linearLayoutSubjectImageView.addView(layoutImage);
                    countLoop = countLoop + 1;
                }
            }

        }
        mSelectedSubject.clear();
    }


    public void setmProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    private void initialization() {


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    startActivity(new Intent(getActivity(), SettingFlyout.class));
                    return true;
                }
                return false;
            }
        });
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        linearLayoutSubjectTextView = (LinearLayout) view.findViewById(R.id.linearLayoutSubjectTextView);
        linearLayoutSubjectImageView = (LinearLayout) view.findViewById(R.id.linearLayoutSubjectImageView);
        //     linearLayoutlinear = (LinearLayout) view.findViewById(R.id.linearLayoutlinear1);
        linearLayoutLocationImageView = (LinearLayout) view.findViewById(R.id.linearLayoutLocationImageView);
        linearLayoutLocationTextView = (LinearLayout) view.findViewById(R.id.linearLayoutLocationTextView);
        linearLayoutPeopleImageView = (LinearLayout) view.findViewById(R.id.linearLayoutPeopleImageView);
        linearLayoutPeopleTextView = (LinearLayout) view.findViewById(R.id.linearLayoutPeopleTextView);
        txtPlaceholderLocation = (LinearLayout) view.findViewById(R.id.txtPlaceholderLocation);
        txtPlaceholderSubject = (LinearLayout) view.findViewById(R.id.txtPlaceholderSubject);
        txtPlaceholderPeople = (LinearLayout) view.findViewById(R.id.txtPlaceholderPeople);
        //mSearchView = (SearchView) view.findViewById(R.id.tutorsearch_searchView);
        txtLocationName = (TextView) view.findViewById(R.id.locationName);
        txtSubjectName = (TextView) view.findViewById(R.id.subjectName);
        txtPeopleName = (TextView) view.findViewById(R.id.peopleName);
        mContactList = new ArrayList<>();
        mClearSearch = (ImageView) view.findViewById(R.id.imgeClear);

        mTagsEditText = (TagsEditText) view.findViewById(R.id.tagsEditText);
        mTagsEditText.setSingleLine();
        mTagsEditText.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH);
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mTagsEditText.requestFocus();

        mTagsEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e(TAG, "All text  " + mTagsEditText.getText().toString());
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    if (!mUnselectedKeyword.equals("")) {
                        SearchKeyword = SearchKeyword + "," + mUnselectedKeyword;
                    }

                    Log.e(TAG, "./../...//./." + SearchKeyword);
                    editor.putString("search_keyword", SearchKeyword);
                    if (SearchKeyword != null && !SearchKeyword.isEmpty() && !SearchKeyword.equals("null")) {
                        editor.putString("selectedId", "1");
                    } else {
                        editor.putString("selectedId", "0");
                    }
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getActivity(), SettingFlyout.class);
                    startActivity(intent);
                    Log.e(TAG, "Final Text---> " + SearchKeyword);
                    return true;
                }
                return false;
            }
        });


        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Cancel();
        ClearData();

        handler = new Handler();

        final Runnable r = new Runnable() {
            String temp2;
            String temp1;

            public void run() {

                mainString = mStringDataSubject.length() + mStringDataLocation.length() + mStringDataPeople.length();
                acb = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
               // Log.e(TAG, "abababab" + removeSpace);
                if (removeSpace == null || removeSpace.isEmpty() || removeSpace.equals("null")) {
                    SearchKeyword = mTagsEditText.getText().toString().trim();
                }

                String temp = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                try {
                  //  Log.e(TAG, "All text././././" + removeSpace.length() + "./././." + acb);

                   // Log.e(TAG, "doneeeeeeeee" + mTagsEditText.getText().toString().substring(removeSpace.length(), acb));

                    mUnselectedKeyword = temp.substring(removeSpace.length(), acb);
                    if (removeSpace.length() == acb) {
                        mUnselectedKeyword = "";
                        Log.e(TAG, "clear dataaaaa ");

                    //    Log.e(TAG, "mUnselectedKeyword" + mUnselectedKeyword);
                    }
                } catch (Exception e) {

                }

                if (acb < mainString) {

                    mStringDataSubject = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "");
                }
                if (String.valueOf(acb).equals("0")) {
                    mEditableText = "";
                    mClearSearch.setVisibility(View.GONE);
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                    linearLayoutSubjectTextView.removeAllViews();
                    linearLayoutSubjectImageView.removeAllViews();
                    linearLayoutLocationTextView.removeAllViews();
                    linearLayoutPeopleTextView.removeAllViews();
                    linearLayoutLocationImageView.removeAllViews();
                    linearLayoutPeopleImageView.removeAllViews();
                 /*   txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                } else {
                    mClearSearch.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this, 10);
            }
        };

        handler.postDelayed(r, 10);
        mTagsEditText.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH);


        mTagsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtPlaceholderLocation.setVisibility(View.GONE);
                txtPlaceholderSubject.setVisibility(View.GONE);
                txtPlaceholderPeople.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(final Editable editable) {


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
                String temp = collectionString.replaceAll(" ", "").trim();
                String temp2 = temp.substring(1, temp.length() - 1);
                removeSpace = temp2.replaceAll(",", "");
                Log.e(TAG, "onTagsChanged: " + removeSpace + "Size-->" + removeSpace.length());
                SearchKeyword = collectionString.substring(1, collectionString.length() - 1);
                Log.e(TAG, "final Text: " + SearchKeyword);
                Log.e(TAG, "onTagsChanged:/./././. " + mTagsEditText.getText().toString().trim() + "Size-->" + mTagsEditText.getText().toString().trim().length());
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
                txtPlaceholderLocation.setVisibility(View.VISIBLE);
                txtPlaceholderSubject.setVisibility(View.VISIBLE);
                txtPlaceholderPeople.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                editor.putString("search_keyword", "");
                editor.clear();
                editor.apply();
            }
        });
    }


    private void Cancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SettingFlyout.class);
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


                   /* if (mSubjectListSize >= 0) {
                        txtSubjectName.setVisibility(View.VISIBLE);
                    }
                    if (mLocationListSize >= 0) {
                        txtLocationName.setVisibility(View.VISIBLE);
                    }
                    if (mPeopleListSize >= 0) {
                        txtPeopleName.setVisibility(View.VISIBLE);
                    }*/
                    txtPeopleName.setVisibility(View.VISIBLE);
                    txtLocationName.setVisibility(View.VISIBLE);
                    txtSubjectName.setVisibility(View.VISIBLE);

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

                    //setSubjectListView(mSubjectListSize, arryListSubject);
                    //     setLocationListView(mLocationListSize, arryListLocation);
                    //  setPeopleListView(mPeopleListSize, arryListPeople);
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

            rowTextView2 = new TextView(getActivity());
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
                    String mFirstName = peopleResponse.get(finalI).getFirstName();
                    Log.e(TAG, "onClick-=--=: " + mFirstName);
                    Log.e(TAG, "PEOPLE: " + mPeople);
                    mStringDataSubject = mStringDataSubject + mFirstName.replaceAll("\\s+", "");
                    mTagsEditText.setText(mFirstName);
                    /*txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                    Log.e(TAG, "FLAG VLUE" + FLAG);

                    mSizeAfter = mTagsEditText.getText().toString().trim().length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                }

            });
            rowTextView2.setPadding(5, 52, 0, 10);
            rowTextView2.setTextColor(Color.parseColor("#000000"));
            imagePeople = new ImageView(getActivity());
            imagePeople.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));
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

            rowTextView1 = new TextView(getActivity());
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
                   /* txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().toString().trim().replaceAll("\\s+", "").length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                }
            });
            rowTextView1.setPadding(5, 52, 0, 10);
            rowTextView1.setTextColor(Color.parseColor("#000000"));
            imageLocation = new ImageView(getActivity());
            imageLocation.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

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
            rowTextView = new TextView(getActivity());
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

                 /*   txtLocationName.setVisibility(View.GONE);
                    txtSubjectName.setVisibility(View.GONE);
                    txtPeopleName.setVisibility(View.GONE);*/
                    Log.e(TAG, "FLAG VLUE" + FLAG);
                    mSizeAfter = mTagsEditText.getText().length();
                    txtPlaceholderLocation.setVisibility(View.VISIBLE);
                    txtPlaceholderSubject.setVisibility(View.VISIBLE);
                    txtPlaceholderPeople.setVisibility(View.VISIBLE);
                }
            });

            rowTextView.setPadding(5, 50, 0, 10);
            imageSubject = new ImageView(getActivity());
            imageSubject.setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));

            imageSubject.setPadding(0, 50, 0, 10);
            imageSubject.setImageResource(R.drawable.notebook);

            linearLayoutSubjectTextView.addView(rowTextView);
            linearLayoutSubjectImageView.addView(imageSubject);

        }
    }


}
