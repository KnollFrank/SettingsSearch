package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.Summaries4MatchingSearchableInfosAdapter.addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo;

import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Preferences;

class PreferenceSearcher {

    private final PreferenceScreen preferenceScreen;
    private final PreferenceScreenResetter preferenceScreenResetter;
    private final SearchableInfoProvider searchableInfoProvider;

    public PreferenceSearcher(final PreferenceScreen preferenceScreen,
                              final PreferenceScreenResetter preferenceScreenResetter,
                              final SearchableInfoProvider searchableInfoProvider) {
        this.preferenceScreen = preferenceScreen;
        this.preferenceScreenResetter = preferenceScreenResetter;
        this.searchableInfoProvider = searchableInfoProvider;
    }

    public static PreferenceSearcher createPreferenceSearcher(
            final MergedPreferenceScreen mergedPreferenceScreen,
            final SearchableInfoProvider searchableInfoProvider) {
        return new PreferenceSearcher(
                mergedPreferenceScreen.preferenceScreen,
                new PreferenceScreenResetter(mergedPreferenceScreen.summaryByPreference),
                searchableInfoProvider);
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private void prepareSearch(final String needle) {
        preferenceScreenResetter.reset(preferenceScreen);
        addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
                preferenceScreen,
                searchableInfoProvider,
                needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                Preferences
                        .getAllPreferences(preferenceScreen)
                        .stream()
                        .filter(preference -> !(preference instanceof PreferenceGroup))
                        .map(preference -> PreferenceMatcher.getPreferenceMatches(preference, needle))
                        .collect(Collectors.toList()));
    }
}
