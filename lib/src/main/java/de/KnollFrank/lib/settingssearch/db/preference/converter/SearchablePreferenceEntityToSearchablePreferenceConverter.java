package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;

class SearchablePreferenceEntityToSearchablePreferenceConverter {

    private final SearchablePreferenceEntity.DbDataProvider dbDataProvider;

    public SearchablePreferenceEntityToSearchablePreferenceConverter(final SearchablePreferenceEntity.DbDataProvider dbDataProvider) {
        this.dbDataProvider = dbDataProvider;
    }

    public Set<SearchablePreference> fromEntities(final Set<SearchablePreferenceEntity> entities) {
        return entities
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toSet());
    }

    private SearchablePreference fromEntity(final SearchablePreferenceEntity entity) {
        return new SearchablePreference(
                entity.id(),
                entity.key(),
                entity.title(),
                entity.summary(),
                entity.iconResourceIdOrIconPixelData(),
                entity.layoutResId(),
                entity.widgetLayoutResId(),
                entity.fragment(),
                entity.classNameOfReferencedActivity(),
                entity.visible(),
                entity.extras().get(),
                entity.searchableInfo(),
                fromEntities(entity.getChildren(dbDataProvider)));
    }
}
