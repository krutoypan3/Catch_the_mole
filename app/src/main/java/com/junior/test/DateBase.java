package com.junior.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DateBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "score.db";
    public static SQLiteDatabase sqLiteDatabase;
    /**
     * Класс отвечающий за первичное создание \ подключение к локальной базе данных
     * @param context Контекст приложения
     */
    public DateBase(Context context) {
        super(context, DATABASE_NAME, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createDB(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createDB(db);
    }

    private void createDB(SQLiteDatabase db){
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"scores\" (\n" +
                    "\t\"player_name\"\tTEXT,\n" +
                    "\t\"score\"\tINTEGER\n" +
                    ")");
        }
        catch (Exception ignored){}
    }
}
