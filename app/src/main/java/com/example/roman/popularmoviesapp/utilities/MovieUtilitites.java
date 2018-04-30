package com.example.roman.popularmoviesapp.utilities;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.roman.popularmoviesapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Helper functions.
 */

public class MovieUtilitites {

    private final static String BASE_URL_FOR_POSTER = "http://image.tmdb.org/t/p/";
    private final static String BASE_URL_FOR_TRAILER = "https://www.youtube.com/watch?v=";
    private final static String BASE_URL_FOR_TRAILER_THUMBNAIL = "https://img.youtube.com/vi/{key}/mqdefault.jpg";
    private final static String RESOLUTION_URL_FOR_POSTER = "w300";

    public static String getFullPosterPath(String posterPath) {
        return (BASE_URL_FOR_POSTER + RESOLUTION_URL_FOR_POSTER + posterPath);
    }

    public static String getFullTrailerPath(String trailerKey) {
        return BASE_URL_FOR_TRAILER + trailerKey;
    }

    public static String getTrailerThumbnailImagePath(String trailerKey) {
        return BASE_URL_FOR_TRAILER_THUMBNAIL.replace("{key}", trailerKey);
    }

    public static String getReleaseDateString(Date releaseDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        return dateFormat.format(releaseDate);
    }

    public static Long DateToLong(Date date) {
        return date.getTime();
    }

    public static Date LongtoDate(Long timeInMls) {
        return new Date(timeInMls);
    }

    public static int getRatingColor(double voteAverage, Context context) {
        int voteCategory = (int) voteAverage;
        switch (voteCategory) {
            case 0:
                return ContextCompat.getColor(context, R.color.magnitude0);
            case 1:
                return ContextCompat.getColor(context, R.color.magnitude1);
            case 2:
                return ContextCompat.getColor(context, R.color.magnitude2);
            case 3:
                return ContextCompat.getColor(context, R.color.magnitude3);
            case 4:
                return ContextCompat.getColor(context, R.color.magnitude4);
            case 5:
                return ContextCompat.getColor(context, R.color.magnitude5);
            case 6:
                return ContextCompat.getColor(context, R.color.magnitude6);
            case 7:
                return ContextCompat.getColor(context, R.color.magnitude7);
            case 8:
                return ContextCompat.getColor(context, R.color.magnitude8);
            case 9:
                return ContextCompat.getColor(context, R.color.magnitude9plus);
            default:
                return ContextCompat.getColor(context, R.color.magnitude9plus);
        }
    }

}
