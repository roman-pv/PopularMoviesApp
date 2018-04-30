package com.example.roman.popularmoviesapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * POJO that contains info about reviews.
 */

public class Review {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }


    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }


}
