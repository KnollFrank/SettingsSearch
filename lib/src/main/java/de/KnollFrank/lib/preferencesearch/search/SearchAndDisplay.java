package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher, final PreferenceScreen preferenceScreen) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<Preference> preferences = preferenceSearcher.searchFor(query);
        // FK-TODO: den gefundenen Suchtext query in den Suchergebnissen farblich hervorheben. Siehe https://developer.android.com/develop/ui/views/text-and-emoji/spans
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(preferences, preferenceScreen);
    }
}
