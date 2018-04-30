package com.example.roman.popularmoviesapp.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Provides the information about SQLite database for Android
 */

public class FavoritesContract {

    static final String CONTENT_AUTHORITY = "com.example.roman.popularmoviesapp";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIE = "movie";


    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE).build();

        static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";

        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";

        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";

        public static Uri buildMovieUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }





    }


}
