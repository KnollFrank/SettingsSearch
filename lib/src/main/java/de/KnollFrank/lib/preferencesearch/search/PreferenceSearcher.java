package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.matcher.PreferenceMatcher;

class PreferenceSearcher {

    private final List<Preference> haystack;

    public PreferenceSearcher(final List<Preference> haystack) {
        this.haystack = haystack;
    }

    public List<Preference> searchFor(final String keyword) {
        return this
                .haystack
                .stream()
                // FK-TODO: verschiebe den Test auf PreferenceGroup nach PreferenceMatcher.matches()?
                .filter(preference -> !(preference instanceof PreferenceGroup))
                .filter(preference -> PreferenceMatcher.matches(preference, keyword))
                .collect(Collectors.toList());
    }
}
