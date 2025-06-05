package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

public class SearchablePreferenceEntityToSearchablePreferenceConverter {

    private final SearchablePreferenceEntity.DbDataProvider dbDataProvider;
    private final Map<SearchablePreferenceEntity, SearchablePreference> pojoByEntityCache = new HashMap<>();

    public SearchablePreferenceEntityToSearchablePreferenceConverter(final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        this.dbDataProvider = dbDataProvider;
    }

    public Set<SearchablePreference> fromEntities(final Set<SearchablePreferenceEntity> entities) {
        return entities
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toSet());
    }

    public SearchablePreference fromEntity(final SearchablePreferenceEntity entity) {
        if (!pojoByEntityCache.containsKey(entity)) {
            pojoByEntityCache.put(entity, _fromEntity(entity));
        }
        return pojoByEntityCache.get(entity);
    }

    private SearchablePreference _fromEntity(final SearchablePreferenceEntity entity) {
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
                fromEntities(entity.getChildren(dbDataProvider)),
                entity.getPredecessor(dbDataProvider).map(this::fromEntity));
    }
}
