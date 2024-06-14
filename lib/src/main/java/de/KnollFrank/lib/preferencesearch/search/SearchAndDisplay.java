package de.KnollFrank.lib.preferencesearch.search;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.PreferenceWithHost;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher, final PreferenceScreen preferenceScreen) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceWithHost> preferenceWithHostList = preferenceSearcher.searchFor(query);
        // FK-TODO: den gefundenen Suchtext query in den Suchergebnissen farblich hervorheben
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(
                getPreferences(preferenceWithHostList),
                preferenceScreen);
    }

    private static List<Preference> getPreferences(final List<PreferenceWithHost> preferenceWithHostList) {
        return preferenceWithHostList
                .stream()
                .map(preferenceWithHost -> preferenceWithHost.preference)
                .collect(Collectors.toList());
    }
}
