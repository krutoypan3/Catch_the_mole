package com.junior.test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    TextView timerTextView;
    TextView btn_score;
    public static int score = 0;
    public static boolean IS_PLAY = false;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60 * -1;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 1000);
            if (seconds <= 0){
                timerHandler.removeCallbacks(timerRunnable);
                IS_PLAY = false;
                startActivity(new Intent(getApplicationContext(), GameFinal.class));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game);


        timerTextView = findViewById(R.id.timer_tv);

        btn_score = findViewById(R.id.score_tv);
        btn_score.setText("START GAME");
        btn_score.setOnClickListener(v -> {
            score = 0;
            startTime = System.currentTimeMillis() + 30 * 1000;
            timerHandler.postDelayed(timerRunnable, 0);
            IS_PLAY = true;
            btn_score.setText(R.string.score);
            new GameLogic(GameActivity.this).start();
            btn_score.setClickable(false);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
