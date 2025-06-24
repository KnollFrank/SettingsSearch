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
                        screenToConvertToEntity,
                        entity,
                        predecessorEntity));
    }

    private static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen screen) {
        return new SearchablePreferenceScreenEntity(
                screen.id(),
                screen.host(),
                screen.title(),
                screen.summary());
    }

    private static DbDataProviderData getDbDataProviderData(final SearchablePreferenceScreen screenToConvertToEntity,
                                                            final SearchablePreferenceScreenEntity entity,
                                                            final Optional<SearchablePreferenceEntity> predecessorEntity) {
        final Set<DetachedSearchablePreferenceEntity> detachedSearchablePreferenceEntities =
                getDetachedSearchablePreferenceEntities(
                        screenToConvertToEntity,
                        entity,
                        predecessorEntity);
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
                                                SearchablePreferenceToSearchablePreferenceEntityTransformerFactory
                                                        .createTransformer(searchablePreferenceEntities)
                                                        .transform(
                                                                ChildrenByPreferenceProvider.getChildrenByPreference(
                                                                        screenToConvertToEntity.allPreferences())))
                                        .build())
                        .build());
    }

    private static Set<DetachedSearchablePreferenceEntity> getDetachedSearchablePreferenceEntities(
            final SearchablePreferenceScreen screenToConvertToEntity,
            final SearchablePreferenceScreenEntity entity,
            final Optional<SearchablePreferenceEntity> predecessorEntity) {
        final Map<SearchablePreference, SearchablePreference> parentPreferenceByPreference = ParentPreferenceByPreferenceProvider.getParentPreferenceByPreference(screenToConvertToEntity);
        return toEntities(
                screenToConvertToEntity.allPreferences(),
                preference ->
                        SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                preference,
                                Maps
                                        .get(
                                                parentPreferenceByPreference,
                                                preference)
                                        .map(SearchablePreference::getId),
                                entity,
                                predecessorEntity));
    }

    private static Set<DetachedSearchablePreferenceEntity> toEntities(
            final Set<SearchablePreference> searchablePreferences,
            final Function<SearchablePreference, DetachedSearchablePreferenceEntity> toEntity) {
        return searchablePreferences
                .stream()
                .map(toEntity)
                .collect(Collectors.toSet());
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
