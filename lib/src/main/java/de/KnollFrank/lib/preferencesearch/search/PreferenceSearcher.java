package de.KnollFrank.lib.preferencesearch.search;

import static de.KnollFrank.lib.preferencesearch.search.Summaries4MatchingSearchableInfosAdapter.addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo;

import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.preferencesearch.MergedPreferenceScreen;
import de.KnollFrank.lib.preferencesearch.common.Lists;
import de.KnollFrank.lib.preferencesearch.common.Preferences;
import de.KnollFrank.lib.preferencesearch.search.provider.ISearchableInfoProviderInternal;
import de.KnollFrank.lib.preferencesearch.search.provider.SummarySetter;

class PreferenceSearcher {

    private final MergedPreferenceScreen mergedPreferenceScreen;
    private final SummarySetter summarySetter;
    private final ISearchableInfoProviderInternal searchableInfoProviderInternal;

    public PreferenceSearcher(final MergedPreferenceScreen mergedPreferenceScreen,
                              final SummarySetter summarySetter,
                              final ISearchableInfoProviderInternal searchableInfoProviderInternal) {
        this.mergedPreferenceScreen = mergedPreferenceScreen;
        this.summarySetter = summarySetter;
        this.searchableInfoProviderInternal = searchableInfoProviderInternal;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private void prepareSearch(final String needle) {
        mergedPreferenceScreen.resetPreferenceScreen();
        addSearchableInfos2SummariesOfPreferencesIfQueryMatchesSearchableInfo(
                mergedPreferenceScreen.preferenceScreen,
                searchableInfoProviderInternal,
                summarySetter,
                needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                Preferences
                        .getAllPreferences(mergedPreferenceScreen.preferenceScreen)
                        .stream()
                        .filter(preference -> !(preference instanceof PreferenceGroup))
                        .map(preference -> PreferenceMatcher.getPreferenceMatches(preference, needle))
                        .collect(Collectors.toList()));
    }
}
