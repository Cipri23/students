package com.studios.lucian.students.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.PageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int mTabsNumber;

    private List<GroupTabFragment> fragments;
    PageAdapter adapter;

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        fragments = new ArrayList<>();
        fragments.add(new GroupTabFragment());
        fragments.add(new GroupTabFragment());

        adapter = new PageAdapter(getFragmentManager(), fragments);

//        mTabLayout.addTab(mTabLayout.newTab().setText("Pi"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("Pirelli"));
//        mTabsNumber = 2;

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        return rootView;
    }

    public void addNewTab(String groupNumber) {
        fragments.add(new GroupTabFragment());      // nullPointerEx here
        adapter.notifyDataSetChanged();
    }
}