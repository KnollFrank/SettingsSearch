package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.Summaries4MatchingEntriesAdapter.addEntries2SummariesOfPreferencesIfQueryMatchesAnyEntry;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;
    private final Map<Preference, Optional<CharSequence>> summaryByPreference;
    private final Context context;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final PreferenceScreen preferenceScreen,
                            final Map<Preference, Optional<CharSequence>> summaryByPreference,
                            final Context context) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
        this.summaryByPreference = summaryByPreference;
        this.context = context;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        new PreferenceScreenResetter(summaryByPreference).reset(preferenceScreen);
        addEntries2SummariesOfPreferencesIfQueryMatchesAnyEntry(preferenceScreen, query);
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        PreferenceMatchesHighlighter.highlight(preferenceMatches, context);
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    private static List<Preference> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .collect(Collectors.toList());
    }
}
