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
import com.studios.lucian.students.adapter.PresenceListAdapter;
import com.studios.lucian.students.model.Presence;
import com.studios.lucian.students.repository.PresenceDAO;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.06.2016.
 */
public class PresencesFragment extends ListFragment {

    private static final String ARG_MATRICOL = "matricol";

    private TextView mEmptyText;
    private PresenceDAO mPresenceDao;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmptyText = (TextView) view.findViewById(android.R.id.empty);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMatricol == null) return;

        mPresenceDao = new PresenceDAO(getActivity());
        List<Presence> presences = mPresenceDao.getStudentPresences(mMatricol);
        PresenceListAdapter adapterPresences = new PresenceListAdapter(getActivity(), presences);
        getListView().setAdapter(adapterPresences);
        getListView().setEmptyView(mEmptyText);
    }
}