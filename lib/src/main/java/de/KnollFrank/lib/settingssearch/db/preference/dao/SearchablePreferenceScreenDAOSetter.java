package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceEntity;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenEntity;

class SearchablePreferenceScreenDAOSetter {

    private final SearchablePreferenceScreenEntity.DbDataProvider dao;
    private final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter;

    public SearchablePreferenceScreenDAOSetter(final SearchablePreferenceScreenEntity.DbDataProvider dao,
                                               final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter) {
        this.dao = dao;
        this.searchablePreferenceDAOSetter = searchablePreferenceDAOSetter;
    }

    public SearchablePreferenceScreenEntity setDao(final SearchablePreferenceScreenEntity searchablePreferenceScreenEntity) {
        searchablePreferenceScreenEntity.setDao(dao);
        return searchablePreferenceScreenEntity;
    }

    public Set<SearchablePreferenceScreenEntity> setDao(final Set<SearchablePreferenceScreenEntity> searchablePreferenceScreenEntities) {
        searchablePreferenceScreenEntities.forEach(this::setDao);
        return searchablePreferenceScreenEntities;
    }

    public Set<SearchablePreferenceScreenAndAllPreferences> __setDao(final Set<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesList) {
        searchablePreferenceScreenAndAllPreferencesList.forEach(this::setDao);
        return searchablePreferenceScreenAndAllPreferencesList;
    }

    public Optional<SearchablePreferenceScreenEntity> setDao(final Optional<SearchablePreferenceScreenEntity> searchablePreferenceScreen) {
        searchablePreferenceScreen.ifPresent(this::setDao);
        return searchablePreferenceScreen;
    }

    public void _setDao(final Map<SearchablePreferenceScreenEntity, Set<SearchablePreferenceEntity>> allPreferencesBySearchablePreferenceScreen) {
        allPreferencesBySearchablePreferenceScreen.forEach(
                (searchablePreferenceScreen, allPreferences) -> {
                    searchablePreferenceScreen.setDao(dao);
                    allPreferences.forEach(searchablePreferenceDAOSetter::setDao);
                });
    }

    public void setDao(final Map<SearchablePreferenceEntity, SearchablePreferenceScreenEntity> hostByPreference) {
        hostByPreference.forEach(
                (preference, host) -> {
                    searchablePreferenceDAOSetter.setDao(preference);
                    host.setDao(dao);
                });
    }

    private SearchablePreferenceScreenAndAllPreferences setDao(final SearchablePreferenceScreenAndAllPreferences searchablePreferenceScreenAndAllPreferences) {
        setDao(searchablePreferenceScreenAndAllPreferences.searchablePreferenceScreen());
        searchablePreferenceDAOSetter.setDao(searchablePreferenceScreenAndAllPreferences.allPreferences());
        return searchablePreferenceScreenAndAllPreferences;
    }
}
