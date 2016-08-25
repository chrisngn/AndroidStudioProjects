package com.example.cnguyen.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();

    // Connects our array list to our list view
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://developer.android.com/training/appbar/setting-up.html#utility
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Setting up our default lists
        if (!places.contains("Find places")) {
            places.add("Find places");
            locations.add(new LatLng(0, 0)); // default value that we will not map
        } else {
            Log.i("onCreate", "Trying to add \"Find places\" again!");
        }
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        // Listens for clicks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String textClicked = places.get(position);
                System.out.println("setOnItemClickListener.textClicked: " + textClicked);

                // Start the maps activity
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("textClicked", textClicked);
                startActivity(i);
            }
        });
    }
}
