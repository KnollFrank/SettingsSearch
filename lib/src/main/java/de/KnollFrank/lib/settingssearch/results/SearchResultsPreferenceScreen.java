package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;

public class SearchResultsPreferenceScreen {

    private final PreferenceScreen preferenceScreen;

    public SearchResultsPreferenceScreen(final PreferenceScreen preferenceScreen) {
        this.preferenceScreen = preferenceScreen;
    }

    public void displayPreferenceMatchesOnPreferenceScreen(final List<PreferenceMatch> preferenceMatches) {
        preferenceScreen.removeAll();
        SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }
}
