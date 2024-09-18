package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.ListPreferenceEntryMatcher.matches;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProvider;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoSetter;

class Summaries4MatchingSearchableInfosAdapter {

    public static void showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final SearchableInfoProvider searchableInfoProvider,
            final SearchableInfoSetter searchableInfoSetter,
            final String query) {
        for (final Preference preference : Preferences.getAllPreferences(preferenceScreen)) {
            searchableInfoProvider
                    .getSearchableInfo(preference)
                    .ifPresent(searchableInfo -> {
                        if (matches(searchableInfo, query)) {
                            searchableInfoSetter.setSearchableInfo(preference, searchableInfo);
                        }
                    });
        }
    }
}
