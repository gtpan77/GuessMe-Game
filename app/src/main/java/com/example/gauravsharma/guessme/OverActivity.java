package com.example.gauravsharma.guessme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OverActivity extends AppCompatActivity {

    public void playAgain(View view) {

        TextView newBestScore = (TextView) findViewById(R.id.newBestScore);
        ImageView star = (ImageView) findViewById(R.id.star);

        newBestScore.setVisibility(View.INVISIBLE);
        star.setVisibility(View.INVISIBLE);

        //MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.buzzer);
        //mplayer.start();

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);

        startActivity(intent);

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
        setContentView(R.layout.activity_over);

        ImageButton play = (ImageButton) findViewById(R.id.playAgainButton);
        TextView score = (TextView) findViewById(R.id.yourScore);
        TextView newBestScore = (TextView) findViewById(R.id.newBestScore);
        ImageView star = (ImageView) findViewById(R.id.star);

        Intent intent = getIntent();

        String string = intent.getStringExtra("performance");

        int current = intent.getIntExtra("score", -1);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.gauravsharma.guessme", Context.MODE_PRIVATE);

        int best = sharedPreferences.getInt("best", -1);

        if (current > best) {
            newBestScore.setVisibility(View.VISIBLE);
            star.setVisibility(View.VISIBLE);
            best = current;
        }

        MainActivity.bestScore.setText(String.valueOf(best));
        sharedPreferences.edit().putInt("best", best).apply();

        score.setText(string);

    }
}
