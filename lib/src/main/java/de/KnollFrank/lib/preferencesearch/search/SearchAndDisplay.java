package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher, final PreferenceScreen preferenceScreen) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        PreferenceMatchesHighlighter.highlight(preferenceMatches, preferenceScreen);
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    private static List<Preference> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .collect(Collectors.toList());
    }
}
