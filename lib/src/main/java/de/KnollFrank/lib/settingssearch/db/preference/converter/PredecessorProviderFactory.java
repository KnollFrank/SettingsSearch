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

class PredecessorProviderFactory {

    public static PredecessorProvider createPredecessorProvider(
            final Optional<SearchablePreferenceScreen> predecessorOfEntities,
            final Set<SearchablePreferenceEntity> entities) {
        return predecessorOfEntities
                .map(_predecessorOfEntities -> createPredecessorProvider(_predecessorOfEntities, entities))
                .orElse(_entity -> Optional.empty());
    }

    private static PredecessorProvider createPredecessorProvider(
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

    private static Map<Integer, SearchablePreference> getSearchablePreferenceById(final Set<SearchablePreference> searchablePreferences) {
        return searchablePreferences
                .stream()
                .collect(
                        Collectors.toMap(
                                SearchablePreference::getId,
                                Function.identity()));
    }
}
