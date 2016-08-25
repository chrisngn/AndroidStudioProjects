package com.example.cnguyen.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final String apiKey = "71e0e988bff16ae893b9b62973643f31";

    EditText zipcode = null;
//    EditText country = null;

    TextView textView = null;

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("result", s);

            try {
                JSONObject json = new JSONObject(s);
                String city = json.getString("name");

                String kLow = json.getJSONObject("main").getString("temp_min");
                double fLow = kelvinToFahrenheit(Double.parseDouble(kLow));

                String kHigh = json.getJSONObject("main").getString("temp_max");
                double fHigh = kelvinToFahrenheit(Double.parseDouble(kHigh));

                String description = json.getJSONArray("weather").getJSONObject(0)
                        .getString("description");

                String output = city + "\n"
                        + "Low: " + fLow + "\n"
                        + "High: " + fHigh + "\n"
                        + description;
                textView.setText(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public double kelvinToFahrenheit(double tk) {
            double tf = tk * 9 /5 - 459.67;
            int temp = (int) (tf * 100);
            tf = temp / 100.0;
            return tf;
        }
    }

    public void getWeather(View view) {
        // hide the keyboard once the button is tapped
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

        DownloadTask dt = new DownloadTask();
        String zip = zipcode.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather"
                + "?zip=" + zip + ",us"
                + "&appid=" + apiKey;
        Log.i("url", url);

        try {
            if (!zip.isEmpty() && zip.length() == 5) {
                dt.execute(url);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a 5 digit zip code!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipcode = (EditText) findViewById(R.id.zipcode);
        textView = (TextView) findViewById(R.id.textView);
    }
}
