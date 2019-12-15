package com.example.bunkmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bunkmanager.CardContract.*;

import androidx.annotation.Nullable;

public class CardDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SubjectList.db";
    public static final int DATABASE_VERSION = 1;

    public CardDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SUBJECTLIST_TABLE = "CREATE TABLE " +
                CardEntry.TABLE_NAME + " (" +
                CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CardEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CardEntry.COLUMN_ATTEND + " INTEGER, " +
                CardEntry.COLUMN_BUNK + " INTEGER" +
                ");";

        db.execSQL(SQL_CREATE_SUBJECTLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CardEntry.TABLE_NAME + " WHERE ID='" + id + "'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public boolean updateData(String id, int attend, int bunk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CardEntry.COLUMN_ATTEND, attend);
        contentValues.put(CardEntry.COLUMN_BUNK, bunk);

        db.update(CardEntry.TABLE_NAME, contentValues, "_ID=?", new String[]{id});
        return true;
    }
}
