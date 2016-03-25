package com.studios.lucian.students.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.studios.lucian.students.fragment.GroupTabFragment;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 15.03.2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter {

    private List<GroupTabFragment> mData;

    public PageAdapter(FragmentManager fm, List<GroupTabFragment> data) {
        super(fm);
        mData = data;
    }

    @Override
    public Fragment getItem(int position){
        return mData.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Pirelli"; // text should be returned by position
    }

    @Override
    public int getCount() {
        Log.v("ADAPTER", "getCount" + mData.size());
        return mData.size();
    }
}
