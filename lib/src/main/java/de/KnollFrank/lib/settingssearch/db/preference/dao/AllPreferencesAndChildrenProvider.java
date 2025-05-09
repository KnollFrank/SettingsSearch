package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public interface AllPreferencesAndChildrenProvider {

    Map<SearchablePreferenceScreen, Set<SearchablePreference>> getAllPreferencesBySearchablePreferenceScreen();

    Map<SearchablePreferenceScreen, List<SearchablePreferenceScreen>> getChildrenBySearchablePreferenceScreen();
}
