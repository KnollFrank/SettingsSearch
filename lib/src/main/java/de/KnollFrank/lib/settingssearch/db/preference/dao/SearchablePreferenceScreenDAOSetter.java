package de.KnollFrank.lib.settingssearch.db.preference.dao;

import java.util.List;

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

    public List<SearchablePreferenceScreen> setDao(final List<SearchablePreferenceScreen> searchablePreferenceScreens) {
        searchablePreferenceScreens.forEach(this::setDao);
        return searchablePreferenceScreens;
    }

    public List<SearchablePreferenceScreenAndAllPreferences> __setDao(final List<SearchablePreferenceScreenAndAllPreferences> searchablePreferenceScreenAndAllPreferencesList) {
        searchablePreferenceScreenAndAllPreferencesList.forEach(this::setDao);
        return searchablePreferenceScreenAndAllPreferencesList;
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
