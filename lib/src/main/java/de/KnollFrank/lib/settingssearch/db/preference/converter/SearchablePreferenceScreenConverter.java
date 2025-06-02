package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenConverter {

    public static SearchablePreferenceScreenEntity toEntity(final SearchablePreferenceScreen searchablePreferenceScreen) {
        return new SearchablePreferenceScreenEntity(
                searchablePreferenceScreen.id(),
                searchablePreferenceScreen.host(),
                searchablePreferenceScreen.title(),
                searchablePreferenceScreen.summary(),
                searchablePreferenceScreen
                        .allPreferences()
                        .stream()
                        .map(
                                preference ->
                                        SearchablePreferenceConverter.toEntity(
                                                preference,
                                                // FK-FIXME: not correct:
                                                Optional.empty(),
                                                searchablePreferenceScreen.id()))
                        .collect(Collectors.toSet()),
                searchablePreferenceScreen.parentId());
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
}
