package com.example.roman.popularmoviesapp.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.roman.popularmoviesapp.R;
import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.databinding.MoviePosterRecyclerviewItemBinding;
import com.example.roman.popularmoviesapp.utilities.MovieUtilitites;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Responsible for demonstrating a grid of movies posters
 * and handling clicks on these posters
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final Context mContext;

    private MoviesAdapterOnItemClickHandler mClickHandler;

    private List<Movie> mMovies;

    private Picasso mPicasso;

    @Inject
    public MoviesAdapter(Context context, Picasso picasso) {
        this.mContext = context;
        this.mPicasso = picasso;
    }

    public void setOnItemClickHandler(MoviesAdapterOnItemClickHandler onItemClickHandler) {
        this.mClickHandler = onItemClickHandler;
    }


    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.movie_poster_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        return new MoviesAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesAdapterViewHolder holder, int position) {

        Movie currentMovie = mMovies.get(position);

        String fullPosterPath = MovieUtilitites.getFullPosterPath(currentMovie.getPosterPath());
        mPicasso.with(mContext)
                .load(fullPosterPath)
                .placeholder(R.drawable.ic_movies_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.binding.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        else return mMovies.size();
    }

    void swapMovies(List<Movie> newMovies) {
        mMovies = newMovies;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onReviewItemClick messages.
     */
    public interface MoviesAdapterOnItemClickHandler {
        void onItemClick(Movie movie);
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final MoviePosterRecyclerviewItemBinding binding;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onItemClick(movie);
        }
    }
}
