package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Group;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 15.03.2016.
 */
public class GroupsGridAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Group> mGroups;

    public GroupsGridAdapter(Context context, List<Group> groups) {
        mContext = context;
        mGroups = groups;
    }

    public void setData(List<Group> mGroups) {
        this.mGroups = mGroups;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = inflater.inflate(R.layout.item_grid_view, null);
            TextView textView = (TextView) gridView.findViewById(R.id.grid_text);
            textView.setText(mGroups.get(i).toString());
        } else {
            gridView = view;
        }
        return gridView;
    }
}
