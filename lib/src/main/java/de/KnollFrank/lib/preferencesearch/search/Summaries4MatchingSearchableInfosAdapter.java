package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.ListPreferenceEntryMatcher.matches;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Map;

import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetter;

class Summaries4MatchingSearchableInfosAdapter {

    public static void addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final ISearchableInfoProviderInternal searchableInfoProviderInternal,
            final Map<Preference, ? extends SummarySetter> summarySetterByPreference,
            final String query) {
        for (final Preference preference : Preferences.getAllPreferences(preferenceScreen)) {
            searchableInfoProviderInternal
                    .getSearchableInfo(preference)
                    .ifPresent(searchableInfo -> {
                        if (matches(searchableInfo, query)) {
                            // FK-TODO: verwende keine Map, sondern einen BiConsumer<Preference, CharSequence>
                            summarySetterByPreference.get(preference).setSummary(getSummaryWithSearchableInfo(preference, searchableInfo));
                        }
                    });
        }
    }

    private static String getSummaryWithSearchableInfo(final Preference preference, final String searchableInfo) {
        return PreferenceAttributes
                .getOptionalSummary(preference)
                .map(summary -> String.format("%s\n%s", summary, searchableInfo))
                .orElse(searchableInfo);
    }
}
