package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.dao.UpdateableDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class SearchablePreferenceConverter {

    public static SearchablePreferenceEntity toEntity(final SearchablePreference preferenceToConvertToEntity,
                                                      final Optional<SearchablePreferenceEntity> parentPreference,
                                                      final String parentScreenId,
                                                      final UpdateableDbDataProvider updateableDbDataProvider) {
        final SearchablePreferenceEntity entity =
                new SearchablePreferenceEntity(
                        preferenceToConvertToEntity.getId(),
                        preferenceToConvertToEntity.getKey(),
                        preferenceToConvertToEntity.getTitle(),
                        preferenceToConvertToEntity.getSummary(),
                        preferenceToConvertToEntity.getIconResourceIdOrIconPixelData(),
                        preferenceToConvertToEntity.getLayoutResId(),
                        preferenceToConvertToEntity.getWidgetLayoutResId(),
                        preferenceToConvertToEntity.getFragment(),
                        preferenceToConvertToEntity.getClassNameOfReferencedActivity(),
                        preferenceToConvertToEntity.isVisible(),
                        preferenceToConvertToEntity.getSearchableInfo(),
                        parentPreference.map(SearchablePreferenceEntity::getId),
                        preferenceToConvertToEntity.getPredecessor().map(SearchablePreference::getId),
                        parentScreenId);
        entity.setDao(updateableDbDataProvider);
        parentPreference.ifPresent(_parentPreference -> updateChildrenByPreferenceWithChildForParent(updateableDbDataProvider.childrenByPreference, entity, _parentPreference));
        return entity;
    }

    public static SearchablePreference fromEntity(final SearchablePreferenceEntity searchablePreferenceEntity) {
        return new SearchablePreference(
                searchablePreferenceEntity.getId(),
                searchablePreferenceEntity.getKey(),
                searchablePreferenceEntity.getTitle(),
                searchablePreferenceEntity.getSummary(),
                searchablePreferenceEntity.getIconResourceIdOrIconPixelData(),
                searchablePreferenceEntity.getLayoutResId(),
                searchablePreferenceEntity.getWidgetLayoutResId(),
                searchablePreferenceEntity.getFragment(),
                searchablePreferenceEntity.getClassNameOfReferencedActivity(),
                searchablePreferenceEntity.isVisible(),
                searchablePreferenceEntity.getSearchableInfo(),
                searchablePreferenceEntity
                        .getChildren()
                        .stream()
                        .map(SearchablePreferenceConverter::fromEntity)
                        .collect(Collectors.toSet()),
                searchablePreferenceEntity.getPredecessor().map(SearchablePreferenceConverter::fromEntity));
    }

    private static void updateChildrenByPreferenceWithChildForParent(final Map<SearchablePreferenceEntity, Set<SearchablePreferenceEntity>> childrenByPreference,
                                                                     final SearchablePreferenceEntity child,
                                                                     final SearchablePreferenceEntity parent) {
        if (!childrenByPreference.containsKey(parent)) {
            childrenByPreference.put(parent, new HashSet<>());
        }
        childrenByPreference.get(parent).add(child);
    }
}
