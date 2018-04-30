package com.example.roman.popularmoviesapp.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Provides access to the database and handles query methods.
 */

public class FavoritesProvider extends ContentProvider {

    public static final String LOG_TAG = FavoritesProvider.class.getSimpleName();

    public static final int CODE_FAVORITES_IN_BULK = 0;
    public static final int CODE_FAVORITES_BY_ID = 1;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoritesDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavoritesContract.PATH_MOVIE, CODE_FAVORITES_IN_BULK);

        uriMatcher.addURI(authority, FavoritesContract.PATH_MOVIE + "/#", CODE_FAVORITES_BY_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITES_BY_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};

                cursor = mDbHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FAVORITES_IN_BULK:
                cursor = mDbHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (sUriMatcher.match(uri) == CODE_FAVORITES_BY_ID) {

            db.beginTransaction();

            long id = db.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values);

            if (id == -1) {
                Log.e(LOG_TAG, "Failed to insert row for " + uri);
                db.endTransaction();
                return null;
            }

            getContext().getContentResolver().notifyChange(uri, null);

            db.setTransactionSuccessful();
            db.endTransaction();

            return ContentUris.withAppendedId(uri, id);

        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVORITES_BY_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};

                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments);
                break;

            case CODE_FAVORITES_IN_BULK:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
