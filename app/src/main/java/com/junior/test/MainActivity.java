package com.junior.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (mp2 == null){
                mp2 = MediaPlayer.create(getApplicationContext(), R.raw.background_music);
                mp2.setLooping(true);
                mp2.start();
            }
            findViewById(R.id.start_new_game_btn).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), GameActivity.class)));
            ArrayList<String> list = new ArrayList<>();
            DateBase.sqLiteDatabase = new DateBase(getApplicationContext()).getWritableDatabase();

            Cursor fr = DateBase.sqLiteDatabase.rawQuery("SELECT * FROM 'scores' ORDER BY \"score\" DESC", null);
            while (fr.moveToNext()) {
                list.add(fr.getString(0) + " - " + fr.getString(1));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);

            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(adapter);
        }
        catch (Exception e){e.printStackTrace();}
    }
}