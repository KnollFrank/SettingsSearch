package de.KnollFrank.lib.settingssearch;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

public record MergedPreferenceScreen(Set<SearchablePreferencePOJO> allPreferencesForSearch,
                                     SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper) {
}
