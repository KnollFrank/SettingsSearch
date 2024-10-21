package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.PreferenceMatcher.matches;

import androidx.preference.PreferenceScreen;

import java.util.stream.Stream;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoSetter;

class MatchingSearchableInfosSetter {

    public static void setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final SearchableInfoSetter searchableInfoSetter,
            final String query) {
        MatchingSearchableInfosSetter
                .getSearchablePreferences(preferenceScreen)
                .forEach(searchablePreference -> setSearchableInfo(searchableInfoSetter, query, searchablePreference));
    }

    private static Stream<SearchablePreference> getSearchablePreferences(final PreferenceScreen preferenceScreen) {
        return Preferences
                .getPreferencesRecursively(preferenceScreen)
                .stream()
                .filter(SearchablePreference.class::isInstance)
                .map(SearchablePreference.class::cast);
    }

    private static void setSearchableInfo(final SearchableInfoSetter searchableInfoSetter,
                                          final String query,
                                          final SearchablePreference searchablePreference) {
        searchablePreference
                .getSearchableInfo()
                .ifPresent(
                        searchableInfo -> {
                            if (matches(searchableInfo, query)) {
                                searchableInfoSetter.setSearchableInfo(searchablePreference, searchableInfo);
                            }
                        });
    }
}
