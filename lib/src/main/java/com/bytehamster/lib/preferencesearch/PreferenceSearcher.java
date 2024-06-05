package com.bytehamster.lib.preferencesearch;

import java.util.List;
import java.util.stream.Collectors;

class PreferenceSearcher {

    private final List<PreferenceItem> preferenceItems;

    public PreferenceSearcher(final List<PreferenceItem> preferenceItems) {
        this.preferenceItems = preferenceItems;
    }

    public List<PreferenceItem> searchFor(final String keyword) {
        return preferenceItems
                .stream()
                .filter(preferenceItem -> preferenceItem.matches(keyword))
                .collect(Collectors.toList());
    }
}
