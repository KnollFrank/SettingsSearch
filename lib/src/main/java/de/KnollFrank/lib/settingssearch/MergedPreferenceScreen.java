package de.KnollFrank.lib.settingssearch;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        Set<SearchablePreferencePOJO> allPreferencesForSearch,
        SearchResultsDisplayer searchResultsDisplayer,
        PreferencePathNavigator preferencePathNavigator) {
}
