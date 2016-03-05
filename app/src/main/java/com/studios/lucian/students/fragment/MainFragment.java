package com.studios.lucian.students.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment extends ListFragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private StudentsDBHandler studentsDBHandler;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        studentsDBHandler = new StudentsDBHandler(getActivity());
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        displayStudents();
        return rootView;
    }

    private void displayStudents() {
        List<Student> studentList = studentsDBHandler.getAll();
        ArrayAdapter<Student> list = new ArrayAdapter<>(getActivity(), R.layout.item_student, studentList);
        setListAdapter(list);
    }
}
