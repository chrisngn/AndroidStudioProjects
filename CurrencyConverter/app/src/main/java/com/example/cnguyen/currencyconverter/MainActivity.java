package com.example.cnguyen.currencyconverter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convertCurrency(View view) {
        EditText input = (EditText) findViewById(R.id.editText);
        try {
            String amountAsString = input.getText().toString();
            if (amountAsString.equals("")) {
                // Error: User did not enter anything
                String errorMsg = "Please enter a valid number";
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                Log.i("Error", errorMsg);
            } else {
                double amount = Double.parseDouble(amountAsString);
                double convertedAmount = amount * 0.89;
                convertedAmount = (((int) (convertedAmount * 100)) / 100.0 );
                String convertedAmountAsString = "$" + amount + " = £" + convertedAmount;
                Toast.makeText(getApplicationContext(), convertedAmountAsString, Toast.LENGTH_LONG).show();
                Log.i("Success", convertedAmountAsString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
