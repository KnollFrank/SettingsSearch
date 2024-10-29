package de.KnollFrank.lib.settingssearch;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

public record MergedPreferenceScreen(
        // FK-TODO: move allPreferencesForSearch to SearchResultsPreferenceScreenHelper and remove MergedPreferenceScreen?
        Set<SearchablePreferencePOJO> allPreferencesForSearch,
        SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper) {
}
