package com.bytehamster.lib.preferencesearch;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.preference.PreferenceManager;

public class PreferenceParserFactory {

    @SuppressLint("RestrictedApi")
    public static PreferenceParser fromContext(final Context context) {
        return new PreferenceParser(new PreferenceManager(context));
    }
}
