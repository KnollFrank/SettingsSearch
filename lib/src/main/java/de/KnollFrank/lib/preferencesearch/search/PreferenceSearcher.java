package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;

class PreferenceSearcher {

    private final List<Preference> haystack;

    public PreferenceSearcher(final List<Preference> haystack) {
        this.haystack = haystack;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        return Lists.concat(
                this
                        .haystack
                        .stream()
                        .filter(preference -> !(preference instanceof PreferenceGroup))
                        .map(preference -> PreferenceMatcher.getPreferenceMatches(preference, needle))
                        .collect(Collectors.toList()));
    }
}
