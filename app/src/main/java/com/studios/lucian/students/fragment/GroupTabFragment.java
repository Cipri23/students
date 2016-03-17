package com.studios.lucian.students.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentsDBHandler = new StudentsDBHandler(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home_group_tab, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");

                    alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(getContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
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
