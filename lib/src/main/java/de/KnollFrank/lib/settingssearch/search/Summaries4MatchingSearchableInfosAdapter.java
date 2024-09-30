package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.ListPreferenceEntryMatcher.matches;

import androidx.preference.PreferenceScreen;

import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoSetter;

// FK-TODO: rename class
class Summaries4MatchingSearchableInfosAdapter {

    public static void showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final SearchableInfoSetter searchableInfoSetter,
            final String query) {
        Summaries4MatchingSearchableInfosAdapter
                .getSearchablePreferences(preferenceScreen)
                .forEach(
                        searchablePreference ->
                                searchablePreference
                                        .getSearchableInfo()
                                        .ifPresent(
                                                searchableInfo -> {
                                                    if (matches(searchableInfo, query)) {
                                                        searchableInfoSetter.setSearchableInfo(searchablePreference, searchableInfo);
                                                    }
                                                }));
    }

    private static Stream<SearchablePreference> getSearchablePreferences(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getAllPreferences(preferenceScreen)
                .stream()
                .filter(SearchablePreference.class::isInstance)
                .map(SearchablePreference.class::cast);
    }
}
