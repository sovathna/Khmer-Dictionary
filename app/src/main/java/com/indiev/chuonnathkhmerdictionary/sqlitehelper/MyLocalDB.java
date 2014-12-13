package com.indiev.chuonnathkhmerdictionary.sqlitehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sovathna on 12/10/14.
 */
public class MyLocalDB extends SQLiteOpenHelper {

    private static final String sqlHistory = "CREATE TABLE IF NOT EXISTS history(id integer not null primary key, word varchar(100),definition text);";

    public MyLocalDB(Context context) {
        super(context, "localdb.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(sqlHistory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);

    }

    public long insert(String table, String word, String def) {
        SQLiteDatabase db = getWritableDatabase();
        if (isExist(table, word)) {
            db.execSQL("DELETE FROM history WHERE word='" + word + "'");
        }
        ContentValues values = new ContentValues();
        values.put("word", word);
        values.put("definition", def);
        return db.insert(table, null, values);
    }

    private boolean isExist(String table, String word) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE word = '" + word + "'", null);
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public ArrayList<String> getWords(String table) {
        ArrayList<String> words = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT word FROM " + table + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                words.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public String getDefByWord(String table, String word) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT definition FROM " + table + " WHERE word = '" + word + "'", null);
        cursor.moveToFirst();
        String def = cursor.getString(0);
        cursor.close();

        return def;
    }

    public boolean clear() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM history WHERE 1=1");
            return true;
        } catch (SQLiteException ex) {
            return false;
        }

    }
}
