package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreference;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;

class SearchablePreferenceScreenDAOSetter {

    private final SearchablePreferenceScreen.DbDataProvider dao;
    private final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter;

    public SearchablePreferenceScreenDAOSetter(final SearchablePreferenceScreen.DbDataProvider dao,
                                               final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter) {
        this.dao = dao;
        this.searchablePreferenceDAOSetter = searchablePreferenceDAOSetter;
    }

    public SearchablePreferenceScreen setDao(final SearchablePreferenceScreen searchablePreferenceScreen) {
        searchablePreferenceScreen.setDao(dao);
        return searchablePreferenceScreen;
    }

    public Set<SearchablePreferenceScreen> setDao(final Set<SearchablePreferenceScreen> searchablePreferenceScreens) {
        searchablePreferenceScreens.forEach(this::setDao);
        return searchablePreferenceScreens;
    }

    public Set<SearchablePreferenceScreenAndAllPreferences> __setDao(final Set<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesList) {
        searchablePreferenceScreenAndAllPreferencesList.forEach(this::setDao);
        return searchablePreferenceScreenAndAllPreferencesList;
    }

    public Optional<SearchablePreferenceScreen> setDao(final Optional<SearchablePreferenceScreen> searchablePreferenceScreen) {
        searchablePreferenceScreen.ifPresent(this::setDao);
        return searchablePreferenceScreen;
    }

    public void _setDao(final Map<SearchablePreferenceScreen, Set<SearchablePreference>> allPreferencesBySearchablePreferenceScreen) {
        allPreferencesBySearchablePreferenceScreen.forEach(
                (searchablePreferenceScreen, allPreferences) -> {
                    searchablePreferenceScreen.setDao(dao);
                    allPreferences.forEach(searchablePreferenceDAOSetter::setDao);
                });
    }

    public void setDao(final Map<SearchablePreference, SearchablePreferenceScreen> hostByPreference) {
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
