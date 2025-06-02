package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class SearchablePreferenceScreenAndAllPreferencesHelper {

    public static Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen(final Set<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesSet) {
        return searchablePreferenceScreenAndAllPreferencesSet
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreenAndAllPreferences::searchablePreferenceScreen,
                                SearchablePreferenceScreenAndAllPreferences::allPreferences));
    }

    public static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference(final Set<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesSet) {
        return Maps.merge(
                searchablePreferenceScreenAndAllPreferencesSet
                        .stream()
                        .map(SearchablePreferenceScreenAndAllPreferencesHelper::getHostByPreference)
                        .collect(Collectors.toSet()));
    }

    private static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference(final SearchablePreferenceScreenAndAllPreferences searchablePreferenceScreenAndAllPreferences) {
        return searchablePreferenceScreenAndAllPreferences
                .allPreferences()
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                searchablePreference -> searchablePreferenceScreenAndAllPreferences.searchablePreferenceScreen()));
    }
}
