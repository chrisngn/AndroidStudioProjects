package com.example.cnguyen.advancestringmanipulation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//import org.w3c.dom.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public class WebScraping extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();

                // expecting only 1 website to be provided
                /*URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream in = con.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    res += (char) data;
                    data = reader.read();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }
    }

    public void getWebsiteContent(View view) {
        String url = "http://www.posh24.com/celebrities";
        try {
            WebScraping ws = new WebScraping();
            Document doc = ws.execute(url).get();

            // narrow down our html
            Elements elements = doc.getElementById("webx_center")
                    .getElementsByClass("channelList")
                    .select("div.channelListEntry");

            String[][] profiles = getCelebrityNamesAndImage(elements);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // used with the other way of getting html
        /*WebScraping ws = new WebScraping();
        String html = "";
        try {
            html = ws.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }


    private String[][] getCelebrityNamesAndImage(Elements elements) {
        // 0 = name, 1 = image link
        String[][] profiles = new String[100][2];
        int i = 0;
        for (Element e : elements) {
            profiles[i][0] = e.select("div.image").select("img").attr("alt");
            profiles[i][1] = e.select("div.image").select("img").attr("src");
            i++;
            System.out.println(e.select("div.image").select("img").attr("alt")
                    + " = " + e.select("div.image").select("img").attr("src"));
        }
        return profiles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
