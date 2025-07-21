package de.KnollFrank.lib.settingssearch.graph;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.client.searchDatabaseConfig.PreferenceFragmentIdProvider;
import de.KnollFrank.lib.settingssearch.common.Strings;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreens;

public class SearchablePreferenceScreenFinder {

    private final PreferenceFragmentIdProvider preferenceFragmentIdProvider;

    public SearchablePreferenceScreenFinder(final PreferenceFragmentIdProvider preferenceFragmentIdProvider) {
        this.preferenceFragmentIdProvider = preferenceFragmentIdProvider;
    }

    public SearchablePreferenceScreen find(final PreferenceFragmentCompat preferenceFragmentToFind,
                                           final Set<SearchablePreferenceScreen> screensToSearchIn,
                                           final Locale locale) {
        return SearchablePreferenceScreens
                .findSearchablePreferenceScreenById(
                        screensToSearchIn,
                        Strings.addLocaleToId(locale, preferenceFragmentIdProvider.getId(preferenceFragmentToFind)))
                .orElseThrow();
    }
}
