package com.example.roman.popularmoviesapp;

import android.app.Activity;
import android.app.Application;

import com.example.roman.popularmoviesapp.dagger.ApplicationComponent;
import com.example.roman.popularmoviesapp.dagger.ApplicationModule;
import com.example.roman.popularmoviesapp.dagger.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/**
 * Application handling creation of dependencies via Dagger 2 api.
 */

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

}
