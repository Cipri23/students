package com.studios.lucian.students.fragment;

import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.StudentsListAdapter;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.util.StudentsDbHandler;
import com.studios.lucian.students.util.Validator;

import java.util.List;

public class GroupDetailFragment extends ListFragment {
    private static final String TAG = GroupDetailFragment.class.getSimpleName();
    private static final String GROUP = "Group ";

    private FloatingActionButton mFloatingActionButton;
    private TextView emptyText;

    private Validator mValidator;
    private StudentsDbHandler mStudentsDbHandler;
    private StudentsListAdapter listAdapter;

    private String mGroupNumber;
    private String EMPTY_SPACE = " ";
    private List<Student> mStudentsList;

    public GroupDetailFragment() {
        Log.v(TAG, "GroupFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        Bundle bundle = this.getArguments();
        mGroupNumber = bundle.getString("groupNumber");
        Log.v(TAG, "backStackCount = " + getFragmentManager().getBackStackEntryCount());

        mStudentsDbHandler = new StudentsDbHandler(getActivity());
        mValidator = new Validator();
        return inflater.inflate(R.layout.fragment_home_group_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        emptyText = (TextView) view.findViewById(android.R.id.empty);
        setButtonClickListener();
        setListClickListener();
        setAdapter();
    }

    private void setListClickListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Clicked" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setEmptyView(emptyText);
        getActivity().setTitle(GROUP + mGroupNumber);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
//        menu.setHeaderTitle(((TextView) ((AdapterView.AdapterContextMenuInfo) menuInfo).targetView).getText());
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.student_menu_edit:
                    editStudent((int) info.id);
                    return true;
                case R.id.student_menu_delete:
                    deleteStudent((int) info.id);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }

    private void addNewStudent(String matricol, String name, String surname) {
        Student student = new Student(String.valueOf(mGroupNumber), matricol, name, surname);
        if (mStudentsDbHandler.addStudent(student)) {
            mStudentsList.add(student);
            listAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.student_added, Toast.LENGTH_SHORT).show();
        } else {
            showWarning(R.string.student_id_duplicated_title, R.string.student_id_duplicated_message);
        }
    }

    private void editStudent(final int id) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertView = inflater.inflate(R.layout.dialog_edit_student, null);

        TextView dialogStudentName = (TextView) alertView.findViewById(R.id.dialog_student_name);
        final TextView dialogMatricolNumber = (TextView) alertView.findViewById(R.id.dialog_matricol_number);
        final EditText dialogName = (EditText) alertView.findViewById(R.id.dialog_edit_name);
        final EditText dialogSurname = (EditText) alertView.findViewById(R.id.dialog_edit_surname);

        Student student = mStudentsList.get(id);

        dialogStudentName.setText(student.toString());
        dialogMatricolNumber.setText(student.getMatricol());

        dialog.setView(alertView)
                .setPositiveButton(R.string.button_update_student, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean inputsAreOk = true;
                        if (!mValidator.isValidName(dialogName.getText().toString())) {
                            inputsAreOk = false;
                            dialogName.setError("Invalid Name");
                        }
                        if (!mValidator.isValidName(dialogSurname.getText().toString())) {
                            inputsAreOk = false;
                            dialogSurname.setError("Invalid Surname");
                        }
                        if (inputsAreOk) {
                            updateStudent(
                                    id,
                                    dialogMatricolNumber.getText().toString(),
                                    dialogName.getText().toString(),
                                    dialogSurname.getText().toString());
                        } else {
                            showWarning(R.string.error_update_student_title, R.string.error_update_student_message);
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void updateStudent(int id, String matricol, String name, String surname) {
        Student student = new Student(String.valueOf(mGroupNumber), matricol, name, surname);
        if (mStudentsDbHandler.updateStudent(student)) {
            mStudentsList.set(id, student);
            listAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), R.string.error_update_student, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent(int id) {
        Student student = mStudentsList.get(id);
        if (mStudentsDbHandler.deleteStudent(student)) {
            mStudentsList.remove(id);
            listAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), R.string.student_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        mStudentsList = mStudentsDbHandler.getStudentsFromGroup(mGroupNumber);
        listAdapter = new StudentsListAdapter(getContext(), R.layout.item_student, mStudentsList);
        setListAdapter(listAdapter);
    }

    private void setButtonClickListener() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View alertView = inflater.inflate(R.layout.dialog_add_student, null);

                TextView dialogGroupNumber = (TextView) alertView.findViewById(R.id.dialog_group_number);
                final EditText dialogMatricol = (EditText) alertView.findViewById(R.id.dialog_student_id);
                final EditText dialogName = (EditText) alertView.findViewById(R.id.dialog_name);
                final EditText dialogSurname = (EditText) alertView.findViewById(R.id.dialog_surname);
                dialogGroupNumber.setText(getDialogGroupNumberText());

                dialog.setView(alertView)
                        .setPositiveButton(R.string.button_add_student, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean inputsAreOk = true;
                                if (!mValidator.isValidMatricol(dialogMatricol.getText().toString())) {
                                    inputsAreOk = false;
                                    dialogMatricol.setError("Invalid Student ID");
                                }
                                if (!mValidator.isValidName(dialogName.getText().toString())) {
                                    inputsAreOk = false;
                                    dialogName.setError("Invalid Name");
                                }
                                if (!mValidator.isValidName(dialogSurname.getText().toString())) {
                                    inputsAreOk = false;
                                    dialogSurname.setError("Invalid Surname");
                                }
                                if (inputsAreOk) {
                                    addNewStudent(
                                            dialogMatricol.getText().toString(),
                                            dialogName.getText().toString(),
                                            dialogSurname.getText().toString());
                                } else {
                                    showWarning(R.string.error_add_student_title, R.string.error_add_student_message);
                                }
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void showWarning(int errorTitle, int errorMessage) {
        new AlertDialog
                .Builder(getContext())
                .setTitle(errorTitle)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .create()
                .show();
    }

    @Override
    public void onPause() {
        unregisterForContextMenu(getListView());
        super.onPause();
    }

    private String getDialogGroupNumberText() {
        return getString(R.string.dialog_group_number) + EMPTY_SPACE + mGroupNumber;
    }
}