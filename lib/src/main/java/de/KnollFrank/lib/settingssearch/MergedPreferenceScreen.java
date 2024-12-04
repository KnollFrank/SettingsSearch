package de.KnollFrank.lib.settingssearch;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsDisplayer;

public record MergedPreferenceScreen(
        Set<SearchablePreferencePOJO> preferences,
        SearchResultsDisplayer searchResultsDisplayer,
        PreferencePathNavigator preferencePathNavigator,
        Map<SearchablePreferencePOJO, Class<? extends PreferenceFragmentCompat>> hostByPreference) {
}
