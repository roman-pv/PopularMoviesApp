package com.example.roman.popularmoviesapp.ui.detail;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.roman.popularmoviesapp.BuildConfig;
import com.example.roman.popularmoviesapp.data.MoviesRepository;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.data.model.Review;
import com.example.roman.popularmoviesapp.data.model.Video;
import com.example.roman.popularmoviesapp.data.network.MovieDatabaseApi;
import com.example.roman.popularmoviesapp.data.network.RetrievedList;

import javax.inject.Inject;

/**
 * Mediates between DetailActivity and Movies Repository,
 * fetching necessary data for the DetailActivity to display
 */

public class DetailActivityViewModel extends ViewModel {
    private MoviesRepository mRepository;
    private Application mApplication;

    @Inject
    public DetailActivityViewModel(MoviesRepository moviesRepository, Application application) {
        this.mRepository = moviesRepository;
        this.mApplication = application;
    }

    public LiveData<RetrievedList<Review>> getReviews(int movieId) {
        return mRepository.getReviewsForMovie(
                movieId, BuildConfig.API_KEY, MovieDatabaseApi.PAGE);
    }

    public LiveData<RetrievedList<Video>> getVideos(int movieId) {
        return mRepository.getVideosForMovie(
                movieId, BuildConfig.API_KEY, MovieDatabaseApi.PAGE);
    }

    public boolean isFavorite(int movieId) {
        return mRepository.isFavorite(movieId, mApplication.getContentResolver());
    }

    public void changeFavoriteStatus(Movie movie) {
        mRepository.changeFavoriteStatus(movie, mApplication.getContentResolver());
    }



}
