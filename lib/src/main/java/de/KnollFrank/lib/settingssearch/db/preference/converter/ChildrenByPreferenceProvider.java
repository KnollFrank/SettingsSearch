package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class ChildrenByPreferenceProvider {

    // FK-TODO: refactor
    public static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference(
            final Set<SearchablePreferenceEntity> preferences,
            final Map<SearchablePreference, Optional<SearchablePreference>> parentPreferenceByPreference) {
        final Map<Integer, SearchablePreferenceEntity> preferenceById =
                preferences
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        SearchablePreferenceEntity::getId,
                                        Function.identity()));
        final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference = new HashMap<>();
        for (final SearchablePreferenceEntity preference : preferences) {
            childrenByPreference.put(preference, new HashSet<>());
        }
        Maps
                .filterPresentValues(parentPreferenceByPreference)
                .forEach((preference, parentPreference) -> childrenByPreference.get(preferenceById.get(parentPreference.getId())).add(preferenceById.get(preference.getId())));
        return childrenByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                preference_childPreferences ->
                                        preferenceById.get(preference_childPreferences.getKey().getId()),
                                preference_childPreferences ->
                                        preference_childPreferences
                                                .getValue()
                                                .stream()
                                                .map(childPreference -> preferenceById.get(childPreference.getId()))
                                                .collect(Collectors.toSet())));
    }
}
