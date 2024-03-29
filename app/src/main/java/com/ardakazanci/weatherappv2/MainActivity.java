package com.ardakazanci.weatherappv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.ardakazanci.weatherappv2.Adapter.ViewPagerAdapter;
import com.ardakazanci.weatherappv2.Common.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // View Reference
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;

    // Location Reference
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = findViewById(R.id.root_view);

        /* Toolbar */
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Runtime  Permission Dexter library
         */
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // Tüm izinler verildiğinde yapılacak işlemler.
                        // Kullanıcının lokasyon bilgisi alınacak
                        if (report.areAllPermissionsGranted()) {

                            buildLocationRequest();
                            buildLocationCallback();
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(coordinatorLayout, "İzinler reddedildi.", Snackbar.LENGTH_LONG).show();
                    }
                }).check();


    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                /**
                 * Son Lokasyon bilgisi alındığında viewpager ve tablayout yüklenecek
                 */
                Common.current_location = locationResult.getLastLocation();
                viewPager = findViewById(R.id.view_pager);
                setupViewPager();
                tabLayout = findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);

                Log.d("Location", locationResult.getLastLocation().getLatitude() + "/"
                        + locationResult.getLastLocation().getLongitude()
                );

            }
        };

    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
        viewPagerAdapter.addFragment(ForecastFragment.getInstance(), "5 DAYS");
        viewPagerAdapter.addFragment(CityFragment.getInstance(), "Cities");
        viewPager.setAdapter(viewPagerAdapter);
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }


}
