package com.example.cnguyen.listviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent i = getIntent();
        String cherrySwitch = i.getStringExtra("cherrySwitch");
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Cherry MX " + cherrySwitch);
    }

    public void switchActivity(View view) {
        // Allows us to switch back to the first activity
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
