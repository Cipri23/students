package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.DialogsHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 29.03.2016.
 */
public class StudentsListAdapter extends ArrayAdapter<Student> {
    private static String TAG = StudentsListAdapter.class.getSimpleName();

    private final DialogsHandler mDialogsHandler;
    private final Context mContext;
    private final List<Student> mStudentsList;
    private final int mResource;

    public StudentsListAdapter(Context context, List<Student> objects, GoogleApiClient googleApiClient) {
        super(context, R.layout.item_student, objects);
        mContext = context;
        mStudentsList = objects;
        mResource = R.layout.item_student;
        mDialogsHandler = new DialogsHandler(context, googleApiClient);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) rowView.findViewById(R.id.student_name_layout);
            viewHolder.textViewUsername = (TextView) rowView.findViewById(R.id.student_username_layout);
            viewHolder.buttonAdd = (Button) rowView.findViewById(R.id.student_list_button_1);
            rowView.setTag(viewHolder);
        }

        final Student student = getItem(position);
        if (student != null) {
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.textViewName.setText(student.toString());
            holder.textViewUsername.setText(student.getMatricol());
            holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogsHandler.showAddGradeDialog(student);
                }
            });
        }
        return rowView;
    }

    static class ViewHolder {
        public TextView textViewName, textViewUsername;
        public Button buttonAdd;
    }
}
