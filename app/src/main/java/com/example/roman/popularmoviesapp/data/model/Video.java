package com.example.roman.popularmoviesapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * POJO that contains info about trailers.
 */

public class Video {

    private String id;
    @SerializedName("iso_639_1")
    private String language;
    @SerializedName("iso_3166_1")
    private String country;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Video(String id, String language, String country, String key, String name,
                 String site, int size, String type) {
        this.id = id;
        this.language = language;
        this.country = country;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

}
