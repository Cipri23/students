package com.studios.ciprian.students.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.studios.ciprian.students.R;
import com.studios.ciprian.students.fragment.ExcelFragment;
import com.studios.ciprian.students.fragment.MainFragment;

import static com.studios.ciprian.students.util.Validator.userIsStudent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;
    private static int USER_OPTION = -1;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Setup the drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerToggle.syncState();
        setupMainFragment(savedInstanceState);
    }

    private void setupMainFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, mMainFragment, "MF")
                    .commit();
            setTitle(getString(R.string.dashboard));
        }
        if (mNavigationView != null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mNavigationView.getMenu().getItem(1).setVisible(!userIsStudent());
            mNavigationView.setNavigationItemSelectedListener(this);

            TextView userEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_email);
            TextView userName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name);
            userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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
        ExcelFragment excelFragment = (ExcelFragment) getSupportFragmentManager().findFragmentByTag("EF");

        if (excelFragment != null && excelFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().remove(excelFragment).commit();
        }
        if (!getSupportFragmentManager().findFragmentByTag("MF").isAdded()) {
            getSupportFragmentManager().popBackStack();
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
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    returnToMainFragmentFromBackStack();
                }
                setTitle(item.getTitle());
                closeNavigationDrawer();
            } else if (id == R.id.nav_excel_file) {
                USER_OPTION = R.id.nav_excel_file;
                if (appHasPermission()) {
                    switchToExcelFragment();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void switchToExcelFragment() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("MF");
        if (mainFragment != null && mainFragment.isVisible()) {
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.main_content, new ExcelFragment(), "EF").
                    addToBackStack(null).
                    commit();
        } else {
            getSupportFragmentManager().
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
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (USER_OPTION == R.id.nav_excel_file) switchToExcelFragment();
            } else {
                Log.i(TAG, "onRequestPermissionsResult: permission was denied");
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

    public MainFragment getMainFragment() {
        return mMainFragment;
    }
}