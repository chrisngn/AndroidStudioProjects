package com.example.cnguyen.numberguessinggame;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Random rand = new Random();
    private int randomNumber = 0;
    private boolean hasWon = false;

    public void guess(View view) {
        EditText input = (EditText) findViewById(R.id.editText);
        String inputAsString = input.getText().toString();
        try {
            // Could be an error if the guess is blank or not an integer
            int guess = Integer.parseInt(inputAsString);

            if (guess > 10 || guess < 1) {
                // Return early because guess is out of range
                String outOfRange = "Your guess is out of range!";
                Toast.makeText(getApplicationContext(), outOfRange, Toast.LENGTH_SHORT).show();
                return;
            }

            String result;
            if (guess > randomNumber) {
                result = "Your guess is greater than the actual number!";
            } else if (guess < randomNumber) {
                result = "Your guess is less than the actual number!";
            } else {
                result = "Congratulations! Your guess is correct!";
                hasWon = true;
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            if (hasWon) {
                // Reset the game
                randomNumber = rand.nextInt(10) + 1;
                hasWon = false;
                String newGame = "Play again!";
                Toast.makeText(getApplicationContext(), newGame, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            String error = "Your guess is invalid!";
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            Log.i("Error", e.getMessage());
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

        randomNumber = rand.nextInt(10) + 1;
        Log.i("Init >>> Random Number", "" + randomNumber);
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
