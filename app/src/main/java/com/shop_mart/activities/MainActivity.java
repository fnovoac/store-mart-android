package com.shop_mart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.shop_mart.R;
import com.shop_mart.adapters.TabAdapter;
import com.shop_mart.constants.Constant;
import com.shop_mart.fragments.MapViewFragment;
import com.shop_mart.fragments.StoreFragment;
import com.shop_mart.listeners.CurrentLocationListener;
import com.shop_mart.location.LocationTracker;
import com.shop_mart.perferences.AppPreferences;
import com.shop_mart.receivers.CurrentLocationReceiver;
import com.shop_mart.utils.AppUtils;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private AppPreferences preferences;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private LocationTracker locationTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager= (ViewPager)findViewById(R.id.viewpager);
        setViewPageAdapter(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        preferences = new AppPreferences(getApplicationContext());

        findLocation();

        boolean checkLocationEnable = AppUtils.enableLocationSettings(this);

        if (!checkLocationEnable) {
            showSettingsAlert();
        }
    }
    private void setViewPageAdapter(ViewPager viewPager){
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new StoreFragment(),"Store List");
        adapter.addFragment(new MapViewFragment(),"Map View");
        viewPager.setAdapter(adapter);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void findLocation() {
        locationTracker = new LocationTracker("my.action")
                .setInterval(1000)
                .setGps(true)
                .setNetWork(false)
                .currentLocation(new CurrentLocationReceiver(new CurrentLocationListener() {

                    @Override
                    public void onCurrentLocation(Location location) {
                        Log.d("callback", ":onCurrentLocation" + location.getLongitude());
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();

                        String lat = String.valueOf(latitude);
                        String lng = String.valueOf(longitude);

                        String streetAddress = AppUtils.getCompleteAddressString(getApplicationContext(),
                                Double.parseDouble(lat),Double.parseDouble(lng));

                        showToast("lat: " + lat + " " + "lng: " + lng );
//                        if (streetAddress != null){
//                            showToast("address: " + streetAddress);
//                        }else{
//                            showToast("unable to get address");
//                        }
                        preferences.putString(Constant.LATITUDE, lat);
                        preferences.putString(Constant.LONGITUDE, lng);
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.d("callback", ":onPermissionDenied");
                        locationTracker.stopLocationService(getBaseContext());
                    }
                })).start(getBaseContext(), MainActivity.this);
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
