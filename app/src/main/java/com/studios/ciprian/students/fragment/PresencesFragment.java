package com.studios.ciprian.students.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.studios.ciprian.students.R;
import com.studios.ciprian.students.adapter.PresenceListAdapter;
import com.studios.ciprian.students.model.Presence;

import org.jetbrains.annotations.NotNull;


public class PresencesFragment extends Fragment {

    private static final String ARG_MATRICOL = "matricol";

    private TextView mEmptyText;
    private String mMatricol = null;

    public static Fragment newInstance(String matricol) {
        PresencesFragment fragment = new PresencesFragment();
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
        return inflater.inflate(R.layout.fragment_presences, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mEmptyText = view.findViewById(android.R.id.empty);

        Query query = FirebaseFirestore.getInstance().collection("presences")
                .whereEqualTo("matricol", mMatricol)
                .whereEqualTo("owner", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .orderBy("date");

        FirestoreRecyclerOptions<Presence> options = new FirestoreRecyclerOptions.Builder<Presence>()
                .setQuery(query, Presence.class)
                .setLifecycleOwner(this)
                .build();

        PresenceListAdapter adapter = new PresenceListAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                mEmptyText.setVisibility(getSnapshots().size() > 0 ? View.GONE : View.VISIBLE);
                recyclerView.setVisibility(getSnapshots().size() > 0 ? View.VISIBLE : View.GONE);
            }
        };
        recyclerView.setAdapter(adapter);
    }
}