package com.studios.ciprian.students.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.studios.ciprian.students.R;
import com.studios.ciprian.students.activity.DisplayStudentActivity;
import com.studios.ciprian.students.adapter.StudentsListAdapter;
import com.studios.ciprian.students.model.Grade;
import com.studios.ciprian.students.model.Group;
import com.studios.ciprian.students.model.Presence;
import com.studios.ciprian.students.model.Student;
import com.studios.ciprian.students.util.DialogsHandler;
import com.studios.ciprian.students.util.StudentButtonsListener;
import com.studios.ciprian.students.util.Validator;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class GroupFragment extends Fragment implements StudentButtonsListener {

    private static final String GROUP = "Group ";
    public static final String KEY_STUDENT = "key_student";

    private TextView emptyText;
    private DialogsHandler mDialogsHandler;
    private RecyclerView mRecyclerView;
    private Group mCurrentGroup;
    private final View.OnClickListener floatingButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View alertView = inflater.inflate(R.layout.dialog_add_student, null);

            TextView dialogGroupNumber = (TextView) alertView.findViewById(R.id.dialog_group_number);
            final EditText dialogMatricol = (EditText) alertView.findViewById(R.id.dialog_student_id);
            final EditText dialogName = (EditText) alertView.findViewById(R.id.dialog_name);
            final EditText dialogSurname = (EditText) alertView.findViewById(R.id.dialog_surname);
            final EditText dialogEmail = (EditText) alertView.findViewById(R.id.dialog_email);
            dialogGroupNumber.setText(getDialogGroupNumberText());

            dialog.setView(alertView)
                    .setPositiveButton(R.string.button_add_student, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            boolean inputsAreOk = true;
                            if (!Validator.isValidMatricol(dialogMatricol.getText().toString())) {
                                inputsAreOk = false;
                                dialogMatricol.setError("Invalid Student ID");
                            }
                            if (!Validator.isValidName(dialogName.getText().toString())) {
                                inputsAreOk = false;
                                dialogName.setError("Invalid Name");
                            }
                            if (!Validator.isValidName(dialogSurname.getText().toString())) {
                                inputsAreOk = false;
                                dialogSurname.setError("Invalid Surname");
                            }
                            if (inputsAreOk) {
                                addNewStudent(
                                        dialogMatricol.getText().toString(),
                                        dialogName.getText().toString(),
                                        dialogSurname.getText().toString(),
                                        dialogEmail.getText().toString());
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
    };

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        mCurrentGroup = (Group) bundle.getSerializable("groupNumber");
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyText = (TextView) view.findViewById(R.id.empty);
        FloatingActionButton mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(floatingButtonClick);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mDialogsHandler = new DialogsHandler(getActivity(), this);
        mFloatingActionButton.setVisibility(Validator.userIsStudent() ? View.GONE : View.VISIBLE);
        setAdapter();
    }

    private void setAdapter() {
        Query query = FirebaseFirestore.getInstance().collection("students")
                .whereEqualTo("groupNumber", mCurrentGroup.getNumber())
                .orderBy("name");

        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class)
                .setLifecycleOwner(this)
                .build();

        StudentsListAdapter listAdapter = new StudentsListAdapter(options,
                new Function1<Student, Unit>() {
                    @Override
                    public Unit invoke(final Student student) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Doriti stergerea acestui student?")
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteStudent(student);
                                    }
                                })
                                .setNegativeButton("Nu", null)
                                .create()
                                .show();
                        return null;
                    }
                },
                new Function1<Student, Unit>() {
                    @Override
                    public Unit invoke(Student student) {
                        onItemClick(student);
                        return null;
                    }
                },
                new Function1<Student, Unit>() {
                    @Override
                    public Unit invoke(Student student) {
                        mDialogsHandler.showAddPresenceDialog(student);
                        return null;
                    }
                },
                new Function1<Student, Unit>() {
                    @Override
                    public Unit invoke(Student student) {
                        mDialogsHandler.showAddGradeDialog(student);
                        return null;
                    }
                }
        ) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                emptyText.setVisibility(getSnapshots().size() == 0 ? View.VISIBLE : View.GONE);
            }
        };
        mRecyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(GROUP + mCurrentGroup.getNumber());
    }

    private void addNewStudent(String matricol, String name, String surname, String email) {
        final Student student = new Student(mCurrentGroup.getNumber(), matricol, name, surname, email);
        FirebaseFirestore.getInstance().collection("students")
                .document(student.getMatricol())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            showWarning(R.string.student_id_duplicated_title, R.string.student_id_duplicated_message);
                        } else {
                            FirebaseFirestore.getInstance().collection("students")
                                    .document(student.getMatricol())
                                    .set(student)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            increaseGroupCount();
                                        }
                                    });
                        }
                    }
                });
    }

    private void increaseGroupCount() {
        FirebaseFirestore.getInstance().collection("groups")
                .document(mCurrentGroup.getNumber())
                .update("studentCount", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), R.string.student_added, Toast.LENGTH_SHORT).show();
                    }
                });
    }
//
//    private void editStudent(final int id) {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        final LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View alertView = inflater.inflate(R.layout.dialog_edit_student, null);
//
//        TextView dialogStudentName = (TextView) alertView.findViewById(R.id.dialog_student_name);
//        final TextView dialogMatricolNumber = (TextView) alertView.findViewById(R.id.dialog_matricol_number);
//        final EditText dialogName = (EditText) alertView.findViewById(R.id.dialog_edit_name);
//        final EditText dialogSurname = (EditText) alertView.findViewById(R.id.dialog_edit_surname);
//
////        final Student student = mStudentsList.get(id);
////
////        dialogStudentName.setText(student.toString());
////        dialogMatricolNumber.setText(student.getMatricol());
////
////        dialog.setView(alertView)
////                .setPositiveButton(R.string.button_update_student, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        boolean inputsAreOk = true;
////                        if (!Validator.isValidName(dialogName.getText().toString())) {
////                            inputsAreOk = false;
////                            dialogName.setError("Invalid Name");
////                        }
////                        if (!Validator.isValidName(dialogSurname.getText().toString())) {
////                            inputsAreOk = false;
////                            dialogSurname.setError("Invalid Surname");
////                        }
////                        if (inputsAreOk) {
////                            updateStudent(
////                                    id,
////                                    dialogMatricolNumber.getText().toString(),
////                                    dialogName.getText().toString(),
////                                    dialogSurname.getText().toString());
////                        } else {
////                            showWarning(R.string.error_update_student_title, R.string.error_update_student_message);
////                        }
////                    }
////                })
////                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        dialogInterface.dismiss();
////                    }
////                })
////                .create()
////                .show();
//    }
//
//    private void updateStudent(int id, String matricol, String name, String surname) {
////        Student student = new Student(mCurrentGroup.getNumber(), matricol, name, surname);
////        if (mStudentsDbHandler.updateStudent(student)) {
////            mStudentsList.set(id, student);
////            listAdapter.notifyDataSetChanged();
////        } else {
////            Toast.makeText(getActivity(), R.string.error_update_student, Toast.LENGTH_SHORT).show();
////        }
//    }

    private void deleteStudent(Student student) {
        FirebaseFirestore.getInstance().collection("students")
                .document(student.getMatricol())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseFirestore.getInstance().collection("groups")
                                .document(mCurrentGroup.getNumber())
                                .update("studentCount", FieldValue.increment(-1));
                    }
                });
    }

    private void showWarning(int errorTitle, int errorMessage) {
        new AlertDialog
                .Builder(getActivity())
                .setTitle(errorTitle)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .create()
                .show();
    }

    private String getDialogGroupNumberText() {
        String EMPTY_SPACE = " ";
        return getString(R.string.dialog_group_number) + EMPTY_SPACE + mCurrentGroup.getNumber();
    }

    private void onItemClick(Student student) {
        if (!Validator.userIsStudent() || student.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            Intent intent = new Intent(getActivity(), DisplayStudentActivity.class);
            intent.putExtra(KEY_STUDENT, student);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Nu puteti vizualiza datele altor colegi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addGrade(final Student student, final Grade grade) {
        FirebaseFirestore.getInstance().collection("grades")
                .whereEqualTo("labNumber", grade.getLabNumber())
                .whereEqualTo("matricol", grade.getMatricol())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            FirebaseFirestore.getInstance().collection("grades")
                                    .add(grade)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), grade.getToastDisplay(student), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Exista deja o nota la acest laborator!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void addPresence(final Student student, final Presence presence) {
        FirebaseFirestore.getInstance().collection("presences")
                .whereEqualTo("labNumber", presence.getLabNumber())
                .whereEqualTo("matricol", presence.getMatricol())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            FirebaseFirestore.getInstance().collection("presences")
                                    .add(presence)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), presence.getToastDisplay(student), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Acestui student i-a fost deja adaugata prezenta la acest curs!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}