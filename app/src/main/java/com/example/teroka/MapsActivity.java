package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.teroka.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLng;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Set Home selected
        bnv.setSelectedItemId(R.id.suggestion);

        //Perform ItemSelectedListener
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.suggestion:
                        startActivity(new Intent(getApplicationContext(), RecommendationActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.planner:
                        startActivity(new Intent(getApplicationContext(), PlannerActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LogSign.class));
                        return true;
                }
                return false;
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
            }
        };

        //setLocationPermission();

        // Add a marker in Sydney and move the camera
        LatLng cs = new LatLng(5.354821805096556, 100.30146077432408);
        LatLng hin = new LatLng(5.412384273153523, 100.32814592449438);
        LatLng avatar = new LatLng(5.463858497865358, 100.30833245333058);
        LatLng kampong = new LatLng(5.540675348939846, 100.38012106682294);
        LatLng atv = new LatLng(5.319938896490884, 100.20477601100198);

        mMap.addMarker(new MarkerOptions().position(hin).title("Marker in Hin Bus Depot"));
        mMap.addMarker(new MarkerOptions().position(cs).title("Marker in School of Computer Science"));
        mMap.addMarker(new MarkerOptions().position(kampong).title("Marker in Kampong Agong"));
        mMap.addMarker(new MarkerOptions().position(avatar).title("Marker in Avatar Secret Garden"));
        mMap.addMarker(new MarkerOptions().position(atv).title("Marker in Penang ATV Eco Balik Pulau"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(cs));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hin));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kampong));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(avatar));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(atv));
    }

    private void setLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}