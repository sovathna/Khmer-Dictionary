package com.indiev.chuonnathkhmerdictionary.sqlitehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sovathna on 12/7/14.
 */
public class MyDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "khtokh";
    private static final int DB_VERSION = 2;


    public MyDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public ArrayList<String> getWords(String table) {
        ArrayList<String> words = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT word FROM " + table, null);

        if (cursor.moveToFirst()) {
            do {
                words.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public long insert(String table, String word, String def) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("definition", def);
        return db.insert(table, null, values);
    }

    public String getDeftByWord(String word) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT definition FROM dict WHERE word = '" + word + "'", null);
        cursor.moveToFirst();
        String def = cursor.getString(0);
        cursor.close();

        return def;
    }

    public String getWordOfTheDay() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT word, definition FROM dict WHERE _id = " + genId(), null);
        cursor.moveToFirst();
        String res = cursor.getString(0) + "\n" + cursor.getString(1);
        cursor.close();
        return res;
    }

    private int genId() {
        Random random = new Random();
        return random.nextInt(17329) + 1;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlHistory = "CREATE TABLE IF NOT EXISTS history(id integer not null primary key, word varchar(100),definition text);";

        db.execSQL(sqlHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);

    }
}
