package de.KnollFrank.lib.settingssearch.db.preference.converter;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderData;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DbDataProviderDatas;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceScreenEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter {

    public static DetachedSearchablePreferenceScreenEntity toEntity(
            final SearchablePreferenceScreen screenToConvertToEntity,
            final Optional<SearchablePreferenceEntity> predecessorEntity) {
        final SearchablePreferenceScreenEntity entity =
                new SearchablePreferenceScreenEntity(
                        screenToConvertToEntity.id(),
                        screenToConvertToEntity.host(),
                        screenToConvertToEntity.title(),
                        screenToConvertToEntity.summary());
        return new DetachedSearchablePreferenceScreenEntity(
                entity,
                getDbDataProviderData(
                        entity,
                        screenToConvertToEntity,
                        predecessorEntity));
    }

    private static DbDataProviderData getDbDataProviderData(final SearchablePreferenceScreenEntity entity,
                                                            final SearchablePreferenceScreen screenToConvertToEntity,
                                                            final Optional<SearchablePreferenceEntity> predecessorEntity) {
        final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId =
                getParentPreferenceIdByPreferenceId(screenToConvertToEntity);
        final Set<DetachedSearchablePreferenceEntity> preferences =
                toPreferenceEntities(
                        screenToConvertToEntity.allPreferences(),
                        preference ->
                                SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                        preference,
                                        parentPreferenceIdByPreferenceId.get(preference.getId()),
                                        entity,
                                        predecessorEntity));
        return DbDataProviderDatas.merge(
                ImmutableSet
                        .<DbDataProviderData>builder()
                        .addAll(getDbDataProviderDatas(preferences))
                        .add(
                                DbDataProviderData
                                        .builder()
                                        .withAllPreferencesBySearchablePreferenceScreen(
                                                Map.of(
                                                        entity,
                                                        getSearchablePreferenceEntities(preferences)))
                                        .withChildrenByPreference(
                                                getChildrenByPreference(
                                                        getSearchablePreferenceEntities(preferences),
                                                        parentPreferenceIdByPreferenceId))
                                        .build())
                        .build());
    }

    private static Set<DetachedSearchablePreferenceEntity> toPreferenceEntities(
            final Set<SearchablePreference> searchablePreferences,
            final Function<SearchablePreference, DetachedSearchablePreferenceEntity> toPreferenceEntity) {
        return searchablePreferences
                .stream()
                .map(toPreferenceEntity)
                .collect(Collectors.toSet());
    }

    // FK-TODO: refactor
    private static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference(
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

    private static Map<Integer, Optional<Integer>> getParentPreferenceIdByPreferenceId(final SearchablePreferenceScreen screen) {
        return getParentPreferenceIdByPreferenceId(screen.allPreferences());
    }

    // FK-TODO: refactor
    private static Map<Integer, Optional<Integer>> getParentPreferenceIdByPreferenceId(final Set<SearchablePreference> searchablePreferences) {
        final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId =
                Maps.merge(
                        searchablePreferences
                                .stream()
                                .map(SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter::getParentPreferenceIdByPreferenceId)
                                .collect(Collectors.toSet()));
        return Maps.merge(
                List.of(
                        parentPreferenceIdByPreferenceId,
                        getParentPreferenceIdByPreferenceIdForUnmappedPreferenceIds(searchablePreferences, parentPreferenceIdByPreferenceId)));
    }

    private static Map<Integer, Optional<Integer>> getParentPreferenceIdByPreferenceIdForUnmappedPreferenceIds(final Set<SearchablePreference> searchablePreferences,
                                                                                                               final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId) {
        return SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter
                .getUnmappedPreferenceIds(searchablePreferences, parentPreferenceIdByPreferenceId)
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preferenceId -> Optional.empty()));
    }

    private static Set<Integer> getUnmappedPreferenceIds(final Set<SearchablePreference> searchablePreferences,
                                                         final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId) {
        return Sets.difference(
                searchablePreferences
                        .stream()
                        .map(SearchablePreference::getId)
                        .collect(Collectors.toSet()),
                parentPreferenceIdByPreferenceId.keySet());
    }

    private static Map<Integer, Optional<Integer>> getParentPreferenceIdByPreferenceId(final SearchablePreference searchablePreference) {
        return searchablePreference
                .getChildren()
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                child -> Optional.of(searchablePreference.getId())));
    }

    private static Set<DbDataProviderData> getDbDataProviderDatas(final Set<DetachedSearchablePreferenceEntity> preferences) {
        return preferences
                .stream()
                .map(DetachedSearchablePreferenceEntity::dbDataProviderData)
                .collect(Collectors.toSet());
    }

    private static Set<SearchablePreferenceEntity> getSearchablePreferenceEntities(final Set<DetachedSearchablePreferenceEntity> preferences) {
        return preferences
                .stream()
                .map(DetachedSearchablePreferenceEntity::preference)
                .collect(Collectors.toSet());
    }
}
