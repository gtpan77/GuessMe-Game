package com.example.gauravsharma.guessme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.gauravsharma.guessme.MainActivity.celebURLs;

public class GameActivity extends AppCompatActivity {

    ImageView imageView;
    TextView timer, points,result;
    Button button0, button1, button2, button3;

    int chosenCeleb = 0;
    int locationAC = 0;
    String[] answers = new String[4];

    int score = 0;
    int trials = 0;

    public void celebChosen(View view) {
        MediaPlayer mplayer;
        if (view.getTag().toString().equals(Integer.toString(locationAC))) {
            //mplayer.reset();
            mplayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
            mplayer.start();
            score++;
            //Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            result.setText("Correct");
        } else {
            //mplayer.reset();
            mplayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
            mplayer.start();
            //Toast.makeText(getApplicationContext(), "Wrong! It was + " + MainActivity.celebNames.get(chosenCeleb) + "!", Toast.LENGTH_SHORT).show();
            result.setText("Wrong! It was " + MainActivity.celebNames.get(chosenCeleb) + "!");
        }
        trials++;
        points.setText(Integer.toString(score) + "/" + Integer.toString(trials));
        newQuestion();
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(in);

                return bitmap;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageView = (ImageView) findViewById(R.id.imageView);
        timer = (TextView) findViewById(R.id.timer);
        points = (TextView) findViewById(R.id.score);
        result = (TextView) findViewById(R.id.result);
        button0 = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        timer.setText("30s");
        points.setText("0/0");
        result.setText("");

        new CountDownTimer(31000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(String.valueOf(millisUntilFinished / 1000) + "s");

            }

            @Override
            public void onFinish() {

                timer.setText("0s");

                Intent intent = new Intent(getApplicationContext(), OverActivity.class);

                intent.putExtra("performance", Integer.toString(score) + "/" + Integer.toString(trials));

                intent.putExtra("score", score);

                startActivity(intent);

            }
        }.start();

        newQuestion();

    }

    public void newQuestion() {

        Random random = new Random();

        chosenCeleb = random.nextInt(MainActivity.celebURLs.size());

        ImageDownloader imageTask = new ImageDownloader();

        Bitmap celebImage;

        try {

            celebImage = imageTask.execute(MainActivity.celebURLs.get(chosenCeleb)).get();

            imageView.setImageBitmap(celebImage);

            locationAC = random.nextInt(4);
            int locationWA;
            int[] mark = new int[MainActivity.celebNames.size()];
            mark[chosenCeleb] = 1;

            for (int i = 0; i < 4; i++) {
                if (i == locationAC) {
                    answers[i] = MainActivity.celebNames.get(chosenCeleb);
                } else {
                    locationWA = random.nextInt(celebURLs.size());

                    while (locationWA == chosenCeleb && mark[locationWA] != 0) {
                        locationWA = random.nextInt(celebURLs.size());
                    }

                    mark[locationWA] = 1;

                    answers[i] = MainActivity.celebNames.get(locationWA);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}