package com.studios.lucian.students.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.IntentSender;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;
import com.studios.lucian.students.R;
import com.studios.lucian.students.adapter.GridAdapter;
import com.studios.lucian.students.model.Group;
import com.studios.lucian.students.util.StudentsDbHandler;

import java.util.List;

/**
 * Created with Love by Lucian and Pi on 21.02.2016.
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private GridView mGridView;
    private TextView mTextViewEmpty;
    private GridAdapter mGridAdapter;
    private StudentsDbHandler mStudentsDbHandler;
    private List<Group> mGroups;
    private GoogleApiClient mGoogleApiClient;

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
        setGridViewClickListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.dashboard);
    }

    private void setGridViewClickListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
    }

    public void addNewGroup(Group group) {
        mGroups.add(group);
        mGridAdapter.notifyDataSetChanged();
    }

    public void setGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        Log.i(TAG, "setGoogleApiClient: " + mGoogleApiClient.isConnected());
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public void createFileInDrive() {
        if (mGoogleApiClient.isConnected()) {
            //Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
        } else {
            Log.i(TAG, "createFileInDrive: client isn't connected. Can't create drive file.");
        }
    }

    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                            .setMimeType("text/html").build();
                    IntentSender intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.getDriveContents())
                            .build(mGoogleApiClient);
//                    try {
//                        startIntentSenderForResult(
//                                intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
//                    } catch (IntentSender.SendIntentException e) {
//                        Log.w(TAG, "Unable to send intent", e);
//                    }
                }
            };
}