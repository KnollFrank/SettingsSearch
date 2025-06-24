package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;

class ChildrenByPreferenceProvider {

    // FK-TODO: refactor
    public static Map<SearchablePreference, Set<SearchablePreference>> getChildrenByPreference(
            final Map<SearchablePreference, Optional<SearchablePreference>> parentPreferenceByPreference) {
        final Map<SearchablePreference, Set<SearchablePreference>> childrenByPreference = new HashMap<>();
        for (final SearchablePreference preference : parentPreferenceByPreference.keySet()) {
            childrenByPreference.put(preference, new HashSet<>());
        }
        Maps
                .filterPresentValues(parentPreferenceByPreference)
                .forEach((preference, parentPreference) -> childrenByPreference.get(parentPreference).add(preference));
        return childrenByPreference;
    }
}
