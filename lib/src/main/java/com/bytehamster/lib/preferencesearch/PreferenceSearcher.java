package com.bytehamster.lib.preferencesearch;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

class PreferenceSearcher {

    private final List<PreferenceItem> preferenceItems;

    public PreferenceSearcher(final List<PreferenceItem> preferenceItems) {
        this.preferenceItems = preferenceItems;
    }

    // FK-TODO: refactor
    public List<PreferenceItem> searchFor(final String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        final List<PreferenceItem> results = new ArrayList<>();
        for (final PreferenceItem preferenceItem : preferenceItems) {
            if (preferenceItem.matches(keyword)) {
                results.add(preferenceItem);
            }
        }

        return results;
    }
}
