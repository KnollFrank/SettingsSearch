package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceSearcher {

    private final List<Preference> haystack;

    public static PreferenceSearcher fromPreferenceScreen(final PreferenceScreen haystack) {
        return new PreferenceSearcher(Preferences.getAllPreferences(haystack));
    }

    private PreferenceSearcher(final List<Preference> haystack) {
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
