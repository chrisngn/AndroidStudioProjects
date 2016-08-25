package com.example.cnguyen.hideandshow;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button showButton = null;
    private Button hideButton = null;
    private ImageView iv = null;

    public void show(View view) {
        //iv.animate().alpha(1f);
        iv.setVisibility(View.VISIBLE);
    }

    public void hide(View view) {
        //iv.animate().alpha(0f);
        iv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showButton = (Button) findViewById(R.id.button);
        hideButton = (Button) findViewById(R.id.button2);
        iv = (ImageView) findViewById(R.id.imageView);
    }
}
