package com.example.roman.popularmoviesapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.roman.popularmoviesapp.R;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.databinding.ActivityDetailBinding;
import com.example.roman.popularmoviesapp.ui.ViewModelFactory;
import com.example.roman.popularmoviesapp.ui.main.MainActivity;
import com.example.roman.popularmoviesapp.utilities.MovieUtilitites;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DetailActivity extends AppCompatActivity
        implements ReviewsAdapter.ReviewsAdapterOnItemClickHandler,
        TrailersAdapter.TrailersAdapterOnItemClickHandler {

    ActivityDetailBinding mBinding;

    private DetailActivityViewModel mViewModel;

    private ReviewsAdapter mReviewsAdapter;

    private TrailersAdapter mTrailersAdapter;

    private LinearLayoutManager mReviewsLayoutManager;

    private LinearLayoutManager mTrailersLayoutManager;

    private static final String REVIEWS_LAYOUT_STATE_KEY = "reviews_state";

    private static final String TRAILERS_LAYOUT_STATE_KEY = "trailers_state";

    private static final String NESTED_SCROLL_POSITION_KEY = "scroll_position";

    private Parcelable mReviewsLayoutState;

    private Parcelable mTrailersLayoutState;

    private int mNestedScrollPosition;

    @Inject
    Picasso mPicasso;

    @Inject
    ViewModelFactory mFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mViewModel = ViewModelProviders.of(this, mFactory).get(DetailActivityViewModel.class);

        // Sets Toolbar as an actionBar
        setActionBar(mBinding.toolbarDetail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Unpacks the movie information from the intent
        Movie movie = getIntent().getParcelableExtra(MainActivity.EXTRA_MOVIE_CLASS);

        // binds the movie information to the UI
        bindMovieToUi(movie);

        setupReviewsAdapter();
        setupTrailersAdapter();

        // Loads (and updates if necessary) reviews for this movie
        mViewModel.getReviews(movie.getId()).observe(this, reviews -> {
            mReviewsAdapter.swapReviews(reviews.getRetrievedList());
            // when there are some reviews, their subtitle becomes visible
            if (!reviews.getRetrievedList().isEmpty()) {
                mBinding.reviewsTitleTv.setVisibility(View.VISIBLE);
                if (mReviewsLayoutState != null) {
                    mReviewsLayoutManager.onRestoreInstanceState(mReviewsLayoutState);
                }
            }
            mBinding.nestedScrollView.post(() -> {
                mBinding.nestedScrollView.scrollTo(0, mNestedScrollPosition);
            });
        });


        // Loads (and updates if necessary) trailers for this movie
        mViewModel.getVideos(movie.getId()).observe(this, trailers -> {
            mTrailersAdapter.swapVideos(trailers.getRetrievedList());
            // restores horizontal scroll position of Trailers Recycler View
            if (mTrailersLayoutState != null) {
                mTrailersLayoutManager.onRestoreInstanceState(mTrailersLayoutState);
            }
            mBinding.nestedScrollView.post(() -> {
                mBinding.nestedScrollView.scrollTo(0, mNestedScrollPosition);
            });

        });

    }

    /**
     * Sets the movie information to the UI
     *
     * @param movie contains all the information that is to be set to UI.
     */

    private void bindMovieToUi(Movie movie) {

        String fullPosterPath = MovieUtilitites.getFullPosterPath(movie.getPosterPath());
        mPicasso.with(this)
                .load(fullPosterPath)
                .placeholder(R.drawable.ic_movies_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(mBinding.detailPoster);

        mBinding.titleTv.setText(movie.getOriginalTitle());
        mBinding.overviewTv.setText(movie.getOverview());
        String releaseDateString = MovieUtilitites.getReleaseDateString(movie.getReleaseDate());
        mBinding.releaseDateTv.setText(releaseDateString);
        mBinding.voteAverageTv.setText(Double.toString(movie.getVoteAverage()));

        GradientDrawable ratingCircle = (GradientDrawable) mBinding.voteAverageTv.getBackground();

        // Get the appropriate background color based on the rating
        int votesAverageColor = MovieUtilitites.getRatingColor(movie.getVoteAverage(), this);

        // Set the color for a votes average rating
        ratingCircle.setColor(votesAverageColor);

        setStarButtonColor(movie);

        // Allows to add or remove the movie from favorites
        mBinding.starButtonImageView.setOnClickListener((View view) -> {
            //Movie status is changed and it added or removed from the database of favorites
            mViewModel.changeFavoriteStatus(movie);
            //A toast message notifies the user
            showSnackbarWhenStarButtonIsClicked(movie);
            // The star button changes color
            setStarButtonColor(movie);
        });
    }


    /**
     * Notifies the user that the movie was added to/deleted from the favorites,
     * showing the corresponding toast message.
     */
    private void showSnackbarWhenStarButtonIsClicked(Movie movie) {
        String snackbarMessage;
        if (mViewModel.isFavorite(movie.getId())) {
            snackbarMessage =
                    getString(R.string.added_to_favorites_toast, movie.getOriginalTitle());
        } else {
            snackbarMessage =
                    getString(R.string.removed_from_favorites_toast, movie.getOriginalTitle());
        }
        Snackbar.make(mBinding.detailLayoutId, snackbarMessage, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Prepares and sets LinearLayoutManager and ReviewsAdapter
     */
    private void setupReviewsAdapter() {

        mReviewsAdapter = new ReviewsAdapter(this);

        mReviewsAdapter.setOnItemClickHandler(this);

        mReviewsLayoutManager = new LinearLayoutManager(this);

        mBinding.recyclerviewReviews.setLayoutManager(mReviewsLayoutManager);

        mBinding.recyclerviewReviews.setNestedScrollingEnabled(false);

        mBinding.recyclerviewReviews.setAdapter(mReviewsAdapter);
    }

    /**
     * Prepares and sets LinearLayoutManager and TrailersAdapter
     */
    private void setupTrailersAdapter() {

        mTrailersAdapter = new TrailersAdapter(this, mPicasso);

        mTrailersAdapter.setOnItemClickHandler(this);

        mTrailersLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        mBinding.recyclerviewTrailers.setLayoutManager(mTrailersLayoutManager);

        mBinding.recyclerviewTrailers.setAdapter(mTrailersAdapter);

    }

    @Override
    public void onReviewItemClick(String url) {

        Uri reviewUrl = Uri.parse(url);
        Intent openReviewIntent = new Intent(Intent.ACTION_VIEW);
        openReviewIntent.setData(reviewUrl);
        startActivity(openReviewIntent);
    }

    @Override
    public void onTrailerItemClick(String key) {

        Uri trailerUrl = Uri.parse(MovieUtilitites.getFullTrailerPath(key));

        Intent openTrailerIntent = new Intent(Intent.ACTION_VIEW);
        openTrailerIntent.setData(trailerUrl);
        startActivity(openTrailerIntent);
    }

    /**
     * Changes the color of the star (UI button for making a movie as a favorite),
     * depending on whether the movie is in favorites or not.
     */
    public void setStarButtonColor(Movie movie) {
        if (mViewModel.isFavorite(movie.getId())) {
            mBinding.starButtonImageView.setImageResource(R.drawable.ic_star_red_24dp);
        } else {
            mBinding.starButtonImageView.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mReviewsLayoutState = mReviewsLayoutManager.onSaveInstanceState();
        outState.putParcelable(REVIEWS_LAYOUT_STATE_KEY, mReviewsLayoutState);

        mTrailersLayoutState = mTrailersLayoutManager.onSaveInstanceState();
        outState.putParcelable(TRAILERS_LAYOUT_STATE_KEY, mTrailersLayoutState);

        outState.putInt(NESTED_SCROLL_POSITION_KEY, mBinding.nestedScrollView.getScrollY());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mReviewsLayoutState = savedInstanceState.getParcelable(REVIEWS_LAYOUT_STATE_KEY);
        mTrailersLayoutState = savedInstanceState.getParcelable(TRAILERS_LAYOUT_STATE_KEY);

        mNestedScrollPosition = savedInstanceState.getInt(NESTED_SCROLL_POSITION_KEY);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
