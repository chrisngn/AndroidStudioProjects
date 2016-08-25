package com.example.cnguyen.memorableplaces;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by cnguyen on 7/25/2016.
 */
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