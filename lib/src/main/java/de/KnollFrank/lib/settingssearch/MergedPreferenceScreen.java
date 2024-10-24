package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.PreferencePOJOs;
import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreen;
import de.KnollFrank.lib.settingssearch.search.PreferenceScreenResetter;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    private final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap;
    public final Set<PreferenceCategory> nonClickablePreferences;
    public final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferencePathNavigator preferencePathNavigator;
    private final SearchResultsPreferenceScreen searchResultsPreferenceScreen;

    public MergedPreferenceScreen(final PreferenceScreen searchablePreferenceScreen,
                                  final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                  final Set<PreferenceCategory> nonClickablePreferences,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute,
                                  final PreferencePathNavigator preferencePathNavigator) {
        this.pojoEntityMap = pojoEntityMap;
        this.nonClickablePreferences = nonClickablePreferences;
        this.preferencePathByPreference = preferencePathByPreference;
        this.preferencePathNavigator = preferencePathNavigator;
        this.searchResultsPreferenceScreen =
                new SearchResultsPreferenceScreen(
                        searchablePreferenceScreen,
                        new PreferenceScreenResetter(searchablePreferenceScreen, searchableInfoAttribute));
    }

    public Map<SearchablePreferencePOJO, SearchablePreference> getPojoEntityMap() {
        return pojoEntityMap;
    }

    public List<SearchablePreferencePOJO> getAllPreferencesForSearch() {
        return PreferencePOJOs.getPreferencesRecursively(pojoEntityMap.keySet());
    }

    public SearchResultsPreferenceScreen getSearchResultsPreferenceScreen() {
        return searchResultsPreferenceScreen;
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(preferencePathByPreference.get(preference));
    }
}
