package de.KnollFrank.lib.preferencesearch.search;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.IPreferenceItem;

class PreferenceSearcher<T extends IPreferenceItem> {

    private final List<T> preferenceItems;

    public PreferenceSearcher(final List<T> preferenceItems) {
        this.preferenceItems = preferenceItems;
    }

    public List<T> searchFor(final String keyword) {
        return preferenceItems
                .stream()
                .filter(preferenceItem -> preferenceItem.matches(keyword))
                .collect(Collectors.toList());
    }
}
