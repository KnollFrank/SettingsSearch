package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;
import de.KnollFrank.lib.preferencesearch.matcher.PreferenceMatcher;

class PreferenceSearcher {

    private final List<PreferenceWithHost> haystack;

    public PreferenceSearcher(final List<PreferenceWithHost> haystack) {
        this.haystack = haystack;
    }

    public List<PreferenceWithHost> searchFor(final String keyword) {
        return this
                .haystack
                .stream()
                .filter(preferenceWithHost -> !(preferenceWithHost.preference instanceof PreferenceGroup))
                .filter(preferenceWithHost -> PreferenceMatcher.matches(preferenceWithHost.preference, keyword))
                .collect(Collectors.toList());
    }
}
