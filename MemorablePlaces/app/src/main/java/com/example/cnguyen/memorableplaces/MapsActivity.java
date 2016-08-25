package com.example.cnguyen.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, LocationListener {

    private GoogleMap mMap;
    private final String apiKey = "71e0e988bff16ae893b9b62973643f31"; // openweathermap.com's api

    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.my_child_toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
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

        // My code starts here
        Intent i = getIntent();
        String textClicked = i.getStringExtra("textClicked");
        if (textClicked.equalsIgnoreCase("Find places")) {
            // Display all the locations we currently have
            displaySavedPlaces();

            // Display the user's current location on the map
            displayUserCurrentLocation();

            // Listen for long clicks
            mMap.setOnMapLongClickListener(this);
        } else {
            // Zoom into that specific location
            int index = MainActivity.places.indexOf(textClicked);
            String address = MainActivity.places.get(index);
            LatLng latLng = MainActivity.locations.get(index);
            MarkerOptions m = new MarkerOptions();
            m.position(latLng);
            m.title(address);
            mMap.addMarker(m);
            // zoom into this specific location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }

    private void placeMarker(LatLng latLng) {
        Address address = getAddress(latLng);
        if (address != null) {
            String firstLineAddress = address.getAddressLine(0);

            MarkerOptions m = new MarkerOptions();
            m.position(latLng);
            m.title(firstLineAddress); // use the first line of the address as the title
            mMap.addMarker(m);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Update the listView
            MainActivity.places.add(firstLineAddress);
            MainActivity.locations.add(latLng);
            MainActivity.arrayAdapter.notifyDataSetChanged();
            Log.i("places", MainActivity.places.toString());
        } else {
            Toast.makeText(MapsActivity.this, "This location does not have an address!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void displaySavedPlaces() {
        for (int j = 1; j < MainActivity.places.size(); j++) {
            String address = MainActivity.places.get(j);
            LatLng latLng = MainActivity.locations.get(j);
            MarkerOptions m = new MarkerOptions();
            m.position(latLng);
            m.title(address);
            mMap.addMarker(m);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void displayUserCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);

        // Request permission if not yet granted
        // Choosing not to override onRequestPermissionsResult
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    0
            );
        }

        // Execute code if user granted permission
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(provider);
                onLocationChanged(location);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "Cannot get your current location!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        placeMarker(latLng);
    }

    private Address getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Address address = null;
        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    // May use this later
    private String getLatLngData(LatLng latLng) {
        String url = "http://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + latLng.latitude
                + "&lon=" + latLng.longitude
                + "&appid=" + apiKey;
        Log.i("url", url);
        DownloadTask dt = new DownloadTask();
        String result = "";
        try {
            result = dt.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Address address = getAddress(latLng);
        String firstLineAddress = address.getAddressLine(0);

        MarkerOptions m = new MarkerOptions();
        m.position(latLng);
        m.title(firstLineAddress); // use the first line of the address as the title
        mMap.addMarker(m);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // Choosing to let this be handled the default way
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home: {
//                this.finish();
//                return true;
//            } default: {
//                Log.i("onOptionsItemSelected", "The user's action was not recognized!");
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//            }
//        }
//    }

}
