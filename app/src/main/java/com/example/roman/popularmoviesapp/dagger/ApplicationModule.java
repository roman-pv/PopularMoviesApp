package com.example.roman.popularmoviesapp.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of Application and ApplicationContext.
 */
@Module
public interface ApplicationModule {

    @Binds
    @Singleton
    Context provideContext(Application application);

}
