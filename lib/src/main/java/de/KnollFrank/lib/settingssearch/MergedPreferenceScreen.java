package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreen;
import de.KnollFrank.lib.settingssearch.search.provider.SearchableInfoAttribute;

public class MergedPreferenceScreen {

    public final Set<PreferenceCategory> nonClickablePreferences;
    public final Map<Preference, PreferencePath> preferencePathByPreference;
    private final PreferencePathNavigator preferencePathNavigator;
    private final SearchResultsPreferenceScreen searchResultsPreferenceScreen;
    public final List<SearchablePreferencePOJO> allPreferencesForSearch;

    public MergedPreferenceScreen(final PreferenceScreen searchablePreferenceScreen,
                                  final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                  final Set<PreferenceCategory> nonClickablePreferences,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final SearchableInfoAttribute searchableInfoAttribute,
                                  final PreferencePathNavigator preferencePathNavigator,
                                  final List<SearchablePreferencePOJO> allPreferencesForSearch) {
        this.nonClickablePreferences = nonClickablePreferences;
        this.preferencePathByPreference = preferencePathByPreference;
        this.preferencePathNavigator = preferencePathNavigator;
        this.allPreferencesForSearch = allPreferencesForSearch;
        this.searchResultsPreferenceScreen =
                new SearchResultsPreferenceScreen(
                        searchablePreferenceScreen,
                        searchableInfoAttribute,
                        pojoEntityMap);
    }

    public SearchResultsPreferenceScreen getSearchResultsPreferenceScreen() {
        return searchResultsPreferenceScreen;
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(preferencePathByPreference.get(preference));
    }
}
