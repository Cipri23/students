package com.studios.lucian.students.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;
import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.GroupsGridAdapter;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.model.Student;
import com.studios.lucian.students.repository.GroupDAO;
import com.studios.lucian.students.sync.DriveSyncHandler;
import com.studios.lucian.students.util.DriveFileCallBackListener;
import com.studios.lucian.students.util.StudentsDbHandler;
import com.studios.lucian.students.util.parser.CsvParser;
import com.studios.lucian.students.util.parser.ExcelParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener, DriveFileCallBackListener {

    private static final String TAG = MainFragment.class.getSimpleName();
    private GridView mGridView;
    private TextView mTextViewEmpty;
    private GroupsGridAdapter mGroupsGridAdapter;
    private StudentsDbHandler mStudentsDbHandler;
    private DriveSyncHandler mDriveSyncHandler;
    private List<Group> mGroups;
    private GoogleApiClient mGoogleApiClient;
    private String groupNumber = null;
    private GroupDAO mGroupDao;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentsDbHandler = new StudentsDbHandler(getActivity());
        mGroupDao = new GroupDAO(getActivity());
        mGroups = mGroupDao.getAll();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mTextViewEmpty = (TextView) view.findViewById(android.R.id.empty);

        mGroupsGridAdapter = new GroupsGridAdapter(getActivity(), mGroups);
        mGridView.setAdapter(mGroupsGridAdapter);
        mGridView.setEmptyView(mTextViewEmpty);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.dashboard);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Group selectedGroup = (Group) adapterView.getItemAtPosition(i);
        GroupFragment groupFragment = new GroupFragment();
        Bundle args = new Bundle();

        args.putString("groupNumber", selectedGroup.getNumber());
        groupFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, groupFragment, "detail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    public GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient;
        } else {
            Log.i(TAG, "getGoogleApiClient: mGoogleApiClient is null");
            return null;
        }
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.mGoogleApiClient = googleApiClient;
        Log.i(TAG, "setGoogleApiClient: " + mGoogleApiClient.isConnected());
        this.mDriveSyncHandler = new DriveSyncHandler(googleApiClient, this);
    }

    @Override
    public void setDriveFileId(String groupNumber, DriveId driveId) {
        mGroupDao.setDriveId(groupNumber, driveId.encodeToString());
        syncGroups();
    }

    public void addNewGroup(final File file, final String groupNumber, final String fileType) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Student> studentList = new ArrayList<>();

                if (fileType.equals("csv")) {
                    final CsvParser csvParser = new CsvParser(groupNumber, file);
                    studentList = csvParser.parseFile();
                } else if (fileType.equals("excel")) {
                    final ExcelParser excelParser = new ExcelParser(groupNumber, file.getAbsolutePath());
                    studentList = excelParser.parseFile();
                }
                Group group = new Group(groupNumber, studentList.size());
                if (mGroupDao.add(group)) {
                    mStudentsDbHandler.insertStudents(studentList);
                } else {
                    Log.i(TAG, "doInBackground: err");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mDriveSyncHandler != null) {
                    mDriveSyncHandler.syncNewFile(groupNumber);
                }
                syncGroups();
            }
        }.execute();
    }

    public void syncGroups() {
        mGroups = mGroupDao.getAll();
        mGroupsGridAdapter.setData(mGroups);
    }
}