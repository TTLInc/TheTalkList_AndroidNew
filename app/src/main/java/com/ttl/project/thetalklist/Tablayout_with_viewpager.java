package com.ttl.project.thetalklist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttl.project.thetalklist.Services.LoginService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 3/29/2017.
 */

//Tab layout contains the biography and MyDetails page
public class Tablayout_with_viewpager extends android.support.v4.app.Fragment {
    public ViewPager viewPager;
    public TabLayout tabLayout;

    Bundle savedInstanceState;

    int roleId;
    int status;


    int tab=0;

    @SuppressLint("ValidFragment")
    public Tablayout_with_viewpager(int tab) {
        status = tab;
        //changeTab(tab);
    }


    public Tablayout_with_viewpager() {
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    View convertView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.tab_layout_and_viewpager, null);

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


//                LoginService loginService = new LoginService();
//                loginService.login(getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("email", ""), getContext().getSharedPreferences("loginStatus", Context.MODE_PRIVATE).getString("pass", ""), getContext());
                if (savedInstanceState == null) {





                }

            }
        }, 1000);*/
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("roleAndStatus", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("roleId", roleId);
        editor.putInt("status", status);
        editor.apply();
        initScreen();
        return convertView;

    }

//Initialize the screen
    public void initScreen() {




        SharedPreferences pref = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        //status = pref.getInt("status", 1);
        if (status == 1) roleId = 1;
        else roleId = pref.getInt("roleId", 0);

        tabLayout = (TabLayout) convertView.findViewById(R.id.tabX);

        TabBackStack tabBackStack=TabBackStack.getInstance();


        viewPager = (ViewPager) convertView.findViewById(R.id.viewpagerTabLayout);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabBackStack.setClassName("class Tablayout_with_viewpager");


        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));
        tabLayout.getTabAt(tabBackStack.getTabPosition()).select();

    }


    //Set up viewpager frament with tablayout
    private void setupViewPager(ViewPager viewPager) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("roleAndStatus", 0);
        sharedPreferences.contains("roleId");
        sharedPreferences.contains("status");
                    adapter.addFragment(new MyDetailsB(), "Details");
                    adapter.addFragment(new Biography(), "Biography");
                    viewPager.setAdapter(adapter);


    }
    //Change the tab
    public void changeTab(int tab){
        tabLayout = (TabLayout) convertView.findViewById(R.id.tabX);
        tabLayout.getTabAt(tab);
    }

//Viewoager adapter
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
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
