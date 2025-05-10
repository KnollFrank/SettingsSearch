package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchablePreferenceScreenAndAllPreferencesHelper {

    public static Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen(final Set<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesSet) {
        return searchablePreferenceScreenAndAllPreferencesSet
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceScreenAndAllPreferences::searchablePreferenceScreen,
                                SearchablePreferenceScreenAndAllPreferences::allPreferences));
    }
}
