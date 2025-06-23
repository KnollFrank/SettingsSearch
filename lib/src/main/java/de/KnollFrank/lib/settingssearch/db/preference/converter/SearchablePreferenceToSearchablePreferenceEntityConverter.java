package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.DbDataProviderDataBuilder;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.DetachedSearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceToSearchablePreferenceEntityConverter {

    public static DetachedSearchablePreferenceEntity toEntity(
            final SearchablePreference preferenceToConvertToEntity,
            final Optional<Integer> parentId,
            final SearchablePreferenceScreenEntity parentScreen,
            final Optional<SearchablePreferenceEntity> predecessor) {
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
                        parentId,
                        predecessor.map(SearchablePreferenceEntity::getId),
                        parentScreen.getId());
        return new DetachedSearchablePreferenceEntity(
                entity,
                new DbDataProviderDataBuilder()
                        .withHostByPreference(Map.of(entity, parentScreen))
                        .withPredecessorByPreference(Map.of(entity, predecessor))
                        .createDbDataProviderData());
    }
}
