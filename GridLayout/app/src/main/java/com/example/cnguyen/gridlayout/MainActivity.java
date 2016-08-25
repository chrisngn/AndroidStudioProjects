package com.example.cnguyen.gridlayout;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = null;
    private SeekBar trackControl = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        trackControl = (SeekBar) findViewById(R.id.trackSeekbar);
    }

    public void playPhrase(View view) {
//        mediaPlayer = getAudio(view);

        int viewId = view.getId();
        Log.i("viewId", ""+viewId);

        String resourceName = view.getResources().getResourceEntryName(viewId);
        Log.i("resourceName", resourceName);

        int resourceId = getResources().getIdentifier(resourceName, "raw", getPackageName());
        Log.i("resourceId", ""+resourceId);
        Log.i("package", getPackageName());

        mediaPlayer = MediaPlayer.create(this, resourceId);
        mediaPlayer.start();

        volumeListener();
        trackListener(mediaPlayer);
    }

    private MediaPlayer getAudio(View view) {
        MediaPlayer mp = null;
        String tag = view.getTag().toString();
        Log.i("view's tag", tag);
        switch (tag) {
            case "button":
                mp = MediaPlayer.create(this, R.raw.doyouspeakenglish);
                break;
            case "button2":
                mp = MediaPlayer.create(this, R.raw.goodevening);
                break;
            case "button3":
                mp = MediaPlayer.create(this, R.raw.hello);
                break;
            case "button4":
                mp = MediaPlayer.create(this, R.raw.howareyou);
                break;
            case "button5":
                mp = MediaPlayer.create(this, R.raw.ilivein);
                break;
            case "button6":
                mp = MediaPlayer.create(this, R.raw.mynameis);
                break;
            case "button7":
                mp = MediaPlayer.create(this, R.raw.please);
                break;
            case "button8":
                mp = MediaPlayer.create(this, R.raw.welcome);
                break;
        }
        return mp;
    }

    private void volumeListener() {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);

        volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeControl.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void trackListener(MediaPlayer mp) {
        final MediaPlayer mediaPlayer = mp;

        trackControl.setMax(mp.getDuration());
        trackControl.setProgress(0);

        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("media player position", mediaPlayer.getCurrentPosition() + "");
                trackControl.setProgress(mediaPlayer.getCurrentPosition());

//                Log.i("duration", mediaPlayer.getDuration() + "");
                if (!mediaPlayer.isPlaying())
                    t.cancel();
            }
        }, 0, 10);

        trackControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.cnguyen.gridlayout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.cnguyen.gridlayout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
