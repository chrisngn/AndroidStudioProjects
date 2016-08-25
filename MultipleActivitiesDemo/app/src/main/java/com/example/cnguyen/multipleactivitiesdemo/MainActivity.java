package com.example.cnguyen.multipleactivitiesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void switchActivity(View view) {
        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
        Button b = (Button) view;
        String text = (String) b.getText();
        Log.i("button text", text);
        i.putExtra("text", text);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
