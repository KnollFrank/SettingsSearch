package de.KnollFrank.lib.settingssearch.db.preference.converter;

import android.util.Pair;

import java.util.Map;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProvider;
import de.KnollFrank.lib.settingssearch.db.preference.dao.DetachedDbDataProviderBuilder;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceToSearchablePreferenceEntityConverter {

    public static Pair<SearchablePreferenceEntity, DetachedDbDataProvider> toEntity(
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
        return Pair.create(
                entity,
                new DetachedDbDataProviderBuilder()
                        .withHostByPreference(Map.of(entity, parentScreen))
                        .withPredecessorByPreference(Map.of(entity, predecessor))
                        .createDetachedDbDataProvider());
    }
}
