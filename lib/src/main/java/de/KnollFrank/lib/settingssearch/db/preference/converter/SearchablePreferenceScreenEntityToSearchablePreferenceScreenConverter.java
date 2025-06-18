package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Maps;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter {

    private final Function<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferences;
    private final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter;

    public SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
            final Function<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> getAllPreferences,
            final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter) {
        this.getAllPreferences = getAllPreferences;
        this.preferenceConverter = preferenceConverter;
    }

    public SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity entity,
                                                 final Optional<SearchablePreferenceScreen> predecessorOfEntity) {
        final Set<SearchablePreferenceEntity> searchablePreferenceEntities = getAllPreferences.apply(entity);
        return new SearchablePreferenceScreen(
                entity.getId(),
                entity.getHost(),
                entity.getTitle(),
                entity.getSummary(),
                preferenceConverter.fromEntities(
                        searchablePreferenceEntities,
                        createGetPredecessor(predecessorOfEntity, searchablePreferenceEntities)));
    }

    private static Function<SearchablePreferenceEntity, Optional<SearchablePreference>> createGetPredecessor(
            final Optional<SearchablePreferenceScreen> predecessorOfEntities,
            final Set<SearchablePreferenceEntity> entities) {
        return predecessorOfEntities
                .map(_predecessorOfEntities -> createGetPredecessor(_predecessorOfEntities, entities))
                .orElse(_entity -> Optional.empty());
    }

    private static Function<SearchablePreferenceEntity, Optional<SearchablePreference>> createGetPredecessor(
            final SearchablePreferenceScreen predecessorOfEntities,
            final Set<SearchablePreferenceEntity> entities) {
        final Map<SearchablePreferenceEntity, SearchablePreference> predecessorByEntity = getPredecessorByEntity(predecessorOfEntities, entities);
        return entity -> Maps.get(predecessorByEntity, entity);
    }

    private static Map<SearchablePreferenceEntity, SearchablePreference> getPredecessorByEntity(
            final SearchablePreferenceScreen predecessorOfEntities,
            final Set<SearchablePreferenceEntity> entities) {
        final Map<Integer, SearchablePreference> predecessorById = getSearchablePreferenceById(predecessorOfEntities.allPreferences());
        return entities
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                entity ->
                                        Maps
                                                .get(predecessorById, entity.getPredecessorId().orElseThrow())
                                                .orElseThrow()));
    }

    public static Map<Integer, SearchablePreference> getSearchablePreferenceById(final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                Function.identity()));
    }
}
