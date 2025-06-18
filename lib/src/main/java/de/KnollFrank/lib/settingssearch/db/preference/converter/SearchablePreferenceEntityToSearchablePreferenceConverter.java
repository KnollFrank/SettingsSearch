package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceEntityToSearchablePreferenceConverter {

    private final SearchablePreferenceEntity.DbDataProvider dbDataProvider;

    public SearchablePreferenceEntityToSearchablePreferenceConverter(final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        this.dbDataProvider = dbDataProvider;
    }

    public Set<SearchablePreference> fromEntities(final Set<SearchablePreferenceEntity> entities,
                                                  final Function<SearchablePreferenceEntity, Optional<SearchablePreference>> getPredecessor) {
        return entities
                .stream()
                .map(entity -> fromEntity(entity, getPredecessor))
                .collect(Collectors.toSet());
    }

    private SearchablePreference fromEntity(final SearchablePreferenceEntity entity,
                                            final Function<SearchablePreferenceEntity, Optional<SearchablePreference>> getPredecessor) {
        return new SearchablePreference(
                entity.getId(),
                entity.getKey(),
                entity.getTitle(),
                entity.getSummary(),
                entity.getIconResourceIdOrIconPixelData(),
                entity.getLayoutResId(),
                entity.getWidgetLayoutResId(),
                entity.getFragment(),
                entity.getClassNameOfReferencedActivity(),
                entity.isVisible(),
                entity.getSearchableInfo(),
                fromEntities(entity.getChildren(dbDataProvider), getPredecessor),
                getPredecessor.apply(entity));
    }
}
