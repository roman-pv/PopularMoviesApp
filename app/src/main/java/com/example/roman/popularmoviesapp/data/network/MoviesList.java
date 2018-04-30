package com.example.roman.popularmoviesapp.data.network;

import com.example.roman.popularmoviesapp.data.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a list of Movie class objects, used by Gson
 */

public class MoviesList {
    @SerializedName("results")
    List<Movie> moviesList;

    public List<Movie> getMovies() {
        return moviesList;
    }

    public void setMovies(List<Movie> movies) {
        this.moviesList = movies;
    }

}
