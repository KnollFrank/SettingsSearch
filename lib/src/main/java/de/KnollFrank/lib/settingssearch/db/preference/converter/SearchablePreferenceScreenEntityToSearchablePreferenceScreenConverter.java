package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

public class SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter {

    private final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider;
    private final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter;

    public SearchablePreferenceScreenEntityToSearchablePreferenceScreenConverter(
            final SearchablePreferenceScreenEntity.DbDataProvider dbDataProvider,
            final SearchablePreferenceEntityToSearchablePreferenceConverter preferenceConverter) {
        this.dbDataProvider = dbDataProvider;
        this.preferenceConverter = preferenceConverter;
    }

    public SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity entity,
                                                 final Optional<SearchablePreferenceScreen> predecessorOfEntity) {
        final Set<SearchablePreferenceEntity> searchablePreferenceEntities = entity.getAllPreferences(dbDataProvider);
        return new SearchablePreferenceScreen(
                entity.id(),
                entity.host(),
                entity.title(),
                entity.summary(),
                preferenceConverter.fromEntities(
                        searchablePreferenceEntities,
                        PredecessorProviderFactory.createPredecessorProvider(predecessorOfEntity, searchablePreferenceEntities)));
    }
}
