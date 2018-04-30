package com.example.roman.popularmoviesapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.roman.popularmoviesapp.BuildConfig;
import com.example.roman.popularmoviesapp.data.MoviesRepository;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.data.network.MovieDatabaseApi;
import com.example.roman.popularmoviesapp.data.network.MoviesList;
import com.example.roman.popularmoviesapp.data.network.RetrievedList;

import javax.inject.Inject;

/**
 * Mediates between MainActivity and Movies Repository,
 * fetching necessary data for the MainActivity to display
 */

public class MainActivityViewModel extends ViewModel {

    private MoviesRepository mRepository;
    private Application mApplication;

    @Inject
    public MainActivityViewModel(MoviesRepository moviesRepository, Application application) {
        this.mRepository = moviesRepository;
        this.mApplication = application;
    }

    public LiveData<RetrievedList<Movie>> getMoviesListWhenCategoryChanged() {
        return mRepository.getMoviesWhenCategoryChanged(BuildConfig.API_KEY, MovieDatabaseApi.PAGE,
                mApplication.getContentResolver());
    }

    public LiveData<RetrievedList<Movie>> getMoviesList() {
        return mRepository.getMoviesList(
                getCategoryToLoad(), BuildConfig.API_KEY, MovieDatabaseApi.PAGE,
               mApplication.getContentResolver());
    }


    public String getCategoryToLoad() {return mRepository.getCategoryToLoad().getValue(); }

    public void setCategoryToLoad(String category) {
        mRepository.setCategoryToLoad(category);
        mRepository.getCategoryToLoad();
    }

}
