package com.studios.lucian.students.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.DialogsHandler;
import com.studios.lucian.students.util.GradesDbHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 29.03.2016.
 */
public class StudentsListAdapter extends ArrayAdapter<Student> {
    private static String TAG = StudentsListAdapter.class.getSimpleName();

    private DialogsHandler mDialogsHandler;
    private GradesDbHandler mGradesDbHandler;
    private Context mContext;
    private List<Student> mStudentsList;
    private int mResource;

    static class ViewHolder {
        public TextView textViewName, textViewUsername;
        public Button buttonAdd;
    }

    public StudentsListAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        mContext = context;
        mStudentsList = objects;
        mResource = resource;
        mDialogsHandler = new DialogsHandler(context);
        mGradesDbHandler = new GradesDbHandler(context);
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
}
