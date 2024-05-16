package com.bytehamster.lib.preferencesearch;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Searcher {

    private static final int MAX_RESULTS = 10;

    private final List<PreferenceItem> preferenceItems;

    public Searcher(List<PreferenceItem> preferenceItems) {
        this.preferenceItems = preferenceItems;
    }

    public List<PreferenceItem> searchFor(final String keyword, boolean fuzzy) {
        if (TextUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        final List<PreferenceItem> results = new ArrayList<>();

        for (final PreferenceItem preferenceItem : preferenceItems) {
            if ((fuzzy && preferenceItem.matchesFuzzy(keyword)) || (!fuzzy && preferenceItem.matches(keyword))) {
                results.add(preferenceItem);
            }
        }

        results.sort(Comparator.comparingDouble(preferenceItem -> preferenceItem.getScore(keyword)));
        if (results.size() > MAX_RESULTS) {
            return results.subList(0, MAX_RESULTS);
        } else {
            return results;
        }
    }
}
