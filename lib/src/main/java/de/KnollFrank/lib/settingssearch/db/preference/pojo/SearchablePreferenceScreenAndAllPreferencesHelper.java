package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;

public class SearchablePreferenceScreenAndAllPreferencesHelper {

    public static Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferencesBySearchablePreferenceScreen(final Set<SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy> searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchySet) {
        return searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchySet
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy::searchablePreferenceScreen,
                                SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy::allPreferencesOfPreferenceHierarchy));
    }

    public static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference(final Set<SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy> searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchySet) {
        return Maps.merge(
                searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchySet
                        .stream()
                        .map(SearchablePreferenceScreenAndAllPreferencesHelper::getHostByPreference)
                        .collect(Collectors.toSet()));
    }

    private static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> getHostByPreference(final SearchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy) {
        return searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy
                .allPreferencesOfPreferenceHierarchy()
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                searchablePreference -> searchablePreferenceScreenAndAllPreferencesOfPreferenceHierarchy.searchablePreferenceScreen()));
    }
}
