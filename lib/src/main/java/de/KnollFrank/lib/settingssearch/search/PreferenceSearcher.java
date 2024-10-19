package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.MatchingSearchableInfosSetter.setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

class PreferenceSearcher {

    private final MergedPreferenceScreen mergedPreferenceScreen;
    private final SearchableInfoAttribute searchableInfoAttribute;

    public PreferenceSearcher(final MergedPreferenceScreen mergedPreferenceScreen,
                              final SearchableInfoAttribute searchableInfoAttribute) {
        this.mergedPreferenceScreen = mergedPreferenceScreen;
        this.searchableInfoAttribute = searchableInfoAttribute;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private void prepareSearch(final String needle) {
        mergedPreferenceScreen.resetPreferenceScreen();
        setSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                mergedPreferenceScreen.getSearchablePreferenceScreenForDisplay(),
                searchableInfoAttribute,
                needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                mergedPreferenceScreen
                        .getAllPreferencesForSearch()
                        .stream()
                        .map(searchablePreference -> PreferenceMatcher.getPreferenceMatches(searchablePreference, needle))
                        .collect(Collectors.toList()));
    }
}
