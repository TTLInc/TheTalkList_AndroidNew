package com.ttl.project.thetalklist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttl.project.thetalklist.Adapter.SearchViewAdepter;
import com.ttl.project.thetalklist.model.CountyModel;
import com.ttl.project.thetalklist.retrofit.ApiClient;
import com.ttl.project.thetalklist.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountrySearchFragment extends Fragment {
    View mView;
    RecyclerView mRecyclerView;
    ApiInterface mApiInterface;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_serchview, container, false);
        initialization();
        return mView;
    }

    private void initialization() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.availableTutorList);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ApiGetCoutryCall();

    }


    private void ApiGetCoutryCall() {
        Call<CountyModel> modelCall = mApiInterface.getCountryList();
        modelCall.enqueue(new Callback<CountyModel>() {
            @Override
            public void onResponse(Call<CountyModel> call, Response<CountyModel> response) {
                //   Log.e("Size of getdata", response.body().getData().size() + "");
                mAdapter = new SearchViewAdepter(response.body().getCountries(), getActivity());
                mRecyclerView.setHasFixedSize(true);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<CountyModel> call, Throwable t) {

            }
        });
    }
}
