package com.studios.lucian.students.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.studios.lucian.students.R;
import com.studios.lucian.students.fragment.CsvFragment;
import com.studios.lucian.students.fragment.ExcelFragment;
import com.studios.lucian.students.fragment.MainFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final int REQUEST_CODE_CREATOR = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;
    private static int USER_OPTION = -1;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private MainFragment mMainFragment;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Setup the drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mDrawerToggle.syncState();
        setupMainFragment(savedInstanceState);
    }

    private void setupMainFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, mMainFragment, "MF")
                    .commit();
            setTitle(getString(R.string.dashboard));
        }
        if (mNavigationView != null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                if (resultCode == RESULT_OK) {
                    if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                        Log.i(TAG, "onActivityResult: connecting client...");
                        mGoogleApiClient.connect();
                    }
                }
                break;
            case REQUEST_CODE_CREATOR:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data
                            .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    Toast.makeText(MainActivity.this, "ID: " + driveId, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onActivityResult: " + resultCode);
                }
                finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        super.onBackPressed();
                    } else {
                        if (!returnToMainFragmentFromBackStack()) {
                            super.onBackPressed();
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private boolean returnToMainFragmentFromBackStack() {
        CsvFragment csvFragment = (CsvFragment) getFragmentManager().findFragmentByTag("CSVF");
        ExcelFragment excelFragment = (ExcelFragment) getFragmentManager().findFragmentByTag("EF");

        if (csvFragment != null && csvFragment.isVisible()) {
            getFragmentManager().beginTransaction().remove(csvFragment).commit();
        }
        if (excelFragment != null && excelFragment.isVisible()) {
            getFragmentManager().beginTransaction().remove(excelFragment).commit();
        }
        if (!getFragmentManager().findFragmentByTag("MF").isAdded()) {
            getFragmentManager().popBackStack();
            setTitle(R.string.dashboard);
            mNavigationView.getMenu().getItem(0).setChecked(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        try {
            if (id == R.id.nav_home) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    returnToMainFragmentFromBackStack();
                }
                setTitle(item.getTitle());
                closeNavigationDrawer();
            } else if (id == R.id.nav_excel_file) {
                USER_OPTION = R.id.nav_excel_file;
                if (appHasPermission()) {
                    switchToExcelFragment();
                }
            } else if (id == R.id.nav_csv_file) {
                USER_OPTION = R.id.nav_csv_file;
                if (appHasPermission()) {
                    switchToCsvFragment();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void switchToCsvFragment() {
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("MF");
        if (mainFragment != null && mainFragment.isVisible()) {
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.main_content, new CsvFragment(), "CSVF").
                    addToBackStack(null).
                    commit();
        } else {
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.main_content, new CsvFragment(), "CSVF").
                    commit();
        }
        setTitle(R.string.students_from_csv);
        closeNavigationDrawer();
    }

    private void switchToExcelFragment() {
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("MF");
        if (mainFragment != null && mainFragment.isVisible()) {
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.main_content, new ExcelFragment(), "EF").
                    addToBackStack(null).
                    commit();
        } else {
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.main_content, new ExcelFragment(), "EF").
                    commit();
        }
        setTitle(R.string.students_from_excel);
        closeNavigationDrawer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (USER_OPTION == R.id.nav_excel_file) switchToExcelFragment();
                    if (USER_OPTION == R.id.nav_csv_file) switchToCsvFragment();
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: permission was denied");
                }
            }
        }
    }

    private boolean appHasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    private void closeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "API client connected.");
        setGoogleApiClientToFragment();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while starting resolution activity", e);
            }
        } else {
            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
    }

    private void setGoogleApiClientToFragment() {
        try {
            mMainFragment.setGoogleApiClient(mGoogleApiClient);
        } catch (NullPointerException ex) {
            Log.i(TAG, "setGoogleApiClientToFragment: " + ex.getMessage());
        }
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }
}

//    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
//            new ResultCallback<DriveApi.DriveContentsResult>() {
//                @Override
//                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
//                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
//                            .setMimeType("application/vnd.google-apps.ritz").build();
//                    IntentSender intentSender = Drive.DriveApi
//                            .newCreateFileActivityBuilder()
//                            .setInitialMetadata(metadataChangeSet)
//                            .setInitialDriveContents(result.getDriveContents())
//                            .build(mGoogleApiClient);
//                    try {
//                        startIntentSenderForResult(intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
//                    } catch (IntentSender.SendIntentException e) {
//                        Log.w(TAG, "Unable to send intent", e);
//                    }
//                }
//            };