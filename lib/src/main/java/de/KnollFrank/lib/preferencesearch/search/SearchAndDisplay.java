package de.KnollFrank.lib.preferencesearch.search;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;
    private final Context context;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final PreferenceScreen preferenceScreen,
                            final Context context) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
        this.context = context;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        PreferenceMatchesHighlighter.highlight(preferenceMatches, preferenceScreen, context);
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
