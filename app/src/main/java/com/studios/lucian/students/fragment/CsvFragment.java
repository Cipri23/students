package com.studios.lucian.students.fragment;

import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
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
import com.studios.lucian.students.activity.MainActivity;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDbHandler;
import com.studios.lucian.students.util.parser.CsvParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link } subclass.
 */
public class CsvFragment
        extends ListFragment
        implements AdapterView.OnItemClickListener {

    private static final String FORMAT_NOT_SUPPORTED = "The selected file doesn't have the proper format.";
    private static final String DIALOG_MESSAGE = "Please specify the group number for this file";
    private static final String DIALOG_TITLE = "Group Number";
    private static final String CSV = ".csv";
    private static final String DOT = ".";
    private final String TAG = CsvFragment.class.getSimpleName();
    private final String mFileExplorerRoot;
    private String mGroupNumber;
    private List<String> mPath;
    private StudentsDbHandler mStudentsDbHandler;

    private TextView mTextViewPath;

    public CsvFragment() {
        mFileExplorerRoot = Environment.getExternalStorageDirectory().getPath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_csv, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextViewPath = (TextView) view.findViewById(R.id.path);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mStudentsDbHandler = new StudentsDbHandler(getActivity());
        getListView().setOnItemClickListener(this);
        getDirectories(mFileExplorerRoot);
    }

    private void getDirectories(String dirPath) {
        mTextViewPath.setText(String.format("%s%s", getString(R.string.location), dirPath));
        List<String> item = new ArrayList<>();
        mPath = new ArrayList<>();
        File file = new File(dirPath);
        File[] files = file.listFiles();

        if (!dirPath.equals(mFileExplorerRoot)) {
            item.add(mFileExplorerRoot);
            mPath.add(mFileExplorerRoot);
            item.add("../");
            mPath.add(file.getParent());
        }

        for (File file1 : files) {
            if (!file1.isHidden() && file1.canRead()) {
                mPath.add(file1.getPath());
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
        File file = new File(mPath.get(position));
        if (file.isDirectory()) {
            if (file.canRead()) {
                getDirectories(mPath.get(position));
            } else {
                new AlertDialog.Builder(this.getActivity()).setTitle("[" + file.getName() + "] folder can't be read!").setPositiveButton("OK", null).show();
            }
        } else {
            String fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(DOT));
            if (fileExtension.equals(CSV)) {
                selectGroupNumberDialogBox(file);
            } else {
                Toast.makeText(getContext(), FORMAT_NOT_SUPPORTED + fileExtension, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectGroupNumberDialogBox(final File file) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(DIALOG_TITLE);
        dialogBuilder.setMessage(DIALOG_MESSAGE);

        final EditText input = new EditText(getContext());
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setWidth(20);
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGroupNumber = input.getText().toString();
                handleCsvFile(file);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void handleCsvFile(File file) {
        int studentsCount = insertRecords(file);
        Group group = new Group(mGroupNumber, studentsCount);
        redirectToMainFragment(group);
        setNavDrawerItemAsChecked();
    }

    private int insertRecords(File fileName) {
        CsvParser csvParser = new CsvParser(mGroupNumber, fileName);
        List<Student> studentList = csvParser.parseFile();
        mStudentsDbHandler.insertStudents(studentList);
        return studentList.size();
    }

    private void redirectToMainFragment(Group group) {
        MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
        mainFragment.addNewGroup(group);
        getFragmentManager().beginTransaction().replace(R.id.main_content, mainFragment).commit();
    }

    private void setNavDrawerItemAsChecked() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}