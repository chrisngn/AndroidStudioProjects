package com.example.cnguyen.timerdemo;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    private SeekBar sb = null;
    private TextView tv = null;
    private CountDownTimer timer = null;

    public void startTimer(final View view) {
        // Cancel previous timers if we want to start another timer before
        // the current one is completed
        if (timer != null) {
            timer.cancel();
        }

        // TODO: grey out the seekbar

        int countDownFrom = sb.getProgress() * 1000 + 200;
        Log.i("countDownFrom", "" + countDownFrom);

        timer = new CountDownTimer(countDownFrom, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText(String.valueOf(millisUntilFinished/1000));
                Log.i("millisUntilFinished", millisUntilFinished+"");
            }

            @Override
            public void onFinish() {
                tv.setText("0:00");

                // MainActivity.this == getApplicationContext()
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.rooster_crowing);
                mp.start();

                timer = null;
            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);

        sb = (SeekBar) findViewById(R.id.seekBar);
        sb.setMax(300);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    sb.setProgress(progress);

                    // If there is already a timer don't let the new input from the user
                    // change the current timer
                    if (timer == null) {
                        String m = String.valueOf(progress / 60);
                        String s = String.valueOf(progress % 60);
                        if (Integer.parseInt(s) < 10) {
                            s = "0" + s;
                        }
                        String t = "" + m + ":" + s;
                        tv.setText(String.valueOf(t));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // one way of controlling time for executions
//        final Handler handler = new Handler();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this, 1000);
//            }
//        };
//        handler.post(run);

    }
}
