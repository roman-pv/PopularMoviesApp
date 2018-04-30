package com.example.roman.popularmoviesapp.dagger;

import android.app.Application;

import com.example.roman.popularmoviesapp.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * Application component connecting modules for dependency injection.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        DataModule.class,
        ActivityBuilder.class})
public interface ApplicationComponent extends AndroidInjector<App>{

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }

    void inject(App app);

}
