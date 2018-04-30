package com.example.roman.popularmoviesapp.data.network;


import com.example.roman.popularmoviesapp.data.model.Movie;
import com.example.roman.popularmoviesapp.data.model.Review;
import com.example.roman.popularmoviesapp.data.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Provides the api requests to the MovieDatabase for the Retrofit
 */

public interface MovieDatabaseApi {

    String BASE_URL = "http://api.themoviedb.org";
    int PAGE = 1;

    @GET("/3/movie/{type}")
    Call<RetrievedList<Movie>> getMovieInformation(@Path("type") String typeMovie,
                                                  @Query("api_key") String keyApi,
                                                  @Query("page") int page);

    @GET("/3/movie/{id}/reviews")
    Call<RetrievedList<Review>> getReviews(@Path("id") int movieId,
                                           @Query("api_key") String keyApi,
                                           @Query("page") int page);

    @GET("/3/movie/{id}/videos")
    Call<RetrievedList<Video>> getVideos(@Path("id") int movieId,
                                         @Query("api_key") String keyApi,
                                         @Query("page") int page);
}
