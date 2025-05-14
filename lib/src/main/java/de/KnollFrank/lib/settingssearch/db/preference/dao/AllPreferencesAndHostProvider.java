package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public interface AllPreferencesAndHostProvider {

    // FK-TODO: change to "Set<SearchablePreference> getAllPreferences(SearchablePreferenceScreen screen);"
    Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen();

    // FK-TODO: change to SearchablePreferenceScreen getHost(SearchablePreference preference);
    Map<SearchablePreference, SearchablePreferenceScreen> getHostByPreference();
}
