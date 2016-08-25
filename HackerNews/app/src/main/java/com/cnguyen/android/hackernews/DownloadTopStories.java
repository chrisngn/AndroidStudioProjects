package com.cnguyen.android.hackernews;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cnguyen on 8/11/2016.
 */
public class DownloadTopStories extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        //android.os.Debug.waitForDebugger(); // for debugging

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
    protected void onPostExecute(String topStoriesContent) {
        //super.onPostExecute(s);

        //android.os.Debug.waitForDebugger(); // for debugging

        // Download each story and display them to the user
        String prefix = "https://hacker-news.firebaseio.com/v0/item/";
        String postfix = ".json?print=pretty";
        String storyUrl;
        try {
            JSONArray topStoriesArray = new JSONArray(topStoriesContent);
            int numStories = topStoriesArray.length() > 10 ? 10 : topStoriesArray.length();
            for (int i = 0; i < numStories; i++) {
                storyUrl = prefix + topStoriesArray.get(i) + postfix;
                // Require a new instance for each story to run asynchronously
                new DownloadStory().execute(storyUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
