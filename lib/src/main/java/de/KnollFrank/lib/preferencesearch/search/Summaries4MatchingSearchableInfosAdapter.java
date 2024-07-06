package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.ListPreferenceEntryMatcher.matches;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SearchableInfoSetter;

class Summaries4MatchingSearchableInfosAdapter {

    public static void showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final ISearchableInfoProviderInternal searchableInfoProviderInternal,
            final SearchableInfoSetter searchableInfoSetter,
            final String query) {
        for (final Preference preference : Preferences.getAllPreferences(preferenceScreen)) {
            searchableInfoProviderInternal
                    .getSearchableInfo(preference)
                    .ifPresent(searchableInfo -> {
                        if (matches(searchableInfo, query)) {
                            searchableInfoSetter.setSearchableInfo(preference, searchableInfo);
                        }
                    });
        }
    }
}
