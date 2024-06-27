package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.getOptionalSummary;
import static de.KnollFrank.lib.preferencesearch.search.PreferenceAttributes.setSummary;

import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.Optional;

import de.KnollFrank.lib.preferencesearch.common.Preferences;

class Summaries4MatchingEntriesAdapter {

    public static void addEntries2SummariesOfPreferencesIfQueryMatchesAnyEntry(
            final PreferenceScreen preferenceScreen,
            final String query) {
        Preferences
                .getAllPreferences(preferenceScreen)
                .forEach(preference -> _addEntries2SummaryOfPreferenceIfQueryMatchesAnyEntry(preference, query));
    }

    private static void _addEntries2SummaryOfPreferenceIfQueryMatchesAnyEntry(final Preference preference, final String query) {
        Summaries4MatchingEntriesAdapter
                .getEntries(preference)
                .ifPresent(entries -> addEntries2SummaryOfPreferenceIfQueryMatchesAnyEntry(preference, query, entries));
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

    private static void addEntries2SummaryOfPreferenceIfQueryMatchesAnyEntry(final Preference preference,
                                                                             final String query,
                                                                             final CharSequence[] entries) {
        if (ListPreferenceEntryMatcher.matchesAnyEntry(entries, query)) {
            setSummary(preference, getSummaryWithEntries(preference, entries));
        }
    }

    private static String getSummaryWithEntries(final Preference preference, final CharSequence[] entries) {
        return addEntries2Summary(getOptionalSummary(preference), enumerate(entries));
    }

    private static String addEntries2Summary(final Optional<CharSequence> summary, final String entries) {
        return summary
                .map(_summary -> String.format("%s\n%s", _summary, entries))
                .orElse(entries);
    }

    private static String enumerate(final CharSequence[] entries) {
        return String.join(", ", entries);
    }
}
