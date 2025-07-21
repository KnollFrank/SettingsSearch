package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Set;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;

public class SearchablePreferenceScreenFinder {

    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public SearchablePreferenceScreenFinder(final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public SearchablePreferenceScreen find(final PreferenceFragmentCompat preferenceFragmentToFind,
                                           final Set<SearchablePreferenceScreen> screensToSearchIn) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        screensToSearchIn,
                        preferenceFragmentIdProvider.getId(preferenceFragmentToFind))
                .orElseThrow();
    }
}
