package com.example.roman.popularmoviesapp.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roman.popularmoviesapp.data.MoviesRepository;
import com.example.roman.popularmoviesapp.ui.detail.DetailActivityViewModel;
import com.example.roman.popularmoviesapp.ui.main.MainActivityViewModel;

import javax.inject.Inject;

/**
 * Allows to create a ViewModel using a constructor that takes a {@link MoviesRepository}
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesRepository mRepository;
    private final Application mApplication;

    @Inject
    public ViewModelFactory(MoviesRepository repository, Application application) {
        this.mRepository = repository;
        this.mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(mRepository, mApplication);
        } else if (modelClass.isAssignableFrom(DetailActivityViewModel.class)) {
            return (T) new DetailActivityViewModel(mRepository, mApplication);
        }

        throw new IllegalArgumentException(
                modelClass.getName() + " ViewModel is not supported.");

    }


}
