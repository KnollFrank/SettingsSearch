package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class ChildrenByPreferenceProvider {

    // FK-TODO: refactor
    public static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference(
            final Set<SearchablePreferenceEntity> preferences,
            final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId) {
        final Map<Integer, Set<Integer>> childrenByPreference = new HashMap<>();
        for (final SearchablePreferenceEntity preference : preferences) {
            childrenByPreference.put(preference.getId(), new HashSet<>());
        }
        Maps
                .filterPresentValues(parentPreferenceIdByPreferenceId)
                .forEach((preferenceId, parentPreferenceId) -> childrenByPreference.get(parentPreferenceId).add(preferenceId));
        final Map<Integer, SearchablePreferenceEntity> preferenceById =
                preferences
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        SearchablePreferenceEntity::getId,
                                        Function.identity()));
        return childrenByPreference
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                preferenceId_childPreferenceIds ->
                                        preferenceById.get(preferenceId_childPreferenceIds.getKey()),
                                preferenceId_childPreferenceIds ->
                                        preferenceId_childPreferenceIds
                                                .getValue()
                                                .stream()
                                                .map(preferenceById::get)
                                                .collect(Collectors.toSet())));
    }
}
