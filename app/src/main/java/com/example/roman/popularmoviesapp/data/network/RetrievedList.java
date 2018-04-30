package com.example.roman.popularmoviesapp.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a list of POJOs retrieved by Gson.
 */

public class RetrievedList<T> {
    @SerializedName("results")
    List<T> retrievedList;

    public List<T> getRetrievedList() {
        return retrievedList;
    }

    public void setRetrievedList(List<T> retrievedList) {
        this.retrievedList = retrievedList;
    }

}