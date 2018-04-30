package com.example.roman.popularmoviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.roman.popularmoviesapp.R;

import javax.inject.Inject;

/**
 * Abstracts getting and setting of shared preferences
 */

public class MoviesSharedPreferences {

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Inject
    public MoviesSharedPreferences(SharedPreferences sharedPreferences, Context context) {
        this.mSharedPreferences = sharedPreferences;
        this.mContext = context;
    }

    public String getCategoryToLoadPreference() {
        return mSharedPreferences.getString(mContext.getString(R.string.pref_switch_categories_key),
                mContext.getString(R.string.pref_category_popular));
    }

    public void setCategoryToLoadPreference(String category) {
        mSharedPreferences.edit()
                .putString(mContext.getString(R.string.pref_switch_categories_key), category)
                .apply();
    }


}
