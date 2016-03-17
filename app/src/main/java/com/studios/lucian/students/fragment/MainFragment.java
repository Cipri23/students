package com.studios.lucian.students.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.PageAdapter;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    ViewPager viewPager;
    TabLayout tabLayout;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        PageAdapter adapter = new PageAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.setTabsFromPagerAdapter(adapter);

        return rootView;
    }
}
