package com.junior.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameLogic extends Thread{
    Activity act;
    int[] img_ids = new int[]{
            R.id.img11, R.id.img12, R.id.img13,
            R.id.img21, R.id.img22, R.id.img23,
            R.id.img31, R.id.img32, R.id.img33};
    double start_speed = 1000;

    List<Integer> img_ids_contain_crot = new ArrayList<>();

    public GameLogic(Activity activity){
        act = activity;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void run(){
        try {
            ConstraintLayout update_layout = act.findViewById(R.id.layout_back);
            ImageView lopata_img = act.findViewById(R.id.lopata_img);
            update_layout.setOnTouchListener((v, event) -> { // При нажатии на фон
                lopata_img.setX(event.getX()-50); // Определяем позицию нажатия по X
                lopata_img.setY(event.getY()-50); // Определяем позицию нажатия по Y
                switch (event.getAction()){ // Получаем информацию о том, что делает пользователь
                    case MotionEvent.ACTION_DOWN: // Если опускает палец на экран
                        lopata_img.setVisibility(View.VISIBLE); // Делаем невидимую картинку видимой
                        MediaPlayer mp = MediaPlayer.create(act, R.raw.popal);
                        mp.start();
                        mp.setOnCompletionListener(MediaPlayer::release);
                        break;
                    case MotionEvent.ACTION_UP: // Если поднимает палец
                        lopata_img.setVisibility(View.INVISIBLE); // Скрываем плавающий логотип
                        lopata_img.setImageResource(R.drawable.lopata); // И возвращаем его (если была активирована фича с Леонардо)
                        break;
                }
                return true;
            });
            while (GameActivity.IS_PLAY) {
                new Thread(() -> {
                    int rand_ind = new Random().nextInt(9);
                    int id = img_ids[rand_ind];
                    ImageView imageView = act.findViewById(id);
                    img_ids_contain_crot.add(id);
                    act.runOnUiThread(() -> {
                        imageView.setImageResource(R.drawable.hole_true);
                        imageView.setOnTouchListener((v, event) -> { // При нажатии на фон
                            float x_pos = event.getRawX();
                            float y_pos = event.getRawY();
                            lopata_img.setX(x_pos-90); // Определяем позицию нажатия по X
                            lopata_img.setY(y_pos-110); // Определяем позицию нажатия по Y
                            switch (event.getAction()){ // Получаем информацию о том, что делает пользователь
                                case MotionEvent.ACTION_DOWN: // Если опускает палец на экран
                                    lopata_img.setVisibility(View.VISIBLE); // Делаем невидимую картинку видимой
                                    if (img_ids_contain_crot.contains(id)) {
                                        MediaPlayer mp = MediaPlayer.create(act, R.raw.popal);
                                        mp.start();
                                        lopata_img.setImageResource(R.drawable.lopata_2); // Ставим фото Леонардо
                                        GameActivity.score += 1;
                                        TextView btn_score = act.findViewById(R.id.score_tv);
                                        btn_score.setText(act.getString(R.string.score) + ": " + GameActivity.score);
                                        imageView.setImageResource(R.drawable.hole);
                                        img_ids_contain_crot.remove(Integer.valueOf(id));
                                        mp.setOnCompletionListener(MediaPlayer::release);
                                    }else{
                                        MediaPlayer mp2 = MediaPlayer.create(act, R.raw.ne_popal);
                                        mp2.start();
                                        mp2.setOnCompletionListener(MediaPlayer::release);
                                        lopata_img.setImageResource(R.drawable.lopata); // Ставим фото Леонардо
                                    }
                                    break;
                                case MotionEvent.ACTION_UP: // Если поднимает палец
                                    lopata_img.setVisibility(View.INVISIBLE); // Скрываем плавающий логотип
                                    lopata_img.setImageResource(R.drawable.lopata); // И возвращаем его (если была активирована фича с Леонардо)
                                    break;
                            }
                            return true;
                        });

                    });
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    act.runOnUiThread(() -> imageView.setImageResource(R.drawable.hole));
                    if (img_ids_contain_crot.contains(id))
                        img_ids_contain_crot.remove(Integer.valueOf(id));
                }).start();

                start_speed = start_speed*0.97; // Плавное экспоненциальное увеличение скорости появления кротов
                try {
                    TimeUnit.MILLISECONDS.sleep((long) start_speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
}
