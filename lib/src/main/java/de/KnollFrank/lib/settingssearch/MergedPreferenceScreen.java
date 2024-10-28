package de.KnollFrank.lib.settingssearch;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferencePOJO;
import de.KnollFrank.lib.settingssearch.fragment.PreferencePathNavigator;
import de.KnollFrank.lib.settingssearch.results.SearchResultsPreferenceScreenHelper;

public class MergedPreferenceScreen {

    private final PreferencePathNavigator preferencePathNavigator;
    public final SearchResultsPreferenceScreenHelper searchResultsPreferenceScreenHelper;
    public final Set<SearchablePreferencePOJO> allPreferencesForSearch;

    public MergedPreferenceScreen(final PreferenceScreen searchablePreferenceScreen,
                                  final Map<SearchablePreferencePOJO, SearchablePreference> pojoEntityMap,
                                  final Map<Preference, PreferencePath> preferencePathByPreference,
                                  final PreferencePathNavigator preferencePathNavigator,
                                  final Set<SearchablePreferencePOJO> allPreferencesForSearch) {
        this.preferencePathNavigator = preferencePathNavigator;
        this.allPreferencesForSearch = allPreferencesForSearch;
        this.searchResultsPreferenceScreenHelper =
                new SearchResultsPreferenceScreenHelper(
                        searchablePreferenceScreen,
                        pojoEntityMap,
                        preferencePathByPreference);
    }

    public PreferenceFragmentCompat getHost(final Preference preference) {
        return preferencePathNavigator.navigatePreferencePath(searchResultsPreferenceScreenHelper.getPreferencePathByPreference().get(preference));
    }
}
