package com.cnguyen.android.hackernews;

import android.content.ContentValues;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cnguyen on 8/12/2016.
 */
public class DownloadStory extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        String content = "";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream inputStream = con.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();
            while (data != -1) {
                content += (char) data;
                data = inputStreamReader.read();
            }

            inputStreamReader.close();
            inputStream.close();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    protected void onPostExecute(String storyContent) {
        try {
            JSONObject story = new JSONObject(storyContent);
            String title = story.getString("title");
            String link = story.getString("url");

            if (!MainActivity.titles.contains(title)) {
                // Insert into our DB
                String sql = "INSERT INTO news (title, link) " +
                        "VALUES ('" + title + "','" + link + "')";
                MainActivity.db.execSQL(sql);

                // Update our array lists
                MainActivity.titles.add(title);
                MainActivity.links.add(link);
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
