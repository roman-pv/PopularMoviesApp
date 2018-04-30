package com.example.roman.popularmoviesapp.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.roman.popularmoviesapp.data.database.FavoritesContract.FavoritesEntry;

/**
 * Handles creation of SQLite database
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 1;


    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CreateTable = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +

                FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " FLOAT(2), " +
                FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + " INTEGER, " +
                FavoritesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +

                "UNIQUE (" + FavoritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(CreateTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);

    }
}
