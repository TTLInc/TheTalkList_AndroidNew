package com.ttl.project.thetalklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 20/04/2017.
 */

public class AvailableTutor_Fevorite_tablayout extends Fragment {
    public ViewPager viewPager;
    public TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*Here flag bit is the Available_tutor's constructor value*/
    View convertView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.availabletutor_fevorites_tablayout, null);
        if (savedInstanceState == null) {

           /* flag = getArguments().getInt("flag");
            credit = getArguments().getInt("credit");*/
        }

        FragmentStack fragmentStack=FragmentStack.getInstance();
//        fragmentStack.add(new AvailableTutor_Fevorite_tablayout());

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();
        */
        initScreen();

    }

    SharedPreferences preferences;
    public void initScreen() {

     /*   if (roleId == 0) {
            View view1 = toolbar.getRootView();
            view1.findViewById(R.id.studentToolbar).setVisibility(View.VISIBLE);
            view1.findViewById(R.id.tutorToolbar).setVisibility(View.GONE);
            view1.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

        }
        if (roleId == 1 || roleId == 2 || roleId == 3) {
            View view1 = toolbar.getRootView();
            view1.findViewById(R.id.tutorToolbar).setVisibility(View.VISIBLE);
            view1.findViewById(R.id.studentToolbar).setVisibility(View.GONE);
            view1.findViewById(R.id.expandableToolbar).setVisibility(View.GONE);

//                }

        }*/
        /*flag = getArguments().getInt("flag");
        credit = getArguments().getInt("credit");*/
        preferences =getContext().getSharedPreferences("videoCallTutorDetails", Context.MODE_PRIVATE);


        FragmentStack fragmentStack = FragmentStack.getInstance();
//            fragmentStack.push(new Tablayout_with_viewpager());
        /*TTL ttl=(TTL) getContext().getApplicationContext();
        ttl.ExitBit=2;*/

        tabLayout = (TabLayout) convertView.findViewById(R.id.available_fevorite_tab);


        viewPager = (ViewPager) convertView.findViewById(R.id.available_fevorite_viewpagerTabLayout);
        setupViewPager(viewPager);
//            viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);




//        fragmentTransaction.addToBackStack(null);


        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getContext(), tab.getText(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        AvailableTutor_Fevorite_tablayout.ViewPagerAdapter adapter = new AvailableTutor_Fevorite_tablayout.ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new Available_tutor(preferences.getInt("flag",0), preferences.getFloat("credit",0),preferences.getString("tutorName","")), "Available Tutor");
        adapter.addFragment(new Free_credits(), "Free Credits");
        viewPager.setAdapter(adapter);

    }


    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
