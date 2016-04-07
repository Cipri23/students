package com.studios.lucian.students;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.studios.lucian.students.fragment.CsvFragment;
import com.studios.lucian.students.fragment.ExcelFragment;
import com.studios.lucian.students.fragment.MainFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Setup the drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerToggle.syncState();
        setupMainFragment(savedInstanceState);
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }

    private void setupMainFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_content, mMainFragment, "MF").commit();
            setTitle(getString(R.string.dashboard));
        }
        if (mNavigationView != null) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    Log.v(TAG, "if backStackCount = " + getFragmentManager().getBackStackEntryCount());
                    super.onBackPressed();
                } else {
                    Log.v(TAG, "else backStackCount = " + getFragmentManager().getBackStackEntryCount());
                    returnToMainFragmentFromBackStack();
                }
            }
        }
    }

    private void returnToMainFragmentFromBackStack() {
        Log.v(TAG, "returnToMF");
        CsvFragment csvFragment = (CsvFragment) getFragmentManager().findFragmentByTag("CSVF");
        ExcelFragment excelFragment = (ExcelFragment) getFragmentManager().findFragmentByTag("EF");

        if (csvFragment != null && csvFragment.isVisible()) {
            getFragmentManager().beginTransaction().remove(csvFragment).commit();
        }
        if (excelFragment != null && excelFragment.isVisible()) {
            getFragmentManager().beginTransaction().remove(excelFragment).commit();
        }
        // todo: handle if main fragment is visible and back stack count is > 0
        getFragmentManager().popBackStack();
        setTitle(R.string.dashboard);
        mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.students, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        Log.v(TAG, "onNavItemSelected");

        if (id == R.id.nav_home) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                returnToMainFragmentFromBackStack();
            }
        } else if (id == R.id.nav_excel_file) {
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
        } else if (id == R.id.nav_csv_file) {
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
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


//        mToolbar.setSubtitle();
        setTitle(item.getTitle());
        closeNavigationDrawer();
        return true;
    }

    private void closeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}