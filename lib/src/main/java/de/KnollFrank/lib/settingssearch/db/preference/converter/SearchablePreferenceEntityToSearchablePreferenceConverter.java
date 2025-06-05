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

    public Set<SearchablePreference> convert(final Set<SearchablePreferenceEntity> entities) {
        return entities
                .stream()
                .map(this::convert)
                .collect(Collectors.toSet());
    }

    public SearchablePreference convert(final SearchablePreferenceEntity entity) {
        if (!pojoByEntityCache.containsKey(entity)) {
            pojoByEntityCache.put(entity, _convert(entity));
        }
        return pojoByEntityCache.get(entity);
    }

    private SearchablePreference _convert(final SearchablePreferenceEntity entity) {
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
                convert(entity.getChildren(dbDataProvider)),
                entity.getPredecessor(dbDataProvider).map(this::convert));
    }
}
