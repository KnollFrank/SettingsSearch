package de.KnollFrank.lib.settingssearch.db.preference.converter;

import com.google.common.collect.ImmutableSet;

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
                mapToIds(ParentPreferenceByPreferenceProvider.getParentPreferenceByPreference(screenToConvertToEntity));
        final Set<DetachedSearchablePreferenceEntity> detachedSearchablePreferenceEntities =
                toEntities(
                        screenToConvertToEntity.allPreferences(),
                        preference ->
                                SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                        preference,
                                        parentPreferenceIdByPreferenceId.get(preference.getId()),
                                        entity,
                                        predecessorEntity));
        final Set<SearchablePreferenceEntity> searchablePreferenceEntities =
                getSearchablePreferenceEntities(detachedSearchablePreferenceEntities);
        return DbDataProviderDatas.merge(
                ImmutableSet
                        .<DbDataProviderData>builder()
                        .addAll(getDbDataProviderDatas(detachedSearchablePreferenceEntities))
                        .add(
                                DbDataProviderData
                                        .builder()
                                        .withAllPreferencesBySearchablePreferenceScreen(
                                                Map.of(
                                                        entity,
                                                        searchablePreferenceEntities))
                                        .withChildrenByPreference(
                                                ChildrenByPreferenceProvider.getChildrenByPreference(
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
