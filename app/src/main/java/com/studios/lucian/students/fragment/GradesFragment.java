package com.studios.lucian.students.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.GradesListAdapter;
import com.studios.lucian.students.model.Grade;
import com.studios.lucian.students.util.GradesDbHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.06.2016.
 */
public class GradesFragment extends ListFragment {

    private static final String ARG_MATRICOL = "matricol";
    private GradesDbHandler mGradesHandler;
    private TextView mEmptyText;
    private String mUsername;
    private String mMatricol = null;

    public static Fragment newInstance(String matricol) {
        GradesFragment fragment = new GradesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATRICOL, matricol);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMatricol = getArguments().getString(ARG_MATRICOL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyText = (TextView) view.findViewById(android.R.id.empty);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMatricol == null) return;

        mGradesHandler = new GradesDbHandler(getActivity());
        List<Grade> grades = mGradesHandler.getStudentGrades(mMatricol);
        GradesListAdapter adapterGrades = new GradesListAdapter(getActivity(), grades);
        getListView().setAdapter(adapterGrades);
        getListView().setEmptyView(mEmptyText);
    }
}