package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.ListPreferenceEntryMatcher.matches;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProviderInternal;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoSetter;

class Summaries4MatchingSearchableInfosAdapter {

    public static void showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final SearchableInfoProviderInternal searchableInfoProviderInternal,
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
