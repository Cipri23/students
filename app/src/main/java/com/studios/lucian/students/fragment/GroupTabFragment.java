package com.studios.lucian.students.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.util.List;

public class GroupTabFragment extends ListFragment {

    private static final String TAG = GroupTabFragment.class.getSimpleName();

    private FloatingActionButton mFloatingActionButton;
    private StudentsDBHandler mStudentsDBHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_home_group_tab, container, false);
        mStudentsDBHandler = new StudentsDBHandler(getActivity());
        mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        setButtonClickListener();
        displayStudents();
        return rootView;
    }

    private void setButtonClickListener() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
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
                        input.getText();
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

    private void displayStudents() {
        List<Student> studentList = mStudentsDBHandler.getAll();
        ArrayAdapter<Student> list = new ArrayAdapter<>(getActivity(), R.layout.item_student, studentList);
        setListAdapter(list);
    }
}