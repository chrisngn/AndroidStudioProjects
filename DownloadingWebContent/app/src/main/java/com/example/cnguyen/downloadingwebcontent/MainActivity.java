package com.example.cnguyen.downloadingwebcontent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection connection;

            for (int i = 0; i < params.length; i++) {
                try {
                    url = new URL(params[i]);
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream inStream = connection.getInputStream();
                    InputStreamReader inStreamReader = new InputStreamReader(inStream);

                    int data = inStreamReader.read();
                    while(data != -1) {
                        result += (char) data;
                        data = inStreamReader.read();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DownloadTask task = new DownloadTask();
        String url = "http://www.ecowebhosting.co.uk/";
        String result = "";
        try {
            result = task.execute(url).get();
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }

        Log.i("Result", result);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
