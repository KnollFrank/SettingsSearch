package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public interface AllPreferencesAndHostProvider {

    Set<SearchablePreference> getAllPreferences(SearchablePreferenceScreen screen);

    // FK-TODO: change to SearchablePreferenceScreen getHost(SearchablePreference preference);
    Map<SearchablePreference, SearchablePreferenceScreen> getHostByPreference();
}
