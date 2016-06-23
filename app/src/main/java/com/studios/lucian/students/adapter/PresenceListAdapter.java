package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Presence;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.06.2016.
 */
public class PresenceListAdapter extends ArrayAdapter<Presence> {

    private final Context mContext;
    private final List<Presence> presenceList;
    private final int mResource;

    public PresenceListAdapter(Context applicationContext, List<Presence> presences) {
        super(applicationContext, R.layout.presence_item, presences);
        this.mContext = applicationContext;
        this.presenceList = presences;
        this.mResource = R.layout.presence_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.textViewLabNr = (TextView) rowView.findViewById(R.id.textViewLabNumber);
            viewHolder.textViewDate = (TextView) rowView.findViewById(R.id.textViewDate);
            rowView.setTag(viewHolder);
        }

        Presence presence = getItem(position);
        if (presence != null) {
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.textViewLabNr.setText(String.valueOf(presence.getLabNumber()));
            holder.textViewDate.setText(presence.getDate());
        }
        return rowView;
    }

    static class ViewHolder {
        public TextView textViewLabNr, textViewDate;
    }
}
