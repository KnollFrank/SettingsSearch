package de.KnollFrank.lib.settingssearch.db.preference.converter;

import static de.KnollFrank.lib.settingssearch.db.preference.converter.ParentPreferenceByPreferenceProvider.getParentPreferenceByPreference;

import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.HashSet;
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
        final SearchablePreferenceScreenEntity entity = toEntity(screenToConvertToEntity);
        return new DetachedSearchablePreferenceScreenEntity(
                entity,
                getDbDataProviderData(
                        entity,
                        screenToConvertToEntity,
                        predecessorEntity));
    }

    private static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen screen) {
        return new SearchablePreferenceScreenEntity(
                screen.id(),
                screen.host(),
                screen.title(),
                screen.summary());
    }

    private static DbDataProviderData getDbDataProviderData(final SearchablePreferenceScreenEntity entity,
                                                            final SearchablePreferenceScreen screenToConvertToEntity,
                                                            final Optional<SearchablePreferenceEntity> predecessorEntity) {
        final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId =
                mapToIds(getParentPreferenceByPreference(screenToConvertToEntity));
        final Set<DetachedSearchablePreferenceEntity> preferences =
                toEntities(
                        screenToConvertToEntity.allPreferences(),
                        preference ->
                                SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                        preference,
                                        parentPreferenceIdByPreferenceId.get(preference.getId()),
                                        entity,
                                        predecessorEntity));
        final Set<SearchablePreferenceEntity> searchablePreferenceEntities =
                getSearchablePreferenceEntities(preferences);
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
                                                        searchablePreferenceEntities))
                                        .withChildrenByPreference(
                                                getChildrenByPreference(
                                                        searchablePreferenceEntities,
                                                        parentPreferenceIdByPreferenceId))
                                        .build())
                        .build());
    }

    private static Set<DetachedSearchablePreferenceEntity> toEntities(
            final Set<SearchablePreference> searchablePreferences,
            final Function<SearchablePreference, DetachedSearchablePreferenceEntity> toEntity) {
        return searchablePreferences
                .stream()
                .map(toEntity)
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

    private static Map<Integer, Optional<Integer>> mapToIds(final Map<SearchablePreference, Optional<SearchablePreference>> map) {
        return Maps.mapKeysAndValues(
                map,
                SearchablePreference::getId,
                searchablePreference -> searchablePreference.map(SearchablePreference::getId));
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
