package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Grade;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 11.04.2016.
 */
public class GradesListAdapter extends ArrayAdapter<Grade> {

    private final Context mContext;
    private final List<Grade> mStudentsList;
    private final int mResource;

    public GradesListAdapter(Context context, List<Grade> objects) {
        super(context, R.layout.item_grade, objects);
        mContext = context;
        mStudentsList = objects;
        mResource = R.layout.item_grade;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.textViewDate = (TextView) rowView.findViewById(R.id.textViewDate);
            viewHolder.textViewGrade = (TextView) rowView.findViewById(R.id.textViewGrade);
            viewHolder.textViewLabNr = (TextView) rowView.findViewById(R.id.textViewLabNumber);
            rowView.setTag(viewHolder);
        }

        Grade grade = getItem(position);
        if (grade != null) {
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.textViewGrade.setText(String.valueOf(grade.getGrade()));
            holder.textViewLabNr.setText(String.valueOf(grade.getLabNumber()));
            holder.textViewDate.setText(grade.getDate());
        }
        return rowView;
    }

    static class ViewHolder {
        public TextView textViewGrade, textViewLabNr, textViewDate;
    }
}
