package de.KnollFrank.lib.preferencesearch.search;

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
        // FK-TODO: replace unhighlight() with a method which performs a RESET of preferenceScreen to its original state
        PreferenceScreenUnhighlighter.unhighlight(preferenceScreen, summaryByPreference);
        // FK-TODO: if preferenceScreen has a Preference with an entry match, adapt the summary of this Preference to contain a list of those entries (such that the entry matches become summary matches in preferenceSearcher.searchFor())
        final List<PreferenceMatch> preferenceMatches = preferenceSearcher.searchFor(query);
        PreferenceMatchesHighlighter.highlight(preferenceMatches, summaryByPreference, context);
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
