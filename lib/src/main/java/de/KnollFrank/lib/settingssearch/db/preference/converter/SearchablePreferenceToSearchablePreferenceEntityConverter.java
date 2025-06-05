package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class SearchablePreferenceToSearchablePreferenceEntityConverter {

    public static SearchablePreferenceEntity toEntity(final SearchablePreference preferenceToConvertToEntity,
                                                      final Optional<Integer> parentPreferenceId,
                                                      final String parentScreenId) {
        return new SearchablePreferenceEntity(
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
                parentPreferenceId,
                preferenceToConvertToEntity.getPredecessor().map(SearchablePreference::getId),
                parentScreenId);
    }
}
