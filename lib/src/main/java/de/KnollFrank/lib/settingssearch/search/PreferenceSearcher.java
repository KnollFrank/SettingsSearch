package de.KnollFrank.lib.settingssearch.search;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.Lists;

class PreferenceSearcher {

    private final MergedPreferenceScreen mergedPreferenceScreen;

    public PreferenceSearcher(final MergedPreferenceScreen mergedPreferenceScreen) {
        this.mergedPreferenceScreen = mergedPreferenceScreen;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        mergedPreferenceScreen.searchResultsPreferenceScreen.prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                mergedPreferenceScreen
                        .allPreferencesForSearch
                        .stream()
                        .map(searchablePreference -> PreferenceMatcher.getPreferenceMatches(searchablePreference, needle))
                        .collect(Collectors.toList()));
    }
}
