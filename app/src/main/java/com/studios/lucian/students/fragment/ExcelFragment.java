package com.studios.lucian.students.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studios.lucian.students.R;
import com.studios.lucian.students.util.StudentsDBHandler;
import com.studios.lucian.students.util.ExcelParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by Lucian and @Pi on 25.01.2016.
 */
public class ExcelFragment extends android.support.v4.app.ListFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = ExcelFragment.class.getSimpleName();

    private List<String> path = null;
    private String root;
    private TextView myPath;

    public ExcelFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        View rootView = inflater.inflate(R.layout.fragment_from_excel, container, false);
        myPath = (TextView) rootView.findViewById(R.id.path);
        root = Environment.getExternalStorageDirectory().getPath();
        getDir(root);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
    }

    private void getDir(String dirPath) {
        myPath.setText(String.format("%s%s", getString(R.string.location), dirPath));
        List<String> item = new ArrayList<>();
        path = new ArrayList<>();
        File file = new File(dirPath);
        File[] files = file.listFiles();

        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(file.getParent());
        }

        for (File file1 : files) {
            if (!file1.isHidden() && file1.canRead()) {
                path.add(file1.getPath());
                if (file1.isDirectory()) {
                    item.add(file1.getName() + "/");
                } else {
                    item.add(file1.getName());
                }
            }
        }
        ArrayAdapter<String> fileList = new ArrayAdapter<>(this.getActivity(), R.layout.row, item);
        setListAdapter(fileList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = new File(path.get(position));

        if (file.isDirectory()) {
            if (file.canRead()) {
                getDir(path.get(position));
            } else {
                new AlertDialog.Builder(this.getActivity()).setTitle("[" + file.getName() + "] folder can't be read!").setPositiveButton("OK", null).show();
            }
        } else {
            ExcelParser excelParser = new ExcelParser(file.getAbsolutePath());
            StudentsDBHandler studentsDBHandler = new StudentsDBHandler(getActivity());
            studentsDBHandler.clearStudents();
            studentsDBHandler.insertStudents(excelParser.getStudentsList());

            MainFragment mainFragment = new MainFragment();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, mainFragment);
            fragmentTransaction.commit();
        }
    }
}
