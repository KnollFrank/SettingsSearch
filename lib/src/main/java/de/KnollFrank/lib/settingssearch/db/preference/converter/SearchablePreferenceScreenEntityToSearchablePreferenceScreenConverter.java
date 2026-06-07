package de.KnollFrank.lib.settingssearch.db.preference.converter;

import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Sets;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
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

    public SearchablePreferenceScreen fromEntity(final SearchablePreferenceScreenEntity entity) {
        final Set<SearchablePreferenceEntity> allPreferencesOfPreferenceHierarchy = entity.getAllPreferencesOfPreferenceHierarchy(dbDataProvider);
        return new SearchablePreferenceScreen(
                entity.id(),
                entity.host().asFragmentClassOfActivity(),
                entity.title(),
                entity.summary(),
                getRoots(preferenceConverter.fromEntities(allPreferencesOfPreferenceHierarchy)));
    }

    // FK-TODO: refactor
    private Set<SearchablePreference> getRoots(final Set<SearchablePreference> searchablePreferences) {
        final Set<SearchablePreference> allChildren =
                Sets.union(
                        searchablePreferences
                                .stream()
                                .map(SearchablePreference::getImmediateChildren)
                                .collect(Collectors.toSet()));
        return Sets.difference(searchablePreferences, allChildren);
    }
}
