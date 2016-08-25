package com.example.cnguyen.parsingjson;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    final String apiKey = "71e0e988bff16ae893b9b62973643f31";


    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream in = con.getInputStream();
                InputStreamReader inRead = new InputStreamReader(in);

                int data = inRead.read();
                while (data != -1) {
                    result += (char) data;
                    data = inRead.read();
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
                JSONObject json =  new JSONObject(s);
                JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
                Log.i("weather", weather.toString());

                while(weather.keys().hasNext()) {
                    String k = weather.keys().next();
                    String v = weather.getString(k);
                    weather.remove(k);
                    Log.i(k, v);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void download(View view) {
        DownloadTask dt = new DownloadTask();
        String location = "atlanta,us";
        String url = "http://api.openweathermap.org/data/2.5/weather"
                + "?q=" + location
                + "&appid=" + apiKey;
        String result = "";
        try {
            dt.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
