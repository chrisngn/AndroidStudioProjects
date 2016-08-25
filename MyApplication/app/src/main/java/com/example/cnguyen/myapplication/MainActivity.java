package com.example.cnguyen.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

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

        // Code starts here
        textView = (TextView) findViewById(R.id.textView);

        // Saving data
        SharedPreferences sp = getSharedPreferences("com.example.cnguyen.myapplication", Context.MODE_PRIVATE);
        String language = sp.getString("language", "undefined");
        if (language.equalsIgnoreCase("undefined")) {
            setLanguage();
        } else {
            // Update the textView to the default language
            textView.setText(language);
        }
    }

    public void setLanguage() {
        final SharedPreferences sp = getSharedPreferences("com.example.cnguyen.myapplication", Context.MODE_PRIVATE);
        // Alert
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Settings")
                .setMessage("Choose your default language")
                .setPositiveButton("Korean", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("onCreate >>> Korean");
                        String language = "Korean";
                        sp.edit().putString("language", language).apply();
                        textView.setText(language);
                    }
                })
                .setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("onCreate >>> English");
                        String language = "English";
                        sp.edit().putString("language", language).apply();
                        textView.setText(language);
                    }
                })
                .show();
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
        if (id == R.id.change_language) {
            System.out.println("onOptionsItemSelected >>> USER WISHES TO CHANGE LANGUAGE");
            setLanguage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
