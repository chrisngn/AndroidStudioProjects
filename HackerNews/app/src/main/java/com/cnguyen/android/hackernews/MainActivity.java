package com.cnguyen.android.hackernews;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> titles = new ArrayList<>();
    static ArrayList<String> links = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter = null;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState == null) {
            // Set up the database
            db = this.openOrCreateDatabase("NewsDB", MODE_PRIVATE, null);
            db.execSQL("DROP TABLE news");
            db.execSQL("CREATE TABLE IF NOT EXISTS news(" +
                    "title VARCHAR," +
                    "link VARCHAR," +
                    "UNIQUE(title) ON CONFLICT REPLACE)");
            //db.execSQL("DELETE FROM news");
            //db.execSQL("INSERT INTO news (title, link) " +
            //"VALUES ('Breaking News', 'https://www.google.com')");

            // Link the data to the ListView and display it on the MainActivity
            arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    titles);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(arrayAdapter);

            Log.i("savedInstanceState", "null");
        } else {
            Log.i("savedInstanceState", "not null");
        }

        // Retrieve data if we have any from database and initialize our ListView
        Cursor cursor = db.rawQuery("SELECT * FROM news", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int titleIndex = cursor.getColumnIndex("title");
            int linkIndex = cursor.getColumnIndex("link");
            String title = cursor.getString(titleIndex);
            String link = cursor.getString(linkIndex);
            titles.add(title);
            links.add(link);
            cursor.moveToNext();
        }
        arrayAdapter.notifyDataSetChanged();

        // Get additional top stories from Hacker News API and display them to the user
        getTopStories();

        // Listen for clicks on the titles
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String link = links.get(i);
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });

        // TODO: allow long clicks to delete content of news feed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTopStories() {
        // Pulls initial data from Hacker News API
        DownloadTopStories dts = new DownloadTopStories();
        String topStoriesUrl = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
        try {
            // Post execution is handled in DownloadTopStories#onPostExecute
            dts.execute(topStoriesUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
