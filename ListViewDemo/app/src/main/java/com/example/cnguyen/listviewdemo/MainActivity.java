package com.example.cnguyen.listviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // My code starts here
        listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> al = new ArrayList<>();
        al.add("Red");
        al.add("Blue");
        al.add("Brown");
        al.add("Clear");
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
        listView.setAdapter(aa);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Object o = listView.getItemAtPosition(position);
                String o = al.get(position);

                // Allows us to switch to the second activity
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                i.putExtra("cherrySwitch", o.toString()); // passing data to the second activity
                startActivity(i);
                Log.i("cherrySwitch", o.toString());
            }
        });
    }


}
