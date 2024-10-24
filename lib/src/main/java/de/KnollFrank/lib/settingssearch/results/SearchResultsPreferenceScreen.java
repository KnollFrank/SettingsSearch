package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;

public class SearchResultsPreferenceScreen {

    private final PreferenceScreen preferenceScreen;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public SearchResultsPreferenceScreen(final PreferenceScreen preferenceScreen,
                                         final PreferenceScreenResetter preferenceScreenResetter) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceScreenResetter = preferenceScreenResetter;
    }

    public PreferenceScreen getPreferenceScreen() {
        return preferenceScreen;
    }

    public void displayPreferenceMatchesOnPreferenceScreen(final List<PreferenceMatch> preferenceMatches) {
        preferenceScreen.removeAll();
        SearchablePreferenceFromPOJOConverter.addConvertedPOJOs2Parent(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    public void resetPreferenceScreen() {
        preferenceScreenResetter.reset();
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }
}
