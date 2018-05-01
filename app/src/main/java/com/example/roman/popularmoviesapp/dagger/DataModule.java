package com.example.roman.popularmoviesapp.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.roman.popularmoviesapp.data.MoviesRepository;
import com.example.roman.popularmoviesapp.data.MoviesSharedPreferences;
import com.example.roman.popularmoviesapp.data.network.MovieDatabaseApi;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides instances of all classes required to get and access data
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(MovieDatabaseApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    MovieDatabaseApi provideMovieDatabaseApi(Retrofit retrofit) {
        return retrofit.create(MovieDatabaseApi.class);
    }

    @Provides
    @Singleton
    MoviesRepository provideMoviesRepository(MovieDatabaseApi movieDatabaseApi,
                                             MoviesSharedPreferences preferencesManager) {
        return new MoviesRepository(movieDatabaseApi, preferencesManager);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Context context) {
        return new Picasso.Builder(context)
                .build();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
