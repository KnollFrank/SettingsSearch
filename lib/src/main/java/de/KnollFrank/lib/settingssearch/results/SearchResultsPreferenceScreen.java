package de.KnollFrank.lib.settingssearch.results;

import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.converter.SearchablePreferenceFromPOJOConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter;
import de.KnollFrank.lib.settingssearch.search.PreferenceMatch;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class SearchResultsPreferenceScreen {

    private final PreferenceScreen preferenceScreen;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final PreferenceScreenResetter preferenceScreenResetter;

    public SearchResultsPreferenceScreen(final PreferenceScreen preferenceScreen,
                                         final SearchableInfoAttribute searchableInfoAttribute) {
        this.preferenceScreen = preferenceScreen;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.preferenceScreenResetter =
                new PreferenceScreenResetter(
                        preferenceScreen,
                        searchableInfoAttribute);
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

    public void setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(final String needle) {
        MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoAttribute,
                needle);
    }

    private static List<SearchablePreferencePOJO> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(PreferenceMatch::preference)
                .collect(Collectors.toList());
    }
}
