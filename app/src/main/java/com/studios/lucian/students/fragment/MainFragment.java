package com.studios.lucian.students.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.GridAdapter;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.util.StudentsDbHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment
        extends Fragment
        implements ResultCallback<DriveApi.DriveContentsResult>,
        AdapterView.OnItemClickListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private GridView mGridView;
    private TextView mTextViewEmpty;
    private GridAdapter mGridAdapter;
    private StudentsDbHandler mStudentsDbHandler;
    private List<Group> mGroups;
    private GoogleApiClient mGoogleApiClient;
    private String groupNumber = null;
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(@NonNull DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Toast.makeText(getContext(), "Error while trying to create the file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, "onResult: file created with id: " + result.getDriveFile().getDriveId());
                    String groupDriveId = result.getDriveFile().getDriveId().encodeToString();
                    if (groupNumber != null) {
                        mStudentsDbHandler.setGroupDriveId(groupDriveId, groupNumber);
                        groupNumber = null;
                    } else {
                        Log.i(TAG, "onResult: DB can't be updated with the drive id.");
                    }
                }
            };

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mStudentsDbHandler = new StudentsDbHandler(getContext());
        mGroups = mStudentsDbHandler.getUniqueGroups();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mTextViewEmpty = (TextView) view.findViewById(android.R.id.empty);

        mGridAdapter = new GridAdapter(getContext(), mGroups);
        mGridView.setAdapter(mGridAdapter);
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
        GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
        Bundle args = new Bundle();

        args.putString("groupNumber", selectedGroup.getNumber());
        groupDetailFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, groupDetailFragment, "detail")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    public void addNewGroup(Group group) {
        mGroups.add(group);
        groupNumber = group.getNumber();
        mGridAdapter.notifyDataSetChanged();
        createFileInDrive();
    }

    public GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient;
        } else {
            Log.i(TAG, "getGoogleApiClient: mGoogleApiClient is null");
            return null;
        }
    }

    public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        Log.i(TAG, "setGoogleApiClient: " + mGoogleApiClient.isConnected());
        this.mGoogleApiClient = mGoogleApiClient;
    }

    private void createFileInDrive() {
        if (mGoogleApiClient.isConnected()) {
            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(this);
        } else {
            Log.i(TAG, "createFileInDrive: client isn't connected. Can't create drive file.");
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Create File in Drive CallBack
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
        if (!driveContentsResult.getStatus().isSuccess()) {
            Toast.makeText(getContext(), "Error while trying to create new file contents", Toast.LENGTH_SHORT).show();
            return;
        }

        final DriveContents driveContents = driveContentsResult.getDriveContents();

        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    writer.write("Activity for group " + groupNumber + "\n");
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("Group " + groupNumber)
                        .setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .setStarred(true)
                        .build();

                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }
}