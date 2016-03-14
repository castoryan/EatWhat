package com.example.castoryan.eatwhat_beta3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private SoundPool soundPool;

    public void setTitle(String newTitle){
        mTitle = newTitle;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    Fragment fr1 = new MyMainFragment();
    Fragment fr2 = new ShakeOne();
    Fragment fr3 = new CrazyShake();


    public void onSectionAttached(int number) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fr1 = new MyMainFragment();
        //fr2 = new Acceleration();
        //fr3 = new NewResturant();
        FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
                android.R.anim.slide_in_left, R.anim.abc_slide_out_top);
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                transaction.remove(fr2);
                transaction.remove(fr3);
                transaction.replace(R.id.container, fr1)
                        .commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                transaction.remove(fr1);
                transaction.remove(fr3);
                transaction.replace(R.id.container, fr2);
                transaction.commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                transaction.remove(fr1);
                transaction.remove(fr2);
                transaction.replace(R.id.container, fr3)
                        .commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager ft = getSupportFragmentManager();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addresturant) {
            NewResturantDo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    TextView name;
    TextView addr;
    TextView number;
    TextView latitude;
    TextView longtitude;
    Resturant newres;

    private void NewResturantDo(){
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View myLoginView = layoutInflater.inflate(R.layout.addrestaurant, null);
        name = (TextView)myLoginView.findViewById(R.id.new_name);
        addr = (TextView)myLoginView.findViewById(R.id.new_addr);
        number = (TextView)myLoginView.findViewById(R.id.new_number);
        latitude = (TextView)myLoginView.findViewById(R.id.new_lat);
        longtitude = (TextView)myLoginView.findViewById(R.id.new_lon);


        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("addRestaurant").
                setIcon(R.drawable.ic_launcher).
                setView(myLoginView).
                setPositiveButton("save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String resname = name.getText().toString();
                        String resaddr = addr.getText().toString();
                        String resnumber = number.getText().toString();
                        if (latitude.getText().toString().isEmpty() || longtitude.getText().toString().isEmpty()) {
                            newres = new Resturant(0, resname, resaddr, resnumber, 5, null, 1.0, 1.0);
                        } else {
                            Double reslat = Double.parseDouble(latitude.getText().toString());
                            Double reslon = Double.parseDouble(longtitude.getText().toString());
                            newres = new Resturant(0, resname, resaddr, resnumber, 5, null, reslat, reslon);

                        }


                        if (resname.isEmpty() || resaddr.isEmpty() || resnumber.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Oops! The name or address or number is empty", Toast.LENGTH_LONG).show();
                            NewResturantDo();
                        } else {

                            DataBaseHandler dbhand = new DataBaseHandler(getApplicationContext());
                            dbhand.addRestaurant(newres);
                            Toast.makeText(getApplicationContext(), "the new resturant is added", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment fr1 = new MyMainFragment();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fr1)
                                    .commit();
                        }
                    }
                }).
                setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
