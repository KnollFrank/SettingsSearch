package de.KnollFrank.lib.settingssearch.search;

import static de.KnollFrank.lib.settingssearch.search.Summaries4MatchingSearchableInfosAdapter.showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo;

import androidx.preference.PreferenceGroup;

import java.util.List;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.common.Lists;
import de.KnollFrank.lib.settingssearch.common.Preferences;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoProviderInternal;

class PreferenceSearcher {

    private final MergedPreferenceScreen mergedPreferenceScreen;
    private final SearchableInfoAttribute searchableInfoAttribute;
    private final SearchableInfoProviderInternal searchableInfoProviderInternal;

    public PreferenceSearcher(final MergedPreferenceScreen mergedPreferenceScreen,
                              final SearchableInfoAttribute searchableInfoAttribute,
                              final SearchableInfoProviderInternal searchableInfoProviderInternal) {
        this.mergedPreferenceScreen = mergedPreferenceScreen;
        this.searchableInfoAttribute = searchableInfoAttribute;
        this.searchableInfoProviderInternal = searchableInfoProviderInternal;
    }

    public List<PreferenceMatch> searchFor(final String needle) {
        prepareSearch(needle);
        return getPreferenceMatches(needle);
    }

    private void prepareSearch(final String needle) {
        mergedPreferenceScreen.resetPreferenceScreen();
        showSearchableInfosOfPreferencesIfQueryMatchesSearchableInfo(
                mergedPreferenceScreen.preferenceScreen,
                searchableInfoProviderInternal,
                searchableInfoAttribute,
                needle);
    }

    private List<PreferenceMatch> getPreferenceMatches(final String needle) {
        return Lists.concat(
                Preferences
                        .getAllPreferences(mergedPreferenceScreen.preferenceScreen)
                        .stream()
                        .filter(preference -> !(preference instanceof PreferenceGroup))
                        .map(preference ->
                                PreferenceMatcher.getPreferenceMatches(
                                        preference,
                                        needle,
                                        searchableInfoAttribute))
                        .collect(Collectors.toList()));
    }
}
