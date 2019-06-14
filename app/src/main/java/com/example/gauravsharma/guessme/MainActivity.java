package com.example.gauravsharma.guessme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageButton play;
    static ArrayList<String> celebURLs = new ArrayList<>();
    static ArrayList<String> celebNames = new ArrayList<>();
    public static TextView bestScore;

    public void play(View view) {

        //MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.buzzer);
        //mplayer.start();

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);

        startActivity(intent);

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (ImageButton) findViewById(R.id.playButton);
        bestScore = (TextView) findViewById(R.id.bestScore);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.gauravsharma.guessme", Context.MODE_PRIVATE);

        int cur = sharedPreferences.getInt("best", -1);

        if (cur == -1) {
            sharedPreferences.edit().putInt("best", 0).apply();
        } else {
            bestScore.setText(String.valueOf(cur));
        }

        if (celebNames.isEmpty()) {

            DownloadTask task = new DownloadTask();
            String result = null;

            try {

                result = task.execute("http://www.posh24.se/kandisar").get();

                String[] splitResult = result.split("<div class=\"sidebarContainer\">");

                Pattern p = Pattern.compile("<img src=\"(.*?)\"");

                Matcher m = p.matcher(splitResult[0]);

                while (m.find()) {
                    celebURLs.add(m.group(1));
                }

                p = Pattern.compile("alt=\"(.*?)\"");

                m = p.matcher(splitResult[0]);

                while (m.find()) {
                    celebNames.add(m.group(1));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
