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
import com.studios.lucian.students.util.DialogsHandler;
import com.studios.lucian.students.util.StudentsDbHandler;
import com.studios.lucian.students.util.Validator;
import com.studios.lucian.students.util.parser.ExcelParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with love by Lucian and @Pi on 25.01.2016.
 */
public class ExcelFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = ExcelFragment.class.getSimpleName();
    private static final String NOT_IMPLEMENTED_YET = "XLSX format is not supported yet.";
    private static final String ERROR_TRYING_PARSE = "An error occurred when the file was parsed.";
    private static final String FORMAT_NOT_SUPPORTED = "The selected file doesn't have the proper format: ";
    private static final String DIALOG_MESSAGE = "Please specify the group number for this file (between 0 and 10000):";
    private static final String DIALOG_TITLE = "Group Number";
    private static final int POSITION_MAIN_FRAGMENT = 0;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String DOT = ".";
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";

    private final String mFileExplorerRoot;
    private List<String> mPath;
    private TextView mTextViewPath;
    private StudentsDbHandler mStudentsDbHandler;

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

        Log.i(TAG, dirPath);

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
            switch (fileExtension) {
                case XLS:
                    dialogBoxSelectionGroupNumber(file);
                    break;
                case XLSX:
                    Toast.makeText(getContext(), NOT_IMPLEMENTED_YET, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getContext(), FORMAT_NOT_SUPPORTED + fileExtension, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void dialogBoxSelectionGroupNumber(final File file) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());

        dialogBuilder.setTitle(DIALOG_TITLE);
        dialogBuilder.setMessage(DIALOG_MESSAGE);

        final EditText input = new EditText(getContext());
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setView(input);

        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userInput = input.getText().toString();
                if (Validator.isValidGroupNumber(userInput)) {
                    handleXlsFile(file, userInput);
                } else {
                    DialogsHandler.showWrongGroupNumber(userInput, getContext());
                }
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

    private void handleXlsFile(File file, String groupNumber) {
        try {
            int studentsCount = insertRecords(file, groupNumber);
            Group group = new Group(groupNumber, studentsCount);
            redirectToMainFragment(group);
            setNavDrawerItemAsChecked();
        } catch (Exception e) {
            DialogsHandler.showErrorMessage(e.getMessage(), getContext());
        }
    }

    private void redirectToMainFragment(Group group) {
        MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
        mainFragment.addNewGroup(group);
        mainFragment.createFileInDrive();
        getFragmentManager().beginTransaction().replace(R.id.main_content, mainFragment).commit();
    }

    private int insertRecords(File fileName, String groupNumber) throws Exception {
        try {
            ExcelParser excelParser = new ExcelParser(groupNumber, fileName.getAbsolutePath());
            List<Student> studentList = excelParser.parseFile();
            mStudentsDbHandler.insertStudents(studentList);
            return studentList.size();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private void setNavDrawerItemAsChecked() {
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(POSITION_MAIN_FRAGMENT).setChecked(true);
    }
}
