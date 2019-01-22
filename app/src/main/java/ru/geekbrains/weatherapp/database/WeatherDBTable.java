package ru.geekbrains.weatherapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDBTable {
    private static WeatherDBTable instance = null;
    private final String TABLE_NAME = "sites"; // Название таблицы в БД

    // Названия столбцов
    private static final String COLUMN_ID = "_id"; //integer
    private static final String COLUMN_SITE = "site"; //text
    private static final String COLUMN_ACTUAL_ID = "actual_id"; //integer
    private static final String COLUMN_TEMP = "temperature"; //real
    private static final String COLUMN_PRESSURE = "pressure"; //integer
    private static final String COLUMN_HUMIDITY = "humidity"; //integer

    private String[] siteAllColumn = {
            COLUMN_ID, COLUMN_SITE, COLUMN_ACTUAL_ID, COLUMN_TEMP, COLUMN_PRESSURE, COLUMN_HUMIDITY
    };

    private WeatherDBTable() {}

    public static WeatherDBTable getInstance() {
        if(instance == null) {
            instance = new WeatherDBTable();
        }

        return instance;
    }

    void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SITE + " TEXT, "
                + COLUMN_ACTUAL_ID + " INTEGER, "
                + COLUMN_TEMP + " REAL, "
                + COLUMN_PRESSURE + " INTEGER, "
                + COLUMN_HUMIDITY + " INTEGER);");
    }

    void updateTable(SQLiteDatabase db) {
        String upgradeQuery = "ALTER TABLE " + TABLE_NAME
                + " ADD COLUMN " + COLUMN_SITE + " TEXT DEFAULT 'Site'";
        db.execSQL(upgradeQuery);
    }

    // Find site
    public SiteModel findSite(SQLiteDatabase db, String site) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{"*"}, COLUMN_SITE + " = \"" + site + "\"", null, null, null, null);
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        SiteModel siteModel = readDataFromCursor(cursor);
        cursor.close();

        return siteModel;
    }

    // Добавить новую запись
    public void addSite(SQLiteDatabase db, String site, int actual_id, float temp, int pressure, int humidity) {
        SiteModel siteModel = findSite(db, site);
        if (siteModel != null) {
            editSite(db, siteModel, site, actual_id, temp, pressure, humidity);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_SITE, site);
        values.put(COLUMN_ACTUAL_ID, actual_id);
        values.put(COLUMN_TEMP, temp);
        values.put(COLUMN_PRESSURE, pressure);
        values.put(COLUMN_HUMIDITY, humidity);
        // Добавление записи
        db.insert(TABLE_NAME, null, values);
        //Или пишем просто сырой апрос
        //long insertIdRawQuery = db.execSQL("INSERT INTO " + TABLE_NAME + " " + (какие поля)" VALUES(значение1, значение 2...)");
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Изменить запись
    public void editSite(SQLiteDatabase db, SiteModel siteModel, String site, int actual_id, float temp, int pressure, int humidity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, siteModel.id);
        values.put(COLUMN_SITE, site);
        values.put(COLUMN_ACTUAL_ID, actual_id);
        values.put(COLUMN_TEMP, temp);
        values.put(COLUMN_PRESSURE, pressure);
        values.put(COLUMN_HUMIDITY, humidity);
        // Изменение записи
        db.update(TABLE_NAME, values, COLUMN_ID + "=" + siteModel.id, null);
        /*db.execSQL("UPDATE notes SET id = 5 AND note = 'note description' AND title = 'New Title'" +
                " WHERE _id = 2");*/
    }

    // Удалить запись
    public void deleteSite(SQLiteDatabase db, SiteModel site) {
        long id = site.id;
        db.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
    }

    // Очистить таблицу
    public void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

    private SiteModel readDataFromCursor(Cursor cursor) {
        final int idIdx = cursor.getColumnIndex(COLUMN_ID);
        final int siteIdx = cursor.getColumnIndex(COLUMN_SITE);
        final int actual_idIdx = cursor.getColumnIndex(COLUMN_ACTUAL_ID);
        final int tempIdx = cursor.getColumnIndex(COLUMN_TEMP);
        final int pressureIdx = cursor.getColumnIndex(COLUMN_PRESSURE);
        final int humidityIdx = cursor.getColumnIndex(COLUMN_HUMIDITY);

        SiteModel siteModel = new SiteModel();

        if (idIdx != -1) siteModel.id = cursor.getLong(idIdx);
        if (siteIdx != -1) siteModel.site = cursor.getString(siteIdx);
        if (actual_idIdx != -1) siteModel.actual_id = cursor.getInt(actual_idIdx);
        if (tempIdx != -1) siteModel.temp = cursor.getFloat(tempIdx);
        if (pressureIdx != -1) siteModel.pressure = cursor.getInt(pressureIdx);
        if (humidityIdx != -1) siteModel.humidity = cursor.getInt(humidityIdx);

        return siteModel;
    }
}
