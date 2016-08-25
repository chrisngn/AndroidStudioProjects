package com.example.cnguyen.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> titles = new ArrayList<>();
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    ListView listView;
    EditText newNote;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        // Hide until the user clicks add
        newNote = (EditText) findViewById(R.id.editText);
        newNote.setVisibility(View.INVISIBLE);

        button = (Button) findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        // Retrieve data if possible
        SharedPreferences sp = getSharedPreferences("com.example.cnguyen.notes", MODE_PRIVATE);


        // Init data
        if (titles.size() == 0) {
            initData();
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        // Listen for click
        // Click = expand note
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("title", titles.get(i));
                intent.putExtra("note", notes.get(i));
                startActivity(intent);
        });

        // Listen for long click
        // Long click = delete note
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            sp.edit().remove(titles.get(i)).apply();
            titles.remove(i);
            notes.remove(i);
            arrayAdapter.notifyDataSetChanged();
            return true;
        });
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
        if (id == R.id.add) {
            System.out.println("onOptionsItemSelected >>> R.id.add");
            addNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        // Alert to get title
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a Title");

        // Allows user to input the title
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialogInterface, i) -> {
            // Allows user input note
            listView.setVisibility(View.INVISIBLE);
            newNote.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            titles.add(input.getText().toString());
            arrayAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });
        builder.show();
    }

    public void submit(View view) {
        // Update the listView
        String note = newNote.getText().toString();
        notes.add(note);

        SharedPreferences sp = getSharedPreferences("com.example.cnguyen.notes", MODE_PRIVATE);
        int i = titles.size() - 1;
        sp.edit().putString(titles.get(i), notes.get(i)).apply();

        // Display the note after it is created
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("title", titles.get(titles.size() - 1));
        intent.putExtra("note", note);
        startActivity(intent);
    }

    // Init only once
    private void initData() {
        SharedPreferences sp = getSharedPreferences("com.example.cnguyen.notes", MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        Log.i("map.entrySet().size()", map.entrySet().size()+"");

        if (map.entrySet().size() == 0) {
         // no initial data so add some default data
            titles.add("You Exist in My Song");
            notes.add("Ni cun zai wo shen shen de nao hai li"
                    + "\nWo de meng li"
                    + "\nWo de xin li"
                    + "\nWo de ge sheng li");
            int i = titles.size() - 1;
            sp.edit().putString(titles.get(i), notes.get(i)).apply();

            titles.add("I Did Not Want Love From You");
            notes.add("You are leaving"
                    + "\nLove is leaving"
                    + "\nYou left, leaving behind the pain of love"
                    + "\nI did not want love from you"
                    + "\nAll I said was for you to stay by my side"
                    + "\nDid you have to leave like that?"
                    + "\nJust as you came like it was nothing"
                    + "\nYou, too, left like it was nothing");
            i = titles.size() - 1;
            sp.edit().putString(titles.get(i), notes.get(i)).apply();
        } else {
            // add data from last time
            for (Map.Entry entry : map.entrySet()) {
                titles.add((String) entry.getKey());
                notes.add((String) entry.getValue());
            }
        }
    }
}
