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

import com.studios.lucian.students.MainActivity;
import com.studios.lucian.students.R;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.Constants;
import com.studios.lucian.students.util.ExcelParser;
import com.studios.lucian.students.util.StudentsDBHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by Lucian and @Pi on 25.01.2016.
 */
public class ExcelFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = ExcelFragment.class.getSimpleName();
    private static final int POSITION_MAIN_FRAGMENT = 0;
    private static String DOT = ".";
    public static String XLS = ".xls";
    public static String XLSX = ".xlsx";

    private String mGroupNumber;
    private String mFileExplorerRoot;
    private List<String> mPath;
    private TextView mTextViewPath;
    private StudentsDBHandler mStudentsDBHandler;

    public ExcelFragment() {
        mFileExplorerRoot = Environment.getExternalStorageDirectory().getPath();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        return inflater.inflate(R.layout.fragment_excel, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextViewPath = (TextView) view.findViewById(R.id.path);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mStudentsDBHandler = new StudentsDBHandler(getActivity());
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
            if (fileExtension.equals(XLS) || fileExtension.equals(XLSX)) {
                dialogBoxSelectionGroupNumber(file);
            } else {
                Toast.makeText(getContext(), Constants.FORMAT_NOT_SUPPORTED + fileExtension, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dialogBoxSelectionGroupNumber(final File file) {
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
                mGroupNumber = input.getText().toString();
                handleXlsFile(file);
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

    private void handleXlsFile(File file) {
        int studentsCount = insertRecords(file);
        Group group = new Group(mGroupNumber, studentsCount);
        redirectToMainFragment(group);
        setNavDrawerItemAsChecked();
    }

    private void redirectToMainFragment(Group group) {
        MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
        mainFragment.addNewGroup(group);
    //    getChildFragmentManager().popBackStack();
        getFragmentManager().beginTransaction().replace(R.id.main_content, mainFragment).commit();
    }

    private int insertRecords(File fileName) {
        ExcelParser excelParser = new ExcelParser(mGroupNumber, fileName.getAbsolutePath());
        List<Student> studentList = excelParser.parseFile();
        mStudentsDBHandler.insertStudents(studentList);
        return studentList.size();
    }

    private void setNavDrawerItemAsChecked() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(POSITION_MAIN_FRAGMENT).setChecked(true);
    }
}
