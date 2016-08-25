package com.example.cnguyen.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Document doc;
    private String[][] profiles = new String[100][2];
    private boolean[] celebTracker = new boolean[100]; // track which celeb has been seen in game
    boolean hasBegan = false;
    int numQuestionsCorrect = 0;

    String correctAnswer = "";
    ImageView celebImage = null;
    Button beginGame = null;
    Button choice = null;
    Button choice2 = null;
    Button choice3 = null;
    Button choice4 = null;

    public class DownloadTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                System.out.println("malformed url = " + params[0]);
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream inputStream = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public Bitmap downloadImage(String url) {
        DownloadImage di = new DownloadImage();
        Bitmap image = null;
        try {
            image = di.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setRandomAnswers() {
        Random rand = new Random();

        // each answer should have a unique name
        List<Integer> nameIndices = new ArrayList<>();
        while(nameIndices.size() < 4) {
            int temp = rand.nextInt(100);
            if (!nameIndices.contains(temp)) {
                nameIndices.add(temp);
            }
        }
        choice.setText(profiles[nameIndices.get(0)][0]);
        choice2.setText(profiles[nameIndices.get(1)][0]);
        choice3.setText(profiles[nameIndices.get(2)][0]);
        choice4.setText(profiles[nameIndices.get(3)][0]);
    }

    public void setCorrectAnswer() {
        Random rand = new Random();
        int temp = rand.nextInt(100);

        // set the correct name and image
        while (celebTracker[temp]) {
            temp = rand.nextInt(100);
        }
        System.out.println(temp + ": " + profiles[temp][0]);
        correctAnswer = profiles[temp][0];
        celebTracker[temp] = true;

        Bitmap bm = downloadImage(profiles[temp][1]);
        System.out.println("url = " + profiles[temp][1]);
        celebImage.setImageBitmap(bm);

        // set random choice for the correct answer
        switch (rand.nextInt(4)) {
            case 0:
                choice.setText(correctAnswer);
                break;
            case 1:
                choice2.setText(correctAnswer);
                break;
            case 2:
                choice3.setText(correctAnswer);
                break;
            case 3:
                choice4.setText(correctAnswer);
                break;
        }
    }

    public void userAnswer(View view) {
        if (hasBegan) {
            Button b = (Button) view;
            String answer = (String) b.getText();
            if (answer.equalsIgnoreCase(correctAnswer)) {
                numQuestionsCorrect++;
                if(numQuestionsCorrect == 100) {
                    Toast.makeText(MainActivity.this, "You won!", Toast.LENGTH_LONG).show();
                }
                nextCeleb();
            } else {
                Toast.makeText(MainActivity.this, "Sorry you lost! You answered "
                        + numQuestionsCorrect + "/100 correctly!"
                        , Toast.LENGTH_LONG).show();
                reset();
            }
        }
    }

    private void reset() {
        //celebImage.animate().alpha(0).setDuration(1000l);
        celebImage.setVisibility(View.INVISIBLE);
        beginGame.setVisibility(View.VISIBLE);
        hasBegan = false;
        numQuestionsCorrect = 0;
    }

    public void nextCeleb() {
        setRandomAnswers();
        setCorrectAnswer();
    }

    public void beginGame(View view) {
        Button b = (Button) view;
        b.setVisibility(View.INVISIBLE);
        celebImage.setVisibility(View.VISIBLE);
        nextCeleb();
        hasBegan = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebImage = (ImageView) findViewById(R.id.imageView);
        celebImage.setVisibility(View.INVISIBLE);

        beginGame = (Button) findViewById(R.id.button);
        choice = (Button) findViewById(R.id.choice);
        choice2 = (Button) findViewById(R.id.choice2);
        choice3 = (Button) findViewById(R.id.choice3);
        choice4 = (Button) findViewById(R.id.choice4);

        DownloadTask dt = new DownloadTask();
        String url = "http://www.posh24.com/celebrities";
        try {
            doc = dt.execute(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementById("webx_center")
                .getElementsByClass("channelList").select("div.channelListEntry");
        int i =  0;
        for (Element e : elements) {
            profiles[i][0] = e.select("div.image").select("img").attr("alt");
            profiles[i][1] = e.select("div.image").select("img").attr("src");
            i++;
            //System.out.println(profiles[i][0] + ": " + profiles[i][1]);
        }
    }
}
