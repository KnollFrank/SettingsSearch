package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreen;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    public final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferencePathNavigator preferencePathNavigator;
    public final SearchResultsPreferenceScreen searchResultsPreferenceScreen;
    public final Set<SearchablePreferencePOJO> allPreferencesForSearch;

    public MergedPreferenceScreen(final PreferenceScreen searchablePreferenceScreen,
                                  final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute,
                                  final PreferencePathNavigator preferencePathNavigator,
                                  final Set<SearchablePreferencePOJO> allPreferencesForSearch) {
        this.preferencePathByPreference = preferencePathByPreference;
        this.preferencePathNavigator = preferencePathNavigator;
        this.allPreferencesForSearch = allPreferencesForSearch;
        this.searchResultsPreferenceScreen =
                new SearchResultsPreferenceScreen(
                        searchablePreferenceScreen,
                        searchableInfoAttribute,
                        pojoEntityMap);
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(preferencePathByPreference.get(preference));
    }
}
