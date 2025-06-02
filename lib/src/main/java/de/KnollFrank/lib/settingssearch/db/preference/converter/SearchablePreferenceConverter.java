package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;

public class SearchablePreferenceConverter {

    public static SearchablePreferenceEntity toEntity(final SearchablePreference searchablePreference,
                                                      final Optional<SearchablePreference> parentPreference,
                                                      final SearchablePreferenceScreen parentScreen) {
        return new SearchablePreferenceEntity(
                searchablePreference.getId(),
                searchablePreference.getKey(),
                searchablePreference.getTitle(),
                searchablePreference.getSummary(),
                searchablePreference.getIconResourceIdOrIconPixelData(),
                searchablePreference.getLayoutResId(),
                searchablePreference.getWidgetLayoutResId(),
                searchablePreference.getFragment(),
                searchablePreference.getClassNameOfReferencedActivity(),
                searchablePreference.isVisible(),
                searchablePreference.getSearchableInfo(),
                parentPreference.map(SearchablePreference::getId),
                searchablePreference.getPredecessor().map(SearchablePreference::getId),
                parentScreen.id());
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
}
