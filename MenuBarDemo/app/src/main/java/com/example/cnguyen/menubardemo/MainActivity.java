package com.example.cnguyen.menubardemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Working with Menu Bar
        // https://developer.android.com/training/appbar/setting-up.html
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Alert
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Greeting")
                .setMessage("How are you?")
                .setPositiveButton("Good", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("onCreate >>> GOOD BUTTON CLICK ON ALERT");
                    }
                })
                .setNegativeButton("Bad", null)
                .show();
    }

    // Allows menu items to be displayed and used on the menu bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plus: {
                // User chose the "Settings" item, show the app settings UI...
                System.out.println(">>> SETTINGS BUTTON TAPPED");
                return true;
            } default: {
                System.out.println(">>> IN DEFAULT CASE");
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
            }
        }

    }
}
