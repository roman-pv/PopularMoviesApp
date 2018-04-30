package com.example.roman.popularmoviesapp.dagger;

import com.example.roman.popularmoviesapp.ui.detail.DetailActivity;
import com.example.roman.popularmoviesapp.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector()
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector()
    abstract DetailActivity bindDetailActivity();
}
