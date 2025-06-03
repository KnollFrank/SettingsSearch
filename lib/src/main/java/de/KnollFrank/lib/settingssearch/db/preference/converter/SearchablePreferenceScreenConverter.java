package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.dao.UpdateableDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenConverter {

    public static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen screenToConvertToEntity,
                                                            final UpdateableDbDataProvider updateableDbDataProvider) {
        final Set<SearchablePreferenceEntity> allPreferences = allPreferencesOfScreenToEntities(screenToConvertToEntity, updateableDbDataProvider);
        final SearchablePreferenceScreenEntity entityScreen =
                new SearchablePreferenceScreenEntity(
                        screenToConvertToEntity.id(),
                        screenToConvertToEntity.host(),
                        screenToConvertToEntity.title(),
                        screenToConvertToEntity.summary(),
                        allPreferences,
                        screenToConvertToEntity.parentId());
        entityScreen.setDao(updateableDbDataProvider);
        {
            updateableDbDataProvider.allPreferencesBySearchablePreferenceScreen.put(entityScreen, allPreferences);
            updateableDbDataProvider.hostByPreference.putAll(mapPreferencesToHost(allPreferences, entityScreen));
        }
        return entityScreen;
    }

    public static SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity) {
        return new SearchablePreferenceScreen(
                searchablePreferenceScreenEntity.getId(),
                searchablePreferenceScreenEntity.getHost(),
                searchablePreferenceScreenEntity.getTitle(),
                searchablePreferenceScreenEntity.getSummary(),
                searchablePreferenceScreenEntity
                        .getAllPreferences()
                        .stream()
                        .map(SearchablePreferenceConverter::fromEntity)
                        .collect(Collectors.toSet()),
                searchablePreferenceScreenEntity.getParentId());
    }

    private static Set<SearchablePreferenceEntity> allPreferencesOfScreenToEntities(
            final SearchablePreferenceScreen screen,
            final UpdateableDbDataProvider updateableDbDataProvider) {
        return screen
                .allPreferences()
                .stream()
                .map(
                        preference ->
                                SearchablePreferenceConverter.toEntity(
                                        preference,
                                        // FK-FIXME: not correct:
                                        Optional.empty(),
                                        screen.id(),
                                        updateableDbDataProvider))
                .collect(Collectors.toSet());
    }

    private static Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> mapPreferencesToHost(final Set<SearchablePreferenceEntity> preferences, final SearchablePreferenceScreenEntity host) {
        return preferences
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                preference -> host));
    }
}
