package com.ttl.project.thetalklist;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saubhagyam on 19/04/2017.
 */

public class Earn_Buy_tabLayout extends android.support.v4.app.Fragment  {

    public ViewPager viewPager;
    public TabLayout tabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View convertView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (convertView==null)
        convertView = inflater.inflate(R.layout.earn_buy_tablayout, container,false);

        initScreen(convertView);
        return convertView;
    }


    public void initScreen(View view){




        tabLayout = (TabLayout) view.findViewById(R.id.earn_buy_tab);


        viewPager = (ViewPager) view.findViewById(R.id.earn_buy_viewpagerTabLayout);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);


        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#4DB806"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {





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

        Earn_Buy_tabLayout.ViewPagerAdapter1 adapter = new Earn_Buy_tabLayout.ViewPagerAdapter1(fragmentManager);

        adapter.addFragment(new BuyCredits(), "Buy Credits");
        adapter.addFragment(new EarnCredits(), "Cashout Credits");
            viewPager.setAdapter(adapter);

    }


    private static class ViewPagerAdapter1 extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter1(FragmentManager manager) {
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
