package com.example.roman.popularmoviesapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import com.example.roman.popularmoviesapp.data.database.FavoritesContract.FavoritesEntry;
import com.example.roman.popularmoviesapp.data.database.FavoritesProvider;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.data.model.Review;
import com.example.roman.popularmoviesapp.data.model.Video;
import com.example.roman.popularmoviesapp.data.network.MovieDatabaseApi;
import com.example.roman.popularmoviesapp.data.network.RetrievedList;
import com.example.roman.popularmoviesapp.utilities.MovieUtilitites;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents a single point of access to the data,
 * providing access to the network requests, database and shared preferences
 */
@Singleton
public class MoviesRepository {

    private final MovieDatabaseApi mApi;
    private final MoviesSharedPreferences mMoviesPreferences;

    private MutableLiveData<String> mCategory = new MutableLiveData<>();

    @Inject
    public MoviesRepository(MovieDatabaseApi movieDatabaseApi,
                            MoviesSharedPreferences moviesPreferences) {
        this.mApi = movieDatabaseApi;
        this.mMoviesPreferences = moviesPreferences;
    }

    public LiveData<RetrievedList<Movie>> getMoviesList(String category, String apiKey, int page,
                                              ContentResolver contentResolver) {

        final MutableLiveData<RetrievedList<Movie>> movies = new MutableLiveData<>();

        if (category.equals("popular") || category.equals("top_rated")) {

            mApi.getMovieInformation(category, apiKey, page)
                    .enqueue(new Callback<RetrievedList<Movie>>() {
                        @Override
                        public void onResponse(Call<RetrievedList<Movie>> call,
                                               Response<RetrievedList<Movie>> response) {
                            movies.setValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<RetrievedList<Movie>> call, Throwable t) {
                            Log.d("DOWNLOAD", "" + t);
                        }
                    });
        } else if (category.equals("favorites")) {

            RetrievedList<Movie> moviesList = new RetrievedList<>();
            moviesList.setRetrievedList(getFavorites(contentResolver));
            movies.setValue(moviesList);
        }


        return movies;
    }


    public LiveData<RetrievedList<Movie>> getMoviesWhenCategoryChanged(String apiKey, int page,
                                                             ContentResolver contentResolver) {
        return Transformations.switchMap(mCategory, (category) -> {
            return getMoviesList(category,
                    apiKey, page, contentResolver);
        });
    }

    public LiveData<RetrievedList<Review>> getReviewsForMovie(int movieId, String apiKey, int page) {

        final MutableLiveData<RetrievedList<Review>> reviews = new MutableLiveData<>();

        mApi.getReviews(movieId, apiKey, page).enqueue(new Callback<RetrievedList<Review>>() {
            @Override
            public void onResponse(Call<RetrievedList<Review>> call,
                                   Response<RetrievedList<Review>> response) {
                reviews.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RetrievedList<Review>> call, Throwable t) {
                Log.d("DOWNLOAD", "" + t);
            }
        });

        return reviews;
    }

    public LiveData<RetrievedList<Video>> getVideosForMovie(int movieId, String apiKey, int page) {

        final MutableLiveData<RetrievedList<Video>> videos = new MutableLiveData<>();

        mApi.getVideos(movieId, apiKey, page).enqueue(new Callback<RetrievedList<Video>>() {
            @Override
            public void onResponse(Call<RetrievedList<Video>> call,
                                   Response<RetrievedList<Video>> response) {
                videos.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RetrievedList<Video>> call, Throwable t) {
                Log.d("DOWNLOAD", "" + t);
            }
        });

        return videos;
    }


    private List<Movie> getFavorites(ContentResolver contentResolver) {

        Cursor cursor = contentResolver.query(
                FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        List<Movie> favorites = new ArrayList<Movie>();

        if (null != cursor) {

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToPosition(i);

                int id = cursor.getInt(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_ID));
                String title = cursor.getString(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_TITLE));
                String posterPath = cursor.getString(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_POSTER_PATH));
                long dateInMls = cursor.getLong(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE));
                Date date = MovieUtilitites.LongtoDate(dateInMls);
                String overview = cursor.getString(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_OVERVIEW));
                double voteAverage = cursor.getDouble(
                        cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE));


                Movie movie = new Movie(
                        id,
                        title,
                        posterPath,
                        date,
                        overview,
                        voteAverage);

                favorites.add(movie);
            }
            cursor.close();
        }

        return favorites;
    }

    public boolean isFavorite(int id, ContentResolver contentResolver) {

        Uri uri = FavoritesEntry.buildMovieUriWithId(id);
        String[] projection = {FavoritesEntry.COLUMN_MOVIE_ID};

        Cursor cursor = contentResolver.query(
                uri,
                projection,
                null,
                null,
                null);

        boolean isFavorite = false;

        if (null != cursor) {
            if (cursor.getCount() != 0) {
                isFavorite = true;
            }
            cursor.close();
        }
        return isFavorite;
    }

    public void changeFavoriteStatus(Movie movie, ContentResolver contentResolver) {

        int id = movie.getId();
        Uri uri = FavoritesEntry.buildMovieUriWithId(id);

        int match = FavoritesProvider.buildUriMatcher().match(uri);
        Log.d("MATCH", "" + match);

        if (isFavorite(id, contentResolver)) {
            contentResolver.delete(uri, null, null);
        } else {

            ContentValues values = new ContentValues();

            values.put(FavoritesEntry.COLUMN_MOVIE_ID, id);
            values.put(FavoritesEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
            values.put(FavoritesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
            values.put(FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE,
                    MovieUtilitites.DateToLong(movie.getReleaseDate()));
            values.put(FavoritesEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
            values.put(FavoritesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());

            contentResolver.insert(uri, values);
        }
    }


    public LiveData<String> getCategoryToLoad() {
        mCategory.setValue(mMoviesPreferences.getCategoryToLoadPreference());
        return mCategory;
    }

    public void setCategoryToLoad(String category) {
        mMoviesPreferences.setCategoryToLoadPreference(category);
    }

}
