package com.example.roman.popularmoviesapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Contains all information about a movie that is needed for the app.
 */

public class Movie implements Parcelable {

    private int id;
    private String originalTitle;
    private String posterPath;
    private Date releaseDate;
    private String overview;
    private double voteAverage;

    public Movie(int id, String originalTitle, String posterPath, Date releaseDate, String overview,
                 double voteAverage) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
    }

    public Movie(Parcel in) {
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = new Date(in.readLong());
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {

        out.writeInt(id);
        out.writeString(originalTitle);
        out.writeString(posterPath);
        out.writeLong(releaseDate.getTime());
        out.writeString(overview);
        out.writeDouble(voteAverage);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

