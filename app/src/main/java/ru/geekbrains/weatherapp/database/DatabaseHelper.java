package ru.geekbrains.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Класс установки базы данных: создать базу данных, если ее нет; проапгрейдить ее
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // Название БД
    private static final int DATABASE_VERSION = 2; // Версия базы данных

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Вызывается при попытке доступа к базе данных, когда она еще не создана
    @Override
    public void onCreate(SQLiteDatabase db) {
        WeatherDBTable.getInstance().createTable(db);
    }

    // Вызывается, когда необходимо обновление базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        if ((oldVersion == 1) && (newVersion == 2)) {
            WeatherDBTable.getInstance().updateTable(db);
        }
    }
}

