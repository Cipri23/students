package com.studios.lucian.students.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.Constants;
import com.studios.lucian.students.util.CsvParser;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CsvFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private String TAG = CsvFragment.class.getSimpleName();

    private List<String> path = null;
    private String root;
    private TextView myPath;
    StudentsDBHandler studentsDBHandler;

    public CsvFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_csv, container, false);
        studentsDBHandler = new StudentsDBHandler(getActivity());
        myPath = (TextView) rootView.findViewById(R.id.path);
        root = Environment.getExternalStorageDirectory().getPath();
        getDir(root);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
        ArrayAdapter<String> fileList = new ArrayAdapter<>(this.getActivity(), R.layout.item_explorer, item);
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
            String fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(Constants.DOT));
            if (fileExtension.equals(Constants.CSV)) {
                handleCsvFile(file);
            } else {
                Toast.makeText(getContext(), Constants.FORMAT_NOT_SUPPORTED + fileExtension, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleCsvFile(File file) {
        selectGroupNumberDialogBox(file);
        redirectToMainFragment();
        setNavDrawerItemAsChecked();
    }

    private void setNavDrawerItemAsChecked() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void redirectToMainFragment() {
        MainFragment mainFragment = new MainFragment();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, mainFragment);
        fragmentTransaction.commit();
    }

    private void selectGroupNumberDialogBox(final File fileName) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(Constants.DIALOG_TITLE);
        dialogBuilder.setMessage(Constants.DIALOG_MESSAGE);

        final EditText input = new EditText(getContext());
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupNumber = input.getText().toString();
                insertRecords(groupNumber, fileName);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final android.support.v7.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void insertRecords(String groupNumber, File fileName) {
        CsvParser csvParser = new CsvParser(fileName);
        List<Student> studentList = csvParser.getStudentsList(groupNumber);
        storeInDataBase(studentList);
    }

    private void storeInDataBase(List<Student> studentList) {
        new AsyncTask<List<Student>, Void, Void>() {
            @SafeVarargs
            @Override
            protected final Void doInBackground(List<Student>... lists) {
                studentsDBHandler.insertStudents(lists[0]);
                return null;
            }
        }.execute(studentList);
    }
}
