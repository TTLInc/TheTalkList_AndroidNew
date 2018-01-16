package com.ttl.project.thetalklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttl.project.thetalklist.Pagination.Paginator;
import com.srx.widget.PullToLoadView;


public class History extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final PullToLoadView listView = (PullToLoadView) view.findViewById(R.id.history_list_layout);



new Paginator(getContext(),listView);



        return view;
    }


}
