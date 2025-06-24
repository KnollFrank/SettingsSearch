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
        final Function<SearchablePreference, SearchablePreferenceEntity> getSearchablePreferenceEntity = getSearchablePreferenceToSearchablePreferenceEntity(preferences);
        final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference = new HashMap<>();
        for (final SearchablePreferenceEntity preference : preferences) {
            childrenByPreference.put(preference, new HashSet<>());
        }
        Maps
                .filterPresentValues(parentPreferenceByPreference)
                .forEach((preference, parentPreference) -> childrenByPreference.get(getSearchablePreferenceEntity.apply(parentPreference)).add(getSearchablePreferenceEntity.apply(preference)));
        return childrenByPreference;
    }

    private static Function<SearchablePreference, SearchablePreferenceEntity> getSearchablePreferenceToSearchablePreferenceEntity(final Set<SearchablePreferenceEntity> preferences) {
        final Map<Integer, SearchablePreferenceEntity> preferenceById = getPreferenceById(preferences);
        return searchablePreference -> preferenceById.get(searchablePreference.getId());
    }

    private static Map<Integer, SearchablePreferenceEntity> getPreferenceById(final Set<SearchablePreferenceEntity> preferences) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreferenceEntity::getId,
                                Function.identity()));
    }
}
