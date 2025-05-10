package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.Optional;
import java.util.Set;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.SearchablePreferenceScreenAndAllPreferences;

class SearchablePreferenceScreenDAOSetter {

    private final SearchablePreferenceScreenDAO dao;
    private final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter;

    public SearchablePreferenceScreenDAOSetter(final SearchablePreferenceScreenDAO dao,
                                               final SearchablePreferenceDAOSetter searchablePreferenceDAOSetter) {
        this.dao = dao;
        this.searchablePreferenceDAOSetter = searchablePreferenceDAOSetter;
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

    public SearchablePreferenceScreen setDao(final SearchablePreferenceScreen searchablePreferenceScreen) {
        searchablePreferenceScreen.setDao(dao);
        return searchablePreferenceScreen;
    }

    private SearchablePreferenceScreenAndAllPreferences setDao(final SearchablePreferenceScreenAndAllPreferences searchablePreferenceScreenAndAllPreferences) {
        setDao(searchablePreferenceScreenAndAllPreferences.searchablePreferenceScreen());
        searchablePreferenceDAOSetter.setDao(searchablePreferenceScreenAndAllPreferences.allPreferences());
        return searchablePreferenceScreenAndAllPreferences;
    }
}
