package com.example.cnguyen.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView text;
    LocationManager lm;
    String provider;
    final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    final String apiKey = "71e0e988bff16ae893b9b62973643f31"; // openweathermap api

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream in = con.getInputStream();
                InputStreamReader inReader = new InputStreamReader(in);

                int data = inReader.read();
                while (data != -1) {
                    result += (char) data;
                    data = inReader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // My code starts here
        text = (TextView) findViewById(R.id.textView);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);
        provider = lm.getBestProvider(c, true);

        // Request permission if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION
            );
        }

        // Execute code if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("onCreate", ">>> Permission granted");
        } else {
            Log.i("onCreate", ">>> Permission Denied");
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // 3 decimal places the most
        double lat = ((int) (location.getLatitude() * 100)) / 100.0;
        double lon = ((int) (location.getLongitude() * 100)) / 100.0;

        String url = "http://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + apiKey;
        DownloadTask dt = new DownloadTask();
        String info = "";

        try {
            JSONObject weatherData = new JSONObject(dt.execute(url).get());
            Log.i("weatherData", weatherData.toString());

            JSONObject weather = (JSONObject) ((JSONArray) weatherData.get("weather")).get(0);
            Log.i("weather", weather.toString());

            JSONObject main = (JSONObject) weatherData.get("main");
            Log.i("main", main.toString());

            double kLow = Double.parseDouble(main.getString("temp_min"));
            double fLow = ((int) (convertKelvinToFahrenheit(kLow) * 100)) / 100.0;

            double kHigh = Double.parseDouble(main.getString("temp_max"));
            double fHigh = ((int) (convertKelvinToFahrenheit(kHigh) * 100)) / 100.0;

            Address address = getAddress(location);
            if (address != null) {
                info += address.getFeatureName() + ","
                    + "\n" + address.getCountryName()
                    + "\n" + "(" + lat + ", " + lon + ")"
                    + "\n" + "Mainly " + weather.getString("description")
                    + "\n" + "Low: " + fLow
                    + "\n" + "High: " + fHigh
                    + "\n" + "Pressure: " + main.getString("pressure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        text.setText(info);
    }

    private double convertKelvinToFahrenheit(double temp) {
        return 9.0 / 5.0 * temp - 459.67;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission asked", "user granted!");
                } else {
                    Log.i("permission asked", "user denied!");
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

            // Requesting permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Requesting permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.removeUpdates(this);
        }
    }

    public Address getAddress(Location loc) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Address address = null;
        try {
            address = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1).get(0);
            if (address == null) {
                Log.i("getAddress", "No information can be found for the current (lat,lng)");
            } else {
                Log.i("address", address.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

}
