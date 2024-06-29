package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.ListPreferenceEntryMatcher.matches;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getOptionalSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setSummary;

import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class Summaries4MatchingSearchableInfosAdapter {

    public static void addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
            final PreferenceScreen preferenceScreen,
            final String query) {
        for (final Preference preference : Preferences.getAllPreferences(preferenceScreen)) {
            getSearchableInfo(preference).ifPresent(searchableInfo -> {
                if (matches(searchableInfo, query)) {
                    setSummary(preference, getSummaryWithSearchableInfo(preference, searchableInfo));
                }
            });
        }
    }

    private static Optional<String> getSearchableInfo(final Preference preference) {
        return Summaries4MatchingSearchableInfosAdapter
                .getEntries(preference)
                .map(Summaries4MatchingSearchableInfosAdapter::enumerate);
    }

    private static Optional<CharSequence[]> getEntries(final Preference preference) {
        if (preference instanceof final ListPreference listPreference) {
            return Optional.ofNullable(listPreference.getEntries());
        }
        if (preference instanceof final MultiSelectListPreference multiSelectListPreference) {
            return Optional.ofNullable(multiSelectListPreference.getEntries());
        }
        return Optional.empty();
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }

    private static String getSummaryWithSearchableInfo(final Preference preference, final String searchableInfo) {
        return addSearchableInfo2Summary(getOptionalSummary(preference), searchableInfo);
    }

    private static String addSearchableInfo2Summary(final Optional<CharSequence> summary,
                                                    final String searchableInfo) {
        return summary
                .map(_summary -> String.format("%s\n%s", _summary, searchableInfo))
                .orElse(searchableInfo);
    }
}
