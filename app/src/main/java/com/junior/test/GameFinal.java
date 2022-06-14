package com.junior.test;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class GameFinal extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_game_end);
        try {
            TextView score_tv = findViewById(R.id.score_tv);
            TextView max_score = findViewById(R.id.max_score);
            Cursor fr = DateBase.sqLiteDatabase.rawQuery("SELECT * FROM 'scores' ORDER BY \"score\" DESC", null);
            if (fr.moveToFirst()) {
                max_score.setText("Max score: " + fr.getString(1));
            }
            score_tv.setText(getString(R.string.score) + ": " + GameActivity.score);
            EditText editText = findViewById(R.id.player_name);
            findViewById(R.id.end_game).setOnClickListener(v -> {
                if (!editText.getText().toString().equals("")) {
                    ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                    rowValues.put("player_name", editText.getText().toString());
                    rowValues.put("score", GameActivity.score);
                    DateBase.sqLiteDatabase.insert("scores", null, rowValues);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    editText.setHintTextColor(getColor(R.color.error));
                }
            });
            findViewById(R.id.end_game2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), GameActivity.class));
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
