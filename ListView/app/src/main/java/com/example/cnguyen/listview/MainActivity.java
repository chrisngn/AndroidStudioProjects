package com.example.cnguyen.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> cherry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);

        cherry = new ArrayList<>();
        cherry.add("red");
        cherry.add("blue");
        cherry.add("brown");

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, cherry);

        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("cherry mx", cherry.get(position));
                Toast.makeText(MainActivity.this, "Cherry mx " + cherry.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
