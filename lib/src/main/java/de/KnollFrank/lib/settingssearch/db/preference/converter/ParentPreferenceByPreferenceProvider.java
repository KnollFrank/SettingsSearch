package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

class ParentPreferenceByPreferenceProvider {

    public static Map<SearchablePreference, SearchablePreference> getParentPreferenceByPreference(final SearchablePreferenceScreen screen) {
            return Maps.merge(
                screen
                        .allPreferencesOfPreferenceHierarchy()
                        .stream()
                        .map(ParentPreferenceByPreferenceProvider::getParentPreferenceByPreference)
                        .collect(Collectors.toSet()));
    }

    private static Map<SearchablePreference, SearchablePreference> getParentPreferenceByPreference(final SearchablePreference searchablePreference) {
        return Maps.mapEachKeyToValue(
                searchablePreference.getChildren(),
                searchablePreference);
    }
}
