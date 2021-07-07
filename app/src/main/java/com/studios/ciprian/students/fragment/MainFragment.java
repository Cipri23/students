package com.studios.ciprian.students.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.studios.ciprian.students.R;
import com.studios.ciprian.students.activity.AuthActivity;
import com.studios.ciprian.students.adapter.GroupsAdapter;
import com.studios.ciprian.students.model.Group;
import com.studios.ciprian.students.model.Student;
import com.studios.ciprian.students.util.parser.CsvParser;
import com.studios.ciprian.students.util.parser.ExcelParser;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainFragment extends Fragment {

    private TextView mTextViewEmpty;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView mGridView = (RecyclerView) view.findViewById(R.id.gridview);
        mGridView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        mTextViewEmpty = (TextView) view.findViewById(R.id.empty);

        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(FirebaseFirestore.getInstance().collection("groups"), Group.class)
                .setLifecycleOwner(this)
                .build();

        GroupsAdapter mGroupsGridAdapter = new GroupsAdapter(options, new Function1<Group, Unit>() {
            @Override
            public Unit invoke(Group group) {
                GroupFragment groupFragment = new GroupFragment();
                Bundle args = new Bundle();
                args.putSerializable("groupNumber", group);
                groupFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content, groupFragment, "detail")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                return null;
            }
        }) {
            @Override
            public void onDataChanged() {
                if (getSnapshots().isEmpty()) {
                    mGridView.setVisibility(View.GONE);
                    mTextViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    mGridView.setVisibility(View.VISIBLE);
                    mTextViewEmpty.setVisibility(View.GONE);
                }
                super.onDataChanged();
            }
        };
        mGridView.setAdapter(mGroupsGridAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.logout_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getActivity(), AuthActivity.class));
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.dashboard);
    }

    @SuppressLint("StaticFieldLeak")
    public void addNewGroup(final File file, final String groupNumber, final String fileType) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Student> studentList = new ArrayList<>();

                if (fileType.equals("excel")) {
                    final ExcelParser excelParser = new ExcelParser(groupNumber, file.getAbsolutePath());
                    studentList = excelParser.parseFile();
                }
                Group group = new Group(groupNumber, studentList.size());
                addGroup(group, studentList);
                return null;
            }
        }.execute();
    }

    private void addGroup(Group group, final List<Student> studentList) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        DocumentReference groupRef = db.collection("groups").document(group.getNumber());
        batch.set(groupRef, group);

        for (Student student : studentList) {
            DocumentReference nycRef = db.collection("students").document(student.getMatricol());
            batch.set(nycRef, student);
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Grupa a fost adaugata cu success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "A intervenit o eroare", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}