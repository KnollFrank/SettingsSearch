package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.Summaries4MatchingSearchableInfosAdapter.addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo;

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

class SearchAndDisplay {

    private final PreferenceSearcher preferenceSearcher;
    private final PreferenceScreen preferenceScreen;
    private final SearchableInfoProvider searchableInfoProvider;
    private final PreferenceScreenResetter preferenceScreenResetter;
    private final Context context;

    public SearchAndDisplay(final PreferenceSearcher preferenceSearcher,
                            final PreferenceScreen preferenceScreen,
                            final SearchableInfoProvider searchableInfoProvider,
                            final PreferenceScreenResetter preferenceScreenResetter,
                            final Context context) {
        this.preferenceSearcher = preferenceSearcher;
        this.preferenceScreen = preferenceScreen;
        this.searchableInfoProvider = searchableInfoProvider;
        this.preferenceScreenResetter = preferenceScreenResetter;
        this.context = context;
    }

    public void searchForQueryAndDisplayResults(final String query) {
        final List<PreferenceMatch> preferenceMatches = searchFor(query);
        PreferenceMatchesHighlighter.highlight(preferenceMatches, context);
        PreferenceVisibility.makePreferencesOfPreferenceScreenVisible(
                getPreferences(preferenceMatches),
                preferenceScreen);
    }

    // FK-TODO: let preferenceSearcher implement a new Interface IPreferenceSearcher and extract of the following method a new subclass which implements IPreferenceSearcher. Use this new subclass in PreferenceSearcherTest.
    private List<PreferenceMatch> searchFor(final String query) {
        preferenceScreenResetter.reset(preferenceScreen);
        addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoProvider,
                query);
        return preferenceSearcher.searchFor(query);
    }

    private static List<Preference> getPreferences(final List<PreferenceMatch> preferenceMatches) {
        return preferenceMatches
                .stream()
                .map(preferenceMatch -> preferenceMatch.preference)
                .collect(Collectors.toList());
    }
}
