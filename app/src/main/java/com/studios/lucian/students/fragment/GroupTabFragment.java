package com.studios.lucian.students.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.studios.lucian.students.MainActivity;
import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.util.List;

public class GroupTabFragment extends ListFragment {

    private StudentsDBHandler studentsDBHandler;
    private static final String TAG = GroupTabFragment.class.getSimpleName();

    public GroupTabFragment() {
    }
//
//    public GroupTabFragment(int position) {
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentsDBHandler = new StudentsDBHandler(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home_group_tab, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                private String m_Text = "";
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Title");

                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }

        displayStudents();
        return rootView;
    }

    private void displayStudents() {
        List<Student> studentList = studentsDBHandler.getAll();
        ArrayAdapter<Student> list = new ArrayAdapter<>(getActivity(), R.layout.item_student, studentList);
        setListAdapter(list);
    }
}
