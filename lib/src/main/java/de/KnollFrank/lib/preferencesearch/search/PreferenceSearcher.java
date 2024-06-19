package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.search.matcher.PreferenceMatcher;

class PreferenceSearcher {

    private final List<Preference> haystack;

    public PreferenceSearcher(final List<Preference> haystack) {
        this.haystack = haystack;
    }

    public List<Preference> searchFor(final String keyword) {
        return this
                .haystack
                .stream()
                .filter(preference -> !(preference instanceof PreferenceGroup))
                .filter(preference -> !PreferenceMatcher.getMatches(preference, keyword).isEmpty())
                .collect(Collectors.toList());
    }
}
