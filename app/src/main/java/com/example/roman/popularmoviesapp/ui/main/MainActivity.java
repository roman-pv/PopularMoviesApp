package com.example.roman.popularmoviesapp.ui.main;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.roman.popularmoviesapp.App;
import com.example.roman.popularmoviesapp.R;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.databinding.ActivityMainBinding;
import com.example.roman.popularmoviesapp.ui.ViewModelFactory;
import com.example.roman.popularmoviesapp.ui.detail.DetailActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnItemClickHandler {

    private MainActivityViewModel mViewModel;

    private ActivityMainBinding mBinding;

    public static final String EXTRA_MOVIE_CLASS =
            "com.example.roman.popularmoviesapp.extra.MOVIE_CLASS";

    private static final String GRID_STATE_KEY = "grid_state";

    private final int GRID_ROWS_PORTRAIT = 2;
    private final int GRID_ROWS_LANDSCAPE = 4;

    private GridLayoutManager mLayoutManager;

    private Parcelable mGridState;

    @Inject
    ViewModelFactory mFactory;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Sets Toolbar as an actionBar and shows there logo instead of a title
        setActionBar(mBinding.toolbar);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.drawable.ic_movies_black_24dp);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        // Retrieves an instance of ViewModel using a constructor from a Factory class
        mViewModel = ViewModelProviders.of(this, mFactory).get(MainActivityViewModel.class);

        // Sets up a spinner that allows to select a category of movies to load
        setupStateSpinner();

        // Sets up a RecyclerView with MoviesAdapter
        setupMoviesAdapter();

        // Loads and updates if necessary movies for this screen
        mViewModel.getMoviesList().observe(this, movies -> {
            mMoviesAdapter.swapMovies(movies.getRetrievedList());
            if (mGridState != null) {
                mLayoutManager.onRestoreInstanceState(mGridState);
            }
        });

        // Reloads movies when the user changes SharedPreference
        // that specifies types of movies to load
        mViewModel.getMoviesListWhenCategoryChanged().observe(this, movies -> {
            mMoviesAdapter.swapMovies(movies.getRetrievedList());
        });

    }

    /**
     * Launches an Intent to open a DetailActivity with an information about a particular movie
     * the poster to which the user clicks.
     *
     * @param movie Parcelable object containing movie info that will be displayed on DetailActivity
     */
    @Override
    public void onItemClick(Movie movie) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(EXTRA_MOVIE_CLASS, movie);
        ActivityOptions options = ActivityOptions.
                makeSceneTransitionAnimation(this, mBinding.recyclerviewPoster, "poster");
        startActivity(movieDetailIntent, options.toBundle());
    }

    /**
     * Prepares and sets GridLayoutManager and MoviesAdapter
     */
    private void setupMoviesAdapter() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, GRID_ROWS_PORTRAIT);
        } else {
            mLayoutManager = new GridLayoutManager(this, GRID_ROWS_LANDSCAPE);
        }

        mBinding.recyclerviewPoster.setLayoutManager(mLayoutManager);

        mMoviesAdapter.setOnItemClickHandler(this);

        mBinding.recyclerviewPoster.setAdapter(mMoviesAdapter);

    }

    private void setupStateSpinner() {

        ArrayAdapter stateSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.movies_categories, R.layout.toolbar_spinner_item);
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mBinding.moviesMenuSpinner.setAdapter(stateSpinnerAdapter);

        // Adds the listener for selecting the spinner items
        mBinding.moviesMenuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Load possible spinner options
                String[] spinnerOptions = getResources().getStringArray(R.array.movies_categories);
                // Check if popular movies was selected
                if (spinnerOptions[position].equals(getString(R.string.menu_show_popular))) {
                    String category = getString(R.string.pref_category_popular);
                    // Sets Popular Movies as a SharedPreference for the category to load
                    mViewModel.setCategoryToLoad(category);
                } else if (spinnerOptions[position].equals(getString(R.string.menu_show_top_rated))) {
                    String category = getString(R.string.pref_category_top_rated);
                    // Sets Top-Rated Movies as a SharedPreference for the category to load
                    mViewModel.setCategoryToLoad(category);
                } else {
                    String category = getString(R.string.pref_category_favorites);
                    // Sets Favorite Movies as a SharedPreference for the category to load
                    mViewModel.setCategoryToLoad(category);
                }

            }

            // Has to be overridden; does nothing.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(GRID_STATE_KEY, mGridState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGridState = savedInstanceState.getParcelable(GRID_STATE_KEY);
    }
}
