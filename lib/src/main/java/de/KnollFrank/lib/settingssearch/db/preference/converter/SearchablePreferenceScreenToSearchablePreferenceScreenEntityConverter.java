package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.util.Pair;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MoreCollectors;
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
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProviderBuilder;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProviders;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEdge;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenToSearchablePreferenceScreenEntityConverter {

    public record DetachedSearchablePreferenceScreenEntity(SearchablePreferenceScreenEntity searchablePreferenceScreenEntity, DetachedDbDataProvider detachedDbDataProvider) {
    }

    // FK-TODO: refactor
    public static DetachedSearchablePreferenceScreenEntity toEntity(
            final SearchablePreferenceScreen screenToConvertToEntity,
            final Optional<Pair<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEdge>> predecessor) {
        final SearchablePreferenceScreenEntity entity =
                new SearchablePreferenceScreenEntity(
                        screenToConvertToEntity.id(),
                        screenToConvertToEntity.host(),
                        screenToConvertToEntity.title(),
                        screenToConvertToEntity.summary());
        final Optional<SearchablePreferenceEntity> predecessorEntity = getSearchablePreferenceEntity(predecessor);
        final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId = getParentPreferenceIdByPreferenceId(screenToConvertToEntity);
        final Set<Pair<SearchablePreferenceEntity, DetachedDbDataProvider>> allPreferenceEntities =
                screenToConvertToEntity
                        .allPreferences()
                        .stream()
                        .map(
                                preference ->
                                        SearchablePreferenceToSearchablePreferenceEntityConverter.toEntity(
                                                preference,
                                                parentPreferenceIdByPreferenceId.get(preference.getId()),
                                                entity,
                                                predecessorEntity))
                        .collect(Collectors.toSet());
        final Set<SearchablePreferenceEntity> _allPreferenceEntities =
                allPreferenceEntities
                        .stream()
                        .map(preference -> preference.first)
                        .collect(Collectors.toSet());
        return new DetachedSearchablePreferenceScreenEntity(
                entity,
                DetachedDbDataProviders.merge(
                        ImmutableSet
                                .<DetachedDbDataProvider>builder()
                                .addAll(
                                        allPreferenceEntities
                                                .stream()
                                                .map(preference -> preference.second)
                                                .collect(Collectors.toSet()))
                                .add(
                                        new DetachedDbDataProviderBuilder()
                                                .withAllPreferencesBySearchablePreferenceScreen(
                                                        Map.of(
                                                                entity,
                                                                _allPreferenceEntities))
                                                .withChildrenByPreference(getChildrenByPreference(parentPreferenceIdByPreferenceId, _allPreferenceEntities))
                                                .createDetachedDbDataProvider())
                                .build()));
    }

    // FK-TODO: refactor
    private static Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> getChildrenByPreference(
            final Map<Integer, Optional<Integer>> parentPreferenceIdByPreferenceId,
            final Set<SearchablePreferenceEntity> allPreferenceEntities) {
        final Map<Integer, Set<Integer>> childrenByPreference = new HashMap<>();
        for (final SearchablePreferenceEntity entity : allPreferenceEntities) {
            childrenByPreference.put(entity.getId(), new HashSet<>());
        }
        Maps
                .filterPresentValues(parentPreferenceIdByPreferenceId)
                .forEach((preferenceId, parentPreferenceId) -> childrenByPreference.get(parentPreferenceId).add(preferenceId));
        final Map<Integer, SearchablePreferenceEntity> preferenceById =
                allPreferenceEntities
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

    // FK-TODO: refactor
    private static Optional<SearchablePreferenceEntity> getSearchablePreferenceEntity(final Optional<Pair<DetachedSearchablePreferenceScreenEntity, SearchablePreferenceEdge>> predecessor) {
        return predecessor.map(
                _predecessor -> {
                    final Set<SearchablePreferenceEntity> allPreferences = _predecessor.first.searchablePreferenceScreenEntity().getAllPreferences(_predecessor.first.detachedDbDataProvider());
                    final SearchablePreference searchablePreference = predecessor.orElseThrow().second.preference;
                    return allPreferences.stream().filter(_searchablePreference -> _searchablePreference.getId() == searchablePreference.getId()).collect(MoreCollectors.onlyElement());
                });
    }
}
