package com.studios.lucian.students.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.studios.lucian.students.fragment.GroupTabFragment;
import com.studios.lucian.students.fragment.MovieFragment;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 15.03.2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    private int numTabs = 0;

    private List<Fragment> groupsFragments;
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        //return new GroupTabFragment(position);
        switch (position) {
            case 0:
                fragment = new GroupTabFragment();
                break;
            case 1:
                fragment = new MovieFragment();
                break;
        }
        return fragment;
        // return new GroupTabFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Game";
                break;
            case 1:
                title = "Movie";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
