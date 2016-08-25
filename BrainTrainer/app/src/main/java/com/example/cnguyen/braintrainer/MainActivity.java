package com.example.cnguyen.braintrainer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // add effects
    private boolean heartbeatTriggered = false;
    private MediaPlayer heartBeatMedia = null;
//    private MediaPlayer marioMedia = null;
//    private SoundPool marioSoundPool = null;
//    private int soundID = 0;
//    private int volume = 0;
//    private AudioManager audioManager = null;

    private TextView incorrectTextView = null;
    private TextView correctTextView = null;
    private TextView timeTextView = null;
    private TextView questionTextView = null;
    private Button answer1 = null;
    private Button answer2 = null;
    private Button answer3 = null;
    private Button answer4 = null;

    private CountDownTimer timer = null;
    private int correctAnswerCount = 0;
    private int incorrectAnswerCount = 0;
    private int randomNumber;
    private boolean gameStarted = false;

    public void submitAnswer(View view) {
        Button b = (Button) view;
        String bText = b.getText().toString();
        Log.i("button text", bText);

        if (gameStarted && timer != null) {
            int userAnswer = Integer.parseInt(bText);
            if (userAnswer == randomNumber) {
                correctAnswerCount++;
                correctTextView.setText(String.valueOf(correctAnswerCount));
                //TODO: limitation if user is answering too quickly
                //marioMedia = MediaPlayer.create(this, R.raw.mario_jumping);
                //marioMedia.start();
                //marioSoundPool.play(soundID, volume, volume, 1, 0, 0.5f);
            } else {
                incorrectAnswerCount++;
                incorrectTextView.setText(String.valueOf(incorrectAnswerCount));
                // TODO: include sound or animation to indicate incorrect answer
            }
            generateProblem();
        }
    }

    private void generateProblem() {
        Random rand = new Random();
        int x = rand.nextInt(21); // [0, 20]
        int y = rand.nextInt(21); // [0, 20]
        setRandomQuestion(x, y);

        // every answer should be unique
        ArrayList<Integer> takenNumbers = new ArrayList<>();

        // correct answer will be set later
        // for now random answer should not contain correct answer
        takenNumbers.add(randomNumber);

        int i = 0;
        int a;
        while (i == 0) {
            a = setRandomAnswer(answer1);
            if (!takenNumbers.contains(a)) {
                takenNumbers.add(a);
                i++;
            }
            Log.i("takenNumbers", takenNumbers.toString());
            Log.i("a", "" + a);
        }

        i = 0;
        while (i == 0) {
            a = setRandomAnswer(answer2);
            if (!takenNumbers.contains(a)) {
                takenNumbers.add(a);
                i++;
            }
            Log.i("takenNumbers", takenNumbers.toString());
            Log.i("a", ""+a);
        }
        i = 0;
        while (i == 0) {
            a = setRandomAnswer(answer3);
            if (!takenNumbers.contains(a)) {
                takenNumbers.add(a);
                i++;
            }
            Log.i("takenNumbers", takenNumbers.toString());
            Log.i("a", ""+a);
        }
        i = 0;
        while (i == 0) {
            a = setRandomAnswer(answer4);
            if (!takenNumbers.contains(a)) {
                takenNumbers.add(a);
                i++;
            }
            Log.i("takenNumbers", takenNumbers.toString());
            Log.i("a", ""+a);
        }
        // set the correct answer after every answer has been randomized
        setCorrectAnswer();
    }

    private void setCorrectAnswer() {
        Button b;
        Random rand = new Random();
        int n = rand.nextInt(4);
        switch (n) {
            case 0:
                b = (Button) findViewById(R.id.button);
                b.setText(String.valueOf(randomNumber));
                break;
            case 1:
                b = (Button) findViewById(R.id.button2);
                b.setText(String.valueOf(randomNumber));
                break;
            case 2:
                b = (Button) findViewById(R.id.button3);
                b.setText(String.valueOf(randomNumber));
                break;
            case 3:
                b = (Button) findViewById(R.id.button4);
                b.setText(String.valueOf(randomNumber));
                break;
        }
    }

    private int setRandomAnswer(Button b) {
        Random rand = new Random();
        int x = rand.nextInt(21); // [0, 20];
        int y = rand.nextInt(21); // [0, 20];
        int randomAnswer = -1;

        // avoid division and modulo by 0
        int n = y == 0 ? rand.nextInt(3) : rand.nextInt(5);
        switch (n) {
            case 0:
                randomAnswer = x + y;
                b.setText(String.valueOf(randomAnswer));
                break;
            case 1:
                randomAnswer = x - y;
                b.setText(String.valueOf(randomAnswer));
                break;
            case 2:
                randomAnswer = x * y;
                b.setText(String.valueOf(randomAnswer));
                break;
            case 3:
                randomAnswer = x / y;
                b.setText(String.valueOf(randomAnswer));
                break;
            case 4:
                randomAnswer = x % y;
                b.setText(String.valueOf(randomAnswer));
                break;
        }
        return randomAnswer;
    }

    private void setRandomQuestion(int x, int y) {
        Random rand = new Random();

        // avoid division and modulo by 0
        int n = y == 0 ? rand.nextInt(3) : rand.nextInt(5);
        switch (n) {
            case 0:
                randomNumber = x + y;
                questionTextView.setText(x + " + " + y);
                break;
            case 1:
                randomNumber = x - y;
                questionTextView.setText(x + " - " + y);
                break;
            case 2:
                randomNumber = x * y;
                questionTextView.setText(x + " * " + y);
                break;
            case 3:
                randomNumber = x / y;
                questionTextView.setText(x + " / " + y);
                break;
            case 4:
                randomNumber = x % y;
                questionTextView.setText(x + " % " + y);
        }
    }

    private void resetGame() {
        if (timer != null) {
            timer.cancel();
        }
        timeTextView.setText("0:30");
        incorrectTextView.setText("0");
        correctTextView.setText("0");
        correctAnswerCount = 0;
        incorrectAnswerCount = 0;
        heartbeatTriggered = false;
    }

    public void startGame(View view) {
        resetGame();

        // 30 seconds count down
        // 1 second increment
        // 200 ms padding for processing time
        timer = new CountDownTimer(30000 + 200, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 10000) {
                    timeTextView.setText("0:0" + millisUntilFinished / 1000);
                } else {
                    timeTextView.setText("0:" + millisUntilFinished / 1000);
                }

                // heartbeat sound goes off at 5 seconds or less
                if (millisUntilFinished < 6000 && !heartbeatTriggered) {
                    triggerHeartbeat();
                }
            }

            @Override
            public void onFinish() {
                timeTextView.setText("0:00");
                Toast.makeText(getApplicationContext(), "You scored " +
                        (100.0 * correctAnswerCount / (correctAnswerCount + incorrectAnswerCount)) +
                                "%!", Toast.LENGTH_SHORT).show();
                heartBeatMedia.stop();
                heartBeatMedia.release();
                heartBeatMedia = null;
                timer = null;
                gameStarted = false;
            }
        }.start();
        gameStarted = true;
        generateProblem();
    }

    private void triggerHeartbeat() {
        heartBeatMedia = MediaPlayer.create(this, R.raw.heartbeat);
        heartBeatMedia.start();
        heartbeatTriggered = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incorrectTextView = (TextView) findViewById(R.id.incorrectTextView);
        correctTextView = (TextView) findViewById(R.id.correctTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        questionTextView = (TextView) findViewById(R.id.questionTextView);

        answer1 = (Button) findViewById(R.id.button);
        answer2 = (Button) findViewById(R.id.button2);
        answer3 = (Button) findViewById(R.id.button3);
        answer4 = (Button) findViewById(R.id.button4);
//
//        marioMedia = MediaPlayer.create(this, R.raw.mario_jumping);
//
//        marioSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//        marioSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//
//            }
//        });
//        soundID = marioSoundPool.load(this, R.raw.mario_jumping, 1);
//
//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
