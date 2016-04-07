package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 29.03.2016.
 */
public class StudentsListAdapter extends ArrayAdapter<Student> {

    private Context mContext;
    private List<Student> mStudentsList;
    private int mResource;

    static class ViewHolder {
        public TextView textViewName, textViewUsername;
    }

    public StudentsListAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        mContext = context;
        mStudentsList = objects;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            // Configuring ViewHolder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) rowView.findViewById(R.id.student_name_layout);
            viewHolder.textViewUsername = (TextView) rowView.findViewById(R.id.student_username_layout);
            rowView.setTag(viewHolder);
        }

        Student student = getItem(position);
        if (student != null) {
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.textViewName.setText(student.toString());
            holder.textViewUsername.setText(student.getMatricol());
        }
        return rowView;
    }
}
